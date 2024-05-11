package service;

import model.Claim;

import java.util.SortedSet;

public class ClaimService extends Service<Claim> implements ClaimProcessManager {
    public ClaimService(SortedSet<Claim> data) {
        super(data);
    }
}
