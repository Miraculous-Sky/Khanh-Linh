package model;

import conf.SystemConfig;
import utils.Converter;

import java.util.Date;
import java.util.SortedSet;
import java.util.stream.Collectors;

public class Claim implements Formattable, Comparable<Claim>, Identifiable {
    private final SortedSet<String> documents; //(with the format ClaimId_CardNumber_DocumentName.pdf)
    private final String id; //(with the format f-numbers; 10 numbers)
    private Date claimDate;
    private Customer insuredPerson;
    private String cardNumber;
    private Date examDate;
    private double claimAmount;
    private Status status; // (New, Processing, Done)
    private String receiverBankingInfo; //(Bank-Name-Number)

    public Claim(String id, Date claimDate, Customer insuredPerson, String cardNumber, Date examDate, SortedSet<String> documents, double claimAmount, Status status, String receiverBankingInfo) {
        this.documents = documents;
        this.id = id;
        this.claimDate = claimDate;
        this.insuredPerson = insuredPerson;
        this.cardNumber = cardNumber;
        this.examDate = examDate;
        this.claimAmount = claimAmount;
        this.status = status;
        this.receiverBankingInfo = receiverBankingInfo;
    }

    public String getId() {
        return id;
    }

    public Date getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(Date claimDate) {
        this.claimDate = claimDate;
    }

    public Customer getInsuredPerson() {
        return insuredPerson;
    }

    public void setInsuredPerson(Customer insuredPerson) {
        this.insuredPerson = insuredPerson;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public Date getExamDate() {
        return examDate;
    }

    public void setExamDate(Date examDate) {
        this.examDate = examDate;
    }

    public SortedSet<String> getDocuments() {
        return documents;
    }

    public void addDocuments(String document) {
        this.documents.add(document);
    }

    public double getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(double claimAmount) {
        this.claimAmount = claimAmount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getReceiverBankingInfo() {
        return receiverBankingInfo;
    }

    public void setReceiverBankingInfo(String receiverBankingInfo) {
        this.receiverBankingInfo = receiverBankingInfo;
    }

    @Override
    public String toString() {
        return "model.Claim{" +
                "id= " + id +
                ", claimDate= " + Converter.formatDate(claimDate) +
                ", insuredPerson= " + insuredPerson.getFullName() +
                ", cardNumber= " + cardNumber +
                ", examDate= " + Converter.formatDate(examDate) +
                ", documents= " + documents +
                ", claimAmount= " + claimAmount +
                ", status= " + status +
                ", receiverBankingInfo= '" + receiverBankingInfo + '\'' +
                '}';
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Claim)) return false;

        Claim claim = (Claim) o;
        return id.equals(claim.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compareTo(Claim o) {
        return this.id.compareTo(o.id);
    }

    @Override
    public String format() {
        return id + SystemConfig.CSV_DELIMITER +
                Converter.formatDate(claimDate) + SystemConfig.CSV_DELIMITER +
                insuredPerson.getCustomerID() + SystemConfig.CSV_DELIMITER +
                Converter.formatDate(examDate) + SystemConfig.CSV_DELIMITER +
                documents.stream().map(d -> d.replaceAll(".*_", ""))
                        .collect(Collectors.joining(SystemConfig.LIST_DELIMITER)) + SystemConfig.CSV_DELIMITER +
                claimAmount + SystemConfig.CSV_DELIMITER +
                status.getCode() + SystemConfig.CSV_DELIMITER +
                receiverBankingInfo;
    }

    @Override
    public String getIdentifier() {
        return this.id;
    }
}
