package database;

import model.Claim;
import model.Customer;
import model.InsuranceCard;

import java.util.ArrayList;

public class InMemoryDB {
    private final ArrayList<Claim> claims;
    private final ArrayList<InsuranceCard> insuranceCards;
    private final ArrayList<Customer> customers;

    private static final InMemoryDB inMemoryDB = new InMemoryDB();

    private InMemoryDB() {
        this.claims = new ArrayList<>();
        this.customers = new ArrayList<>();
        this.insuranceCards = new ArrayList<>();
    }

    public static InMemoryDB getInstance() {
        return inMemoryDB;
    }

    public ArrayList<Claim> getClaims() {
        return claims;
    }

    public ArrayList<InsuranceCard> getInsuranceCards() {
        return insuranceCards;
    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }
}
