package utils.fileProcessing;


import conf.SystemConfig;
import model.Formattable;
import model.*;
import service.CardService;
import service.ClaimService;
import service.CustomerService;
import utils.Converter;
import utils.Validation;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.function.BiFunction;

public class CsvFileProcessing implements FileProcessing {
    private final CustomerService customerService;
    private final CardService cardService;
    private final ClaimService claimService;

    public CsvFileProcessing(CustomerService customerService, CardService cardService, ClaimService claimService) {
        this.customerService = customerService;
        this.cardService = cardService;
        this.claimService = claimService;
    }

    @Override
    public List<String> loadFiles() {
        List<String> logs = new ArrayList<>();
        readEntities(SystemConfig.CUSTOMERS_CSV_PATH, this::parseAndAddCustomer, logs);
        readEntities(SystemConfig.CARDS_CSV_PATH, this::parseAndAddCard, logs);
        readEntities(SystemConfig.CLAIMS_CSV_PATH, this::parseAndAddClaim, logs);
        return logs;
    }

    private boolean readEntities(String filePath, BiFunction<String[], Integer, String> entityParser, List<String> logs) {
        logs.add("Read from file: " + filePath);
        try (Scanner scanner = new Scanner(new File(filePath))) {
            scanner.nextLine(); // Skip the header
            int lineNum = 1;
            int count = 0;
            while (scanner.hasNextLine()) {
                String[] data = scanner.nextLine().split(SystemConfig.CSV_DELIMITER);
                String parseError = entityParser.apply(data, lineNum);
                if (parseError != null) {
                    logs.add("\t" + parseError);
                } else count++;
                lineNum++;
            }
            logs.add("Successfully read " + count + (count > 1 ? " lines." : " line."));
            return true;
        } catch (FileNotFoundException e) {
            logs.add("Failed to read from file: " + filePath);
            return false;
        }
    }

    private String parseAndAddCustomer(String[] data, int lineNum) {
        if (data.length < 3 || data.length > 4) {
            return "Line " + lineNum + ": Incorrect number of fields.";
        }

        String id = data[0];
        String name = data[1];
        String type = data[2];
        String policyHolderId = data.length == 4 ? data[3] : null;

        if (Validation.isInvalidCustomerId(id)) {
            return "Line " + lineNum + ": Invalid customer ID.";
        }

        if (Validation.isInvalidCustomerName(name)) {
            return "Line " + lineNum + ": Customer name is empty.";
        }

        if (Validation.isValidCustomerType(type)) {
            return "Line " + lineNum + ": Invalid customer type.";
        }

        Customer newCustomer;
        if ("D".equals(type)) {
            if (Validation.isInvalidCustomerId(policyHolderId)) {
                return "Line " + lineNum + ": Invalid policy holder ID for dependent.";
            }
            PolicyHolder policyHolder = customerService.getPolicyHolder(policyHolderId);
            if (policyHolder == null) {
                return "Line " + lineNum + ": Policy holder does not exist.";
            }
            newCustomer = new Dependent(id, name, policyHolder);
        } else {
            newCustomer = new PolicyHolder(id, name);
        }

        if (!customerService.add(newCustomer)) {
            return "Line " + lineNum + ": Failed to add customer to the service.";
        }

        return null;
    }


    private String parseAndAddCard(String[] data, int lineNum) {
        if (data.length != 4) {
            return "Line " + lineNum + ": Incorrect number of fields.";
        }

        String cardId = data[0];
        String holderId = data[1];
        String cardOwner = data[2];
        Date expirationDate = Converter.parseDate(data[3]);

        if (Validation.isInvalidCardId(cardId)) {
            return "Line " + lineNum + ": Invalid card ID.";
        }
        if (Validation.isInvalidCustomerId(holderId)) {
            return "Line " + lineNum + ": Invalid customer ID.";
        }
        if (expirationDate == null) {
            return "Line " + lineNum + ": Invalid expiration date.";
        }

        Customer cardHolder = customerService.getOne(holderId);
        if (cardHolder == null) {
            return "Line " + lineNum + ": Customer not found.";
        }
        if (cardHolder.getInsuranceCard() != null) {
            return "Line " + lineNum + ": Customer already has an insurance card.";
        }

        InsuranceCard card = new InsuranceCard(cardId, cardHolder, cardOwner, expirationDate);
        if (!cardService.add(card)) {
            return "Line " + lineNum + ": Failed to add insurance card.";
        }

        return null;
    }


    private String parseAndAddClaim(String[] data, int lineNum) {
        if (data.length != 8) {
            return "Line " + lineNum + ": Incorrect number of fields.";
        }

        String claimId = data[0];
        Date claimDate = Converter.parseDate(data[1]);
        String customerId = data[2];
        Date examDate = Converter.parseDate(data[3]);
        String[] documentArray = data[4].split(SystemConfig.LIST_DELIMITER);
        double amount = Converter.parseDouble(data[5]);
        Status status = Status.parse(data[6]);
        String bankingInfo = data[7];

        if (Validation.isInvalidClaimId(claimId)) {
            return "Line " + lineNum + ": Invalid claim ID.";
        }
        if (claimDate == null || examDate == null) {
            return "Line " + lineNum + ": Invalid date format.";
        }
        if (Validation.isInvalidCustomerId(customerId)) {
            return "Line " + lineNum + ": Invalid customer ID.";
        }
        if (Validation.isInvalidAmount(amount)) {
            return "Line " + lineNum + ": Invalid amount.";
        }
        if (status == null) {
            return "Line " + lineNum + ": Invalid status.";
        }
        if (Validation.isInvalidBankingInfo(bankingInfo)) {
            return "Line " + lineNum + ": Invalid banking info.";
        }

        Customer insured = customerService.getOne(customerId);
        if (insured == null) {
            return "Line " + lineNum + ": Customer not found.";
        }

        TreeSet<String> documents = new TreeSet<>();
        for (String docName : documentArray) {
            documents.add(String.format("%s_%s_%s", claimId, insured.getInsuranceCard().getCardNumber(), docName));
        }

        Claim claim = new Claim(claimId, claimDate, insured, insured.getInsuranceCard().getCardNumber(), examDate, documents, amount, status, bankingInfo);
        if (!claimService.add(claim)) {
            return "Line " + lineNum + ": Failed to add claim.";
        }

        insured.addClaim(claim);
        return null;
    }


    @Override
    public List<String> saveToFiles() {
        String customerTitle = "id,fullName,type,policyHolderId";
        String cardTitle = "cardNumber,cardHolderId,policyOwner,expirationDate";
        String claimTitle = "id,claimDate,insuredPersonId,examDate,documents,claimAmount,status,receiverBankingInfo";

        List<String> logs = new ArrayList<>();
        if (writeEntities(SystemConfig.CUSTOMERS_CSV_PATH, customerTitle, customerService.getAll(), logs))
            if (writeEntities(SystemConfig.CARDS_CSV_PATH, cardTitle, cardService.getAll(), logs))
                writeEntities(SystemConfig.CLAIMS_CSV_PATH, claimTitle, claimService.getAll(), logs);
            else
                logs.add("Abort saving");
        else
            logs.add("Abort saving");
        return logs;
    }

    private <T extends Formattable> boolean writeEntities(String filePath, String title, TreeSet<T> entities, List<String> logs) {
        logs.add("Write to file: " + filePath);
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.write(title + "\n");
            int count = 0;
            for (T e : entities) {
                String entity = e.format();
                try {
                    writer.write(entity + "\n");
                    count++;
                } catch (Exception ex) {
                    logs.add("\tFailed to write entity: " + entity);
                }
            }
            logs.add("Successfully wrote " + count + (count > 1 ? " entities." : " entity."));
            return true;
        } catch (IOException e) {
            logs.add("Failed to write to file: " + filePath);
            return false;
        }
    }

}
