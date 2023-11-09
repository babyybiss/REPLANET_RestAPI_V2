package metaint.replanet.rest.campaign.service;

import metaint.replanet.rest.campaign.dto.CampaignDescriptionDTO;
import metaint.replanet.rest.campaign.entity.CampaignDescription;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.assertj.core.util.DateUtil.now;

@SpringBootTest
public class CampaignServiceTest {

    @Autowired
    private CampaignService campaignService;

    @DisplayName("캠페인 등록 테스트")
    @Test
    void campaignRegistTest() {
        //given
        CampaignDescriptionDTO campaign = new CampaignDescriptionDTO(
                0,
                "끝남",
                "도와주세요~섬바리헤업미",
                LocalDateTime.now(),
                LocalDateTime.of(2021,12,5,16,25),
                "지구촌",
                200000,
                10000111,
                "하이미디어",
                "안녕 매체",
                "02-121-5678"
        );

        //when
        campaignService.registCampaign(campaign);

        //then
        List<CampaignDescription> foundCampaign = campaignService.findCampaignList();
        Assertions.assertNotNull(foundCampaign);

    }

    @DisplayName("캠페인 진행중 전체 조회 테스트")
    @Test
    public void campaignFindAllTest() {
        //when
        List<CampaignDescription> findCampaignList = campaignService.findCampaignList();
        //then
        Assertions.assertNotNull(findCampaignList);
        findCampaignList.forEach(System.out::println);
    }

    @DisplayName("캠페인 종료 전체 조회 테스트")
    @Test
    public void campaignDoneFindAllTest() {
        //when
        List<CampaignDescription> findCampaignList = campaignService.findCampaignListDone();
        //then
        Assertions.assertNotNull(findCampaignList);
        findCampaignList.forEach(System.out::println);
    }

    @DisplayName("캠페인 상세 조회 테스트")
    @Test
    public void campaignFindByIdTest() {
        //given
        int charityCode = 1;

        //when
        CampaignDescription findCampaign = campaignService.findCampaign(charityCode);

        //then
        Assertions.assertNotNull(findCampaign);
        System.out.println(findCampaign);
    }

    @DisplayName("캠페인 조건 조회 종료 임박 순")
    @Test
    public void campaignFindSortTest() {
        //given
        Date currentDate = now();

        //when
        List<CampaignDescription> findCampaignSort = campaignService.findCampaignSort(currentDate);

        //then
        Assertions.assertNotNull(findCampaignSort);

        findCampaignSort.forEach(System.out::println);

    }
}
