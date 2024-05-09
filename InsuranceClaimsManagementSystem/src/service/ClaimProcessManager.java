package service;

import model.Claim;

import java.util.Set;

public interface ClaimProcessManager {
    boolean add(Claim claim);

    boolean delete(Claim claim);

    boolean update(Claim claim);

    Claim getOne(String id);

    Set<Claim> getAll();
}
