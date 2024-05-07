package utils;

import model.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Scanner;

public class FileProcessing {
    public static void readCustomerFile() {
        try {
            File customerFile = new File("customers.csv");
            Scanner myReader = new Scanner(customerFile);
            myReader.nextLine();
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] line = data.split(COMMA_DELIMITER, -1);
                if (line[2].equals("H")) { //Check if the customer is policy holder or not
                    PolicyHolder policyHolder = new PolicyHolder();
                    policyHolder.setCustomerID(line[0]);
                    policyHolder.setFullName(line[1]);
                    inMemoryDB.getCustomers().add(policyHolder);
                } else if (line[2].equals("D")) { //Check if the customer is dependent or not
                    Dependent dependent = new Dependent();
                    dependent.setCustomerID(line[0]);
                    dependent.setFullName(line[1]);
                    inMemoryDB.getCustomers().add(dependent);
                    for (Customer customer : inMemoryDB.getCustomers()) {
                        if (customer.getCustomerID().equals(line[3])) {
                            if (customer instanceof PolicyHolder) {
                                policyHolder.getDependents().add(dependent);
                            }
                        }

                    }
                } else {
                    System.out.println("Re-enter the role of the customer of " + line[0]);
                }
            }
            inMemoryDB.getCustomers().sort((a, b) -> a.compareTo(b));//Arrange the customer list in order of the customer ID
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

    }

    void readInsuranceCard() throws FileNotFoundException, ParseException {
        File insuranceCardFile = new File("insuranceCards.csv");
        Scanner myReader = new Scanner(insuranceCardFile);
        myReader.nextLine();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        while (myReader.hasNextLine()) {
            InsuranceCard insuranceCard = new InsuranceCard();
            String line = myReader.nextLine();
            String[] words = line.split(COMMA_DELIMITER);
            Date claimDate = formatter.parse(words[3]);
            insuranceCard.setExpirationDate(claimDate);
            insuranceCard.setCardNumber(words[0]);
            insuranceCard.setPolicyOwner(words[2]);
            Customer customer = null;
            for (Customer c : inMemoryDB.getCustomers()) { //Find the card holder
                if (c.getCustomerID().equals(words[1])) {
                    customer = c;
                }
            }
            if (customer == null) {
                System.out.println("Can't find the customer");
            } else {
                insuranceCard.setCardHolder(customer);
            }
            inMemoryDB.getInsuranceCards().add(insuranceCard);
            customer.setInsuranceCard(insuranceCard);
        }
        myReader.close();

    }

    public void readClaim() throws FileNotFoundException, ParseException {
        File claimFile = new File("claims.csv");
        Scanner myReader = new Scanner(claimFile);
        myReader.nextLine();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
        while (myReader.hasNextLine()) {
            String line = myReader.nextLine();
            String[] data = line.split(COMMA_DELIMITER, -1);
            Date claimDate = formatter.parse(data[1]);
            Date examDate = formatter.parse(data[4]);
            Claim claim = new Claim();
            Customer customer = null;
            InsuranceCard insuranceCard = null;
            claim.setId(data[0]);
            claim.setClaimDate(claimDate);
            for (Customer c : inMemoryDB.getCustomers()) { //Find the insured person
                if (c.getCustomerID().equals(data[2])) {
                    customer = c;
                }
            }
            if (customer == null) {
                System.out.println("Couldn't find the insured person");
            } else {
                claim.setInsuredPerson(customer);
            }
            claim.setCardNumber(data[3]);
            claim.setExamDate(examDate);
            String[] document = data[5].split(SEMICOLON_DELIMITER);
            ArrayList<String> documents = new ArrayList<>(Arrays.asList(document));
            claim.addDocuments(documents);
            claim.setClaimAmount(Double.parseDouble(data[6]));
            // Switch keyword
            switch (data[7].charAt(0)) { //Set the status of the claim
                // Case statements
                case 'N':
                    claim.setStatus(Status.NEW);
                    break;
                case 'D':
                    claim.setStatus(Status.DONE);
                    break;
                case 'P':
                    claim.setStatus(Status.PROCESSING);
                    break;
            }
            claim.setReceiverBankingInfo(data[8]);
            inMemoryDB.getClaims().add(claim);
            customer.addClaim(claim);
        }

    }

    public void writeToFile() {
        //id,claimDate,insuredPersonId,cardNumber,examDate,documents,claimAmount,status,receiverBankingInfo

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd");
            FileWriter myWriter = new FileWriter("claims.csv");
            myWriter.write("id,claimDate,insuredPersonId,cardNumber,examDate,documents,claimAmount,status,receiverBankingInfo\n");
            for (int i = 0; i < inMemoryDB.getClaims().size(); i++) {
                Claim claim = inMemoryDB.getClaims().get(i);
                myWriter.write(claim.getId());
                myWriter.write(",");
                myWriter.write(formatter.format(claim.getClaimDate()));
                myWriter.write(",");
                myWriter.write(claim.getInsuredPerson().getCustomerID());
                myWriter.write(",");
                myWriter.write(claim.getCardNumber());
                myWriter.write(",");
                myWriter.write(formatter.format(claim.getExamDate()));
                myWriter.write(",");
                for (int j = 0; j < claim.getDocuments().size() - 1; j++) {
                    myWriter.write(claim.getDocuments().get(j));
                    myWriter.write(";");
                }
                if (claim.getDocuments().size() > 0) {
                    myWriter.write(claim.getDocuments().get(claim.getDocuments().size() - 1));
                }
                myWriter.write(",");
                myWriter.write(String.valueOf(claim.getClaimAmount()));
                myWriter.write(",");
                myWriter.write(claim.getStatus().name().charAt(0));
                myWriter.write(",");
                myWriter.write(claim.getReceiverBankingInfo());
                myWriter.write("\n");
            }
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

}
