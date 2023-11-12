package metaint.replanet.rest.campaign.service;

import metaint.replanet.rest.campaign.dto.CampaignDescFileDTO;
import metaint.replanet.rest.campaign.dto.CampaignDescriptionDTO;
import metaint.replanet.rest.campaign.entity.CampaignAndFile;
import metaint.replanet.rest.campaign.entity.CampaignDescFile;
import metaint.replanet.rest.campaign.entity.CampaignDescription;
import metaint.replanet.rest.campaign.entity.Donation;
import metaint.replanet.rest.campaign.repository.CampaignAndFileRepository;
import metaint.replanet.rest.campaign.repository.CampaignFileRepository;
import metaint.replanet.rest.campaign.repository.CampaignRepository;
import metaint.replanet.rest.campaign.repository.DonationRepository1;
import metaint.replanet.rest.util.FileUploadUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CampaignService {


    private final CampaignRepository campaignRepository;
    private final CampaignFileRepository campaignFileRepository;

    private final CampaignAndFileRepository campaignAndFileRepository;
    private final DonationRepository1 donationRepository1;
    private final ModelMapper modelMapper;

    @Value("C:\\filetest")
    private String IMAGE_DIR;

    @Value("C:\\filetest")
    public String IMAGE_URL;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository, CampaignFileRepository campaignFileRepository, CampaignAndFileRepository campaignAndFileRepository, DonationRepository1 donationRepository1, ModelMapper modelMapper) {
        this.campaignRepository = campaignRepository;
        this.campaignFileRepository = campaignFileRepository;
        this.campaignAndFileRepository = campaignAndFileRepository;
        this.donationRepository1 = donationRepository1;
        this.modelMapper = modelMapper;
    }

    //등록 성공
    @Transactional
    public int registCampaign(CampaignDescriptionDTO campaign) {
        // 목표금액 , 제거
        String goalBudger = campaign.getGoalBudget().replaceAll(",", "");
        campaign.setGoalBudget(goalBudger);
        // 현재 날짜
        LocalDateTime startDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        campaign.setStartDate(startDate);

        // 마감일 형변환 String =>  LocalDateTime
        String getEndDate = campaign.getEndDate();
        LocalDateTime endDate = LocalDateTime.parse(getEndDate + "T23:59:59", formatter);

        // 변환된 LocalDateTime을 Entity에 매핑
        CampaignDescription campaignEntity = modelMapper.map(campaign, CampaignDescription.class);

        campaignEntity.endDate(endDate).builder();
        System.out.println(campaignEntity);
        campaignRepository.save(campaignEntity);
        System.out.println(campaignEntity.getCampaignCode() + " 여기가 캠페인 코드다!!");

        return campaignEntity.getCampaignCode();
    }

    // 파일 등록 성공
    @Transactional
    public String registImage(MultipartFile imageFile, int campaignCode) {

        CampaignDescFileDTO campaignFile = new CampaignDescFileDTO();

        //String imageName = UUID.randomUUID().toString().replace("-", "");
        //String replaceFileName = null;
        int result = 0;
        //String replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, imageFile);

        // 파일 정보 가져오기
        String fileOriginName = imageFile.getOriginalFilename();
        String fileExtension = fileOriginName.substring(fileOriginName.lastIndexOf("."));
        String fileSaveName = UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;


        // 파일 정보 세팅
        campaignFile.setFileOriginName(fileOriginName);
        campaignFile.setFileSaveName(fileSaveName);
        campaignFile.setFileSavePath(IMAGE_URL);
        campaignFile.setFileExtension(fileExtension);
        campaignFile.setCampaignCode(campaignCode);
        //campaignFile.setFileSaveName(replaceFileName);
        try {
            // 폴더 없으면 폴더 생성
            File directory = new File(IMAGE_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            imageFile.transferTo(new File(IMAGE_URL + "/" + fileSaveName));

            System.out.println(campaignFile + "얜 켐펜팔");
            CampaignDescFile campaignDescFile = modelMapper.map(campaignFile, CampaignDescFile.class);
            campaignDescFile.fileOriginPath("이게 필요할라나?");
            campaignFileRepository.save(campaignDescFile);
            result = 1;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("파일이 읍서");
        }


        return (result > 0) ? "상품 입력 성공" : "상품 입력 실패";
    }


    // 전체 진행중 조회 성공
    public List<CampaignDescription> findCampaignList() {
//        LocalDateTime currentDate = LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHH")));
        LocalDateTime currentDate = LocalDateTime.now();
        System.out.println(currentDate);

        List<CampaignDescription> campaignList = campaignRepository.findByEndDateAfter(currentDate);

        System.out.println(campaignList);
        return campaignList;
    }

    // 전체 종료 조회 성공
    public List<CampaignDescription> findCampaignListDone() {
        LocalDateTime currentDate = LocalDateTime.now();
        List<CampaignDescription> campaignList = campaignRepository.findByEndDateBefore(currentDate);

        return campaignList;
    }

    // 상세 조회 성공
    public CampaignDescription findCampaign(int campaignCode) {
        CampaignDescription findCampaign = campaignRepository.findById(campaignCode).orElseThrow(IllegalArgumentException::new);

        System.out.println(findCampaign);
        return findCampaign;
    }

    // 종료 임박 순 조회 성공
    public List<CampaignDescription> findCampaignSort(Date currentDate) {
        List<CampaignDescription> findCampaignSort = campaignRepository.findCampaignSort(currentDate);

        return findCampaignSort;
    }


    // 캠페인 삭제 성공
    @Transactional
    public void deleteCampaign(int campaignCode) {
        System.out.println(campaignCode + "여기는 캠펜코드");
        CampaignAndFile campaign = campaignAndFileRepository.findById(campaignCode).orElse(null);
        System.out.println(campaign + "이건 삭제 캠펜");
        if (campaign != null) {
            campaignAndFileRepository.delete(campaign);
        }

        //campaignRepository.deleteById(campaignCode);

        //campaignFileRepository.deleteByCampaignCode();
    }
    // 캠페인 수정
    @Transactional
    public void modifyCampaign(CampaignDescriptionDTO campaignDTO,
                               MultipartFile imageFile) {

        /* update 할 엔티티 조회 */
        CampaignAndFile campaign = campaignAndFileRepository.findById(campaignDTO.getCampaignCode()).get();

        System.out.println(campaign + "이놈 수정 캠펜");

        CampaignDescFile oriImage = (CampaignDescFile) campaign.getCampaignDescfileList();
        System.out.println(oriImage + " 이놈 수정 하기전 원본 이미지");

        /* update를 위한 엔티티 값 수정 */

        campaign = campaign.campaignTitle(campaignDTO.getCampaignTitle())
                .campaignContent(campaignDTO.getCampaignContent())
                .endDate(LocalDateTime.parse(campaignDTO.getEndDate()+ "T23:59:59"))
                .campaignCategory(campaignDTO.getCampaignCategory())
                .goalBudget(Integer.parseInt(campaignDTO.getGoalBudget().replaceAll(",", "")))
                .orgName(campaignDTO.getOrgName())
                .orgDescription(campaignDTO.getOrgDescription())
                .orgTel(campaignDTO.getOrgTel()).builder();

        if(imageFile != null){
            CampaignDescFileDTO campaignFile = new CampaignDescFileDTO();

            String fileOriginName = imageFile.getOriginalFilename();
            String fileExtension = fileOriginName.substring(fileOriginName.lastIndexOf("."));
            String fileSaveName = UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;

            // 파일 정보 세팅
            campaignFile.setFileOriginName(fileOriginName);
            campaignFile.setFileSaveName(fileSaveName);
            campaignFile.setFileSavePath(IMAGE_URL);
            campaignFile.setFileExtension(fileExtension);
            campaignFile.setCampaignCode(campaign.getCampaignCode());


            campaign.campaignDescfileList((List<CampaignDescFile>) oriImage).builder();

            System.out.println(campaign + "마지막 수정");
            //campaignDescFile.setFileOriginPath("이게 필요할라나?");
            //campaignFileRepository.save(campaignDescFile);

        }else {

            /* 이미지 변경 없을 시 */
            campaign = campaign.campaignDescfileList((List<CampaignDescFile>) oriImage);
            System.out.println(campaign + "얜 수정 캠펜");
        }


    }

    public List<Donation> findDonationList(int campaignCode) {
        List<Donation> donationList = donationRepository1.findByCampaignDescription_CampaignCode(campaignCode);
        System.out.println(donationList + "도네");
        return donationList;
    }
}
