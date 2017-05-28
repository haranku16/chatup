package com.haranku16.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.haranku16.models.Message;

public interface MessageRepository extends MongoRepository<Message, String> {
	public Message findById(String id);
}
