package com.enigma.wmb_api.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.Objects;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "m_trans_type", schema = "public", catalog = "wmb_api_db")
public class TransType {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Column(name = "id")
    private String id;
    @Basic
    @Column(name = "description")
    private String description;
    @JsonManagedReference
    @OneToMany(mappedBy = "transType")
    private Collection<Bill> bills;
}