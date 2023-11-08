package metaint.replanet.rest.reviews.model.controller;

import metaint.replanet.rest.reviews.dto.CampaignDTO;
import metaint.replanet.rest.reviews.dto.CombineReviewDTO;
import metaint.replanet.rest.reviews.dto.ReviewDTO;
import metaint.replanet.rest.reviews.entity.Review;
import metaint.replanet.rest.reviews.model.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "http://localhost:3000")
public class ReviewController {
    private static final Logger logger = Logger.getLogger(ReviewController.class.getName());
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @GetMapping("/")
    public ResponseEntity<List<CombineReviewDTO>> getReviewList() {

        System.out.println("(ReviewController) 들어옴");

        List<CombineReviewDTO> details = reviewService.findAllReviews();

        System.out.println("(review controller): 가져온 총 결과 : " +details);
        return  ResponseEntity.ok(details);

    }

    @GetMapping("/{campaignCode}")
    public ResponseEntity<CombineReviewDTO> getSpecificReview(@PathVariable Long campaignCode) {
        System.out.println("(ReviewController) getSpecificReview code : " + campaignCode);

        CombineReviewDTO details = reviewService.findCampaignByCampaignCode(campaignCode);

        System.out.println("(review controller): 가져온 총 결과 : " +details);
        return ResponseEntity.ok(details);
    }

    @PostMapping("/")
    public ResponseEntity<?> registReview(@RequestBody ReviewDTO reviewDTO) {

        System.out.println("(Review Controller) RegistReview : " + reviewDTO);
        reviewService.registNewReview(reviewDTO);

        return ResponseEntity.ok("신규 리뷰 등록 성공!");

    }

    @GetMapping("?sort={searchFilter}")
    public ResponseEntity<List<CombineReviewDTO>> getReviewsBySearchFilter(@PathVariable String searchFilter) {

        System.out.println("(ReviewController) getReviewsBySearchFilter : " + searchFilter);
        logger.info("(ReviewController) getReviewsBySearchFilter : " + searchFilter);
        List<CombineReviewDTO> details = reviewService.findReviewsBySearchFilter(searchFilter);

        return ResponseEntity.ok(details);
    }


}
