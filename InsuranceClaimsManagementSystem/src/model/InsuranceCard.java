package model;

import conf.SystemConfig;
import utils.Converter;

import java.util.Date;

public class InsuranceCard implements Identifiable, Formattable, Comparable<InsuranceCard> {
    private final String cardNumber; //10 digits
    private Customer cardHolder;
    private String policyOwner;
    private Date expirationDate;

    public InsuranceCard(String cardNumber, Customer cardHolder, String policyOwner, Date expirationDate) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.policyOwner = policyOwner;
        this.expirationDate = expirationDate;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Customer getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(Customer cardHolder) {
        this.cardHolder = cardHolder;
        this.cardHolder.setInsuranceCard(this);
    }

    public String getPolicyOwner() {
        return policyOwner;
    }

    public void setPolicyOwner(String policyOwner) {
        this.policyOwner = policyOwner;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InsuranceCard)) return false;

        InsuranceCard that = (InsuranceCard) o;
        return cardNumber.equals(that.cardNumber);
    }

    @Override
    public int hashCode() {
        return cardNumber.hashCode();
    }

    @Override
    public String toString() {
        return "model.InsuranceCard{" +
                "cardNumber=" + cardNumber +
                ", cardHolder=" + cardHolder.getCustomerID() +
                ", policyOwner=" + policyOwner +
                ", expirationDate=" + Converter.formatDate(expirationDate) +
                '}';
    }

    @Override
    public int compareTo(InsuranceCard o) {
        return this.cardNumber.compareTo(o.cardNumber);
    }

    @Override
    public String format() {
        return cardNumber + SystemConfig.CSV_DELIMITER +
                cardHolder.getCustomerID() + SystemConfig.CSV_DELIMITER +
                policyOwner + SystemConfig.CSV_DELIMITER +
                Converter.formatDate(expirationDate);
    }

    @Override
    public String getIdentifier() {
        return this.cardNumber;
    }
}
