package com.project.SnackVendingMachine.repository;

import com.project.SnackVendingMachine.model.Purchase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface PurchaseRepository extends CrudRepository<Purchase,Long> {
    List<Purchase> findByDateOfPurchaseBetween(Date startDate, Date endDate);
}
