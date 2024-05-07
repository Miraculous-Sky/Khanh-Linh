package model;

import java.util.HashSet;
import java.util.Set;

public class PolicyHolder extends Customer {
    private final Set<Customer> dependents = new HashSet<>();

    public PolicyHolder(String customerID, String fullName) {
        super(customerID, fullName);
    }

    public Set<Customer> getDependents() {
        return dependents;
    }

    public boolean addDependents(Customer dependent) {
        return this.dependents.add(dependent);
    }

    @Override
    public String toString() {
        return getCustomerID() + ":" + getFullName() + dependents;
    }


}
