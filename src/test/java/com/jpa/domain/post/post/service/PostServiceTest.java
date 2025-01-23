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

		// Post에서 Member 정보가 필요할 때 가능한 방법은 2가지다
		// 1. Post를 먼저 조회해서 member id를 알아온 후 -> member 조회 -> select 2번 조회
		// 2. post와 member를 join해서 한번에 조회
		Post post = posts.get(0);
		System.out.println("post.getId() = " + post.getId());
		System.out.println("post.getTitle() = " + post.getTitle());
		System.out.println("post.getWriter().getUsername() = " + post.getWriter().getUsername()); // <- member에 대해 select

		// JPA에선 결과적으로 회원 정보를 가져올 땐 1번 방법을 사용했다
		// Join을 하긴 했지만 회원 정보가 없다 -> 필요할 때 다시 조회(LAZY)

		// 대부분의 경우, JPA는 연관된 정보를 가져올 때 SELECT Query를 여러 번 날린다
		// EAGER로 미리 다 가져오면 되지 않을까? -> 그래도 동일한 쿼리가 발생했다
	}

	@Transactional
	@Test
	@DisplayName("글 목록에서 회원 정보 가져오기")
	void findMemberFromPosts() {
		List<Post> posts = postService.findAll();

		for (Post post : posts) {
			System.out.println(post.getId() + ", " + post.getTitle() + ", " + post.getWriter().getUsername());
		}
	}
}
