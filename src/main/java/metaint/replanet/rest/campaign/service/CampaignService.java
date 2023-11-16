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
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
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

    @Value("http://localhost:3000/campaigns")
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
        Double overGoalBudger = Double.parseDouble(goalBudger);

        // 금액 체크
        if (overGoalBudger >= 1000000000) {
            return -2;
        }

        // 현재 날짜
        LocalDateTime startDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        campaign.setStartDate(startDate);
        System.out.println(startDate + "스타트 데이트");
        // 마감일 형변환 String =>  LocalDateTime
        String getEndDate = campaign.getEndDate();
        LocalDateTime endDate = LocalDateTime.parse(getEndDate + "T23:59:59", formatter);
        if (endDate.isBefore(startDate)) {
            return -1;
        }


        // 변환된 LocalDateTime을 Entity에 매핑
        CampaignDescription campaignEntity = modelMapper.map(campaign, CampaignDescription.class);


        campaignEntity.endDate(endDate).builder();

        campaignRepository.save(campaignEntity);
        System.out.println(campaignEntity.getCampaignCode() + " 여기가 캠페인 코드다!!");

        return campaignEntity.getCampaignCode();
    }

    // 파일 등록 성공
    @Transactional
    public String registImage(MultipartFile imageFile, int campaignCode) {

        CampaignDescFileDTO campaignFileDTO = new CampaignDescFileDTO();

        String fileSaveName = null;
        int result = 0;

        Path rootPath;
        String IMAGE_DIR = null;
        if (FileSystems.getDefault().getSeparator().equals("/")) {
            Path MACPath = Paths.get("/REPLANET_ReactAPI/public/campaigns").toAbsolutePath();
            // Unix-like system (MacOS, Linux)
            rootPath = Paths.get("/User").toAbsolutePath();
            Path relativePath = rootPath.relativize(MACPath);
            IMAGE_DIR = String.valueOf(relativePath);
        } else {
            // Windows
            Path WinPath = Paths.get("/dev/metaint/REPLANET_React/public/campaigns").toAbsolutePath();
            rootPath = Paths.get("C:\\").toAbsolutePath();
            Path relativePath = rootPath.resolve(WinPath);
            IMAGE_DIR = String.valueOf(relativePath);
        }


        try {
            // 파일 정보 가져오기
            String fileOriginName = imageFile.getOriginalFilename();
            String fileExtension = fileOriginName.substring(fileOriginName.lastIndexOf("."));
            String imageName = UUID.randomUUID().toString().replaceAll("-", "");

            fileSaveName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, imageFile);

            // 파일 정보 세팅
            campaignFileDTO.setFileOriginName(fileOriginName);
            campaignFileDTO.setFileOriginPath("얜 필요 없을거음");
            campaignFileDTO.setFileSaveName(fileSaveName);
            campaignFileDTO.setFileSavePath(IMAGE_URL);
            campaignFileDTO.setFileExtension(fileExtension);
            campaignFileDTO.setCampaignCode(campaignCode);
            //campaignFile.setFileSaveName(replaceFileName);
            CampaignDescFile campaignDescFile = modelMapper.map(campaignFileDTO, CampaignDescFile.class);
            campaignFileRepository.save(campaignDescFile);
            result = 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*try {
            // 폴더 없으면 폴더 생성
            File directory = new File(IMAGE_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            imageFile.transferTo(new File(IMAGE_URL + "/" + fileSaveName));

            System.out.println(campaignFileDTO + "얜 켐펜팔");
            CampaignDescFile campaignDescFile = modelMapper.map(campaignFileDTO, CampaignDescFile.class);
            campaignDescFile.fileOriginPath("이게 필요할라나?");
            campaignFileRepository.save(campaignDescFile);
            result = 1;
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("파일이 읍서");
        }*/


        return (result > 0) ? "상품 입력 성공" : "상품 입력 실패";
    }


    // 전체 진행중 조회 성공
    public List<CampaignAndFile> findCampaignList() {
        LocalDateTime currentDate = LocalDateTime.now();
        List<CampaignAndFile> campaignList = campaignAndFileRepository.findByEndDateAfter(currentDate);
        //List<CampaignAndFile> campaignList1 = campaignAndFileRepository.findByEndDateAfter1(currentDate);


        System.out.println(campaignList);
        return campaignList;
    }

    // 전체 종료 조회 성공
    public List<CampaignAndFile> findCampaignListDone() {
        LocalDateTime currentDate = LocalDateTime.now();

        List<CampaignAndFile> campaignList = campaignAndFileRepository.findByEndDateBefore(currentDate);

        //List<CampaignDescFile> campaignList = campaignFileRepository.findByEndDateBefore(currentDate);

        System.out.println(campaignList + "앤드조회");
        return campaignList;
    }

    // 상세 조회 성공
    public CampaignAndFile findCampaign(int campaignCode) {
        CampaignAndFile findCampaign = campaignAndFileRepository.findById(campaignCode).orElseThrow(IllegalArgumentException::new);
        //CampaignDescription findCampaign = campaignRepository.findById(campaignCode).orElseThrow(IllegalArgumentException::new);
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

        List<CampaignDescFile> campaign = campaignFileRepository.findByCampaignCodeCampaignCode(campaignCode);
        System.out.println(campaign + "화긴");
        if (campaign != null) {
            campaignFileRepository.deleteByCampaignCodeCampaignCode(campaignCode);
            campaignRepository.deleteById(campaignCode);

        }


    }

    // 캠페인 수정
    @Transactional
    public int modifyCampaign(CampaignDescriptionDTO campaignDTO,
                              MultipartFile imageFile) {

        int campaignCode = campaignDTO.getCampaignCode();
        // update 할 엔티티 조회
        CampaignAndFile campaign = campaignAndFileRepository.findById(campaignCode).get();
        // 파일 원본
        List<CampaignDescFile> oriImage = campaignFileRepository.findByCampaignCodeCampaignCode(campaignCode);
        System.out.println(oriImage + "니놈 누구?");

        //List<?> oriImage = camFile.
        //System.out.println(oriImage + " 이놈 수정 하기전 원본 이미지");

        // 목표금액 , 제거
        String goalBudger = campaignDTO.getGoalBudget().replaceAll(",", "");
        campaignDTO.setGoalBudget(goalBudger);
        Double overGoalBudger = Double.parseDouble(goalBudger);

        // 금액 체크
        if (overGoalBudger >= 1000000000) {
            return -2;
        }

        // 현재 날짜
        LocalDateTime startDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        campaignDTO.setStartDate(startDate);
        System.out.println(startDate + "스타트 데이트");
        // 마감일 형변환 String =>  LocalDateTime
        String getEndDate = campaignDTO.getEndDate();
        LocalDateTime endDate = LocalDateTime.parse(getEndDate + "T23:59:59", formatter);
        if (endDate.isBefore(startDate)) {
            return -1;
        }


        // 변환된 LocalDateTime을 Entity에 매핑
        CampaignDescription campaignEntity = modelMapper.map(campaign, CampaignDescription.class);


        campaignEntity.endDate(endDate).builder();


        // update를 위한 엔티티 값 수정

        campaign = campaign.campaignTitle(campaignDTO.getCampaignTitle())
                .campaignContent(campaignDTO.getCampaignContent())
                .endDate(LocalDateTime.parse(campaignDTO.getEndDate() + "T23:59:59"))
                .campaignCategory(campaignDTO.getCampaignCategory())
                .goalBudget(Integer.parseInt(campaignDTO.getGoalBudget().replaceAll(",", "")))
                .orgName(campaignDTO.getOrgName())
                .orgDescription(campaignDTO.getOrgDescription())
                .orgTel(campaignDTO.getOrgTel()).builder();

        if (imageFile != null) {
            CampaignDescFileDTO campaignFile = new CampaignDescFileDTO();

            String fileOriginName = imageFile.getOriginalFilename();
            String fileExtension = fileOriginName.substring(fileOriginName.lastIndexOf("."));
            String fileSaveName = UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;

            // 파일 정보 세팅
            campaignFile.setFileOriginName(fileOriginName);
            campaignFile.setFileOriginPath("이놈 필요함?수정");
            campaignFile.setFileSaveName(fileSaveName);
            campaignFile.setFileSavePath(IMAGE_URL);
            campaignFile.setFileExtension(fileExtension);
            campaignFile.setCampaignCode(campaign.getCampaignCode());
            System.out.println(campaignFile + " 이놈 수정 dto");
            // 바뀐 값 저장
            //oriImage = modelMapper.map(campaignFile, CampaignDescFile.class);
            //campaign.campaignDescfile(oriImage).builder();

            //campaignFileRepository.save(oriImage);
            System.out.println(campaign + "마지막 수정");
            //campaignDescFile.setFileOriginPath("이게 필요할라나?");
            //campaignFileRepository.save(campaignDescFile);

        } else {

            //이미지 변경 없을 시
            //campaign = campaign.campaignDescfileList(oriImage);
            System.out.println(campaign + "얜 수정 캠펜");
        }
        return 0;
    }

    public List<Donation> findDonationList(int campaignCode) {
        List<Donation> donationList = donationRepository1.findByCampaignDescription_CampaignCode(campaignCode);
        System.out.println(donationList + "도네");
        return donationList;
    }
}
