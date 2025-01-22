package com.jpa.domain.post.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jpa.domain.post.post.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

	List<Post> findByTitle(String title); 	// 네이밍 규칙만 지키면 동작한다
											// return 타입을 변경해서 Optional 혹은 여러개 반환 가능

	List<Post> findByTitleAndBody(String title, String body);

	List<Post> findByTitleLike(String title);
}
