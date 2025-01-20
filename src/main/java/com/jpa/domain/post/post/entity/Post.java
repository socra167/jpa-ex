package com.jpa.domain.post.post.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	@Column(length = 100)
	private String title;
	@Column(columnDefinition = "TEXT")
	private String body;
}
