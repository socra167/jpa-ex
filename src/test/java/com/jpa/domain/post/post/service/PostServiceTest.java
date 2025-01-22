package com.jpa.domain.post.post.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@ActiveProfiles("test") // application.yml을 실행한 후 application-test.yml을 실행한 설정을 사용하도록 한다
@SpringBootTest
class PostServiceTest {
	@Autowired
	private PostService postService;

	@Transactional
	// @Rollback // SpringBootTest에선 Transactional이 있으면 Rollback은 생략해도 기본으로 동작한다
				// 만약 실제로 반영되게 하고 싶다면, @Rollback(false)로 명시적으로 설정하면 된다
	@Test
	@DisplayName("글을 2게 작성한다")
	void test() {
		postService.write("title1", "body1");
		postService.write("title2", "body2");
	}
}