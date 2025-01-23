package com.jpa;

import static org.assertj.core.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.jpa.domain.post.post.service.Post;

@ActiveProfiles("test")
@SpringBootTest
class JpaApplicationTests {

	@Test
	@DisplayName("equals() hashCode()")
	void t1() {
		Post p1 = new Post(1L, "title1");
		Post p2 = new Post(1L, "title1");

		assertThat(p1).isEqualTo(p2); 	// equals()로 객체 비교를 하면 객체 참조값으로 비교하기 때문에 실패한다
										// Post의 equals()를 재정의해 참조 비교가 아닌 필드 비교로 적용할 수 있다

		// assertThat(p1.hashCode()).isEqualTo(p2.hashCode());	// hashCode()를 재정의하지 않으면 다른 해시코드가 나오기 때문에 실패한다
																// hashCode는 달라도 상관없으므로(eqals()비교로 충분한 상태이므로) 재정의하지 않겠다

		Set<Post> posts = new HashSet<>(); // Set : 중복을 허용하지 않는다
		posts.add(p1);
		posts.add(p2);

		// 우리는 equals()를 재정의했기 때문에 p1, p2는 같다. 따라서 세트에는 1개가 저장될 것을 기대한다
		// -> 실패 / HashSet은 동등성을 비교할 때 `Hash`값으로 비교한다. "HashSet"
		// 따라서, equals()를 재정의할 때 hashCode()도 오버라이딩하는 것이다.
		// -> 성공 / hashCode()를 재정의하니 기대했던대로 HashSet에 1개의 post만 추가되었다
		assertThat(posts).hasSize(1);
	}
}
