package metaint.replanet.rest.campaign.dto;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDateTime;

public class CampaignDescriptionDTO {

    private String campaignCode; // 모금 코드
    private String campaignTitle; // 모금 제목
    private String campaignContent; // 모금 내용
    private LocalDateTime startDate; // 모금 시작 일자
    private String endDate; // 모금 마감 일자
    private String campaignCategory; // 모금 카테고리
    private String currentBudget; // 현재 모금액
    private String goalBudget; // 목표 모금액
    private String orgName; // 단체명
    private String orgDescription; //단체 소개
    private String orgTel; // 단체 연락처

    public CampaignDescriptionDTO() {
    }

    @Override
    public String toString() {
        return "CampaignDescriptionDTO{" +
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

    public CampaignDescriptionDTO(String campaignTitle, String campaignContent, String endDate, String campaignCategory, String currentBudget, String goalBudget, String orgName, String orgDescription, String orgTel) {
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
    }

    public String getCampaignCode() {
        return campaignCode;
    }

    public void setCampaignCode(String campaignCode) {
        this.campaignCode = campaignCode;
    }

    public String getCampaignTitle() {
        return campaignTitle;
    }

    public void setCampaignTitle(String campaignTitle) {
        this.campaignTitle = campaignTitle;
    }

    public String getCampaignContent() {
        return campaignContent;
    }

    public void setCampaignContent(String campaignContent) {
        this.campaignContent = campaignContent;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getCampaignCategory() {
        return campaignCategory;
    }

    public void setCampaignCategory(String campaignCategory) {
        this.campaignCategory = campaignCategory;
    }

    public String getCurrentBudget() {
        return currentBudget;
    }

    public void setCurrentBudget(String currentBudget) {
        this.currentBudget = currentBudget;
    }

    public String getGoalBudget() {
        return goalBudget;
    }

    public void setGoalBudget(String goalBudget) {
        this.goalBudget = goalBudget;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgDescription() {
        return orgDescription;
    }

    public void setOrgDescription(String orgDescription) {
        this.orgDescription = orgDescription;
    }

    public String getOrgTel() {
        return orgTel;
    }

    public void setOrgTel(String orgTel) {
        this.orgTel = orgTel;
    }
}
