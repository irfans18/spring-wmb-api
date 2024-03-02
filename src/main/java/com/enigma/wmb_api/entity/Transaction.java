package com.enigma.wmb_api.entity;

import com.enigma.wmb_api.constant.ConstantTable;
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
@Table(name = ConstantTable.TRANSACTION, schema = "public", catalog = "wmb_api_db")
public class Transaction {
    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Column(name = "id")
    private String id;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "trans_date")
    private Date trxDate;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "table_id", referencedColumnName = "id")
    private DinningTable dinningTable;
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "trans_type", referencedColumnName = "id")
    private TransType trxType;
    @JsonManagedReference
    @OneToMany(mappedBy = "transaction")
    private Collection<BillDetail> billDetails;

}
