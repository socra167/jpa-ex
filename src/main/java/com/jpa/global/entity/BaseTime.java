package com.jpa.global.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// 대부분의 Entity는 ID, 날짜를 갖지만 Tag에서는 날짜를 제외하고 싶다
// 날짜 정보만 있는 Entity를 분리하자
@AllArgsConstructor
@NoArgsConstructor
@Getter
@MappedSuperclass
public class BaseTime extends BaseEntity { // 실제 Entity에서는 ID도 필요한데 다중 상속이 불가능하므로 이렇게 상속받는다
	@CreatedDate // Jpa에서 생성일을 관리한다 (JpaAuditing 기능)
	@Column(updatable = false) // 생성일자가 수정되지 않도록 한다
	@Setter(AccessLevel.PRIVATE) // 이 필드에 Setter를 사용할 수 없도록 PRIVATE으로 만든다
	private LocalDateTime createdDate;

	@LastModifiedDate // Jpa에서 엔티티의 수정일을 관리한다 (JpaAuditing 기능)
	@Setter(AccessLevel.PRIVATE) // 이 필드에 Setter를 사용할 수 없도록 PRIVATE으로 만든다
	private LocalDateTime modifiedDate;
}
