package com.jpa.global;

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
}
