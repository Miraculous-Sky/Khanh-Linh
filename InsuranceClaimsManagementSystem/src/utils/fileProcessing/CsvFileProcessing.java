package utils.fileProcessing;

import conf.SystemConfig;
import manager.CardManagerImpl;
import manager.ClaimProcessManager;
import manager.ClaimProcessManagerImpl;
import manager.CustomerManagerImpl;
import model.*;
import utils.Converter;
import utils.validation.CardValidation;
import utils.validation.ClaimValidation;
import utils.validation.CustomerValidation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class CsvFileProcessing implements FileProcessing {
    CustomerManagerImpl customerManager = new CustomerManagerImpl();
    CardManagerImpl cardManager = new CardManagerImpl();
    ClaimProcessManager claimProcessManager = new ClaimProcessManagerImpl();

    private static SortedSet<String> getDocs(String[] data, String cardNumber) {
        SortedSet<String> docs = new TreeSet<>();
        String[] splitDocs = data[4].split(SystemConfig.LIST_DELIMITER);
        for (String document : splitDocs) {
            // Regex to match "ClaimId_CardNumber_DocumentName.pdf"
            if (document.matches(data[0] + "_" + cardNumber + "_[\\w\\s]+\\.pdf")) {
                docs.add(document);
            } else if (document.toLowerCase().endsWith(".pdf")) {  // Only include if it's a PDF file
                // If document doesn't fit the format but is a PDF, format it correctly:
                String correctedDocument = data[0] + "_" + cardNumber + "_" + document;
                docs.add(correctedDocument);
            }
        }
        return docs;
    }

    @Override
    public boolean readCustomers() {
        return readCustomers(new ArrayList<>());
    }

    @Override
    public boolean readInsuranceCards() {
        return readInsuranceCards(new ArrayList<>());
    }

    @Override
    public boolean readClaims() {
        return readClaims(new ArrayList<>());
    }

    @Override
    public boolean writeToFiles() {
        return writeToFiles(new ArrayList<>());
    }

    @Override
    public boolean readCustomers(List<String> errorLogs) {
        File customerFile = new File(SystemConfig.CUSTOMERS_CSV_PATH);
        try (Scanner scanner = new Scanner(customerFile)) {
            scanner.nextLine(); // Skip the header line.
            String[] data;
            int line = 0;
            while (scanner.hasNextLine()) {
                line++;
                data = scanner.nextLine().split(SystemConfig.CSV_DELIMITER);

                // Validate data length and corresponding type.
                if ((data.length == 3 && !"H".equals(data[2])) || (data.length == 4 && !"D".equals(data[2]))) {
                    errorLogs.add("Incorrect data length or type at line " + line);
                    continue;
                }

                // Validate customer ID.
                if (CustomerValidation.isInvalidId(data[0]) || customerManager.getOne(data[0]) != null) {
                    errorLogs.add("Invalid or duplicate 'Customer ID' at line " + line);
                    continue;
                }

                Customer newCustomer;
                if ("D".equals(data[2])) {  // Handling for dependents.
                    if (CustomerValidation.isInvalidId(data[3])) {
                        errorLogs.add("Invalid 'Policy Holder ID' for dependent at line " + line);
                        continue;
                    }
                    PolicyHolder policyHolder = customerManager.getPolicyHolder(data[3]);
                    if (policyHolder == null) {
                        errorLogs.add("'Policy Holder' is not found for dependent at line " + line);
                        continue;
                    }
                    newCustomer = new Dependent(data[0], data[1], policyHolder);
                    policyHolder.addDependents(newCustomer);
                } else {  // Handling for policy holders.
                    newCustomer = new PolicyHolder(data[0], data[1]);
                }

                // Add new customer to the manager and only add dependent to policy holder if add is successful.
                customerManager.add(newCustomer);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean readInsuranceCards(List<String> errorLogs) {
        File cardFile = new File(SystemConfig.CARDS_CSV_PATH);
        try (Scanner myReader = new Scanner(cardFile)) {
            myReader.nextLine(); // Skip header

            String[] data;
            int line = 0;
            while (myReader.hasNextLine()) {
                line++;
                data = myReader.nextLine().split(SystemConfig.CSV_DELIMITER);

                if (data.length != 4) {
                    errorLogs.add("Incorrect data length at line " + line);
                    continue;
                }

                if (!CardValidation.isIdValid(data[0]) || cardManager.getOne(data[0]) != null) {
                    errorLogs.add("Invalid or duplicate 'Card ID' at line " + line);
                    continue;
                }

                Customer cardHolder = customerManager.getOne(data[1]);
                if (CustomerValidation.isInvalidId(data[1]) || cardHolder == null) {
                    errorLogs.add("Invalid 'Customer ID' or customer is not exist at line " + line);
                    continue;
                }

                Date expirationDate = Converter.parseDate(data[3]);
                if (expirationDate == null) {
                    errorLogs.add("Invalid 'Expiration Date' format at line " + line);
                    continue;
                }

                InsuranceCard card = new InsuranceCard(data[0], cardHolder, data[2], expirationDate);
                cardManager.add(card);
                cardHolder.setInsuranceCard(card);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean readClaims(List<String> errorLogs) {
        File claimFile = new File(SystemConfig.CLAIMS_CSV_PATH);
        try (Scanner scanner = new Scanner(claimFile)) {
            scanner.nextLine(); // Skip the header line
            String[] data;
            int line = 0;
            while (scanner.hasNextLine()) {
                line++;
                data = scanner.nextLine().split(SystemConfig.CSV_DELIMITER);

                // Validate data length.
                if (data.length != 8) {
                    errorLogs.add("Incorrect data length at line " + line);
                    continue;
                }

                // Validate claim ID.
                if (ClaimValidation.isInvalidId(data[0]) || claimProcessManager.getOne(data[0]) != null) {
                    errorLogs.add("Invalid or duplicate 'Claim ID' at line " + line);
                    continue;
                }

                // Validate insured person ID.
                Customer insuredPerson = customerManager.getOne(data[2]);
                if (CustomerValidation.isInvalidId(data[2]) || insuredPerson == null) {
                    errorLogs.add("Invalid 'Insured Person ID' or customer does not exist at line " + line);
                    continue;
                }

                // check card info of insured person
                if (insuredPerson.getInsuranceCard() == null) {
                    errorLogs.add("The customer does not have card at line " + line);
                    continue;
                }
                String cardNumber = insuredPerson.getInsuranceCard().getCardNumber();

                // Validate dates and convert to Date objects
                Date claimDate = Converter.parseDate(data[1]);
                Date examDate = Converter.parseDate(data[3]);
                if (claimDate == null || examDate == null) {
                    errorLogs.add("Invalid date format at line " + line);
                    continue;
                }

                double claimAmount = Converter.parseDouble(data[5]);
                // Validate and parse claim amount.
                if (ClaimValidation.isInvalidAmount(claimAmount)) {
                    errorLogs.add("Invalid 'Claim Amount' at line " + line);
                    continue;
                }

                // Validate status.
                Status status = Status.parse(data[6]);
                if (status == null) {
                    errorLogs.add("Invalid 'Status' at line " + line);
                    continue;
                }

                SortedSet<String> docs = getDocs(data, cardNumber);

                if (ClaimValidation.isInvalidBankingInfo(data[7])) {
                    errorLogs.add("Invalid 'Banking Info' at line " + line);
                    continue;
                }

                // Create and add the claim.
                Claim claim = new Claim(data[0], claimDate, insuredPerson, cardNumber, examDate, docs, claimAmount, status, data[7]);
                claimProcessManager.add(claim);
                insuredPerson.addClaim(claim);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }

    @Override
    public boolean writeToFiles(List<String> errorLogs) {
        boolean isSuccess = true;
        isSuccess &= writeCustomersToFile(errorLogs);
        isSuccess &= writeCardsToFile(errorLogs);
        isSuccess &= writeClaimsToFile(errorLogs);
        return isSuccess;
    }

    private boolean writeCustomersToFile(List<String> errorLogs) {
        try (FileWriter writer = new FileWriter(SystemConfig.CUSTOMERS_CSV_PATH)) {
            writer.write("id,fullName,type,policyHolderId\n");
            for (Customer customer : customerManager.getAll()) {
                if (customer instanceof Dependent) {
                    writer.write(customer.getCustomerID() + SystemConfig.CSV_DELIMITER + customer.getFullName() + SystemConfig.CSV_DELIMITER +
                            "D," + ((Dependent) customer).getPolicyHolder().getCustomerID() + "\n");
                } else {
                    writer.write(customer.getCustomerID() + SystemConfig.CSV_DELIMITER + customer.getFullName() + SystemConfig.CSV_DELIMITER + "P,\n");
                }
            }
            return true;
        } catch (IOException e) {
            errorLogs.add("Failed to write to customers file");
            return false;
        }
    }

    private boolean writeCardsToFile(List<String> errorLogs) {
        try (FileWriter writer = new FileWriter(SystemConfig.CARDS_CSV_PATH)) {
            writer.write("cardNumber,cardHolderId,policyOwner,expirationDate\n");  // Example header
            for (InsuranceCard card : cardManager.getAll()) {
                writer.write(card.getCardNumber() + SystemConfig.CSV_DELIMITER + card.getCardHolder().getCustomerID() +
                        SystemConfig.CSV_DELIMITER + card.getPolicyOwner() + SystemConfig.CSV_DELIMITER + Converter.formatDate(card.getExpirationDate()) + "\n");
            }
            return true;
        } catch (IOException e) {
            errorLogs.add("Failed to write to cards file");
            return false;
        }
    }

    private boolean writeClaimsToFile(List<String> errorLogs) {
        try (FileWriter writer = new FileWriter(SystemConfig.CLAIMS_CSV_PATH)) {
            writer.write("id,claimDate,insuredPersonId,examDate,documents,claimAmount,status,receiverBankingInfo\n");  // Example header
            for (Claim claim : claimProcessManager.getAll()) {
                writer.write(claim.getId() + SystemConfig.CSV_DELIMITER +
                        Converter.formatDate(claim.getClaimDate()) + SystemConfig.CSV_DELIMITER +
                        claim.getInsuredPerson().getCustomerID() + SystemConfig.CSV_DELIMITER +
                        Converter.formatDate(claim.getExamDate()) + SystemConfig.CSV_DELIMITER +
                        String.join(SystemConfig.LIST_DELIMITER, claim.getDocuments()) + SystemConfig.CSV_DELIMITER +
                        claim.getClaimAmount() + SystemConfig.CSV_DELIMITER +
                        claim.getStatus().getCode() + SystemConfig.CSV_DELIMITER +
                        claim.getReceiverBankingInfo() + "\n");
            }
            return true;
        } catch (IOException e) {
            errorLogs.add("Failed to write to claims file");
            return false;
        }
    }
}
