package metaint.replanet.rest.reviews.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.Column;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CommentDTO {

    private Long commentCode;
    private String commentContext;
    private String commentWriter;
    private String commentDate;
    private Long reviewCode;
}
