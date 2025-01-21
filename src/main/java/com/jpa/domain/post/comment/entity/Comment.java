package com.jpa.domain.post.comment.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.jpa.domain.post.post.entity.Post;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Comment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Setter(AccessLevel.PRIVATE)
	private Long id;

	@CreatedDate
	@Column(updatable = false)
	@Setter(AccessLevel.PRIVATE)
	private LocalDateTime createdDate;

	@LastModifiedDate
	@Setter(AccessLevel.PRIVATE)
	private LocalDateTime modifiedDate;

	@Column(columnDefinition = "TEXT")
	private String body;

	@ManyToOne
	@JoinColumn(name = "post_id")
	private Post post;
}
