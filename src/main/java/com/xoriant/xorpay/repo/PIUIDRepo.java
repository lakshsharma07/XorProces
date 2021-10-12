package com.xoriant.xorpay.repo;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.PIUIDEntity;

@Repository
@Transactional
public interface PIUIDRepo extends JpaRepository<PIUIDEntity, Integer> {

	List<PIUIDEntity> findByActiveInd(Character statusY);

	PIUIDEntity findByProfileName(String profileName);

	List<PIUIDEntity> findByProfileNameIn(List<String> profileList);

	List<PIUIDEntity> findByActiveIndAndProfileNameIgnoreCaseIn(Character statusY, Set<String> profileList);

}
