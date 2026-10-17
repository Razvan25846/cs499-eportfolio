#include <iostream>
#include <string>
using namespace std;

char username[100];
int choice;
int changechoice;
int newservice;
int answer;

char name1[16] = "Bob Jones";
char name2[16] = "Sarah Davis";
char name3[16] = "Amy Friendly";
char name4[16] = "Johnny Smith";
char name5[16] = "Carol Spears";

int num1 = 1;
int num2 = 2;
int num3 = 1;
int num4 = 1;
int num5 = 2;

char password[20] = "123";

int CheckUserPermissionAccess();
void DisplayInfo();
void ChangeCustomerChoice();

int main() {
    cout << "Created by Roberto Castro" << endl;
    cout << "Hello! Welcome to our Investment Company" << endl;

    do {
        answer = CheckUserPermissionAccess();
        if (answer != 1) {
            cout << "Invalid Password. Please try again" << endl;
        }
    } while (answer != 1);

    do {
        cout << "What would you like to do?" << endl;
        cout << "DISPLAY the client list (enter 1)" << endl;
        cout << "CHANGE a client's choice (enter 2)" << endl;
        cout << "Exit the program.. (enter 3)" << endl;
        cin >> choice;
        cout << "You chose " << choice << endl;

        if (choice == 1) {
            DisplayInfo();
        } else if (choice == 2) {
            ChangeCustomerChoice();
        }
    } while (choice != 3);

    return 0;
}

int CheckUserPermissionAccess() {
    string enteredPassword = "";
    int result = 0;

    cout << "Enter your username: " << endl;
    cin >> username;
    cout << "Enter your password: " << endl;
    cin >> enteredPassword;

    result = enteredPassword.compare("123");
    if (result == 0) {
        return 1;
    }
    return 2;
}

void DisplayInfo() {
    cout << "  Client's Name    Service Selected (1 = Brokerage, 2 = Retirement)" << endl;
    cout << "1. " << name1 << " selected option " << num1 << endl;
    cout << "2. " << name2 << " selected option " << num2 << endl;
    cout << "3. " << name3 << " selected option " << num3 << endl;
    cout << "4. " << name4 << " selected option " << num4 << endl;
    cout << "5. " << name5 << " selected option " << num5 << endl;
}

void ChangeCustomerChoice() {
    cout << "Enter the number of the client that you wish to change" << endl;
    cin >> changechoice;
    cout << "Please enter the client's new service choice (1 = Brokerage, 2 = Retirement)" << endl;
    cin >> newservice;

    if (changechoice == 1) {
        num1 = newservice;
    } else if (changechoice == 2) {
        num2 = newservice;
    } else if (changechoice == 3) {
        num3 = newservice;
    } else if (changechoice == 4) {
        num4 = newservice;
    } else if (changechoice == 5) {
        num5 = newservice;
    }
}
