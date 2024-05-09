package view.cli;

import service.ClaimService;

public class AddClaimCommand implements Command {
    private final ClaimService claimService;

    public AddClaimCommand(ClaimService claimService) {
        this.claimService = claimService;
    }

    @Override
    public void execute() {
        System.out.println("Adding a new claim:");
        String id = InputHelper.getString("Enter claim ID: ");
        String claimantName = InputHelper.getString("Enter claimant's name: ");
        String date = InputHelper.getString("Enter date (YYYY-MM-DD): ");
        String type = InputHelper.getString("Enter claim type: ");
        double amount = InputHelper.getDouble("Enter claim amount: ");

        Claim claim = new Claim(id, claimantName, date, type, amount);
        claimService.addClaim(claim);
        System.out.println("Claim added successfully: " + claim);
    }
}
