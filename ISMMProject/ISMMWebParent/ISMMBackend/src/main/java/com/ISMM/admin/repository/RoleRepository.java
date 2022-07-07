package com.ISMM.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ISMM.common.domain.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer>{

}
