package utils.fileProcessing;

import java.util.List;

public class BinaryFileProcessing implements FileProcessing {
    @Override
    public boolean readCustomers() {
        return false;
    }

    @Override
    public boolean readInsuranceCards() {
        return false;
    }

    @Override
    public boolean readClaims() {
        return false;
    }

    @Override
    public boolean writeToFiles() {
        return false;
    }

    @Override
    public boolean readCustomers(List<String> errorLogs) {
        return false;
    }

    @Override
    public boolean readInsuranceCards(List<String> errorLogs) {
        return false;
    }

    @Override
    public boolean readClaims(List<String> errorLogs) {
        return false;
    }

    @Override
    public boolean writeToFiles(List<String> errorLogs) {
        return false;
    }
}
