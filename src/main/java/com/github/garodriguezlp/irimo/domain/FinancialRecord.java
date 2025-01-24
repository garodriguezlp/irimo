package com.github.garodriguezlp.irimo.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record FinancialRecord(
    LocalDate date,
    String description,
    BigDecimal amount,
    String source
) {

}
