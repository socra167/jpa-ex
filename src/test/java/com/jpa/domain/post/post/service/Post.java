package com.jpa.domain.post.post.service;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // 특정 값으로만 비교하도록 설정한다
public class Post {
	@EqualsAndHashCode.Include // 이렇게 선언하면 이 값으로만 비교한다
	private Long id;

	private String title;

	/* Lombok @EqualsAndHashCode로 대체할 수 있다
	// equals() 메서드를 재정의해서 참조값 비교가 아닌 필드값 비교를 하도록 한다
	@Override
	public boolean equals(Object o) {
		if (o == null || getClass() != o.getClass())
			return false;
		Post post = (Post)o;
		return Objects.equals(id, post.id) && Objects.equals(title, post.title);
	}

	@Override
	public int hashCode() {
		// return Objects.hash(id, title);	// 내부 값으로 해시 코드를 만든다
											// 해시 : `동일한 입력값`이 들어오면 항상 `동일한 출력값`이 나온다
		return Objects.hash(id); // 애초에 id는 중복되지 않으므로 id로만 해시코드를 생성해도 무방하다
	}
	*/
}
