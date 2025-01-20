package com.jpa.domain.post.post.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class) // 엔티티의 변화를 감지해 엔티티와 매핑된 테이블의 데이터를 조작한다 / 이벤트 리스너를 넣어 엔티티의 영속, 수정 이벤트를 감지한다
@NoArgsConstructor // JPA에서 엔티티를 Reflection으로 생성할 때 사용한다
@AllArgsConstructor // Builder는 내부적으로 AllArgsConstructor를 사용한다
@Builder
@Getter
@Setter
@Entity
public class Post {
	@Id // PRIMARY KEY
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
	@Setter(AccessLevel.PRIVATE) // 이 필드에 Setter를 사용할 수 없도록 PRIVATE으로 만든다
	private Long id; // long보다 null이 가능한 Long이 적합하다. long은 0으로 초기화되는데 값이 없는 것인지, 실제 값이 0인지 판별할 수 없다.

	@CreatedDate // Jpa에서 생성일을 관리한다 (JpaAuditing 기능)
	@Column(updatable = false) // 생성일자가 수정되지 않도록 한다
	private LocalDateTime createdDate;

	@LastModifiedDate // Jpa에서 엔티티의 수정일을 관리한다 (JpaAuditing 기능)
	private LocalDateTime modifiedDate;

	@Column(length = 100)
	private String title;

	@Column(columnDefinition = "TEXT")
	private String body;
}
