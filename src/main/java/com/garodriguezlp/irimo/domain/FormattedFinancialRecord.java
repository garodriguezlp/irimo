package com.garodriguezlp.irimo.domain;

public record FormattedFinancialRecord(
    String date,
    String description,
    String amount,
    String source
) {

}
