package service;

import model.Customer;
import model.PolicyHolder;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class CustomerService extends Service<Customer> {
    public CustomerService(SortedSet<Customer> data) {
        super(data);
    }

    public PolicyHolder getPolicyHolder(String id) {
        return getAll().stream()
                .filter(c -> c instanceof PolicyHolder)
                .map(c -> (PolicyHolder) c)
                .filter(p -> p.getCustomerID().equals(id))
                .findAny().orElse(null);
    }

}
