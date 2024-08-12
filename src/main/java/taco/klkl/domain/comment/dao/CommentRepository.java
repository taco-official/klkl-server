package taco.klkl.domain.comment.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import taco.klkl.domain.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {
	List<Comment> findAllByProduct_Id(Long productId);
}
