package database;

import model.Claim;
import model.Customer;
import model.InsuranceCard;

import java.util.SortedSet;
import java.util.TreeSet;

public class InMemoryDB {
    private static final InMemoryDB inMemoryDB = new InMemoryDB();
    private final SortedSet<Claim> claims;
    private final SortedSet<InsuranceCard> insuranceCards;
    private final SortedSet<Customer> customers;

    private InMemoryDB() {
        this.claims = new TreeSet<>();
        this.customers = new TreeSet<>();
        this.insuranceCards = new TreeSet<>();
    }

    public static InMemoryDB getInstance() {
        return inMemoryDB;
    }

    // SELECT * FROM CLAIM ORDER BY ID
    public SortedSet<Claim> getClaims() {
        return claims;
    }

    // SELECT * FROM INSURANCE_CARD ORDER BY CARD_NUMBER
    public SortedSet<InsuranceCard> getInsuranceCards() {
        return insuranceCards;
    }

    // SELECT * FROM CUSTOMER ORDER BY CUSTOMER_ID
    public SortedSet<Customer> getCustomers() {
        return customers;
    }
}
