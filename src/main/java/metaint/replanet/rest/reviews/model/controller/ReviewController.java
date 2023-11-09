package metaint.replanet.rest.reviews.model.controller;

import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.reviews.dto.CombineReviewDTO;
import metaint.replanet.rest.reviews.dto.ReviewDTO;
import metaint.replanet.rest.reviews.dto.ReviewFileCombineDTO;
import metaint.replanet.rest.reviews.model.service.ReviewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class ReviewController {
    private static final Logger logger = Logger.getLogger(ReviewController.class.getName());
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    @GetMapping("/")
    public ResponseEntity<List<CombineReviewDTO>> getReviewList() {

        log.info("(ReviewController) 들어옴");

        List<CombineReviewDTO> details = reviewService.findAllReviews();

        log.info("(review controller): 가져온 총 결과 : " +details);
        return  ResponseEntity.ok(details);

    }

    @GetMapping("/{campaignCode}")
    public ResponseEntity<CombineReviewDTO> getSpecificReview(@PathVariable Long campaignCode) {
        log.info("(ReviewController) getSpecificReview code : " + campaignCode);

        CombineReviewDTO details = reviewService.findCampaignByCampaignCode(campaignCode);

        log.info("(review controller): 가져온 총 결과 : " +details);
        return ResponseEntity.ok(details);
    }

    @PostMapping("/")
    public ResponseEntity<?> registReview(@ModelAttribute ReviewDTO reviewDTO,
                                          MultipartFile imageFile) throws IOException {

        log.info("(Review Controller) RegistReview : " + reviewDTO);
        log.info("(Review Controller) RegistReview Image : " + imageFile);

        reviewService.registNewReview(reviewDTO, imageFile);

        return ResponseEntity.ok("신규 리뷰 등록 성공!");
    }

    /*@PostMapping("/uploadImage")
    public ResponseEntity<String> handleFileUpload(@RequestParam("multipartFiles") MultipartFile file) {

        log.info("(Review Controller) handleFileUpload : " + file.getOriginalFilename());

        String imageUrl = reviewService.uploadFiles(file);


        return ResponseEntity.ok(imageUrl);

    }*/



    @GetMapping("")
    public ResponseEntity<List<CombineReviewDTO>> getReviewsBySearchFilter(@RequestParam(name = "sort") String searchFilter) {
        log.info("(ReviewController) getReviewsBySearchFilter : " + searchFilter);
        log.info("(ReviewController) getReviewsBySearchFilter : " + searchFilter);

        List<CombineReviewDTO> details = reviewService.findReviewsBySearchFilter(searchFilter);

        return ResponseEntity.ok(details);
    }



}
