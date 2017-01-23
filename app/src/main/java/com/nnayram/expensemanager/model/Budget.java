package com.nnayram.expensemanager.model;

import com.nnayram.expensemanager.util.NumberUtil;

import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;

/**
 * Created by Rufo on 1/23/2017.
 */
public class Budget {
    private Long id;
    private String description;
    private BigDecimal totalAmount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public String getFormattedTotalAmount() {
        return NumberUtil.format().format(totalAmount);
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}
