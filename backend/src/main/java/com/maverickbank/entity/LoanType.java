package com.maverickbank.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class LoanType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String loanName;

    @Column(nullable = false)
    private Double interestRate;

    @Column(nullable = false)
    private Integer maxTenureMonths;

    @Column(nullable = false)
    private BigDecimal maxAmount;

    @Column
    private String description;

    @Column(nullable = false)
    private Boolean isActive = true;

    public LoanType() {}

    public LoanType(String loanName, Double interestRate, Integer maxTenureMonths, BigDecimal maxAmount, String description) {
        this.loanName = loanName;
        this.interestRate = interestRate;
        this.maxTenureMonths = maxTenureMonths;
        this.maxAmount = maxAmount;
        this.description = description;
        this.isActive = true;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLoanName() { return loanName; }
    public void setLoanName(String loanName) { this.loanName = loanName; }
    public Double getInterestRate() { return interestRate; }
    public void setInterestRate(Double interestRate) { this.interestRate = interestRate; }
    public Integer getMaxTenureMonths() { return maxTenureMonths; }
    public void setMaxTenureMonths(Integer maxTenureMonths) { this.maxTenureMonths = maxTenureMonths; }
    public BigDecimal getMaxAmount() { return maxAmount; }
    public void setMaxAmount(BigDecimal maxAmount) { this.maxAmount = maxAmount; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
