package metaint.replanet.rest.campaign.entity;

import javax.persistence.*;

@Entity(name = "campaignDescFile")
@Table(name = "tbl_campaign_desc_file")
public class CampaignDescFile {

    @Id
    @Column(name = "campaign_file_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int fileCode; // 파일 코드
    @JoinColumn(name = "campaign_code")
    @ManyToOne
    private CampaignDescription campaignDescription; // 모금 코드
    @Column(name = "file_origin_name")
    private String fileOriginName; // 원본 파일명
    @Column(name = "file_origin_path")
    private String fileOriginPath; // 원본 파일 경로
    @Column(name = "file_save_name")
    private String fileSaveName; // 저장 파일명
    @Column(name = "file_save_path")
    private String fileSavePath; // 저장 경로
    @Column(name = "file_extension")
    private String fileExtension; // 확장자

}
