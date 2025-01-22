package com.jpa.domain.post.post.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.jpa.domain.member.entity.Member;
import com.jpa.domain.post.comment.entity.Comment;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@EntityListeners(AuditingEntityListener.class)
// 엔티티의 변화를 감지해 엔티티와 매핑된 테이블의 데이터를 조작한다 / 이벤트 리스너를 넣어 엔티티의 영속, 수정 이벤트를 감지한다
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

	@ManyToOne(fetch = FetchType.LAZY)
	private Member writer;

	@OneToMany(mappedBy = "post", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = false)
	// @OneToMany만 사용하면 post_comment 테이블이 생성된다
	// 외래 키를 누가 가져야 하는가? Comment 클래스에 있는 연관된 변수 이름 (외래키는 항상 다쪽에 생긴다)
	// mapped를 사용하지 않은 쪽이 외래키의 주인이 된다(Comment)
	// (cascade = CascadeTyoe.ALL / PERSIST / REMOVE / ...): 영속성의 전파 설정
	// -> applicationRunner8()
	// (orphanRemoval = true): 연관 데이터를 삭제할 것인지 설정
	// 부모와 연결이 끊겼다는 의미에서의 orphan
	// comments 리스트에서 comment(객체)가 제거되면 DB에서 실제 Comment가 delete된다
	@Builder.Default // 기본 초기화 값이 존재하므로, 빌더 패턴에서 제외시킨다
	private List<Comment> comments = new ArrayList<>();
	// @OneToMany를 붙이지 않으면 컴파일 오류가 나오는데
	// 'Basic' attribute type should not be a container
	// Spring Data JPA에서 이 데이터를 어떻게 저장해야 할 지 모르기 때문이다

	public void addComment(Comment comment) {
		comments.add(comment);
		comment.setPost(this);
	}

	public void removeComment(Comment c1) {
		comments.remove(c1);
	}

	public void removeConmment(long id) {
		Optional<Comment> opComment = comments.stream()
			.filter(com -> com.getId() == id)
			.findFirst();

		opComment.ifPresent(comment -> comments.remove(comment));
	}

	public void removeAllComments() {
		comments.stream()
			.forEach(comment -> {
				comment.setPost(null);
			});
		comments.clear();
	}
}
