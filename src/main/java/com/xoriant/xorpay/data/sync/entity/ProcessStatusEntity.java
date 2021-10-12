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

@Table(name = "XP_PROCESS_STATUS")
public class ProcessStatusEntity {

    @Id
    //@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PSS") // , generator="SEQ")
    //@SequenceGenerator(name = "SEQ_PSS", sequenceName = "SEQ_XP_PROCESS_STATUS_SLNO", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "SLNO")
    private Integer id;
    @Column(name = "ETL_START_TIME")
    private LocalDateTime startTime;
    @Column(name = "ETL_END_TIME")
    private LocalDateTime endTime;
    @Column(name = "ETL_RECORDS_PROCESSED")
    private Integer recordsProcessed;
    @Column(name = "ETL_ERR_MSG")
    private String errMsg;
    @Column(name = "STATUS")
    private String status;
    @Column(name = "ETL_STATUS_INITIATED")
    private Character statusInitiated;
    @Column(name = "ETL_ERR_CODE")
    private String errCode;
    @Column(name = "ETL_PROCESS_NAME")
    private String processName;
    @Column(name = "ETL_SOURCE_TABLE")
    private String sourceTable;
    @Column(name = "ETL_TARGET_TABLE")
    private String targetTable;
    @Column(name = "created_by")
	private String createdBy;
	@Column(name = "creation_date")
	private LocalDateTime creationDate;
	@Column(name = "last_updated_by")
	private String lastUpdateBy;
	@Column(name = "last_update_date")
	private LocalDateTime lastUpdateDate;

}
