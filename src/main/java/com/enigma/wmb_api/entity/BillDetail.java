package com.enigma.wmb_api.entity;

import com.enigma.wmb_api.constant.ConstantTable;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.TRANSACTION_DETAIL, schema = "public", catalog = "wmb_api_db")
public class BillDetail {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Column(name = "id",nullable = false)
    private String id;
    @Basic
    @Column(name = "qty")
    private Integer qty;
    @Basic
    @Column(name = "price")
    private Integer price;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "bill_id", referencedColumnName = "id")
    private Transaction transaction;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    private Menu menu;
}