package com.jpa.domain.post.post.service;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.jpa.domain.post.post.entity.Post;

@ActiveProfiles("test") // application.yml을 실행한 후 application-test.yml을 실행한 설정을 사용하도록 한다
@SpringBootTest
class PostServiceTest {
	@Autowired
	private PostService postService;
	@Autowired
	private ServerProperties serverProperties;

	@Transactional
	// @Rollback // SpringBootTest에선 Transactional이 있으면 Rollback은 생략해도 기본으로 동작한다
				// 만약 실제로 반영되게 하고 싶다면, @Rollback(false)로 명시적으로 설정하면 된다
	@Test
	@DisplayName("글을 작성할 수 있다")
	void writePost() {
		postService.write("title1", "body1");
		postService.write("title2", "body2");
	}

	@Transactional
	@Test
	@DisplayName("등록된 모든 글을 조회할 수 있다")
	void findAllPost() {
		var allPosts = this.postService.findAll();
		assertThat(allPosts.size()).isEqualTo(3);

		var post = allPosts.get(0);
		assertThat(post.getTitle()).isEqualTo("title1");
	}

	@Transactional
	@Test
	@DisplayName("ID로 글을 조회할 수 있다")
	void findPost() {
		var optionalPost = postService.findById(1L);
		assertThat(optionalPost).isPresent();
		assertThat(optionalPost.get().getTitle()).isEqualTo("title1");
	}

	@Transactional
	@Test
	@DisplayName("제목으로 글을 조회할 수 있다")
	void findPostByTitle() {
		var fountPosts = postService.findByTitle("title1"); // select * from post where title = 'title1'
		assertThat(fountPosts).hasSize(3);
	}

	@Transactional
	@Test
	@DisplayName("제목과 내용으로 글을 조회할 수 있다")
	void findPostByTitleAndBody() {
		// SELECT * FROM post WHERE title = ? and body = ?;
		var foundPosts = postService.findByTitleAndBody("title1", "body1");
		assertThat(foundPosts).hasSize(1);
	}

	@Test
	@DisplayName("제목이 포함된 글들을 조회할 수 있다")
	void findByTitleLike() {
		var foundPosts = postService.findByTitleLike("title%");
		assertThat(foundPosts).hasSize(3);
	}

	@Test
	@DisplayName("아이디 순으로 내림차순 정렬되게 조회할 수 있다")
	void findPostByIdDesc() {
		// SELECT * FROM post ORDER BY id DESC;
		List<Post> posts = postService.findByOrderByIdDesc();
		assertThat(posts).hasSize(3);

		assertThat(posts.get(0).getId()).isEqualTo(3);
	}
}
