package com.enigma.wmb_api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_bill_detail", schema = "public", catalog = "wmb_api_db")
public class BillDetail {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Column(name = "id",nullable = false)
    private String id;
    @Basic
    @Column(name = "qty")
    private Double qty;
    @Basic
    @Column(name = "price")
    private Double price;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "bill_id", referencedColumnName = "id")
    private Bill bill;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "menu_id", referencedColumnName = "id")
    private Menu menu;
}