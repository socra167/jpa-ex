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
		Post post = Post.builder()
			.title(title)
			.body(body)
			.createdDate(LocalDateTime.now())
			.modifiedDate(LocalDateTime.now())
			.build();
		// post.setId(1L); : ID는 기본적으로 JPA가 관리한다
		return postRepository.save(post); // repository에서 save 하면 기본적으로 객체를 반환한다
	}

	public long count() {
		return postRepository.count();
	}
}
