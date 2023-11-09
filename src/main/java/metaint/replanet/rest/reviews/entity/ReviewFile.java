package metaint.replanet.rest.reviews.entity;

import javax.persistence.*;

@Entity(name="reviewPkg_campaignRevFile")
@Table(name="tbl_campaign_rev_file")
public class ReviewFile {

    @Id
    @Column(name = "REV_FILE_CODE", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long revFileCode;

    //@Column(name="file_origin_name")
    //private String fileOriginName;

   // @Column(name="file_origin_path")
    //private String fileOriginPath;

    @Column(name="file_save_name")
    private String fileSaveName;

    @Column(name="file_save_path")
    private String fileSavePath;

    @Column(name="file_extension")
    private String fileExtension;

    @ManyToOne
    @JoinColumn(name="campaign_rev_Code")
    private Review review;

    protected ReviewFile() {
    }

    public ReviewFile(Long revFileCode, String fileSaveName, String fileSavePath, String fileExtension, Review review) {
        this.revFileCode = revFileCode;
        this.fileSaveName = fileSaveName;
        this.fileSavePath = fileSavePath;
        this.fileExtension = fileExtension;
        this.review = review;
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

    public Review getReview() {
        return review;
    }

    public void setReview(Review review) {
        this.review = review;
    }

    @Override
    public String toString() {
        return "ReviewFile{" +
                "revFileCode=" + revFileCode +
                ", fileSaveName='" + fileSaveName + '\'' +
                ", fileSavePath='" + fileSavePath + '\'' +
                ", fileExtension='" + fileExtension + '\'' +
                ", review=" + review +
                '}';
    }
}
