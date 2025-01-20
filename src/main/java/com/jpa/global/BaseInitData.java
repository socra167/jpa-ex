package com.jpa.global;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import com.jpa.domain.post.post.entity.Post;
import com.jpa.domain.post.post.entity.service.PostService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class BaseInitData {
	private final PostService postService;

	@Order(1)
	@Bean
	public ApplicationRunner applicationRunner() {
		return args -> {
			if (0 < postService.count()) {
				return;
			}
			// 데이터가 없으면 샘플 데이터 3개 생성
			System.out.println("=== 1번 데이터 생성 ===");
			Post p1 = postService.write("title1", "body1");
			System.out.println("=== 1번 데이터 생성 완료 ===");
			System.out.println("=== 2번 데이터 생성 ===");
			Post p2 = postService.write("title2", "body2");
			System.out.println("=== 2번 데이터 생성 완료 ===");
			System.out.println("=== 3번 데이터 생성 ===");
			Post p3 = postService.write("title3", "body3");
			System.out.println("=== 3번 데이터 생성 완료 ===");
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
	@Bean
	public ApplicationRunner applicationRunner3() {
		return args -> {
			Post p1 = postService.findById(1L).get();
			Post p2 = postService.findById(2L).get();

			System.out.println("===== p1 삭제 =====");
			postService.delete(p1);
			System.out.println("===== p1 삭제 완료 =====");
			System.out.println("===== p2 삭제 =====");
			postService.delete(p2);
			System.out.println("===== p2 삭제 완료 =====");

			// postService.deleteById(1L);
			// postService.deleteById(2L);
		};
	}

	@Order(4)
	@Bean
	public ApplicationRunner applicationRunner4() {
		return new ApplicationRunner() {
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
}
