package view.cli;

import service.ClaimService;

public class GetAllClaimsCommand implements Command {
    private ClaimService customerService;

    public GetAllClaimsCommand(ClaimService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void execute() {
        List<Customer> allCustomers = customerService.getAllCustomers();
        if (allCustomers.isEmpty()) {
            System.out.println("No customers found.");
        } else {
            System.out.println("Listing all customers:");
            allCustomers.forEach(customer -> System.out.println(customer));
        }
    }
}
