package metaint.replanet.rest.chart.service;

import metaint.replanet.rest.chart.dto.CountAndSumByCategoryDTO;
import metaint.replanet.rest.chart.dto.CountAndSumByMonthlyDTO;
import metaint.replanet.rest.chart.dto.DonationByTimeDTO;
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

    @DisplayName("카테고리별 캠페인 통계 조회 테스트")
    @Test
    public void testCountAndSumByCampaignCategory() {
        //when
        List<CountAndSumByCategoryDTO> resultList = chartService.countAndSumByCampaignCategory();
        //then
        Assertions.assertNotNull(resultList);
        // resultList.forEach(System.out::println);
    }
    @DisplayName("당해 등록된 캠페인 통계 조회 테스트")
    @Test
    public void testCountAndSumByCurrentYear() {
        //when
        List<CountAndSumByMonthlyDTO> resultList = chartService.countAndSumByCurrentYear();
        //then
        Assertions.assertNotNull(resultList);
        resultList.forEach(System.out::println);
    }
    @DisplayName("전해 등록된 캠페인 캠페인 통계 조회 테스트")
    @Test
    public void testCountAndSumByPreviousYear() {
        //when
        List<CountAndSumByMonthlyDTO> resultList = chartService.countAndSumByPreviousYear();
        //then
        Assertions.assertNotNull(resultList);
        resultList.forEach(System.out::println);
    }

    @DisplayName("시간별 캠페인 기부금 추이 조회 테스트")
    @Test
    public void testSelectDonationByTime() {
        //when
        List<DonationByTimeDTO> resultList = chartService.selectDonationByTime();
        //then
        Assertions.assertNotNull(resultList);
        // resultList.forEach(System.out::println);
    }
}
