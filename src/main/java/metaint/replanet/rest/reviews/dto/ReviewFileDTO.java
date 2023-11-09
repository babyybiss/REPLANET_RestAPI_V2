package metaint.replanet.rest.reviews.dto;

public class ReviewFileDTO {

    private Long revFileCode;

  //  private String fileOriginName;

  //  private String fileOriginPath;

    private String fileSaveName;

    private String fileSavePath;

    private String fileExtension;

    private Long campaignRevCode;

    public ReviewFileDTO() {
    }

    public ReviewFileDTO(Long revFileCode, String fileSaveName, String fileSavePath, String fileExtension, Long campaignRevCode) {
        this.revFileCode = revFileCode;
        this.fileSaveName = fileSaveName;
        this.fileSavePath = fileSavePath;
        this.fileExtension = fileExtension;
        this.campaignRevCode = campaignRevCode;
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

    public Long getCampaignRevCode() {
        return campaignRevCode;
    }

    public void setCampaignRevCode(Long campaignRevCode) {
        this.campaignRevCode = campaignRevCode;
    }

    @Override
    public String toString() {
        return "ReviewFileDTO{" +
                "revFileCode=" + revFileCode +
                ", fileSaveName='" + fileSaveName + '\'' +
                ", fileSavePath='" + fileSavePath + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                ", campaignRevCode=" + campaignRevCode +
                '}';
    }
}
