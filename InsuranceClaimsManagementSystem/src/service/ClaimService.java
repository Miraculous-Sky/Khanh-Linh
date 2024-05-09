package service;

import model.Claim;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class ClaimService extends Service<Claim> implements ClaimProcessManager {
    public ClaimService(SortedSet<Claim> data) {
        super(data);
    }
}
