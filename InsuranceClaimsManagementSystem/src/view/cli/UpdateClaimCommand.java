package view.cli;

import model.Claim;
import model.Customer;
import model.Status;
import service.ClaimService;
import service.CustomerService;
import utils.Converter;
import utils.InputHelper;

import java.util.Date;
import java.util.Scanner;
import java.util.SortedSet;

public class UpdateClaimCommand implements Command {
    private final ClaimService claimService;
    private final CustomerService customerService;

    public UpdateClaimCommand(ClaimService claimService, CustomerService customerService) {
        this.claimService = claimService;
        this.customerService = customerService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Editing a claim:");
        String id = InputHelper.getExistedClaimId(scanner, false, claimService);
        Claim oldClaim = claimService.getOne(id);
        Date claimDate = InputHelper.getDate(scanner, "Enter claim date [" + Converter.formatDate(oldClaim.getClaimDate() )+ "]: ", true);
        if (claimDate != null)
            oldClaim.setClaimDate(claimDate);
        String insuredPersonId = InputHelper.getCustomerId(scanner, "Enter insured person ID [" + oldClaim.getInsuredPerson().getCustomerID() + "]: ", true);
        if (insuredPersonId != null) {
            Customer insuredPerson = customerService.getOne(insuredPersonId);
            if (!insuredPerson.equals(oldClaim.getInsuredPerson())) {
                oldClaim.getInsuredPerson().removeClaim(oldClaim);
                oldClaim.setInsuredPerson(insuredPerson);
            }
        }
        Date examDate = InputHelper.getDate(scanner, "Enter exam date [" + Converter.formatDate(oldClaim.getExamDate()) + "]: ", true);
        if (examDate != null)
            oldClaim.setExamDate(examDate);

        SortedSet<String> documents = InputHelper.getDocuments(scanner, true);
        if (documents != null) {
            oldClaim.getDocuments().clear();
            documents.forEach(oldClaim::addDocuments);
        }

        Double amount = InputHelper.getClaimAmount(scanner, true);
        if (amount != null) oldClaim.setClaimAmount(amount);

        Status status = InputHelper.getClaimStatus(scanner, true);
        if (status != null) oldClaim.setStatus(status);

        String bankingInfo = InputHelper.getBankingInfo(scanner, true);
        if (bankingInfo != null) oldClaim.setReceiverBankingInfo(bankingInfo);

        claimService.update(oldClaim);
        System.out.println("Claim updated successfully");
    }
}
