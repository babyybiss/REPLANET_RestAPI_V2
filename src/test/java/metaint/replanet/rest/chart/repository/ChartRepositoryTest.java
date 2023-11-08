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


    @DisplayName("JpaRepository 인터페이스 제공 메서드 사용 테스트")
    @Test
    public void testCount() {
        //given
        int expectCount = 3;

        //when
        Long countResult = chartRepository.count();
        //then
        Assertions.assertEquals(expectCount, countResult);
    }

    @DisplayName("카테고리 리스트 조회 테스트")
    @Test
    public void testSelectCategoryOfCampaignUsingDistinct() {
        //given

        //when
        List<Object[]> foundCategory = chartRepository.findAllCampaingCategory();
        //then
        Assertions.assertNotNull(foundCategory);
        foundCategory.forEach(row -> {
            for(Object col : row) {
                System.out.print(col);
            }
            System.out.println();
        });
    }

    @DisplayName("카테고리별 캠페인 수 카운트 조회 테스트")
    @Test
    public void testCountCampaignByCampaignCategory() {
        //given
        //when
        List<Object[]> countByCategory = chartRepository.countCampaignDescriptionByCampaignCategory();
        //then
        Assertions.assertNotNull(countByCategory);

        countByCategory.forEach(row -> {
            for(Object col : row) {
                System.out.print(col + ":::");
            }
            System.out.println();
        });
    }







}
