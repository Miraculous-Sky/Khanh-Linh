package model;

import java.util.ArrayList;

public abstract class Customer implements Comparable<Customer> {
    private final ArrayList<Claim> claims = new ArrayList<>();
    private String customerID; //(with the format c-numbers; 7 numbers) ;
    private String fullName;
    private InsuranceCard insuranceCard;

    public Customer(String customerID, String fullName) {
        this.customerID = customerID;
        this.fullName = fullName;
    }

    public String getCustomerID() {
        return customerID;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public InsuranceCard getInsuranceCard() {
        return insuranceCard;
    }

    public void setInsuranceCard(InsuranceCard insuranceCard) {
        this.insuranceCard = insuranceCard;
    }

    public ArrayList<Claim> getClaims() {
        return claims;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;

        Customer customer = (Customer) o;
        return customerID.equals(customer.customerID);
    }

    @Override
    public int hashCode() {
        return customerID.hashCode();
    }

    public void addClaim(Claim claim) {
        this.claims.add(claim);
    }

    @Override
    public int compareTo(Customer o) {
        return this.customerID.compareTo(o.customerID);
    }
}
