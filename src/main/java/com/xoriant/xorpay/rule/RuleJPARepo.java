package com.xoriant.xorpay.rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.data.sync.repo.AuditJPARepo;
import com.xoriant.xorpay.pojo.ConfigPojo;

@Repository
@Transactional
public class RuleJPARepo {

	@Autowired
	private EntityManager entityManager;

	private final Logger logger = LoggerFactory.getLogger(RuleJPARepo.class);

	private int i;

	private Query query = null;

	String msgId, endToEndId, invoiceNo, profilename;
	int total;
	Map<String, String> piuidTagMap = null;
	Map<String, String> rmtSeqTagMap = null;

	public void getResultSet(Set<String> columnsAggSet, Set<String> columnsRemSet,
			Map<String, List<Map<String, String>>> piuidAggMap,
			Map<String, Map<String, List<Map<String, String>>>> rmtMap, ConfigPojo configPojo) {

		List<String> columnsAgg = new ArrayList<>(columnsAggSet);
		List<String> columnsRem = new ArrayList<>(columnsRemSet);
		StringBuilder aggquery = getQueryColumns(columnsAgg);
		aggquery.append(" from " + XorConstant.AGG_PRTY_PYMT_DTLS + " where xml_status ='" + XorConstant.STATUS_LOADED
				+ "' and erp_src_sys =" + configPojo.getSOURCE_SYS_ID());

		StringBuilder remtquery = getQueryColumns(columnsRem);
		remtquery.append(" from agg_remittance_info where erp_src_sys =" + configPojo.getSOURCE_SYS_ID());

		List<Object[]> rsl = getResult(aggquery);
		if (rsl.size() > 0) {
			total = columnsAgg.size();
			Set<String> msgIdList = new HashSet<>();
			rsl.stream().forEach(e -> {
				profilename = null;
				// endToEndId = null;
				piuidTagMap = new HashMap<>();
				for (int r = 0; r < total; r++) {
					String colName = columnsAgg.get(r);
					if (null != e[r]) {
						piuidTagMap.put(colName, e[r].toString());
						if (null != colName && colName.equalsIgnoreCase("profilename")) {
							profilename = e[r].toString();
						}
						if (null != colName && colName.equalsIgnoreCase("msgid_4")) {
							msgIdList.add(e[r].toString());
						}
						// logger.info("Colname "+ colName + " - "+ e[r]) ;
					} else {
						piuidTagMap.put(colName, null);
					}
				}
				if (null != profilename) {
					addMsgidMap(piuidAggMap, profilename, piuidTagMap);
				}

			});
			if (columnsRemSet.size() > 3) {

				remtquery.append(" and msgid_4 in (");
				createColumnValueForStr(msgIdList, remtquery);
				remtquery.append(")");
				logger.info("remtquery " + remtquery);
				List<Object[]> rmtsl = getResult(remtquery);
				if (rmtsl.size() > 0) {
					total = columnsRem.size();
					rmtsl.stream().forEach(e -> {
						msgId = null;
						endToEndId = null;
						invoiceNo = null;
						rmtSeqTagMap = new HashMap<>();
						for (int r = 0; r < total; r++) {
							String colName = columnsRem.get(r);
							if (null != e[r]) {
								rmtSeqTagMap.put(colName, e[r].toString());
								if (null != colName && colName.equalsIgnoreCase("msgid_4")) {
									msgId = e[r].toString();
								}
								if (null != colName && colName.equalsIgnoreCase("endtoendid_33")) {
									endToEndId = e[r].toString();
								}
								if (null != colName && colName.equalsIgnoreCase("nb_59")) {
									invoiceNo = e[r].toString();
								}

							} else {
								rmtSeqTagMap.put(colName, null);
							}
						}
						// logger.info(msgId + "~" + endToEndId + " " + invoiceNo);
						if (null != msgId && null != endToEndId && null != invoiceNo) {
							String uniqueId = msgId + "~" + endToEndId;
							if (rmtMap.containsKey(uniqueId)) {
								Map<String, List<Map<String, String>>> invMap = rmtMap.get(uniqueId);
								if (invMap.containsKey(invoiceNo)) {
									List<Map<String, String>> rmtList = invMap.get(invoiceNo);
									rmtList.add(rmtSeqTagMap);
									invMap.put(invoiceNo, rmtList);
									rmtMap.put(uniqueId, invMap);
								} else {
									List<Map<String, String>> rmtList = new ArrayList<>(1);
									rmtList.add(rmtSeqTagMap);
									invMap.put(invoiceNo, rmtList);
									rmtMap.put(uniqueId, invMap);
								}
							} else {
								Map<String, List<Map<String, String>>> invMap = new HashMap<>(1);
								List<Map<String, String>> rmtList = new ArrayList<>(1);
								rmtList.add(rmtSeqTagMap);
								invMap.put(invoiceNo, rmtList);
								rmtMap.put(uniqueId, invMap);
							}
						}

					});
				}
			}
		}

	}

	private StringBuilder getQueryColumns(List<String> columnNames) {
		StringBuilder aggquery = new StringBuilder("Select ");
		createColumnQuery(columnNames, aggquery);
		return aggquery;
	}

	public void createColumnQuery(List<String> columnNames, StringBuilder aggquery) {
		i = 0;
		columnNames.stream().forEach(e -> {
			if (i > 0) {
				aggquery.append(", ");
			}
			aggquery.append(e);
			i++;
		});
	}

	public void createColumnValue(List<Integer> columnNames, StringBuilder aggquery) {
		i = 0;
		columnNames.stream().forEach(e -> {
			if (i > 0) {
				aggquery.append(", ");
			}
			aggquery.append(e);
			i++;
		});
	}

	public void createColumnValueForStr(Set<String> columnNames, StringBuilder aggquery) {
		i = 0;
		columnNames.stream().forEach(e -> {
			if (i > 0) {
				aggquery.append(", ");
			}
			aggquery.append("'" + e + "'");
			i++;
		});
	}

	public StringBuilder createSelectQuery(List<String> columns) {

		StringBuilder selectQuery = new StringBuilder();
		selectQuery.append("select ");
		i = 0;
		for (String colName : columns) {
			if (i > 0) {
				selectQuery.append(",");
			}
			selectQuery.append(colName);

			i++;
		}
		selectQuery.append(" from " + XorConstant.AGG_PRTY_PYMT_DTLS + " where xml_status is null");

		logger.info("Agg - " + selectQuery);
		return selectQuery;

	}

	public List<Object[]> getResult(StringBuilder aggquery) {
		Query nativeQuery = entityManager.createNativeQuery(aggquery.toString());
		List<Object[]> rsl = nativeQuery.getResultList();
		return rsl;
	}

	public List<Object> getResultObject(StringBuilder aggquery) {
		Query nativeQuery = entityManager.createNativeQuery(aggquery.toString());
		List<Object> rsl = nativeQuery.getResultList();
		return rsl;
	}

	private void addMsgidMap(Map<String, List<Map<String, String>>> piuidAggMap, String piuid,
			Map<String, String> piuidTagMap) {
		if (piuidAggMap.containsKey(piuid)) {
			List<Map<String, String>> dataList = piuidAggMap.get(piuid);
			dataList.add(piuidTagMap);
			piuidAggMap.put(piuid, dataList);
		} else {
			List<Map<String, String>> dataList = new ArrayList<>(1);
			dataList.add(piuidTagMap);
			piuidAggMap.put(piuid, dataList);
		}
	}

	public void executeQuery(StringBuilder updatequery) {
		try {
			query = entityManager.createNativeQuery(updatequery.toString());
			query.executeUpdate();
		} catch (Exception ex) {
			logger.info("Rule: Exception " + updatequery.toString());
			logger.info("Exception " + ex.getMessage());
		}
		entityManager.flush();
		entityManager.clear();

	}

	public void moveprocessedDate(AuditJPARepo auditJPARepo) {

		StringBuilder inserQuery = new StringBuilder(
				" insert into agg_prty_pymt_dtls_e select * from agg_prty_pymt_dtls where xml_status ='E'");
		try {
			executeQuery(inserQuery);

			inserQuery = new StringBuilder(
					" insert into agg_remittance_info_e select * from agg_remittance_info  where "
							+ " msgid_4 in ( select msgid_4 from agg_prty_pymt_dtls where xml_status ='E')");

			executeQuery(inserQuery);

			auditJPARepo.addBatchAuditMessage("ALL ", null, null,
					"With statsu E Moved to Error table for both AGG and REM ", AuditJPARepo.SUCCESSFULLY);

		} catch (Exception ex) {
			auditJPARepo.addBatchAuditMessage("Error ", null, null, ex.getMessage(), AuditJPARepo.FAILED);
			ex.printStackTrace();
		}
		try {
			inserQuery = new StringBuilder(
					"delete from agg_remittance_info where msgid_4 in (select msgid_4 from agg_prty_pymt_dtls where xml_status ='E')");
			executeQuery(inserQuery);

			inserQuery = new StringBuilder("delete from agg_prty_pymt_dtls where xml_status ='E'");
			executeQuery(inserQuery);
			auditJPARepo.addBatchAuditMessage("ALL ", null, null,
					"With statsu E Cleaned main table for both AGG and REM", AuditJPARepo.SUCCESSFULLY);
		} catch (Exception ex) {
			auditJPARepo.addBatchAuditMessage("Error ", null, null, ex.getMessage(), AuditJPARepo.FAILED);
			ex.printStackTrace();
		}

		entityManager.flush();
		entityManager.clear();
	}
}
