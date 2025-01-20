package com.jpa.global;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jpa.domain.post.post.entity.Post;
import com.jpa.domain.post.post.entity.service.PostService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class BaseInitData {
	private final PostService postService;

	@Bean
	public ApplicationRunner applicationRunner() {
		return args -> {
			// 샘플 데이터 3개 생성
			Post p1 = postService.write("title1", "body1");
			System.out.println("p1 = " + p1.getId());
			Post p2 = postService.write("title2", "body2");
			System.out.println("p2 = " + p2.getId());
			Post p3 = postService.write("title3", "body3");
			System.out.println("p3 = " + p3.getId());
		};
	}
}
