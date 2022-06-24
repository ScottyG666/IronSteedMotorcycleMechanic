package com.ISMM.admin.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.ISMM.common.domain.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

}
