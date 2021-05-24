package com.project.SnackVendingMachine.service;

import com.project.SnackVendingMachine.model.Category;
import com.project.SnackVendingMachine.model.Purchase;
import com.project.SnackVendingMachine.repository.PurchaseRepository;
import javassist.NotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Service
public class ItemService {

    private final CategoryService categoryService;
    private final PurchaseRepository purchaseRepository;


    public ItemService(CategoryService categoryService, PurchaseRepository purchaseRepository) {
        this.categoryService = categoryService;
        this.purchaseRepository = purchaseRepository;
    }

    @Transactional
    public void addItems(String name, Long count) throws NotFoundException {
        Category category = categoryService.findByName(name);
        category.setCount(category.getCount() + count);
    }

    @Transactional
    public void purchase(String nameOfCategory) throws NotFoundException {
        Category category = categoryService.findByName(nameOfCategory);
        category.setCount(category.getCount() - 1);
        Purchase purchase = Purchase.builder().dateOfPurchase(new Date()).category(category).build();
        purchaseRepository.save(purchase);
    }
}
