package com.jpa.domain.post.comment.service;

import org.springframework.stereotype.Service;

import com.jpa.domain.post.comment.entity.Comment;
import com.jpa.domain.post.comment.repository.CommentRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

	private final CommentRepository commentRepository;

	public Comment write(Long postId, String body) {
		Comment comment = Comment.builder()
			.postId(postId)
			.body(body)
			.build();

		return commentRepository.save(comment);
	}

	public long count() {
		return commentRepository.count();
	}
}
