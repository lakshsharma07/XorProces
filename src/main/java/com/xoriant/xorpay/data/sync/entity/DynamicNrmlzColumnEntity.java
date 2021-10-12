package com.xoriant.xorpay.data.sync.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name = "XP_DYNAMIC_NRMLZ_COLUMN")
public class DynamicNrmlzColumnEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DNCS") // , generator="SEQ")
    @SequenceGenerator(name = "SEQ_DNCS", sequenceName = "SEQ_XP_DYNAMIC_NRMLZ_COLUMN_SLNO", allocationSize = 1)
    @Column(name = "SLNO")
    private Integer id;
    @Column(name = "LAST_UPDATE_DATE")
    private LocalDateTime lastUpdateDate;
    @Column(name = "ERP_SRC_SYS")
    private Integer srcSysId;
    @Column(name = "SRC_COL_LENGTH")
    private Integer srcColLength;
    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;
    @Column(name = "TAG_INFO_TYPE")
    private String srcColInfoType;
    @Column(name = "COL_CREATED")
    private Character colCreated;
    @Column(name = "CREATED_BY")
    private String createdBy;
    @Column(name = "LAST_UPDATED_BY")
    private String lastUpdatedBy;
    @Column(name = "SRC_ETL_TRANSFORMATION")
    private String srcEtlTransformation;
    @Column(name = "TAG_NAME")
    private String tagInfoType;
    @Column(name = "ACTIVE_IND")
    private Character activeInd;
    @Column(name = "SRC_COL_NAME")
    private String srcColName;
    @Column(name = "NRMLZ_COL_NAME")
    private String nrmzColName;
    @Column(name = "SRC_DATA_TYPE")
    private Integer srcDataType;
    @Column(name = "XPATH")
    private String srcColXpath;

}
