package conf;

public interface Config {
    String DATE_FORMAT = "yyyy-MM-dd";

    String CSV_DELIMITER = ",";
    String LIST_DELIMITER = ";";

    String CUSTOMERS_FILE_PATH = "customers.csv";
    String CARDS_FILE_PATH = "insuranceCards.csv";
    String CLAIMS_FILE_PATH = "claims.csv";
}
