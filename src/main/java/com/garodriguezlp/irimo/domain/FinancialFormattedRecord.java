package com.garodriguezlp.irimo.domain;

public record FinancialFormattedRecord(
    String date,
    String description,
    String amount,
    String source
) {

}
