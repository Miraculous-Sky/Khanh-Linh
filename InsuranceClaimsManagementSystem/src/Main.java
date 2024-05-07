import utils.fileProcessing.CsvFileProcessing;
import utils.fileProcessing.FileProcessing;
import view.CLI;

public class Main {
    public static void main(String[] args) {
        FileProcessing fileProcessing = new CsvFileProcessing();
        fileProcessing.readCustomers();
        fileProcessing.readInsuranceCards();
        fileProcessing.readClaims();

        new CLI().start();
    }
}
