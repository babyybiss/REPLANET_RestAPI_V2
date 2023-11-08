package metaint.replanet.rest.campaign.entity;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "CampaignDescription")
@Table(name = "tbl_campaign_description")
public class CampaignDescription {

    @Id
    @Column(name = "campaign_code")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int campaignCode; // 모금 코드
    //    @OneToMany(mappedBy = "charityDescription")
//    private List<CharityDescfile> charityDescfileList; // 파일 정보
//    @ManyToOne
//    @JoinColumn(name = "member_code")
//    private Member member; // 회원 정보
    @Column(name = "campaign_title")
    private String campaignTitle; // 모금 제목
    @Column(name = "campaign_content")
    private String campaignContent; // 모금 내용
    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm")
    private LocalDateTime startDate; // 모금 시작 일자
    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd/HH:mm")
    private LocalDateTime endDate; // 모금 마감 일자
    @Column(name = "campaign_category")
    private String campaignCategory; // 모금 카테고리
    @Column(name = "current_budget")
    private int currentBudget; // 현재 모금액
    @Column(name = "goal_budget")
    private int goalBudget; // 목표 모금액
    @Column(name = "org_name")
    private String orgName; // 단체명
    @Column(name = "org_description")
    private String orgDescription; //단체 소개
    @Column(name = "org_tel")
    private String orgTel; // 단체 연락처



    protected CampaignDescription() {}

    public CampaignDescription( String campaignTitle, String campaignContent, LocalDateTime startDate, LocalDateTime endDate, String campaignCategory, int currentBudget, int goalBudget, String orgName, String orgDescription, String orgTel) {
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
    }

    public int getCampaignCode() {
        return campaignCode;
    }

    public String getCampaignTitle() {
        return campaignTitle;
    }

    public String getCampaignContent() {
        return campaignContent;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public String getCampaignCategory() {
        return campaignCategory;
    }

    public int getCurrentBudget() {
        return currentBudget;
    }

    public int getGoalBudget() {
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

    @Override
    public String toString() {
        return "CampaignDescription{" +
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
                '}';
    }
}
