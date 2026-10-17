from CRUD_Python_Module import AnimalShelter

# Connect to the local MongoDB database
shelter = AnimalShelter()

# Representative test records for the AAC animal database
test_animals = [
    {
        "animal_id": "TEST001",
        "name": "Buddy",
        "animal_type": "Dog",
        "breed": "Labrador Retriever Mix",
        "sex_upon_outcome": "Intact Male",
        "age_upon_outcome_in_weeks": 52
    },
    {
        "animal_id": "TEST002",
        "name": "Luna",
        "animal_type": "Dog",
        "breed": "Chesapeake Bay Retriever",
        "sex_upon_outcome": "Intact Female",
        "age_upon_outcome_in_weeks": 78
    },
    {
        "animal_id": "TEST003",
        "name": "Max",
        "animal_type": "Dog",
        "breed": "German Shepherd",
        "sex_upon_outcome": "Neutered Male",
        "age_upon_outcome_in_weeks": 104
    },
    {
        "animal_id": "TEST004",
        "name": "Daisy",
        "animal_type": "Dog",
        "breed": "Golden Retriever",
        "sex_upon_outcome": "Intact Female",
        "age_upon_outcome_in_weeks": 40
    },
    {
        "animal_id": "TEST005",
        "name": "Rocky",
        "animal_type": "Dog",
        "breed": "Labrador Retriever",
        "sex_upon_outcome": "Intact Male",
        "age_upon_outcome_in_weeks": 130
    }
]

print("\n--- CREATE TEST ---")

for animal in test_animals:
    shelter.create(animal)

print("\n--- READ TEST ---")

animals = shelter.read(
    {"animal_type": "Dog"},
    sort_field="age_upon_outcome_in_weeks",
    ascending=True,
    limit=5
)

for animal in animals:
    print(
        animal["animal_id"],
        animal["name"],
        animal["breed"],
        animal["age_upon_outcome_in_weeks"]
    )

print("\n--- ADVANCED SEARCH TEST ---")

results = shelter.advanced_search(
    breeds=["Labrador", "Chesapeake"],
    min_age=26,
    max_age=156,
    limit=10
)

for animal in results:
    print(
        animal["animal_id"],
        animal["name"],
        animal["breed"]
    )