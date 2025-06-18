package models.credit;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreditType {
    @JsonProperty("loanType")
    private String loanType;

    @JsonProperty("loanName")
    private String loanName;

    @JsonProperty("loanTypeId")
    private Integer loanTypeId;

    @JsonProperty("interestRateMin")
    private Double interestRateMin;

    @JsonProperty("interestRateMax")
    private Double interestRateMax;

    @JsonProperty("loanAmountMin")
    private Integer loanAmountMin;

    @JsonProperty("loanAmountMax")
    private Integer loanAmountMax;

    @JsonProperty("loanTermMin")
    private Integer loanTermMin;

    @JsonProperty("loanTermMax")
    private Integer loanTermMax;

    @JsonProperty("penaltyRate")
    private Double penaltyRate;

    // Конструкторы
    public CreditType() {}

    public CreditType(String loanType, String loanName, Integer loanTypeId,
                      Double interestRateMin, Double interestRateMax,
                      Integer loanAmountMin, Integer loanAmountMax,
                      Integer loanTermMin, Integer loanTermMax,
                      Double penaltyRate) {
        this.loanType = loanType;
        this.loanName = loanName;
        this.loanTypeId = loanTypeId;
        this.interestRateMin = interestRateMin;
        this.interestRateMax = interestRateMax;
        this.loanAmountMin = loanAmountMin;
        this.loanAmountMax = loanAmountMax;
        this.loanTermMin = loanTermMin;
        this.loanTermMax = loanTermMax;
        this.penaltyRate = penaltyRate;
    }

    // Геттеры и сеттеры
    public String getLoanType() { return loanType; }
    public void setLoanType(String loanType) { this.loanType = loanType; }

    public String getLoanName() { return loanName; }
    public void setLoanName(String loanName) { this.loanName = loanName; }

    public Integer getLoanTypeId() { return loanTypeId; }
    public void setLoanTypeId(Integer loanTypeId) { this.loanTypeId = loanTypeId; }

    public Double getInterestRateMin() { return interestRateMin; }
    public void setInterestRateMin(Double interestRateMin) { this.interestRateMin = interestRateMin; }

    public Double getInterestRateMax() { return interestRateMax; }
    public void setInterestRateMax(Double interestRateMax) { this.interestRateMax = interestRateMax; }

    public Integer getLoanAmountMin() { return loanAmountMin; }
    public void setLoanAmountMin(Integer loanAmountMin) { this.loanAmountMin = loanAmountMin; }

    public Integer getLoanAmountMax() { return loanAmountMax; }
    public void setLoanAmountMax(Integer loanAmountMax) { this.loanAmountMax = loanAmountMax; }

    public Integer getLoanTermMin() { return loanTermMin; }
    public void setLoanTermMin(Integer loanTermMin) { this.loanTermMin = loanTermMin; }

    public Integer getLoanTermMax() { return loanTermMax; }
    public void setLoanTermMax(Integer loanTermMax) { this.loanTermMax = loanTermMax; }

    public Double getPenaltyRate() { return penaltyRate; }
    public void setPenaltyRate(Double penaltyRate) { this.penaltyRate = penaltyRate; }
}