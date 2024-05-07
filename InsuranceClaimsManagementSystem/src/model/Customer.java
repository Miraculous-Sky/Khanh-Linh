package model;

import java.util.SortedSet;
import java.util.TreeSet;

public abstract class Customer implements Comparable<Customer> {
    private final SortedSet<Claim> claims = new TreeSet<>();
    private final String customerID; //(with the format c-numbers; 7 numbers) ;
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

    public SortedSet<Claim> getClaims() {
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
