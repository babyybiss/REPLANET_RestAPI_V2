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

    @DisplayName("캠페인 수 카운트 조회 테스트")
    @Test
    public void testCountCampaign() {
        //given
        int expectCount = 3;

        //when
        int result = chartRepository.countAllCampaign();

        //then
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectCount, result);
        System.out.println(result);
    }

    @DisplayName("카테고리 리스트 조회")
    @Test
    public void testSelectCategoryOfCampaignUsingDistinct() {
        //given

        //when
        List<String> foundCategory = chartRepository.findAllCampaingCategory();
        //then
        Assertions.assertNotNull(foundCategory);
        foundCategory.forEach(System.out::println);
    }







}
