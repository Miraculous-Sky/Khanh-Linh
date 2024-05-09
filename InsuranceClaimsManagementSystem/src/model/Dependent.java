package model;

import conf.SystemConfig;

public class Dependent extends Customer {
    private final PolicyHolder policyHolder;

    public Dependent(String customerID, String fullName, PolicyHolder policyHolder) {
        super(customerID, fullName);
        this.policyHolder = policyHolder;
        this.policyHolder.addDependents(this);
    }

    public PolicyHolder getPolicyHolder() {
        return policyHolder;
    }

    @Override
    public String toString() {
        return getCustomerID() + ":" + getFullName();
    }

    @Override
    public String format() {
        return getCustomerID() + SystemConfig.CSV_DELIMITER +
                getFullName() + SystemConfig.CSV_DELIMITER +
                "D" + SystemConfig.CSV_DELIMITER +
                policyHolder.getCustomerID();
    }
}
