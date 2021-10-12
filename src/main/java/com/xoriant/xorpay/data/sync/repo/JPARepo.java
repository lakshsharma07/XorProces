package com.xoriant.xorpay.data.sync.repo;

import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public class JPARepo {

	@Autowired
	private EntityManager entityManager;

	private int i;

	public List<Object[]> getResultSet(String aggquery) {
		Query nativeQuery = entityManager.createNativeQuery(aggquery);
		List<Object[]> rsl = nativeQuery.getResultList();
		return rsl;
	}

	public void getDatabasetDataTypeMap(String databaseSystem, Map<Integer, String> dbDataTypeIdMap,
			Map<String, Integer> dbDataTypeStrMap) {
		String selectQuery = "Select slno, " + databaseSystem + " from xp_system_db_datatype";
		List<Object[]> rsl = getResultSet(selectQuery);
		int total;
		if (rsl.size() > 0) {
			total = 2;
			rsl.stream().forEach(e -> {
				dbDataTypeIdMap.put(Integer.parseInt(e[0].toString()), e[1].toString());
				dbDataTypeStrMap.put(e[1].toString(), Integer.parseInt(e[0].toString()));
			});
		}
	}

}
