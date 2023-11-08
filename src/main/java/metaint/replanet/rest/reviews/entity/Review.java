package metaint.replanet.rest.reviews.entity;

import metaint.replanet.rest.reviews.entity.Campaign;

import javax.persistence.*;

@Entity(name = "reviewPkg_entityReview")
@Table(name = "tbl_campaign_review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_rev_code")
    private Long campaignRevCode;

    @Column(name = "review_title")
    private String reviewTitle;

    @Column(name = "description")
    private String description;

    @Column(name = "campaign_code")
    private Long campaignCode;

    // Constructors
    protected Review() {
    }

    public Review(Long campaignRevCode, String reviewTitle, String description, Long campaignCode) {
        this.campaignRevCode = campaignRevCode;
        this.reviewTitle = reviewTitle;
        this.description = description;
        this.campaignCode = campaignCode;
    }

    public Long getCampaignRevCode() {
        return campaignRevCode;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public String getDescription() {
        return description;
    }

    public Long getCampaignCode() {
        return campaignCode;
    }

    @Override
    public String toString() {
        return "Review{" +
                "campaignRevCode=" + campaignRevCode +
                ", reviewTitle='" + reviewTitle + '\'' +
                ", description='" + description + '\'' +
                ", campaignCode=" + campaignCode +
                '}';
    }
}
