package metaint.replanet.rest.reviews.dto;

import lombok.*;
import metaint.replanet.rest.reviews.entity.ReviewFile;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CombineReviewDTO {

    private Long campaignCode;
    private String campaignTitle;
    private String campaignContent;
    private Date startDate;
    private Date endDate;
    private String campaignCategory;
    private Integer currentBudget;
    private Integer goalBudget;
    private String orgName;
    private String orgDescription;
    private String orgTel;
    private Long reviewCode;
    private String reviewTitle;
    private String reviewDescription;
    private Long reviewCampaignCode;
    private List<ReviewFile> reviewFileList;
}/*    private Long revFileCode;
    private String fileOriginName;
    private String fileOriginPath;
    private String fileSaveName;
    private String fileSavePath;
    private String fileExtension;*/