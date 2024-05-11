package view.cli;

import service.ClaimService;
import service.CustomerService;

public class CommandFactory {
    private final ClaimService claimService;
    private final CustomerService customerService;

    public CommandFactory(ClaimService claimService, CustomerService customerService) {
        this.claimService = claimService;
        this.customerService = customerService;
    }

    public Command getCommand(CommandKey commandKey) {
        switch (commandKey) {
            case ADD_CLAIM:
                return new AddClaimCommand(claimService, customerService);
            case UPDATE_CLAIM:
                return new UpdateClaimCommand(claimService, customerService);
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
