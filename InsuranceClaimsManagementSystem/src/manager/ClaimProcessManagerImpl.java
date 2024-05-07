package manager;

import model.Claim;

import java.util.List;

public class ClaimProcessManagerImpl implements ClaimProcessManager {
    private final List<Claim> claims;

    public ClaimProcessManagerImpl(List<Claim> claimList) {
        this.claims = claimList;
    }

    @Override
    public boolean add(Claim claim) {
        if (claims.contains(claim)) {
            return false;
        }
        for (int i = 0; i < claims.size(); i++) {
            if (claims.get(i).compareTo(claim) > 0) {
                claims.add(i, claim);
                return true;
            }
        }
        claims.add(claim);
        return true;
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
    public List<Claim> getAll() {
        return claims;
    }
}
