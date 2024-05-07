import database.InMemoryDB;
import model.PolicyHolder;
import utils.FileProcessing;
import view.CLI;

public class Main {
    public static void main(String[] args) {
        InMemoryDB database = InMemoryDB.getInstance();
        FileProcessing fileProcessing = new FileProcessing();
        fileProcessing.readCustomerFile();
        fileProcessing.readInsuranceCard();
        fileProcessing.readClaim();
        new CLI().start();
    }
}
