package view.cli;

import service.ClaimService;

public class GetOneClaimCommand implements Command {
    private ClaimService customerService;

    public GetOneClaimCommand(ClaimService customerService) {
        this.customerService = customerService;
    }

    @Override
    public void execute() {
        String customerId = InputHelper.getString("Enter customer ID: ");
        Optional<Customer> customer = customerService.getCustomerById(customerId);
        customer.ifPresentOrElse(
                c -> System.out.println("Customer details: " + c),
                () -> System.out.println("Customer not found.")
        );
    }
}
