package com.xoriant.xorpay.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.XMLStructureMasterEntity;

@Repository
@Transactional
public interface XMLStructureMasterRepo extends JpaRepository<XMLStructureMasterEntity, Integer> {

	List<XMLStructureMasterEntity> findByOrderByTagParentIdDesc();

	List<XMLStructureMasterEntity> findByActiveIndOrderById(Character status);

	List<XMLStructureMasterEntity> findTop2ByOrderById();

	List<XMLStructureMasterEntity> findByActiveIndOrderByTagSeq(Character statusY);

	List<XMLStructureMasterEntity> findByActiveInd(Character status);

	XMLStructureMasterEntity findByTagNameAndTagParentId(String tagName, Integer pid);

	// List<XMLStructureMasterEntity> findDistinctByTagParentId();

	@Query("select x from XMLStructureMasterEntity x where x.id "
			+ "in(select distinct(y.tagParentId) from XMLStructureMasterEntity y "
			+ "where y.activeInd ='N' AND y.tagParentId is not null)")
	List<XMLStructureMasterEntity> findDistinctByTagParentId();

	List<XMLStructureMasterEntity> findByTagParentIdAndActiveInd(Integer tagParentId, Character activeFlag);

	XMLStructureMasterEntity findByIdAndActiveInd(Integer childId, Character activeFlag);

	List<XMLStructureMasterEntity> findByIdIn(List<Integer> tagINotPresent);

}
