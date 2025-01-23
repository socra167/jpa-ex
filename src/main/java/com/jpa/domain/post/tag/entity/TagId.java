package com.jpa.domain.post.tag.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Embeddable // 여러 값을 묶어서 객체화하고 ID로 사용하겠다
public class TagId {
	// tag의 name, post_id를 복합 키로 사용하겠다
	@Column(name = "post_id")
	private Long postId;

	@Column(length = 100)
	private String name;
}
