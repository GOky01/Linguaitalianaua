package com.project.SnackVendingMachine.controllers;

import com.project.SnackVendingMachine.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController {

    @Autowired
    private CategoryRepository categoryRepository;
    @GetMapping("/")
    public String home( Model model) {
        return "home";
    }

}