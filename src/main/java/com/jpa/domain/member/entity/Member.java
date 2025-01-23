package com.jpa.domain.member.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.jpa.global.entity.BaseTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class Member extends BaseTime {
	@Column(length = 100)
	private String username;

	@Column(length = 100)
	private String password;

	@Column(length = 100)
	private String nickname;
}
