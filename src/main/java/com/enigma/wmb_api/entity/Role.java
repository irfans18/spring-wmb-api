package com.enigma.wmb_api.entity;

import com.enigma.wmb_api.constant.enums.UserRole;
import com.enigma.wmb_api.constant.ConstantTable;
import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = ConstantTable.USER_ROLE)
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
