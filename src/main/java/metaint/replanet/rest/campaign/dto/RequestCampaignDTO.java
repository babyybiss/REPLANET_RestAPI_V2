package metaint.replanet.rest.campaign.dto;

import lombok.*;

import java.time.LocalDateTime;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class RequestCampaignDTO {

    private int campaignCode; // 모금 코드
    private String campaignTitle; // 모금 제목
    private String campaignContent; // 모금 내용
    private LocalDateTime startDate; // 모금 시작 일자
    private String endDate; // 모금 마감 일자
    private String campaignCategory; // 모금 카테고리
    private String currentBudget; // 현재 모금액
    private String goalBudget; // 목표 모금액
    private String orgCode; // 기부처 코드

//    private String orgName; // 단체명
//    private String orgDescription; //단체 소개
//    private String orgTel; // 단체 연락처


    public RequestCampaignDTO(String campaignTitle, String campaignContent, String endDate, String campaignCategory, String goalBudget) {
        this.campaignTitle = campaignTitle;
        this.campaignContent = campaignContent;
        this.endDate = endDate;
        this.campaignCategory = campaignCategory;
        this.goalBudget = goalBudget;
    }
}
