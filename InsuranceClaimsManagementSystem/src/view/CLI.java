package view;

import database.InMemoryDB;
import model.Claim;
import model.Customer;
import model.PolicyHolder;
import model.Status;
import utils.FileProcessing;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;

public class CLI {
    public void start() {
        ClaimProcessManagerImpl claimProcessManager = new ClaimProcessManagerImpl(inMemoryDB1);
        while (true) {
            System.out.println("_____MENU_____");
            System.out.println(" 1.Add\n 2.Update \n 3.Delete \n 4.getOne \n 5.getAll \n 6.Exit");
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter the number: ");
            String claimID;
            switch (Integer.parseInt(scanner.nextLine())) {
                case 1:
                    Claim claim = new Claim();
                    ArrayList<String> documents = new ArrayList<>();
                    System.out.println("Please enter the ID a claim (f-xxxxxxxxxx): ");
                    claimID = scanner.nextLine();
                    while (claimProcessManager.getOne(claimID) != null) {
                        System.out.println("Please re-check your claim ID and enter it: ");
                        claimID = scanner.nextLine();
                    }
                    claim.setId(claimID);
                    System.out.println("Please enter the claim date: ");
                    DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date claimDate = df.parse(scanner.nextLine());
                    claim.setClaimDate(claimDate);
                    System.out.println("Please enter the ID of the insured person (c-xxxxxxx): ");
                    String insuredPersonID = scanner.nextLine();
                    boolean found = false;
                    do {
                        // Iterate through customers to find a match
                        for (Customer c : inMemoryDB1.getCustomers()) {
                            if (c.getCustomerID().equals(insuredPersonID)) {
                                claim.setInsuredPerson(c);
                                found = true; // Set the flag to true if match is found
                                break; // Break the loop once a match is found
                            }
                        }
                        // Prompt for re-entry if no match is found
                        if (!found) {
                            System.out.println("Insured person ID not found. Please re-check the ID of the insured person and enter it: ");
                            insuredPersonID = scanner.nextLine();
                        }
                    } while (!found); // Loop until a match is found
                    claim.setCardNumber(claim.getInsuredPerson().getInsuranceCard().getCardNumber());
                    System.out.println("Please enter the exam date: ");
                    Date examDate = df.parse(scanner.nextLine());
                    claim.setExamDate(examDate);
                    System.out.println("How many document you want to add ?");
                    int n = Integer.parseInt(scanner.nextLine());
                    for (int i = 0; i < n; i++) {
                        System.out.println("Please enter the document ( with format ClaimId_CardNumber_DocumentName.pdf ): ");
                        String document = scanner.nextLine();
                        documents.add(document);
                    }
                    claim.addDocuments(documents);
                    System.out.println("Please enter the claim amount: ");
                    double claimAmount = Double.parseDouble(scanner.nextLine());
                    claim.setClaimAmount(claimAmount);
                    System.out.println("Please enter the status of claim : ");
                    Status status = Status.valueOf(scanner.nextLine());
                    claim.setStatus(status);
                    System.out.println("Please enter the receiver information ( Bank – Name – Number ): ");
                    String bankNameNumber = scanner.nextLine();
                    claim.setReceiverBankingInfo(bankNameNumber);
                    claimProcessManager.add(claim);
                    System.out.println(claim);
                    break;
                case 2:
                    ArrayList<String> documents1 = new ArrayList<>();
                    DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
                    System.out.println("Please enter the ID a claim: ");
                    claimID = scanner.nextLine();
                    Claim claim1 = claimProcessManager.getOne(claimID);
                    System.out.println("Please enter the claim date: " + df1.format(claim1.getClaimDate()));
                    String line = scanner.nextLine();
                    if (!line.isBlank()) {
                        Date claimDate1 = df1.parse(line);
                        claim1.setClaimDate(claimDate1);
                    }
                    System.out.println("Please enter the ID of the insured person (c-xxxxxxx): ");
                    line = scanner.nextLine();
                    if (!line.isBlank()) {
                        boolean found1 = false;
                        do {
                            // Iterate through customers to find a match
                            for (Customer c : inMemoryDB1.getCustomers()) {
                                if (c.getCustomerID().equals(line)) {
                                    claim1.setInsuredPerson(c);
                                    found1 = true; // Set the flag to true if match is found
                                    break; // Break the loop once a match is found
                                }
                            }
                            // Prompt for re-entry if no match is found
                            if (!found1) {
                                System.out.println("Insured person ID not found. Please re-check the ID of the insured person and enter it: ");
                                line = scanner.nextLine();
                            }
                        } while (!found1); // Loop until a match is found
                    }
                    claim1.setCardNumber(claim1.getInsuredPerson().getInsuranceCard().getCardNumber());
                    System.out.println("Please enter the exam date: ");
                    line = scanner.nextLine();
                    if (!line.isBlank()) {
                        Date claimDate1 = df1.parse(line);
                        claim1.setExamDate(claimDate1);
                    }
                    System.out.println("How many document you want to add ?");
                    line = scanner.nextLine();
                    if (!line.isBlank()) {
                        int n1 = Integer.parseInt(line);
                        for (int i = 0; i < n1; i++) {
                            System.out.println("Please enter the document ( with format ClaimId_CardNumber_DocumentName.pdf ): ");
                            String document = scanner.nextLine();
                            documents1.add(document);
                        }
                        claim1.addDocuments(documents1);
                    }
                    System.out.println("Please enter the claim amount: ");
                    line = scanner.nextLine();
                    if (!line.isBlank()) {
                        double claimAmount1 = Double.parseDouble(line);
                        claim1.setClaimAmount(claimAmount1);
                    }
                    System.out.println("Please enter the status of claim : ");
                    line = scanner.nextLine();
                    if (!line.isBlank()) {
                        Status status1 = Status.valueOf(line);
                        claim1.setStatus(status1);
                    }
                    System.out.println("Please enter the receiver information ( Bank – Name – Number ): ");
                    line = scanner.nextLine();
                    if (!line.isBlank()) {
                        claim1.setReceiverBankingInfo(line);
                    }
                    claimProcessManager.update(claim1);
                    System.out.println(claim1);
                    break;
                case 3:
                    Claim claim2 = new Claim();
                    System.out.println("Please enter the ID a claim: ");
                    claimID = scanner.nextLine();
                    for (Claim c : inMemoryDB1.getClaims()) {
                        if (c.getId().equals(claimID)) {
                            claim2 = c;
                        }
                    }
                    claimProcessManager.delete(claim2);
                    System.out.println(inMemoryDB1.getClaims());
                    break;
                case 4:
                    Claim claim3 = new Claim();
                    System.out.println("Please enter the ID a claim: ");
                    claimID = scanner.nextLine();
                    claim3.setId(claimID);
                    System.out.println(claimProcessManager.getOne(claimID));
                    break;
                case 5:
                    System.out.println(claimProcessManager.getAll());
                    break;
                case 6:
                    fileProcessing.writeToFile();

                    return;

            }
        }
    }
}
