package metaint.replanet.rest.reviews.dto;

import metaint.replanet.rest.reviews.entity.Campaign;

import java.sql.Date;

public class ReviewDTO{

    private Long campaignRevCode;
    private String reviewTitle;
    private String description;
    private Long campaignCode;

   /* private String campaignTitle;
    private String campaignContent;
    private Date startDate;
    private Date endDate;
    private String campaignCategory;
    private Integer currentBudget;
    private Integer goalBudget;
    private String orgName;
    private String orgDescription;
    private String orgTel;
*/
    public ReviewDTO() {
    }

    public ReviewDTO(Long campaignRevCode, String reviewTitle, String description, Long campaignCode) {
        this.campaignRevCode = campaignRevCode;
        this.reviewTitle = reviewTitle;
        this.description = description;
        this.campaignCode = campaignCode;
    }

    public Long getCampaignRevCode() {
        return campaignRevCode;
    }

    public void setCampaignRevCode(Long campaignRevCode) {
        this.campaignRevCode = campaignRevCode;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCampaignCode() {
        return campaignCode;
    }

    public void setCampaignCode(Long campaignCode) {
        this.campaignCode = campaignCode;
    }

    @Override
    public String toString() {
        return "ReviewDTO{" +
                "campaignRevCode=" + campaignRevCode +
                ", reviewTitle='" + reviewTitle + '\'' +
                ", description='" + description + '\'' +
                ", campaignCode=" + campaignCode +
                '}';
    }
}


