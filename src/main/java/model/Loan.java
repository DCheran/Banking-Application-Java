package model;

public class Loan {

    private final String type;
    private final double interestRate;
    private final String eligibility;

    public Loan(String type, double interestRate, String eligibility) {
        this.type = type;
        this.interestRate = interestRate;
        this.eligibility = eligibility;
    }

    public String getType() {
        return type;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public String getEligibility() {
        return eligibility;
    }
}
