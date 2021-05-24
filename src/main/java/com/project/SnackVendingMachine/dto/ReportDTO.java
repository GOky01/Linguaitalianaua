package com.project.SnackVendingMachine.dto;

import lombok.Data;

@Data
public class ReportDTO {
    private String categoryName;
    private Long amount;
    private Double earnings;
}
