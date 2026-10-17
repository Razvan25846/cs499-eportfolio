from pymongo import MongoClient
from pymongo.errors import ConnectionFailure, OperationFailure

class AnimalShelter:
    """
    CRUD operations for the AAC Animal Shelter database.
    """

    def __init__(self, username, password):
        """
        Initialize the MongoDB client connection using provided credentials.
        """
        try:
            self.client = MongoClient(
        f"mongodb://{username}:{password}@localhost:27017/?authSource=aac"
            )
            self.database = self.client["aac"]
            self.collection = self.database["animals"]
            print("Connected successfully!")
        except ConnectionFailure as e:
            raise Exception("MongoDB connection failed: " + str(e))

    def create(self, data):
        """
        Insert a document into the MongoDB collection.

        Returns:
            True if successful, False otherwise.
        """
        if data is not None and isinstance(data, dict):
            try:
                result = self.collection.insert_one(data)
                return True if result.inserted_id else False
            except Exception as e:
                print("Create failed:", e)
                return False
        else:
            print("Invalid data format. Must be a dictionary.")
            return False

    def read(self, query):
        """
        Query for documents using find().

        Returns:
            A list of matching documents, or empty list.
        """
        if query is None:
            query = {}

        try:
            documents = list(self.collection.find(query))
            return documents
        except Exception as e:
            print("Read failed:", e)
            return []

    def update(self, query, new_values):
        """
        Update document(s) matching the query.

        Returns:
            Number of documents updated.
        """
        if query is None or new_values is None:
            print("Query and update values cannot be None.")
            return 0

        try:
            result = self.collection.update_many(query, {"$set": new_values})
            return result.modified_count
        except Exception as e:
            print("Update failed:", e)
            return 0

    def delete(self, query):
        """
        Delete document(s) matching the query.

        Returns:
            Number of documents deleted.
        """
        if query is None:
            print("Query cannot be None.")
            return 0

        try:
            result = self.collection.delete_many(query)
            return result.deleted_count
        except Exception as e:
            print("Delete failed:", e)
            return 0