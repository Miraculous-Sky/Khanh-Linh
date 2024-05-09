package service;

import model.InsuranceCard;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class CardService extends Service<InsuranceCard> {
    public CardService(SortedSet<InsuranceCard> data) {
        super(data);
    }
}
