package com.github.garodriguezlp.irimo.domain;

// @todo: Rename it to something like TargetFinancialRecord
public record FormattedFinancialRecord(
    String date,
    String description,
    String amount,
    String source
) {

}
