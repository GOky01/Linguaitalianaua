package com.project.SnackVendingMachine.repository;

import com.project.SnackVendingMachine.model.Category;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends CrudRepository<Category,Long> {
    Optional<Category> findByName(String name);

    void deleteByName(String name);
}
