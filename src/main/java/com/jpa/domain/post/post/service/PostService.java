package com.jpa.domain.post.post.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
			.build();
		// post.setId(1L); : ID는 기본적으로 JPA가 관리한다
		return postRepository.save(post); // repository에서 save 하면 기본적으로 객체를 반환한다
	}

	@Transactional
	public Post modify(Post post1, String title, String body) {
		Post post = postRepository.findById(post1.getId()).get();
		post.setTitle(title);
		post.setBody(body);
		return post;
	}

	public long count() {
		return postRepository.count();
	}

	public Optional<Post> findById(long id) {
		return postRepository.findById(id);
	}

	public void delete(Post post) {
		postRepository.delete(post);
	}

	public void deleteById(long id) {
		postRepository.deleteById(id);
	}
}
