package utils.fileProcessing;

import java.util.List;

public interface FileProcessing {
    boolean readCustomers();

    boolean readInsuranceCards();

    boolean readClaims();

    boolean writeToFiles();

    boolean readCustomers(List<String> errorLogs);

    boolean readInsuranceCards(List<String> errorLogs);

    boolean readClaims(List<String> errorLogs);

    boolean writeToFiles(List<String> errorLogs);
}
