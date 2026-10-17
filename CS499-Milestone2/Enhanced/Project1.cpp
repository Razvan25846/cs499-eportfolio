#include <iostream>
#include <limits>
#include <string>
#include <vector>

using namespace std;

// Represents one investment company client.
class Client {
private:
    string name;
    int serviceChoice;

public:
    Client(const string& clientName, int service)
        : name(clientName), serviceChoice(service) {
    }

    string getName() const {
        return name;
    }

    int getServiceChoice() const {
        return serviceChoice;
    }

    void setServiceChoice(int service) {
        serviceChoice = service;
    }
};

// Function prototypes
bool checkUserPermissionAccess();
void displayInfo(const vector<Client>& clients);
void changeCustomerChoice(vector<Client>& clients);
int getValidatedInput(int minimum, int maximum);

int main() {
    // Store all client information in one collection instead of
    // separate global variables for every client.
    vector<Client> clients = {
        Client("Bob Jones", 1),
        Client("Sarah Davis", 2),
        Client("Amy Friendly", 1),
        Client("Johnny Smith", 1),
        Client("Carol Spears", 2)
    };

    cout << "Created by Roberto Castro" << endl;
    cout << "Hello! Welcome to our Investment Company" << endl;

    // Continue requesting credentials until the correct password is entered.
    while (!checkUserPermissionAccess()) {
        cout << "Invalid Password. Please try again." << endl;
    }

    int choice = 0;

    while (choice != 3) {
        cout << "\nWhat would you like to do?" << endl;
        cout << "DISPLAY the client list (enter 1)" << endl;
        cout << "CHANGE a client's choice (enter 2)" << endl;
        cout << "Exit the program (enter 3)" << endl;

        choice = getValidatedInput(1, 3);

        if (choice == 1) {
            displayInfo(clients);
        }
        else if (choice == 2) {
            changeCustomerChoice(clients);
        }
    }

    cout << "Exiting program." << endl;

    return 0;
}

// Checks the user's password before allowing access.
bool checkUserPermissionAccess() {
    string username;
    string enteredPassword;

    cout << "Enter your username: " << endl;
    cin >> username;

    cout << "Enter your password: " << endl;
    cin >> enteredPassword;

    return enteredPassword == "123";
}

// Displays each client and the service currently selected.
void displayInfo(const vector<Client>& clients) {
    cout << "\nClient's Name    Service Selected "
         << "(1 = Brokerage, 2 = Retirement)" << endl;

    for (size_t i = 0; i < clients.size(); ++i) {
        cout << i + 1 << ". "
             << clients[i].getName()
             << " selected option "
             << clients[i].getServiceChoice()
             << endl;
    }
}

// Allows the user to select a client and update the client's service.
void changeCustomerChoice(vector<Client>& clients) {
    cout << "\nEnter the number of the client that you wish to change:"
         << endl;

    int clientNumber =
        getValidatedInput(1, static_cast<int>(clients.size()));

    cout << "Please enter the client's new service choice "
         << "(1 = Brokerage, 2 = Retirement):" << endl;

    int newService = getValidatedInput(1, 2);

    clients[clientNumber - 1].setServiceChoice(newService);

    cout << clients[clientNumber - 1].getName()
         << "'s service choice was updated successfully."
         << endl;
}

// Validates numeric input and prevents invalid menu selections.
int getValidatedInput(int minimum, int maximum) {
    int value;

    while (true) {
        cin >> value;

        if (cin.fail()) {
            cin.clear();
            cin.ignore(numeric_limits<streamsize>::max(), '\n');

            cout << "Invalid input. Please enter a number between "
                 << minimum << " and " << maximum << ": "
                 << endl;
        }
        else if (value < minimum || value > maximum) {
            cout << "Invalid choice. Please enter a number between "
                 << minimum << " and " << maximum << ": "
                 << endl;
        }
        else {
            cin.ignore(numeric_limits<streamsize>::max(), '\n');
            return value;
        }
    }
}