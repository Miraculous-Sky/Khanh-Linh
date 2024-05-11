import database.InMemoryDB;
import service.CardService;
import service.ClaimService;
import service.CustomerService;
import utils.fileProcessing.CsvFileProcessing;
import utils.fileProcessing.FileProcessing;
import view.cli.CLI;
import view.cli.CommandFactory;

public class ApplicationContext {
    private final CustomerService customerService;
    private final CardService cardService;
    private final ClaimService claimService;
    private final CommandFactory commandFactory;

    public ApplicationContext() {
        InMemoryDB db = InMemoryDB.getInstance();
        customerService = new CustomerService(db.getCustomers());
        cardService = new CardService(db.getInsuranceCards());
        claimService = new ClaimService(db.getClaims());
        commandFactory = new CommandFactory(claimService, customerService);
    }

    public static void main(String[] args) {
        ApplicationContext context = new ApplicationContext();
        context.initialize();
        context.startApplication();
        context.endApplication();
    }

    private void endApplication() {
        FileProcessing fileProcessing = new CsvFileProcessing(customerService, cardService, claimService);
        fileProcessing.saveToFiles();
    }

    public void initialize() {
        FileProcessing fileProcessing = new CsvFileProcessing(customerService, cardService, claimService);
        fileProcessing.loadFiles();
    }

    public void startApplication() {
        new CLI(customerService, claimService, commandFactory).start();
    }
}
