package view.cli;

import service.ClaimService;

public class UpdateClaimCommand implements Command {
    private final ClaimService claimService;

    public UpdateClaimCommand(ClaimService claimService) {
        this.claimService = claimService;
    }

    @Override
    public void execute() {
        String id = InputHelper.getString("Enter claim ID to update: ");
        Claim existingClaim = claimService.getClaim(id).orElse(null);
        if (existingClaim == null) {
            System.out.println("No claim found with ID: " + id);
            return;
        }

        System.out.println("Current claim details: " + existingClaim);
        System.out.println("Enter new details (leave blank to keep current):");

        String newClaimantName = InputHelper.getString("Enter new claimant's name: ");
        String newDate = InputHelper.getString("Enter new date (YYYY-MM-DD): ");
        String newType = InputHelper.getString("Enter new claim type: ");
        String amountInput = InputHelper.getString("Enter new claim amount: ");

        // Update claimant's name if provided
        if (!newClaimantName.isEmpty()) {
            existingClaim.setClaimantName(newClaimantName);
        }

        // Update date if provided
        if (!newDate.isEmpty()) {
            existingClaim.setDate(newDate);
        }

        // Update type if provided
        if (!newType.isEmpty()) {
            existingClaim.setType(newType);
        }

        // Update amount if provided and valid
        if (!amountInput.isEmpty()) {
            try {
                double newAmount = Double.parseDouble(amountInput);
                existingClaim.setAmount(newAmount);
            } catch (NumberFormatException e) {
                System.out.println("Invalid amount entered. Keeping the current amount.");
            }
        }

        // After all potential updates
        claimService.updateClaim(existingClaim);
        System.out.println("Claim updated successfully: " + existingClaim);
    }
}
