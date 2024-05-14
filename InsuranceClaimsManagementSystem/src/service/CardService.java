package service;

import model.InsuranceCard;

import java.util.SortedSet;

public class CardService extends Service<InsuranceCard> {
    public CardService(SortedSet<InsuranceCard> data) {
        super(data);
    }
}
