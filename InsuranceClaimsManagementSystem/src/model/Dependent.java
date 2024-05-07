package model;

public class Dependent extends Customer {
    private final PolicyHolder policyHolder;

    public Dependent(String customerID, String fullName, PolicyHolder policyHolder) {
        super(customerID, fullName);
        this.policyHolder = policyHolder;
    }

    public PolicyHolder getPolicyHolder() {
        return policyHolder;
    }

    @Override
    public String toString() {
        return getCustomerID() + ":" + getFullName();
    }
}
