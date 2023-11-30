package metaint.replanet.rest.reviews.repository;

import metaint.replanet.rest.reviews.entity.Campaign;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
@SpringBootTest
public class CampaignReviewRepositoryTest {

    @Autowired
    private CampaignReviewRepository campaignReviewRepository;


    @DisplayName("재단별로 리뷰 없는 캠페인 조회")
    @Test
    public void findReviewsNeededCampaignByOrganization() {
        //given
        Long memberCode = 6L;

        //when
        List<Campaign> campaign = campaignReviewRepository.findUnassociatedCampaignsByOrgCode(memberCode);

        //then
        Assertions.assertEquals(3, campaign.size());
        System.out.println("result : " + campaign);
    }
}
