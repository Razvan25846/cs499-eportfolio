from pymongo import MongoClient
from pymongo.errors import (
    ConnectionFailure,
    OperationFailure,
    PyMongoError
)


class AnimalShelter:
    """
    CRUD operations for the AAC Animal Shelter database.
    """

    def __init__(self, username=None, password=None):
        """
        Initialize and verify the MongoDB connection.

        Supports authenticated connections and local development connections.
        """
        try:
            if username and password:
                connection_string = (
                    f"mongodb://{username}:{password}"
                    "@localhost:27017/?authSource=aac"
                )
            else:
                connection_string = "mongodb://localhost:27017/"

            self.client = MongoClient(
                connection_string,
                serverSelectionTimeoutMS=5000
            )

            # Verify that MongoDB is available
            self.client.admin.command("ping")

            self.database = self.client["aac"]
            self.collection = self.database["animals"]

            print("MongoDB connection verified successfully!")

        except ConnectionFailure as e:
            raise ConnectionError(
                f"Unable to connect to MongoDB: {e}"
            )

        except OperationFailure as e:
            raise PermissionError(
                f"MongoDB authentication failed: {e}"
            )

        except PyMongoError as e:
            raise RuntimeError(
                f"MongoDB error: {e}"
            )

    def create(self, data):
        """
        Insert a document into the MongoDB collection.

        Returns:
            True if the document was inserted successfully.
        """
        if not isinstance(data, dict) or not data:
            print("Create failed: data must be a non-empty dictionary.")
            return False

        try:
            result = self.collection.insert_one(data)

            if result.acknowledged:
                print(f"Document created with ID: {result.inserted_id}")
                return True

            return False

        except OperationFailure as e:
            print(f"Create failed due to database operation error: {e}")
            return False

        except PyMongoError as e:
            print(f"Create failed due to MongoDB error: {e}")
            return False

    def read(self, query=None, sort_field=None, ascending=True, limit=0):
        """
        Query documents from the MongoDB collection.

        Args:
            query: Dictionary containing MongoDB query criteria.
            sort_field: Optional field used to sort results.
            ascending: True for ascending order, False for descending.
            limit: Maximum number of documents to return.
                   A value of 0 returns all matching documents.

        Returns:
            A list of matching documents.
        """
        if query is None:
            query = {}

        if not isinstance(query, dict):
            print("Read failed: query must be a dictionary.")
            return []

        try:
            cursor = self.collection.find(query)

            # Sort results when a sort field is provided
            if sort_field:
                sort_direction = 1 if ascending else -1
                cursor = cursor.sort(sort_field, sort_direction)

            # Limit the number of returned records when requested
            if isinstance(limit, int) and limit > 0:
                cursor = cursor.limit(limit)

            return list(cursor)

        except OperationFailure as e:
            print(f"Read failed due to database operation error: {e}")
            return []

        except PyMongoError as e:
            print(f"Read failed due to MongoDB error: {e}")
            return []

    def advanced_search(
        self,
        breeds=None,
        min_age=None,
        max_age=None,
        sex=None,
        limit=50
    ):
        """
        Search for animals using multiple optional criteria.

        Args:
            breeds: List of breed names or patterns.
            min_age: Minimum age in weeks.
            max_age: Maximum age in weeks.
            sex: Animal sex value to search for.
            limit: Maximum number of records returned.

        Returns:
            A list of matching animal documents.
        """
        query = {}

        if breeds:
            if not isinstance(breeds, list):
                print("Search failed: breeds must be provided as a list.")
                return []

            query["$or"] = [
                {"breed": {"$regex": breed, "$options": "i"}}
                for breed in breeds
            ]
        if min_age is not None or max_age is not None:
            query["age_upon_outcome_in_weeks"] = {}

            if min_age is not None:
                query["age_upon_outcome_in_weeks"]["$gte"] = min_age

            if max_age is not None:
                query["age_upon_outcome_in_weeks"]["$lte"] = max_age

        if sex:
            query["sex_upon_outcome"] = sex

        try:
            cursor = self.collection.find(query)

            if isinstance(limit, int) and limit > 0:
                cursor = cursor.limit(limit)

            return list(cursor)

        except OperationFailure as e:
            print(f"Advanced search failed due to database operation error: {e}")
            return []

        except PyMongoError as e:
            print(f"Advanced search failed due to MongoDB error: {e}")
            return []

    def update(self, query, new_values):
        """
        Update documents matching the query.

        Returns:
            Number of documents modified.
        """
        if not isinstance(query, dict) or not query:
            print("Update failed: query must be a non-empty dictionary.")
            return 0

        if not isinstance(new_values, dict) or not new_values:
            print("Update failed: new values must be a non-empty dictionary.")
            return 0

        try:
            result = self.collection.update_many(
                query,
                {"$set": new_values}
            )

            print(
                f"Matched {result.matched_count} document(s); "
                f"modified {result.modified_count} document(s)."
            )

            return result.modified_count

        except OperationFailure as e:
            print(f"Update failed due to database operation error: {e}")
            return 0

        except PyMongoError as e:
            print(f"Update failed due to MongoDB error: {e}")
            return 0

    def delete(self, query):
        """
        Delete documents matching the query.

        Returns:
            Number of documents deleted.
        """
        if not isinstance(query, dict) or not query:
            print("Delete failed: query must be a non-empty dictionary.")
            return 0

        try:
            result = self.collection.delete_many(query)

            print(f"Deleted {result.deleted_count} document(s).")

            return result.deleted_count

        except OperationFailure as e:
            print(f"Delete failed due to database operation error: {e}")
            return 0

        except PyMongoError as e:
            print(f"Delete failed due to MongoDB error: {e}")
            return 0