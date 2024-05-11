package view.cli;

import model.Claim;
import service.ClaimService;

import java.util.Scanner;
import java.util.SortedSet;

public class GetAllClaimsCommand implements Command {
    private ClaimService claimService;

    public GetAllClaimsCommand(ClaimService claimService) {
        this.claimService = claimService;
    }

    @Override
    public void execute(Scanner scanner) {
        SortedSet<Claim> allCustomers = claimService.getAll();
        if (allCustomers.isEmpty()) {
            System.out.println("No claim found.");
        } else {
            System.out.println("Listing all claims:");
            allCustomers.forEach(System.out::println);
        }
    }
}
