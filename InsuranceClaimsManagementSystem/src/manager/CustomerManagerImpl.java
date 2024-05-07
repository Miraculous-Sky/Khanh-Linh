package manager;

import database.InMemoryDB;
import model.Customer;
import model.PolicyHolder;

import java.util.SortedSet;

public class CustomerManagerImpl {
    private final SortedSet<Customer> customers = InMemoryDB.getInstance().getCustomers();

    public boolean add(Customer customer) {
        return customers.add(customer);
    }

    public Customer getOne(String id) {
        return customers.stream().filter(c -> c.getCustomerID().equals(id)).findAny().orElse(null);
    }

    public PolicyHolder getPolicyHolder(String id) {
        return customers.stream()
                .filter(c -> c instanceof PolicyHolder)
                .map(c -> (PolicyHolder) c)
                .filter(p -> p.getCustomerID().equals(id))
                .findAny().orElse(null);
    }


    public SortedSet<Customer> getAll() {
        return customers;
    }
}
