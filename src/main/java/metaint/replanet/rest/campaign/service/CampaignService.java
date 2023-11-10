package metaint.replanet.rest.campaign.service;

import metaint.replanet.rest.campaign.dto.CampaignDescriptionDTO;
import metaint.replanet.rest.campaign.entity.CampaignDescription;
import metaint.replanet.rest.campaign.repository.CampaignRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class CampaignService {

    private final CampaignRepository campaignRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CampaignService(CampaignRepository campaignRepository,ModelMapper modelMapper){
        this.campaignRepository = campaignRepository;
        this.modelMapper = modelMapper;
    }

    //등록 성공
    @Transactional
    public void registCampaign(CampaignDescriptionDTO campaign){
        // 목표금액 , 제거
        String goalBudger =  campaign.getGoalBudget().replaceAll(",","");
        campaign.setGoalBudget(goalBudger);
        // 현재 날짜
        LocalDateTime startDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        campaign.setStartDate(startDate);


        System.out.println(campaign + "여기는?!!");

        // 마감일 형변환 String =>  LocalDateTime
        String getEndDate = campaign.getEndDate();
        LocalDateTime endDate = LocalDateTime.parse(getEndDate + "T00:00:00", formatter);
        System.out.println(endDate.getClass().getName() + " 엔드데이트");



        // 변환된 LocalDateTime을 Entity에 매핑
        CampaignDescription campaignEntity = modelMapper.map(campaign, CampaignDescription.class);

        campaignEntity.endDate(endDate).builder();
        campaignRepository.save(campaignEntity);
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
    // 전체 종료 조회
    public List<CampaignDescription> findCampaignListDone() {
        LocalDateTime currentDate = LocalDateTime.now();
        List<CampaignDescription> campaignList = campaignRepository.findByEndDateBefore(currentDate);

        return campaignList;
    }

    // 상세 조회 성공
    public CampaignDescription findCampaign(int campaignCode) {
        CampaignDescription findCampaign = campaignRepository.findById(campaignCode).orElseThrow(IllegalArgumentException::new);

        return findCampaign;
    }

    // 종료 임박 순 조회
    public List<CampaignDescription> findCampaignSort(Date currentDate) {
        List<CampaignDescription> findCampaignSort = campaignRepository.findCampaignSort(currentDate);

        return findCampaignSort;
    }


}
