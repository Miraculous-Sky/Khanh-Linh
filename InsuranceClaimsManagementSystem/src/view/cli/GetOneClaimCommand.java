package view.cli;

import model.Claim;
import service.ClaimService;
import utils.InputHelper;

import java.util.Scanner;

public class GetOneClaimCommand implements Command {
    private ClaimService claimService;

    public GetOneClaimCommand(ClaimService claimService) {
        this.claimService = claimService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Retrieving a claim...");
        String id = InputHelper.getExistedClaimId(scanner, false, claimService);
        Claim customer = claimService.getOne(id);
        System.out.println(customer);
    }
}
