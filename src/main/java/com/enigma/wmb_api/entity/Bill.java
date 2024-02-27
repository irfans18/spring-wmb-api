package com.enigma.wmb_api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.Collection;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "t_bill", schema = "public", catalog = "wmb_api_db")
public class Bill {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Column(name = "id")
    private String id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "trans_date")
    private Date transDate;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "table_id", referencedColumnName = "id")
    private MTable mtable;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "trans_type", referencedColumnName = "id", nullable = false)
    private TransType transType;
    @JsonManagedReference
    @OneToMany(mappedBy = "bill")
    private Collection<BillDetail> billDetails;

}
