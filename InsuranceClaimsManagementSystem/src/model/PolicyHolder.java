package model;

import conf.SystemConfig;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class PolicyHolder extends Customer {
    private final SortedSet<Customer> dependents = new TreeSet<>();

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


    @Override
    public String format() {
        return getCustomerID() + SystemConfig.CSV_DELIMITER +
                getFullName() + SystemConfig.CSV_DELIMITER +
                "H";
    }
}
