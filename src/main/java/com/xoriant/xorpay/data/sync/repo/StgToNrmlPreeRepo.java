package com.xoriant.xorpay.data.sync.repo;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xoriant.xorpay.constants.XorConstant;
import com.xoriant.xorpay.pojo.ConfigPojo;

public class StgToNrmlPreeRepo {

	private static int i;
	private final static Logger logger = LoggerFactory.getLogger(StgToNrmlPreeRepo.class);

	public synchronized static void updateStatusForInitiated(ConfigPojo configPojo, Query query,
			EntityManager entityManager, Map<String, String> batchUUIDMap) {
		logger.info("method locked");
		try {

			StringBuilder selectQuery = new StringBuilder("select " + configPojo.getSOURCE_TABLE_IDENTITY_COLUMN_NAME()
					+ "  from " + configPojo.getSOURCE_TABLE_NAME() + " a  where a.process_status is NULL and"
					+ " exists ( select 1  from xp_piuid_information b" + "	where a."
					+ configPojo.getSOURCE_PROFILE_COLUMN_NAME() + " = b.profilename)");

			deleteUnAuthorizedData(configPojo, entityManager);

			logger.info("selectQuery " + selectQuery);
			List<Object> dataList = getResultSetObject(selectQuery.toString(), entityManager);
			logger.info("after result set " + dataList);

			if (null != dataList && !dataList.isEmpty()) {
				StringBuilder updateSts = getSelectQueryForUpdateStatus(configPojo, dataList);

				logger.info("internalIdList " + updateSts);
				query = entityManager.createNativeQuery(updateSts.toString());
				query.executeUpdate();
				entityManager.flush();
				entityManager.clear();
				selectQuery = new StringBuilder("select " + configPojo.getSOURCE_MESSAGE_ID() + "  from "
						+ configPojo.getSOURCE_TABLE_NAME() + " where " + configPojo.getSOURCE_PROFILE_COLUMN_NAME()
						+ " like '%" + configPojo.getSrcSys().getSrcSystemSort() + "%'  and process_status ='"
						+ XorConstant.PROCESS_INITIATED + "' ");
				dataList = getResultSetObject(selectQuery.toString(), entityManager);
				Set<String> batchidSet = dataList.stream().map(e -> e.toString()).collect(Collectors.toSet());
				batchidSet.stream().forEach(messagId -> {
					String uuid = UUID.randomUUID().toString();
					batchUUIDMap.put(messagId, uuid);
					StringBuilder updateuuid = getSelectQueryToAssignUUID(configPojo, uuid, messagId);
					Query que = entityManager.createNativeQuery(updateuuid.toString());
					que.executeUpdate();
					entityManager.flush();
					entityManager.clear();
				});
			}
			logger.info("method unloked");
		} catch (Exception e) {
			logger.error("STGTONRM Exeption ", e);
			throw e;
		}
	}

	private static void deleteUnAuthorizedData(ConfigPojo configPojo, EntityManager entityManager) {
		Query query;
		StringBuilder deleteUnauthorizedData = new StringBuilder("DELETE from " + configPojo.getSOURCE_TABLE_NAME()
				+ "	a " + " where not exists ( select 1 from xp_piuid_information b where a."
				+ configPojo.getSOURCE_PROFILE_COLUMN_NAME() + " = b.profilename)");
		try {
			logger.info("UT A- " + deleteUnauthorizedData.toString());
			query = entityManager.createNativeQuery(deleteUnauthorizedData.toString());
			query.executeUpdate();
		} catch (Exception ex) {
			logger.info("STG: Exception " + deleteUnauthorizedData.toString());
			logger.info("Exception " + ex.getMessage());
		}
		entityManager.flush();
		entityManager.clear();
	}

	private static StringBuilder getPiuidQueryIn(List<String> activePiuid) {

		StringBuilder piuidIn = new StringBuilder();
		i = 0;
		activePiuid.stream().forEach(p -> {
			if (i > 0) {
				piuidIn.append(", ");
			}
			piuidIn.append("'" + p + "'");
			i++;
		});

		return piuidIn;
	}

	private static StringBuilder getSelectQueryToAssignUUID(ConfigPojo configPojo, String internalUUID,
			String mssageId) {
		StringBuilder updateSts = new StringBuilder(
				"update " + configPojo.getSOURCE_TABLE_NAME() + " set internal_uuid = '" + internalUUID + "' ");
		StringBuilder condition = new StringBuilder(
				" where " + configPojo.getSOURCE_MESSAGE_ID() + " = '" + mssageId + "'");
		updateSts.append(condition);
		return updateSts;
	}

	private static StringBuilder getSelectQueryForUpdateStatus(ConfigPojo configPojo, List<Object> internalIdList) {
		StringBuilder updateSts = new StringBuilder("update " + configPojo.getSOURCE_TABLE_NAME()
				+ " set process_status = '" + XorConstant.PROCESS_INITIATED + "' ");
		StringBuilder condition = new StringBuilder(
				" where " + configPojo.getSOURCE_TABLE_IDENTITY_COLUMN_NAME() + " in( ");
		i = 0;
		internalIdList.stream().forEach(internalId -> {
			if (i > 0) {
				condition.append(", ");
			}
			condition.append(internalId.toString());
			i++;
		});
		condition.append(" ) ");
		updateSts.append(condition);
		return updateSts;
	}

	public static List<Object> getResultSetObject(String aggquery, EntityManager entityManager) {
		Query nativeQuery = entityManager.createNativeQuery(aggquery);
		return nativeQuery.getResultList();
	}
}
