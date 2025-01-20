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
			Post p1 = postService.write("title1", "body1");
			Post p2 = postService.write("title2", "body2");
			Post p3 = postService.write("title3", "body3");
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
}
