package metaint.replanet.rest.reviews.dto;

import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCommentDTO {

    private Long revCommentCode;

    private String revCommentContent;

    private String memberCode;

    private String revCommentDate;

    private Long reviewCode;

    private String revCommentMonitorized;
}
