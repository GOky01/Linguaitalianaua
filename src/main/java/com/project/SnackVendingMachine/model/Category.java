package com.project.SnackVendingMachine.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "Category")
public class Category implements Comparable<Category>{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Double price = 0.00;
    @Column(columnDefinition = "integer default 0")
    private Long count;
    @OneToMany(mappedBy = "category", cascade = CascadeType.REMOVE)
    private Set<Purchase> purchases;

    @Override
    public int compareTo(Category o) {
        return (int) (o.getCount() - this.getCount());
    }
}