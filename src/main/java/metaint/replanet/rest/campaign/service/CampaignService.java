package metaint.replanet.rest.campaign.service;

import metaint.replanet.rest.campaign.dto.*;
import metaint.replanet.rest.campaign.entity.*;
import metaint.replanet.rest.campaign.repository.CampaignDescRepository;
import metaint.replanet.rest.campaign.repository.CampaignFileRepository;
import metaint.replanet.rest.campaign.repository.CampaignRepository;
import metaint.replanet.rest.campaign.repository.ParticipationRepository;
import metaint.replanet.rest.util.FileUploadUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CampaignService {


    private final CampaignRepository campaignRepository;
    private final CampaignDescRepository campaignDescRepository;
    private final CampaignFileRepository campaignFileRepository;

    private final ParticipationRepository participationRepository;
    private final ModelMapper modelMapper;

    @Value("C:\\filetest")
    private String IMAGE_DIR;

    @Value("http://localhost:3000/campaigns")
    public String IMAGE_URL;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository, CampaignFileRepository campaignFileRepository, ParticipationRepository participationRepository, ModelMapper modelMapper
            , CampaignDescRepository campaignDescRepository) {
        this.campaignRepository = campaignRepository;
        this.campaignFileRepository = campaignFileRepository;
        this.participationRepository = participationRepository;
        this.modelMapper = modelMapper;
        this.campaignDescRepository = campaignDescRepository;
    }

    // 전체 진행중 조회
    public List<CampaignDesOrgDTO> findCampaignList() {
        LocalDateTime currentDate = LocalDateTime.now();
        List<Campaign> campaignEntityList = campaignRepository.findByEndDateAfterOrderByEndDate(currentDate);

        return campaignEntityList.stream()
                .map(campaignList -> modelMapper.map(campaignList, CampaignDesOrgDTO.class))
                .collect(Collectors.toList());
    }

    // 전체 종료 조회
    public List<CampaignDesOrgDTO> findCampaignListDone() {
        LocalDateTime currentDate = LocalDateTime.now();
        List<Campaign> campaignEntityList = campaignRepository.findByEndDateBefore(currentDate);

        return campaignEntityList.stream()
                .map(campaignList -> modelMapper.map(campaignList, CampaignDesOrgDTO.class))
                .collect(Collectors.toList());
    }

    // 상세 조회
    public CampaignDesOrgDTO findCampaign(int campaignCode) {
        Campaign findCampaign = campaignRepository.findById(campaignCode).orElseThrow(IllegalArgumentException::new);

        return modelMapper.map(findCampaign, CampaignDesOrgDTO.class);
    }

    // 기부처별 캠페인 진행중 리스트 조회
    public List<CampaignDesOrgDTO> findCampaignByOrg(int orgCode) {

        List<Campaign> campaignEntityList = campaignRepository.findByOrgCode(orgCode);
        return campaignEntityList.stream()
                .map(campaignList -> modelMapper.map(campaignList, CampaignDesOrgDTO.class))
                .collect(Collectors.toList());
    }
    // 기부처별 캠페인 종료 리스트 조회
    public List<CampaignDesOrgDTO> findCampaignByOrgDone(int orgCode) {
        List<Campaign> campaignEntityList = campaignRepository.findByOrgCodeDone(orgCode);

        return campaignEntityList.stream()
                .map(campaignList -> modelMapper.map(campaignList, CampaignDesOrgDTO.class))
                .collect(Collectors.toList());
    }

    // 기부처별 진행중 캠페인 갯수 조회
    public int getCampaignCount(int orgCode) {
        int campaignCount = campaignRepository.getCampaignCount(orgCode);
        return campaignCount;
    }
    // 기부처별 종료된 캠페인 갯수 조회
    public int getCampaignCountDone(int orgCode) {
        Integer campaignCount = campaignRepository.getCampaignCountDone(orgCode);
        if (campaignCount == null){
            return campaignCount = 0;
        }
        return campaignCount;
    }
    
    // 참여 내역 조회 성공
    public List<ParticipationDTO> findparticipationList(int campaignCode) {
        List<Pay> pationEntityList = participationRepository.findByDonationByCampaignCode(campaignCode);

        return pationEntityList.stream()
                .map(pationList -> modelMapper.map(pationList, ParticipationDTO.class))
                .collect(Collectors.toList());
    }


    //등록
    @Transactional
    public int registCampaign(RequestCampaignDTO campaign) {
        // 목표금액 , 제거
        String goalBudger = campaign.getGoalBudget().replaceAll(",", "");
        campaign.setGoalBudget(goalBudger);
        Double overGoalBudger = Double.parseDouble(goalBudger);

        // 금액 체크
        if (overGoalBudger > 1000000000) {
            return -2;
        } else if (overGoalBudger.equals("undefined") || overGoalBudger == null) {
            return -3;
        }

        // 현재 날짜
        LocalDateTime startDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        campaign.setStartDate(startDate);
        // 마감일 형변환 String =>  LocalDateTime
        //LocalDateTime getEndDate = campaign.getEndDate();
        String getEndDate = campaign.getEndDate();
        LocalDateTime endDate = LocalDateTime.parse(getEndDate + "T23:59:59", formatter);

        if (endDate.isBefore(startDate)) {
            return -1;
        }
        // 변환된 LocalDateTime을 Entity에 매핑
        CampaignDescription campaignEntity = modelMapper.map(campaign, CampaignDescription.class);

        campaignEntity.endDate(endDate).builder();

        campaignDescRepository.save(campaignEntity);

        return campaignEntity.getCampaignCode();
    }

    // 파일 등록
    @Transactional
    public String registImage(MultipartFile imageFile, int campaignCode) {

        CampaignDescFileDTO campaignFileDTO = new CampaignDescFileDTO();

        String fileSaveName = null;
        int result = 0;

        Path rootPath;
        String IMAGE_DIR = null;
        if (FileSystems.getDefault().getSeparator().equals("/")) {
            Path MACPath = Paths.get("/REPLANET_ReactAPI_V2/public/campaigns").toAbsolutePath();
            // Unix-like system (MacOS, Linux)
            rootPath = Paths.get("/User").toAbsolutePath();
            Path relativePath = rootPath.relativize(MACPath);
            IMAGE_DIR = String.valueOf(relativePath);
        } else {
            // Windows
            Path WinPath = Paths.get("/dev/metaint/REPLANET_React_V2/public/campaigns").toAbsolutePath();
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
            //campaignFileDTO.setFileOriginPath("얜 필요 없을거가틈");
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

        return (result > 0) ? "성공" : "실패";
    }


//    // 카테고리별 리스트 조회
//    public List<CampaignAndFile>  findCategoryByCampaignList(String category) {
//        List<CampaignDescription> findCategoryByCampaignList = campaignRepository.findByCampaignCategory(category);
//        return findCategoryByCampaignList;
//    }

    // 종료 임박 순 조회
    public List<Campaign> findCampaignSort(LocalDateTime currentDate) {
        List<Campaign> findCampaignSort = campaignRepository.findCampaignSort(currentDate);

        return findCampaignSort;
    }


    // 캠페인 삭제 
    @Transactional
    public int deleteCampaign(int campaignCode) {
        int result = 0;
        List<Campaign> campaign = campaignRepository.findByCampaignCode(campaignCode);
        if (campaign != null) {
            campaignRepository.deleteByCampaignCode(campaignCode);
            result = 1;
            return result;
        }
        return result;
    }

    // 캠페인 수정
    @Transactional
    public int modifyCampaign(RequestCampaignDTO campaignDTO, int campaignCode, MultipartFile imageFile) {
        // 이미지 파일 있음 삭제
        if (imageFile != null) {
            System.out.println("이게 안되는겨?");
            campaignFileRepository.deleteByCampaignCodeCampaignCode(campaignCode);
        } else {
            int result;
            // update 할 엔티티 조회
            Campaign campaign = campaignRepository.findById(campaignCode).get();
            //List<CampaignDescFile> file = campaignFileRepository.findByCampaignCodeCampaignCode(campaignCode);
            // 목표금액 , 제거
            String goalBudger = campaignDTO.getGoalBudget().replaceAll(",", "");
            campaignDTO.setGoalBudget(goalBudger);
            Double overGoalBudger = Double.parseDouble(goalBudger);

            // 금액 체크
            if (overGoalBudger > 1000000000) {
                result = -2;
                return result;
            } else {

                // 현재 날짜
                LocalDateTime startDate = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
                campaignDTO.setStartDate(startDate);
                // 마감일 형변환 String =>  LocalDateTime
//                LocalDateTime endDate = campaignDTO.getEndDate();

                LocalDateTime endDate;
                String getEndDate = campaignDTO.getEndDate();
                if (getEndDate.contains("-")) {
                    endDate = LocalDateTime.parse(getEndDate + "T23:59:59", formatter);
                } else {
                    String[] dateComponents = getEndDate.split(",");

                    int year = Integer.parseInt(dateComponents[0]);
                    int month = Integer.parseInt(dateComponents[1]);
                    int day = Integer.parseInt(dateComponents[2]);
                    int hour = Integer.parseInt(dateComponents[3]);
                    int minute = Integer.parseInt(dateComponents[4]);
                    int second = Integer.parseInt(dateComponents[5]);

                    endDate = LocalDateTime.of(year, month, day, hour, minute, second);
                }
                if (endDate.isBefore(startDate)) {
                    result = -1;
                    return result;
                }

                // 변환된 LocalDateTime을 Entity에 매핑
                CampaignDescription campaignEntity = modelMapper.map(campaign, CampaignDescription.class);
                campaignEntity.endDate(endDate).builder();
                // update를 위한 엔티티 값 수정
                campaign = campaign.campaignTitle(campaignDTO.getCampaignTitle())
                        .campaignContent(campaignDTO.getCampaignContent())
                        .endDate(endDate)
                        .campaignCategory(campaignDTO.getCampaignCategory())
                        .goalBudget(Integer.parseInt(campaignDTO.getGoalBudget().replaceAll(",", "")))
                        .builder();
            }
        }
        return 0;
    }


    // 일일 모금현황 조회
    public List<TodayDonationsDTO> getTodayDonations() {
        List<Object[]> today = participationRepository.findByTodayDonation();

        List<TodayDonationsDTO> list = new ArrayList<>();
        for (Object[] objects : today) {
            int payCode = ((Number) objects[0]).intValue();
            int payAmount = ((Number) objects[1]).intValue();
            String donationDate = (String) objects[2];
            int donationCount = ((Number) objects[3]).intValue();
            int totalDonation = ((Number) objects[4]).intValue();

            TodayDonationsDTO todayDTO = new TodayDonationsDTO();
            todayDTO.setPayCode(payCode);
            todayDTO.setPayAmount(payAmount);
            todayDTO.setDonationDate(donationDate);
            todayDTO.setDonationCount(donationCount);
            todayDTO.setTotalDonation(totalDonation);
            TodayDonationsDTO apply = todayDTO;
            list.add(apply);
        }
        return list;
    }



}
