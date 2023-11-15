package metaint.replanet.rest.reviews.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="tbl_review_comment")
@NoArgsConstructor
@Getter
@ToString
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rev_comment_code")
    private Long commentCode;

    @Column(name = "rev_comment_context")
    private String commentContext;

    @Column(name = "rev_comment_writer")
    private String commentWriter;

    @Column(name = "rev_comment_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime commentDate;

    @Column(name = "reviewCode")
    private Long reviewCode;
}
