package com.iportalen.timestack.domain.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.iportalen.timestack.domain.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	Optional<User> findById(Long id);
	
	User findByUsername(String username);
	
	Optional<User> findByPhonenumber(String phonenumber);
	
	Optional<User> findByEmail(String email);
	
	Optional<User> findByFirebaseUid(String firebaseUid);
}
