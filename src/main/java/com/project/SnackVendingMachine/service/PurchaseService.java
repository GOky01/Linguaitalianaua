package com.project.SnackVendingMachine.service;

import com.project.SnackVendingMachine.dto.ReportDTO;
import com.project.SnackVendingMachine.model.Category;
import com.project.SnackVendingMachine.model.Purchase;
import com.project.SnackVendingMachine.repository.PurchaseRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PurchaseService {

    private final PurchaseRepository purchaseRepository;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat mouthFormat = new SimpleDateFormat("yyyy-MM");


    public PurchaseService(PurchaseRepository purchaseRepository) {
        this.purchaseRepository = purchaseRepository;
    }

    public Collection<ReportDTO> generateReportForMonth(String dateInString) throws ParseException {
        Date startDate = mouthFormat.parse(dateInString);
        Date endDate = dateFormat.parse(dateInString + "-31");
        List<Purchase> purchases = purchaseRepository.findByDateOfPurchaseBetween(startDate, endDate);
        return collectReportDate(purchases);
    }

    public Collection<ReportDTO> generateReportFromDate(String dateInString) throws ParseException {
        Date startDate = dateFormat.parse(dateInString);
        Date endDate = new Date();
        List<Purchase> purchases = purchaseRepository.findByDateOfPurchaseBetween(startDate, endDate);
        return collectReportDate(purchases);
    }

    public Collection<ReportDTO> collectReportDate(List<Purchase> purchases) {
        Map<Category, ReportDTO> collectData = new HashMap<>();
        purchases.forEach(i -> {
            if (collectData.containsKey(i.getCategory())) {
                ReportDTO reportDTO = collectData.get(i.getCategory());
                reportDTO.setAmount(reportDTO.getAmount() + 1);
                reportDTO.setEarnings(reportDTO.getEarnings() + i.getCategory().getPrice());
            } else {
                ReportDTO reportDTO = new ReportDTO();
                reportDTO.setAmount(1L);
                reportDTO.setEarnings(i.getCategory().getPrice());
                collectData.put(i.getCategory(), reportDTO);
            }
        });
        return collectData.keySet().stream().map(i->{
            ReportDTO reportDTO = collectData.get(i);
            reportDTO.setCategoryName(i.getName());
            return reportDTO;
        }).collect(Collectors.toList());
    }


}
