package com.jpa.global;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.annotation.Transactional;

import com.jpa.domain.post.comment.entity.Comment;
import com.jpa.domain.post.comment.service.CommentService;
import com.jpa.domain.post.post.entity.Post;
import com.jpa.domain.post.post.service.PostService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class BaseInitData {
	private final PostService postService;
	private final CommentService commentService;

	@Order(1)
	@Bean
	public ApplicationRunner applicationRunner() {
		return args -> {
			if (0 < postService.count()) {
				return;
			}
			// 데이터가 없으면 샘플 데이터 3개 생성
			Post p1 = postService.write("title1", "body1");
			postService.write("title2", "body2");
			postService.write("title3", "body3");

			commentService.write(p1, "comment1");
			commentService.write(p1, "comment2");
			commentService.write(p1, "comment3");
		};
	}

	// 설정을 하지 않으면 어떤 ApplicationRunner가 먼저 실행될지 보장되지 않는다
	@Order(2) // 빈 생성의 순서를 설정할 수 있다
	@Bean
	public ApplicationRunner applicationRunner2() {
		return args -> {
			Post post = postService.findById(1L).get();
			Thread.sleep(1000);
			postService.modify(post, "new title", "new body");
		};
	}

	@Order(3)
	// @Bean // 삭제 비활성화
	public ApplicationRunner applicationRunner3() {
		return new ApplicationRunner() {
			@Transactional
			@Override
			public void run(ApplicationArguments args) throws Exception {

				Post p1 = postService.findById(1L).get();
				Post p2 = postService.findById(2L).get();

				System.out.println("===== p1 삭제 =====");
				postService.delete(p1);
				System.out.println("===== p1 삭제 완료 =====");
				System.out.println("===== p2 삭제 =====");
				postService.delete(p2);
				System.out.println("===== p2 삭제 완료 =====");
				// @Transactional 이 있을 때, update, delete 쿼리는 트랜잭션이 끝날 때(커밋될 때) 일괄처리된다

				// postService.deleteById(1L);
				// postService.deleteById(2L);
			}
		};
	}

	@Order(4)
	@Bean
	public ApplicationRunner applicationRunner4() {
		return new ApplicationRunner() {
			@Transactional
			@Override
			public void run(ApplicationArguments args) throws Exception {
				Post post = postService.findById(3L).get();
				System.out.println(post.getId() + "번 포스트를 가져왔습니다.");
				System.out.println("====================================");

				Post post2 = postService.findById(3L).get();
				// @Transactional이 있을 때: Entity가 영속성 컨텍스트에 존재하므로, 영속성 컨텍스트에서 가져와 SELECT 쿼리가 발생하지 않았다
				// @Transactional이 없을 때: 각각 findById()가 별개의 트랜잭션이므로, 각각의 호출마다 SELECT 쿼리가 발생했다
				System.out.println(post2.getId() + "번 포스트를 가져왔습니다.");
			}
		};
	}

	@Order(5)
	@Bean
	public ApplicationRunner applicationRunner5() {
		return new ApplicationRunner() {
			@Transactional
			@Override
			public void run(ApplicationArguments args) {
				Post post = postService.findById(3L).get();
				if (commentService.count() > 0) {
					return;
				}
				Comment c5 = Comment.builder()
					.body("comment5")
					.build();
				post.addComment(c5);
			}
		};
	}

	@Order(6)
	@Bean
	public ApplicationRunner applicationRunner6() {
		return new ApplicationRunner() {
			@Override
			@Transactional
			public void run(ApplicationArguments args) throws Exception {
				Comment c1 = commentService.findById(1L).get();
				// SELECT * FROM comment WHERE id = 1;

				Post post = c1.getPost(); // EAGER -> 이미 모든 post 정보를 위에서 Join으로 가져온다.
										// LAZY -> 위에서 찾은 c1의 post는 비어 있다.(null은 아니고, id만 채워져 있다.)
				// post를 얻기 위해 "SELECT * FROM post WHERE id = 1;" 쿼리가 동작할 것이다 -> x
				// 이미 모든 post 정보를 위에서 Join으로 가져온다.

				System.out.println("post.getId() = " + post.getId());
				System.out.println("post.getTitle() = " + post.getTitle());

				// 실제로는 쿼리가 두 번되지 않고, 연관있는 객체를 가져올 때 Join을 해서 가져왔다.
				// 이렇게 데이터를 꺼내오는 방식을 Fetch 라고 한다. / 조인해서 데이터를 한방에 가져오는 방식 FetchType.EAGER(열심히)

				// FetchType.LAZY는 시킨 일만 한다.

				// 안적으면 기본적으로 FetchType.EAGER / LAZY로 적어주는게 좋다.
			}
		};
	}
}
