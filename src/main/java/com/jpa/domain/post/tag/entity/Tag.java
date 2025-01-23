package com.jpa.domain.post.tag.entity;

import com.jpa.domain.post.post.entity.Post;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 게시물(1) : 태그(N)
// 실제로는 N:M에 가깝다

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Tag {
	// id / name / post_id
	// name끼리, post_id끼리는 중복될 수 있지만
	// name + post_id 끼리의 조합은 중복되면 안된다 -> 복합 키로 사용하자
	@EmbeddedId // @EmbeddedId 를 식별자 클래스에 적용해 복합 키로 사용할 수 있다
	private TagId id;

	@EqualsAndHashCode.Include
	@MapsId("postId") // -> 복합키의 post id와 이 post id 중 무엇을 사용해야 할지 에러 -> @MapsId 이 post_id를 참조하라는 뜻
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_id")
	private Post post;
}
