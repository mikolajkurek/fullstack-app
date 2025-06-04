package org.example.pasir_kurek_mikolaj.dto;

import lombok.Data;

@Data
public class BalanceDTO {
    private double totalIncome;
    private double totalExpense;
    private double balance;

    public BalanceDTO(double totalIncome, double totalExpense, double balance) {
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.balance = balance;
    }
}
