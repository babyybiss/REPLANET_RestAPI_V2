package metaint.replanet.rest.reviews.model.controller;

import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.auth.jwt.TokenProvider;
import metaint.replanet.rest.reviews.dto.*;
import metaint.replanet.rest.reviews.model.service.ReviewService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import static java.lang.Long.parseLong;

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
    @Autowired
    private TokenProvider tokenProvider;

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

    @GetMapping("/thumbnail/{reviewCode}")
    public ResponseEntity<ReviewFileDTO> getThumbnailPath(@PathVariable Long reviewCode) {

        ReviewFileDTO details = reviewService.getThumbnailPath(reviewCode);

        log.info("(review controller): 가져온 썸네일 경로 결과 : " + details);
        return ResponseEntity.ok(details);
    }

    @PutMapping("/")
    public ResponseEntity<?> modifyReview(@ModelAttribute ReviewDTO reviewDTO,
                                          MultipartFile imageFile) throws IOException {

        log.info("(Review Controller) RegistReview : " + reviewDTO);
        log.info("(Review Controller) RegistReview Image : " + imageFile);

        reviewService.modifyReview(reviewDTO, imageFile);

        return ResponseEntity.ok("리뷰 수정 성공!");
    }


    @DeleteMapping("{reviewCode}")
    public ResponseEntity<?> deleteReview(@PathVariable Long reviewCode, @RequestParam Long revFileCode) {
        log.info("[Review COntroller] delete Review : " + reviewCode + "and revFileCode : " + revFileCode);

        reviewService.deleteReview(reviewCode, revFileCode);

        return ResponseEntity.ok("리뷰 삭제 성공!");
    }

    @GetMapping("/memberCode")
    public ResponseEntity<Long> getMemberCode(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader){
        String token = extractToken(authorizationHeader);
        Authentication authentication = tokenProvider.getAuthentication(token);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String memberCodeString = userDetails.getUsername();
        Long memberCode = parseLong(memberCodeString);

        log.info("[/kakaoPay memberCode] : " + memberCode);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private String extractToken(String authorizationHeader) {
        log.info("[extractToken(String authorizationHeader)] ----------------------------------------------");
        log.info("[extractToken() authorizationHeader] : " + authorizationHeader);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            log.info("[extractToken()] 토큰 추출 성공 ");
            return authorizationHeader.substring(7);
        }
        log.info("[extractToken()] 토큰 추출 실패 ");
        return null;
    }
/*    @GetMapping("{reviewCode}/comments")
    public ResponseEntity<ReviewCommentsDTO> getCommentsForSpecificReview(@PathVariable Long reviewCode) {
        return ResponseEntity.ok(details);
    }*/




}
