package com.onlinebanking.minorbeneficiaries.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "beneficiaries")
public class Beneficiary {

    @Id
   // @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "beneficiary_id")
    private Long beneficiaryId;
    @Version
    @Column(name = "version")
    private Long version;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private BeneficiaryType beneficiaryType;

    private String accountNumber;
    private String ifscCode;
    private String bankName;
    private String beneficiaryName;
    private String nickname;
    private String email;
    private String mobileNumber;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public enum BeneficiaryType {
        SAME_BANK,
        OTHER_BANK,
        UPI
    }
}
