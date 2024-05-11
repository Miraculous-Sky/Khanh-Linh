package view.cli;

import model.Claim;
import service.ClaimService;
import utils.InputHelper;

import java.util.Scanner;

public class DeleteClaimCommand implements Command {
    private final ClaimService claimService;

    public DeleteClaimCommand(ClaimService claimService) {
        this.claimService = claimService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Deleting a claim...");
        String id = InputHelper.getExistedClaimId(scanner, false, claimService);
        Claim claim = claimService.getOne(id);
        if (claimService.delete(claim)) {
            claim.getInsuredPerson().removeClaim(claim);
            System.out.println("Claim deleted successfully.");
        } else {
            System.out.println("Failed to delete claim or claim not found.");
        }
    }
}
