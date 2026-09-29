package com.maverickbank.dto;

import java.math.BigDecimal;

public class TransferToBeneficiaryRequest {
    private Long fromAccountId;
    private Long beneficiaryId;
    private BigDecimal amount;
    private String description;

    public TransferToBeneficiaryRequest() {}

    public TransferToBeneficiaryRequest(Long fromAccountId, Long beneficiaryId, BigDecimal amount, String description) {
        this.fromAccountId = fromAccountId;
        this.beneficiaryId = beneficiaryId;
        this.amount = amount;
        this.description = description;
    }

    public Long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(Long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public Long getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(Long beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
