package metaint.replanet.rest.reviews.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import metaint.replanet.rest.reviews.entity.Campaign;

import javax.persistence.*;
import java.util.List;

@Entity(name = "reviewPkg_entityReview")
@Table(name = "tbl_review")
@NoArgsConstructor
@Getter
//@ToString
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_code")
    private Long reviewCode;

    @Column(name = "review_title")
    private String reviewTitle;

    @Column(name = "description")
    private String description;

    @OneToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_code", referencedColumnName = "campaign_code")
    private Campaign campaign;

    @OneToMany(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "review_code", referencedColumnName = "review_code")
    private List<ReviewFile> reviewFileList;

}
