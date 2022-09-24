package com.ISMM.admin.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ISMM.common.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

	@Query("SELECT u FROM User u "
			+ "WHERE u.email = :email")
	public User getUserByEmail(@Param("email") String email);
	
	public Long countById(Integer id) ;
	
	@Query("SELECT u FROM User u WHERE CONCAT(u.id, ' ', u.email, ' ', u.firstName, ' ',"
			+ " u.lastName) LIKE %?1%")
	public Page<User> findAll(String keyword, Pageable pageable);
	
	@Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1 ")
	@Modifying
	public void updateEnabledStatus (Integer id, Boolean enabled);
	
}