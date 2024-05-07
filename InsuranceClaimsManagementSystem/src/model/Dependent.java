package model;

public class Dependent extends Customer {
    public Dependent(String customerID, String fullName) {
        super(customerID, fullName);
    }

    @Override
    public String toString() {
        return getCustomerID() + ":" + getFullName();
    }
}
