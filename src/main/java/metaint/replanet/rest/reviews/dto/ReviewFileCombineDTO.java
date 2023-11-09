package metaint.replanet.rest.reviews.dto;

public class ReviewFileCombineDTO {

    private Long revFileCode;

    //private String fileOriginName;

   // private String fileOriginPath;

    private String fileSaveName;

    private String fileSavePath;

    private String fileExtension;

    private Long reviewCampaignRevCode;

    private String reviewTitle;

    private String reviewDescription;
    private Long reviewCampaignCode;

    public ReviewFileCombineDTO(Long revFileCode, String fileSaveName, String fileSavePath, String fileExtension, Long reviewCampaignRevCode, String reviewTitle, String reviewDescription, Long reviewCampaignCode) {
        this.revFileCode = revFileCode;
        this.fileSaveName = fileSaveName;
        this.fileSavePath = fileSavePath;
        this.fileExtension = fileExtension;
        this.reviewCampaignRevCode = reviewCampaignRevCode;
        this.reviewTitle = reviewTitle;
        this.reviewDescription = reviewDescription;
        this.reviewCampaignCode = reviewCampaignCode;
    }

    public Long getRevFileCode() {
        return revFileCode;
    }

    public void setRevFileCode(Long revFileCode) {
        this.revFileCode = revFileCode;
    }

    public String getFileSaveName() {
        return fileSaveName;
    }

    public void setFileSaveName(String fileSaveName) {
        this.fileSaveName = fileSaveName;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }

    public Long getReviewCampaignRevCode() {
        return reviewCampaignRevCode;
    }

    public void setReviewCampaignRevCode(Long reviewCampaignRevCode) {
        this.reviewCampaignRevCode = reviewCampaignRevCode;
    }

    public String getReviewTitle() {
        return reviewTitle;
    }

    public void setReviewTitle(String reviewTitle) {
        this.reviewTitle = reviewTitle;
    }

    public String getReviewDescription() {
        return reviewDescription;
    }

    public void setReviewDescription(String reviewDescription) {
        this.reviewDescription = reviewDescription;
    }

    public Long getReviewCampaignCode() {
        return reviewCampaignCode;
    }

    public void setReviewCampaignCode(Long reviewCampaignCode) {
        this.reviewCampaignCode = reviewCampaignCode;
    }

    @Override
    public String toString() {
        return "ReviewFileCombineDTO{" +
                "revFileCode=" + revFileCode +
                ", fileSaveName='" + fileSaveName + '\'' +
                ", fileSavePath='" + fileSavePath + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                ", reviewCampaignRevCode=" + reviewCampaignRevCode +
                ", reviewTitle='" + reviewTitle + '\'' +
                ", reviewDescription='" + reviewDescription + '\'' +
                ", reviewCampaignCode=" + reviewCampaignCode +
                '}';
    }
}
