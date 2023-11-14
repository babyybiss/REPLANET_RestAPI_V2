package metaint.replanet.rest.reviews.repository;

import metaint.replanet.rest.reviews.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRespository extends JpaRepository<Comment, Long> {

    List<Comment> findByReviewCode(Long reviewCode);
}
