package com.xoriant.xorpay.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xoriant.xorpay.entity.SystemDataTypeEntity;

@Repository
@Transactional
public interface SystemDataTypeRepo extends JpaRepository<SystemDataTypeEntity, Integer> {


}
