package com.project.SnackVendingMachine.controllers;

import com.project.SnackVendingMachine.dto.ReportDTO;
import com.project.SnackVendingMachine.model.Category;
import com.project.SnackVendingMachine.service.CategoryService;
import javassist.NotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;

@Controller
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/category")
    public String category(Model model){
        List<Category> categories = categoryService.findAll();
        categories.sort(Category::compareTo);
        model.addAttribute("category", categories);
        return "category";
    }

    @GetMapping("/category/add")
    public String addCategory(Model model) {
        return "addCategory";
    }

    @PostMapping("/category/add")
    public String addCategoryPost(Model model,@RequestParam String name, @RequestParam Double price, @RequestParam Long count) {
        Category category = Category.builder()
                .name(name)
                .price(price)
                .count(count)
                .build();
        categoryService.save(category);
        return "redirect:/category";
    }

    @PostMapping("/category/clear")
    public String clearCategory(@RequestParam String category) throws NotFoundException {
        categoryService.deleteByName(category);
        return "redirect:/category";
    }

    @GetMapping("/category/report/form")
    public String getFormForReport(Model model) {
        return "specificationOfReport";
    }

    @GetMapping("/category/report")
    public String makeReport(Model model, @RequestParam String date) throws NotFoundException, ParseException {
        Collection<ReportDTO> report = categoryService.getReport(date);
        model.addAttribute("category", report);
        return "reportList";
    }
}
