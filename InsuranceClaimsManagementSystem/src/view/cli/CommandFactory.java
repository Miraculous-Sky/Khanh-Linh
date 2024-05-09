package view.cli;

import service.ClaimService;

public class CommandFactory {
    private final ClaimService claimService;

    public CommandFactory(ClaimService claimService) {
        this.claimService = claimService;
    }

    public Command getCommand(CommandKey commandKey) {
        switch (commandKey) {
            case ADD_CLAIM:
                return new AddClaimCommand(claimService);
            case UPDATE_CLAIM:
                return new UpdateClaimCommand(claimService);
            case DELETE_CLAIM:
                return new DeleteClaimCommand(claimService);
            case GET_ONE_CLAIM:
                return new GetOneClaimCommand(claimService);
            case GET_ALL_CLAIMS:
                return new GetAllClaimsCommand(claimService);
            default:
                return null;
        }
    }
}
