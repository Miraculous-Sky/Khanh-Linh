package model;

import conf.SystemConfig;

public class Dependent extends Customer {
    private PolicyHolder policyHolder;

    public Dependent(String customerID, String fullName) {
        super(customerID, fullName);
    }

    public PolicyHolder getPolicyHolder() {
        return policyHolder;
    }
    public void setPolicyHolder(PolicyHolder policyHolder) {
        this.policyHolder = policyHolder;
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
