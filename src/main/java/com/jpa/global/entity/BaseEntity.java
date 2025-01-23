package com.jpa.global.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// BaseEntity에 대한 테이블은 필요 없으므로 @Entity를 붙이지 않는다
// 그렇다고 아무것도 안해주면 JPA가 무시해버리기 때문에,
// @MappedSuperclass 애너테이션으로 다른 Entity들이 상속받아 사용한다는 것을 알려준다
@AllArgsConstructor
@NoArgsConstructor
@Getter
@MappedSuperclass
public class BaseEntity {
	@Id // PRIMARY KEY
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AUTO_INCREMENT
	@Setter(AccessLevel.PRIVATE) // 이 필드에 Setter를 사용할 수 없도록 PRIVATE으로 만든다
	private Long id; // long보다 null이 가능한 Long이 적합하다. long은 0으로 초기화되는데 값이 없는 것인지, 실제 값이 0인지 판별할 수 없다.
}
