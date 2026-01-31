package ywork.script

import ywork.domain.Employee

void displayEmployeeNameLength(Employee emp) {
    printf "employee: %s, name: %s, name length:: %s%n",
            emp, emp?.name, emp?.name?.length()
}

displayEmployeeNameLength(new Employee("yy", "Admin"))
displayEmployeeNameLength(null);
displayEmployeeNameLength(new Employee(null, null))
