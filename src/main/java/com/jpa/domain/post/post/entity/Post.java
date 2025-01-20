package com.jpa.domain.post.post.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Post {
	@Id // PRIMARY KEY
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
	private Long id; // long보다 null이 가능한 Long이 적합하다. long은 0으로 초기화되는데 값이 없는 것인지, 실제 값이 0인지 판별할 수 없다.
	private LocalDateTime createdDate;
	private LocalDateTime modifiedDate;
	@Column(length = 100)
	private String title;
	@Column(columnDefinition = "TEXT")
	private String body;
}
