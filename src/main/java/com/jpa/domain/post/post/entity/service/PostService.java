package com.jpa.domain.post.post.entity.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.jpa.domain.post.post.entity.Post;
import com.jpa.domain.post.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {
	private final PostRepository postRepository;

	public Post write(String title, String body) {
		Post post = new Post();
		// post.setId(1L); : ID는 기본적으로 JPA가 관리한다
		post.setCreatedDate(LocalDateTime.now());
		post.setModifiedDate(LocalDateTime.now());
		post.setTitle(title);
		post.setBody(body);
		postRepository.save(post);
		return post;
	}
}
