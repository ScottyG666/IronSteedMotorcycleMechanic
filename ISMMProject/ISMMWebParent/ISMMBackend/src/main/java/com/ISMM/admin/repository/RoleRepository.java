package com.ISMM.admin.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ISMM.common.domain.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Integer>{

}
