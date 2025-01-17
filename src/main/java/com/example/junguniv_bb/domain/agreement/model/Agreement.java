package com.example.junguniv_bb.domain.agreement.model;

import com.example.junguniv_bb._core.common.BaseTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "AGREEMENT")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Agreement extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AGREEMENT_IDX")
    private Long agreementIdx;

    @Column(name = "AGREEMENT_TITLE")
    private String agreementTitle;

    @Column(name = "TRAINING_CENTER_NAME")
    private String trainingCenterName;

    @Column(name = "TRAINING_CENTER_URL")
    private String trainingCenterUrl;

    @Column(name = "AGREEMENT_CONTENTS")
    @Lob
    private String agreementContents;

    @Column(name = "OPEN_YN")
    private String openYn;

    @Column(name = "AGREEMENT_TYPE")
    private String agreementType; // (JOIN, COURSE, REFUND)

    public Agreement(Long agreementIdx, String agreementContents, String agreementType, String openYn) {
        this.agreementIdx = agreementIdx;
        this.agreementContents = agreementContents;
        this.agreementType = agreementType;
        this.openYn = openYn;
    }
}
