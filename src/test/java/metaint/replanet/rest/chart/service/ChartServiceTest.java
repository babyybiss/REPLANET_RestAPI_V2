package metaint.replanet.rest.chart.service;

import metaint.replanet.rest.chart.dto.CampaignDescriptionDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ChartServiceTest {

    @Autowired
    private ChartService chartService;

    @DisplayName("캠페인 조회 테스트")
    @Test
    public void testSelectCategory() {
        //when
        List<CampaignDescriptionDTO> campaignList = chartService.findCampaignList();
        //then
        Assertions.assertNotNull(campaignList);
        campaignList.forEach(System.out::println);
    }
    @DisplayName("캠페인 수 카운트 조회 테스트")
    @Test
    public void testCountCampaign() {
        //given
        int expectResult = 3;
        //when
        int countResult = chartService.CountCampaign();
        //then
        Assertions.assertEquals(expectResult, countResult);
    }

}
