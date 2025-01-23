package com.jpa.domain.post.comment.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.jpa.domain.member.entity.Member;
import com.jpa.domain.post.post.entity.Post;
import com.jpa.global.entity.BaseTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Comment extends BaseTime {
	@Column(columnDefinition = "TEXT")
	private String body;

	@ManyToOne(fetch = FetchType.LAZY)
	private Member writer;

	@ManyToOne(fetch = FetchType.LAZY) // 실제로 DB에 저장될 때는 Post 자체가 아니라 post_id가 저장되도록 한다
	@JoinColumn(name = "post_id") // 설정하지 않아도 기본 post_id 로 네이밍된다
	private Post post;
}
