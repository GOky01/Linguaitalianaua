package com.project.SnackVendingMachine.controllers;

import com.project.SnackVendingMachine.service.CategoryService;
import com.project.SnackVendingMachine.service.ItemService;
import javassist.NotFoundException;
import net.bytebuddy.implementation.bind.annotation.Default;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ItemController {
    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping("/category/addItem")
    public String addItem(Model model, @RequestParam String name) {
        model.addAttribute("category", name);
        return "addItem";
    }

    @PostMapping("/category/addItem")
    public String addItemPostByCategoryId(@RequestParam String category,
                                        @RequestParam(defaultValue = "0") Long count) throws NotFoundException {
        itemService.addItems(category, count);
        return "redirect:/category";
    }

    @PostMapping("/category/purchase")
    public String purchaseItem(@RequestParam String category) throws NotFoundException {
        itemService.purchase(category);
        return "redirect:/category";
    }
}
