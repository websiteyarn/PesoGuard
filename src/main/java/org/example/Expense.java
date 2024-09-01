package org.example;

/**
 * For expense table from Expenses
 * */
public class Expense {

    private String week;
    private double expenses;

    public Expense(String week, double expenses) {
        this.week = week;
        this.expenses = expenses;
    }

    public double getExpenses() {
        return expenses;
    }

    public void setExpenses(double expenses) {
        this.expenses = expenses;
    }

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

}
