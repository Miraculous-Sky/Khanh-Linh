package conf;

public interface SystemConfig {
    String DATE_FORMAT = "yyyy-MM-dd";

    String CSV_DELIMITER = ",";
    String LIST_DELIMITER = ";";

    String ROOT_PATH = "InsuranceClaimsManagementSystem/";
    String CUSTOMERS_CSV_PATH = ROOT_PATH + "data/customers.csv";
    String CARDS_CSV_PATH = ROOT_PATH + "data/insuranceCards.csv";
    String CLAIMS_CSV_PATH = ROOT_PATH + "data/claims.csv";


}
