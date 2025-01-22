package com.jpa.domain.post.post.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import com.jpa.domain.member.entity.Member;
import com.jpa.domain.member.service.MemberService;
import com.jpa.domain.post.post.entity.Post;

@ActiveProfiles("test") // application.yml을 실행한 후 application-test.yml을 실행한 설정을 사용하도록 한다
@SpringBootTest
class PostServiceTest {
	@Autowired private PostService postService;
	@Autowired private MemberService memberService;

	@Transactional
	// @Rollback // SpringBootTest에선 Transactional이 있으면 Rollback은 생략해도 기본으로 동작한다
				// 만약 실제로 반영되게 하고 싶다면, @Rollback(false)로 명시적으로 설정하면 된다
	@Test
	@DisplayName("글을 작성할 수 있다")
	void writePost() {
		Member user1 = memberService.findByUsername("user1").get();
		postService.write(user1, "title1", "body1");
		postService.write(user1, "title2", "body2");
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

	@Transactional
	@Test
	@DisplayName("제목이 포함된 글들을 조회할 수 있다")
	void findByTitleLike() {
		var foundPosts = postService.findByTitleLike("title%");
		assertThat(foundPosts).hasSize(3);
	}

	@Transactional
	@Test
	@DisplayName("글을 아이디 순으로 내림차순 정렬되게 조회할 수 있다")
	void findPostByIdDesc() {
		// SELECT * FROM post ORDER BY id DESC;
		List<Post> posts = postService.findByOrderByIdDesc();
		assertThat(posts).hasSize(3);

		assertThat(posts.get(0).getId()).isEqualTo(3);
	}

	@Transactional
	@Test
	@DisplayName("위에서 2개의 글만 조회 ")
	void limitTwoPosts() {
		// SELECT * FROM post WHERE title = ? ORDER BY id DESC LIMIT 2;
		List<Post> foundPosts = postService.findTop2ByTitleOrderByIdDesc("title1");
		assertThat(foundPosts).hasSize(2);
		assertThat(foundPosts.get(0).getId()).isEqualTo(3);
	}

	@Transactional
	@Test
	@DisplayName("글을 페이지로 조회할 수 있다")
	void findPage() {
		// SELECT * FROM post ORDER BY id DESC LIMIT 2, 2;
		// LIMIT 2 : 상위에서 2개를 가져온다
		// LIMIT 2, 2 : 상위에서 2개를 건너뛰고 2개를 가져온다 (3, 4번째 데이터)
		//			-> 페이징에 사용
		// 이런 건 JPA에서 기본 메서드로 제공하지 않는다

		int itemsPerPage = 2; // 한 페이지에 보여줄 아이템 수
		int pageNumber = 2; // 현재 페이지 == 2

		pageNumber--; // 1을 빼는 이유는 jpa는 페이지 번호를 0부터 시작하기 때문
		Pageable pageable = PageRequest.of(pageNumber, itemsPerPage, Sort.by(Sort.Direction.DESC, "id"));
		Page<Post> postPage = postService.findAll(pageable);
		List<Post> posts = postPage.getContent();
		assertEquals(1, posts.size()); // 글이 총 3개이고, 현재 페이지는 2이므로 1개만 보여야 함
		Post post = posts.get(0);
		assertEquals(1, post.getId());
		assertEquals("title1", post.getTitle());
		assertEquals(3, postPage.getTotalElements()); // 전체 글 수
		assertEquals(2, postPage.getTotalPages()); // 전체 페이지 수
		assertEquals(1, postPage.getNumberOfElements()); // 현재 페이지에 노출된 글 수
		assertEquals(pageNumber, postPage.getNumber()); // 현재 페이지 번호
	}

	@Transactional
	@Test
	@DisplayName("sel")
	void sel() {
		// SELECT * FROM post WHERE title LIKE 'title%' ORDER BY id DESC LIMIT 0, 10;
		// 현재 페이지 : 1

		int itemsPerPage = 10; // 한 페이지에 보여줄 아이템 수
		int pageNumber = 1; // 현재 페이지 == 1
		pageNumber--;

		Pageable pageable = PageRequest.of(pageNumber, itemsPerPage, Sort.by(Sort.Direction.DESC, "id"));
		Page<Post> postPage = postService.findByTitleLike("title%", pageable);
		List<Post> posts = postPage.getContent();

		assertThat(posts).hasSize(3);
		Post post = posts.get(0);
		assertEquals(3, post.getId());
		assertEquals("title1", post.getTitle());
		assertEquals(3, postPage.getTotalElements()); // 전체 글 수
		assertEquals(1, postPage.getTotalPages()); // 전체 페이지 수
		assertEquals(3, postPage.getNumberOfElements()); // 현재 페이지에 노출된 글 수
		assertEquals(pageNumber, postPage.getNumber()); // 현재 페이지 번호
	}

	@Transactional
	@Test
	@DisplayName("회원 정보로 글을 조회할 수 있다")
	void findPostByWriterUsername() {
		// 회원 아이디로 회원이 작성한 글 목록 가져오기 / 이번엔 조인이 필요해졌다
		// SELECT * FROM post p JOIN user u ON p.writer_id = u.id WHERE username = 'user1';
		List<Post> posts = postService.findByWriterUsername("user1");
		// Writer의 username으로 찾는다
		assertThat(posts).hasSize(2);
	}
}
