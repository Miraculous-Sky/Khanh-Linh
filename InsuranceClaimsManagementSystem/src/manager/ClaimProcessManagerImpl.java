package manager;

import database.InMemoryDB;
import model.Claim;

import java.util.SortedSet;

public class ClaimProcessManagerImpl implements ClaimProcessManager {
    private final SortedSet<Claim> claims = InMemoryDB.getInstance().getClaims();

    @Override
    public boolean add(Claim claim) {
        return claims.add(claim);
    }

    @Override
    public boolean delete(Claim claim) {
        return claims.remove(claim);
    }

    @Override
    public boolean update(Claim claim) {
        if (claims.contains(claim)) {
            delete(claim);
            add(claim);
            return true;
        }
        return false;
    }

    @Override
    public Claim getOne(String id) {
        return claims.stream().filter(c -> c.getId().equals(id)).findAny().orElse(null);
    }

    @Override
    public SortedSet<Claim> getAll() {
        return claims;
    }
}
