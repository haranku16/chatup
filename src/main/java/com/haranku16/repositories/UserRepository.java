package com.haranku16.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.haranku16.models.User;

public interface UserRepository extends MongoRepository<User, String> {
	public User findByUsername(String username);
	public List<User> findByFirstname(String firstname);
	public List<User> findByLastname(String lastname);
}
