package metaint.replanet.rest.chart.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ChartRepositoryTest {

    @Autowired
    private ChartRepository chartRepository;

    @DisplayName("카테고리별 캠페인 통계 조회 네이티브 쿼리 테스트")
    @Test
    public void testSelectCampaignByCampaignCategory() {
        //when
        List<Object[]> countByCategory = chartRepository.countAndSumByCategory();
        //then
        Assertions.assertNotNull(countByCategory);
        /*countByCategory.forEach(row -> {
            for(Object col : row) {
                System.out.print(col + ":::");
            }
            System.out.println();
        });*/
    }

    @DisplayName("당해 월별 캠페인 통계 조회 네이티브 쿼리 테스트")
    @Test
    public void testSelectCampaignByCurrentyear() {
        //when
        List<Object[]> countByCurrentyear = chartRepository.countAndSumByCurrentYear();
        //then
        Assertions.assertNotNull(countByCurrentyear);
        /* countByCurrentyear.forEach( row -> {
            for(Object col : row) {
                System.out.print(col + ":::");
            }
            System.out.println();
        }); */
    }

    @DisplayName("전해 월별 캠페인 통계 조회 네이티브 쿼리 테스트")
    @Test
    public void testSelectCampaignByPreviousyear() {
        //when
        List<Object[]> countByPreviousyear = chartRepository.countAndSumByPreviousYear();
        //then
        Assertions.assertNotNull(countByPreviousyear);
        /* countByPreviousyear.forEach(row -> {
            for(Object col : row) {
                System.out.print(col + ":::");
            }
            System.out.println();
        }); */
    }

}
