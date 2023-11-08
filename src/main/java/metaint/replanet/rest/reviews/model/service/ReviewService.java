package metaint.replanet.rest.reviews.model.service;

import metaint.replanet.rest.reviews.dto.CampaignDTO;
import metaint.replanet.rest.reviews.dto.CombineReviewDTO;
import metaint.replanet.rest.reviews.dto.ReviewDTO;
import metaint.replanet.rest.reviews.entity.Campaign;
import metaint.replanet.rest.reviews.entity.Review;
import metaint.replanet.rest.reviews.repository.CampaignReviewRepository;
import metaint.replanet.rest.reviews.repository.ReviewRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final CampaignReviewRepository campaignReviewRepository;
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    public ReviewService(CampaignReviewRepository campaignReviewRepository, ReviewRepository reviewRepository, ModelMapper modelMapper) {
        this.campaignReviewRepository = campaignReviewRepository;
        this.reviewRepository = reviewRepository;
        this.modelMapper = modelMapper;
    }

    public List<CombineReviewDTO> findAllReviews() {

        List<Campaign> reviewList = campaignReviewRepository.findAll();

        System.out.println("findAllReviews: " + reviewList);

        return reviewList.stream()
                .map(review -> modelMapper.map(review, CombineReviewDTO.class))
                .collect(Collectors.toList());
    }


    public CombineReviewDTO findCampaignByCampaignCode(Long campaignCode) {
        Campaign campaign = campaignReviewRepository.findById(campaignCode).orElseThrow(IllegalArgumentException::new);

        return modelMapper.map(campaign, CombineReviewDTO.class);


    }

    @Transactional
    public void registNewReview(ReviewDTO reviewDTO) {
        reviewRepository.save(modelMapper.map(reviewDTO, Review.class));
    }

    public List<CombineReviewDTO> findReviewsBySearchFilter(String searchFilter) {
        List<Campaign> filteredReviewList = campaignReviewRepository.findFilteredReviews(searchFilter);

        return filteredReviewList.stream()
                .map(filteredReviews -> modelMapper.map(filteredReviews, CombineReviewDTO.class))
                .collect(Collectors.toList());
    }
}
