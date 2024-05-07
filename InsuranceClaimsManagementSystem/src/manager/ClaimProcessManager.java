package manager;

import model.Claim;

import java.util.SortedSet;

public interface ClaimProcessManager {
    boolean add(Claim claim);

    boolean delete(Claim claim);

    boolean update(Claim claim);

    Claim getOne(String id);

    SortedSet<Claim> getAll();
}
