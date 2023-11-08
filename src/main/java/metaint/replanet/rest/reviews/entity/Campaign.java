package metaint.replanet.rest.reviews.entity;

import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Entity(name = "reviewPkg_entityCampaign")
@Table(name = "tbl_campaign_description")
public class Campaign {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_code")
    private Long campaignCode;

    @Column(name = "campaign_title")
    private String campaignTitle;

    @Column(name = "campaign_content")
    private String campaignContent;

    @Column(name = "start_date")
    private java.sql.Date startDate;

    @Column(name = "end_date")
    private java.sql.Date endDate;

    @Column(name = "campaign_category")
    private String campaignCategory;

    @Column(name = "current_budget")
    private Integer currentBudget;

    @Column(name = "goal_budget")
    private Integer goalBudget;

    @Column(name = "org_name")
    private String orgName;

    @Column(name = "org_description")
    private String orgDescription;

    @Column(name = "org_tel")
    private String orgTel;

    @OneToOne(optional = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "campaign_code", insertable = false, updatable = false)
    private Review review;



    protected Campaign() {
    }

    public Campaign(Long campaignCode, String campaignTitle, String campaignContent, Date startDate, Date endDate, String campaignCategory, Integer currentBudget, Integer goalBudget, String orgName, String orgDescription, String orgTel, Review review) {
        this.campaignCode = campaignCode;
        this.campaignTitle = campaignTitle;
        this.campaignContent = campaignContent;
        this.startDate = startDate;
        this.endDate = endDate;
        this.campaignCategory = campaignCategory;
        this.currentBudget = currentBudget;
        this.goalBudget = goalBudget;
        this.orgName = orgName;
        this.orgDescription = orgDescription;
        this.orgTel = orgTel;
        this.review = review;
    }

    public Long getCampaignCode() {
        return campaignCode;
    }

    public String getCampaignTitle() {
        return campaignTitle;
    }

    public String getCampaignContent() {
        return campaignContent;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getCampaignCategory() {
        return campaignCategory;
    }

    public Integer getCurrentBudget() {
        return currentBudget;
    }

    public Integer getGoalBudget() {
        return goalBudget;
    }

    public String getOrgName() {
        return orgName;
    }

    public String getOrgDescription() {
        return orgDescription;
    }

    public String getOrgTel() {
        return orgTel;
    }

    public Review getReview() {
        return review;
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "campaignCode=" + campaignCode +
                ", campaignTitle='" + campaignTitle + '\'' +
                ", campaignContent='" + campaignContent + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", campaignCategory='" + campaignCategory + '\'' +
                ", currentBudget=" + currentBudget +
                ", goalBudget=" + goalBudget +
                ", orgName='" + orgName + '\'' +
                ", orgDescription='" + orgDescription + '\'' +
                ", orgTel='" + orgTel + '\'' +
                ", review=" + review +
                '}';
    }
}
