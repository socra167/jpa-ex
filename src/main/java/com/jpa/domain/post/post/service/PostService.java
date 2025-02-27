package com.jpa.domain.post.post.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jpa.domain.member.entity.Member;
import com.jpa.domain.post.post.entity.Post;
import com.jpa.domain.post.post.repository.PostRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PostService {
	private final PostRepository postRepository;

	public Post write(Member writer, String title, String body) {
		Post post = Post.builder()
			.writer(writer)
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

	public List<Post> findAll() {
		return postRepository.findAll();
	}

	public List<Post> findByTitle(String title) {
		return postRepository.findByTitle(title);
	}

	public List<Post> findByTitleAndBody(String title, String body) {
		return postRepository.findByTitleAndBody(title, body);
	}

	public List<Post> findByTitleLike(String title) {
		return postRepository.findByTitleLike(title);
	}

	public List<Post> findByOrderByIdDesc() {
		return postRepository.findByOrderByIdDesc();
	}

	public List<Post> findTop2ByTitleOrderByIdDesc(String title) {
		return postRepository.findTop2ByTitleOrderByIdDesc(title);
	}

	public Page<Post> findAll(Pageable pageable) {
		return postRepository.findAll(pageable);
	}

	public Page<Post> findByTitleLike(String keyword, Pageable pageable) {
		return postRepository.findByTitleLike(keyword, pageable);
	}

	public List<Post> findByWriterUsername(String username) {
		return postRepository.findByWriterUsername(username);
	}
}
