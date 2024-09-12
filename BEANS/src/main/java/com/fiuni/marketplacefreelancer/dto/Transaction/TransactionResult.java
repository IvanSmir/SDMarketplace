package com.fiuni.marketplacefreelancer.dto.Transaction;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import com.fiuni.marketplacefreelancer.dto.base.BaseResult;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serial;
import java.util.List;

@XmlRootElement(name = "transactionResult")

public class TransactionResult extends BaseResult<TransactionDTO> {

    @Serial
    private static final long serialVersionUID = 1L;

    @XmlElement
    @JsonProperty("transactions")
    public List<TransactionDTO> getTransactions() {
        return getList();
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        super.setList(transactions);
    }
}
