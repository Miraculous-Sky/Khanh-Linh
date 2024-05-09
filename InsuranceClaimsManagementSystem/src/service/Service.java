package service;

import model.Identifiable;

import java.util.SortedSet;
import java.util.TreeSet;

public class Service<T extends Identifiable> {
    private final SortedSet<T> data;

    public Service(SortedSet<T> data) {
        this.data = data;
    }

    public boolean add(T t) {
        return data.add(t);
    }

    public boolean delete(T t) {
        return data.remove(t);
    }

    public boolean update(T t) {
        if (data.contains(t)) {
            delete(t);
            add(t);
            return true;
        }
        return false;
    }

    public T getOne(String id) {
        return data.stream()
                .filter(c -> c.getIdentifier().equals(id))
                .findAny().orElse(null);
    }

    public SortedSet<T> getAll() {
        return data;
    }
}
