package com.project.SnackVendingMachine.service;

import com.project.SnackVendingMachine.dto.ReportDTO;
import com.project.SnackVendingMachine.model.Category;
import com.project.SnackVendingMachine.repository.CategoryRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final PurchaseService purchaseService;

    Pattern patternForDate = Pattern.compile("([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))");
    Pattern patternForMonth = Pattern.compile("([12]\\d{3}-(0[1-9]|1[0-2]))");


    public CategoryService(CategoryRepository categoryRepository, PurchaseService purchaseService) {
        this.categoryRepository = categoryRepository;
        this.purchaseService = purchaseService;
    }


    public Category findByName(String name) throws NotFoundException {
        return categoryRepository.findByName(name).orElseThrow(() -> new NotFoundException("Category was not fount with name :" + name));
    }

    public List<Category> findAll() {
        return (List<Category>) categoryRepository.findAll();
    }

    public void save(Category category) {
        categoryRepository.save(category);
    }

    @Transactional
    public void deleteByName(String category) {
        categoryRepository.deleteByName(category);
    }

    @Transactional
    public Collection<ReportDTO> getReport(String date) throws ParseException {
        if (patternForDate.matcher(date).matches()) {
            return purchaseService.generateReportFromDate(date);
        } else {
            if (patternForMonth.matcher(date).matches()) {
                return purchaseService.generateReportForMonth(date);
            } else {
                throw new IllegalArgumentException("Wrong format of date. Examples: yyyy-mm or yyyy-mm-dd");
            }
        }
    }


}
