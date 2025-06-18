package models.credit;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreditParametersResponse {
    @JsonProperty("loanType")
    private String loanType;

    @JsonProperty("loanName")
    private String loanName;

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

    // Getters and Setters
    public String getLoanType() { return loanType; }
    public void setLoanType(String loanType) { this.loanType = loanType; }

    public String getLoanName() { return loanName; }
    public void setLoanName(String loanName) { this.loanName = loanName; }

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