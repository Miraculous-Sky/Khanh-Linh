package view.cli;

import service.ClaimService;

public class DeleteClaimCommand implements Command {
    private final ClaimService claimService;

    public DeleteClaimCommand(ClaimService claimService) {
        this.claimService = claimService;
    }

    @Override
    public void execute() {
        String id = InputHelper.getString("Enter claim ID to delete: ");
        if (claimService.deleteClaim(id)) {
            System.out.println("Claim deleted successfully.");
        } else {
            System.out.println("Failed to delete claim or claim not found.");
        }
    }
}
