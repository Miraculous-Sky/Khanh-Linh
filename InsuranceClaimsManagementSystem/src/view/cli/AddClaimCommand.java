package view.cli;

import model.Claim;
import model.Customer;
import model.Status;
import service.ClaimService;
import service.CustomerService;
import utils.InputHelper;

import java.util.Date;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedSet;

public class AddClaimCommand implements Command {
    private final ClaimService claimService;
    private final CustomerService customerService;

    public AddClaimCommand(ClaimService claimService, CustomerService customerService) {
        this.claimService = claimService;
        this.customerService = customerService;
    }

    @Override
    public void execute(Scanner scanner) {
        System.out.println("Adding a new claim:");
        String id = InputHelper.getNewClaimId(scanner, false, claimService);
        Date claimDate = InputHelper.getDate(scanner, "Enter claim date: ", false);
        Customer insuredPerson;
        do {
            insuredPerson = customerService.getOne(InputHelper.getCustomerId(scanner, "Enter insured person ID: ", false));
        }
        while (insuredPerson == null);
        Date examDate = InputHelper.getDate(scanner, "Enter exam date: ", true);
        SortedSet<String> documents = InputHelper.getDocuments(scanner, false);
        Double amount = InputHelper.getClaimAmount(scanner, false);
        Status status = InputHelper.getClaimStatus(scanner, false);
        String bankingInfo = InputHelper.getBankingInfo(scanner, false);

        Claim claim = new Claim(id, claimDate, insuredPerson, insuredPerson.getInsuranceCard().getCardNumber(),
                examDate, documents, amount, status, bankingInfo);
        claimService.add(claim);
        insuredPerson.addClaim(claim);
        System.out.println("Claim added successfully");
    }
}
