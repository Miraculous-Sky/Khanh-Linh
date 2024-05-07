package manager;

import database.InMemoryDB;
import model.InsuranceCard;

import java.util.SortedSet;

public class CardManagerImpl {
    private final SortedSet<InsuranceCard> insuranceCards = InMemoryDB.getInstance().getInsuranceCards();

    public boolean add(InsuranceCard insuranceCard) {
        return insuranceCards.add(insuranceCard);
    }

    public InsuranceCard getOne(String id) {
        return insuranceCards.stream().filter(c -> c.getCardNumber().equals(id)).findAny().orElse(null);
    }

    public SortedSet<InsuranceCard> getAll() {
        return insuranceCards;
    }
}
