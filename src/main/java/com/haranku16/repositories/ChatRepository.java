package com.haranku16.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.haranku16.models.Chat;

public interface ChatRepository extends MongoRepository<Chat, String> {
	public Chat findById(String id);
}
