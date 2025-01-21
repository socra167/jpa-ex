package com.jpa.domain.post.comment.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.jpa.domain.post.comment.entity.Comment;
import com.jpa.domain.post.comment.repository.CommentRepository;
import com.jpa.domain.post.post.entity.Post;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {

	private final CommentRepository commentRepository;

	public Comment write(Post post, String body) {
		Comment comment = Comment.builder()
			.post(post)
			.body(body)
			.build();

		return commentRepository.save(comment);
	}

	public long count() {
		return commentRepository.count();
	}

	public Optional<Comment> findById(Long id) {
		return commentRepository.findById(id);
	}

	public Comment save(Comment comment) {
		return commentRepository.save(comment);
	}
}
