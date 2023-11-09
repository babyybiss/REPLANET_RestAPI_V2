package metaint.replanet.rest.reviews.model.service;

import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.reviews.dto.CombineReviewDTO;
import metaint.replanet.rest.reviews.dto.ReviewDTO;
import metaint.replanet.rest.reviews.dto.ReviewFileCombineDTO;
import metaint.replanet.rest.reviews.dto.ReviewFileDTO;
import metaint.replanet.rest.reviews.entity.Campaign;
import metaint.replanet.rest.reviews.entity.Review;
import metaint.replanet.rest.reviews.entity.ReviewFile;
import metaint.replanet.rest.reviews.repository.CampaignReviewRepository;
import metaint.replanet.rest.reviews.repository.ReviewFileRepository;
import metaint.replanet.rest.reviews.repository.ReviewRepository;
import metaint.replanet.rest.util.FileUploadUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Slf4j
public class ReviewService {

    private final CampaignReviewRepository campaignReviewRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewFileRepository reviewFileRepository;
    private final ModelMapper modelMapper;

    @Value("/Users/babyybiss/Documents/FullStackJava/REPLANET_ReactAPI/public/reviewImgs")
    private String IMAGE_DIR;

    @Value("http://localhost:3000/reviewImgs")
    public String IMAGE_URL;

    public ReviewService(CampaignReviewRepository campaignReviewRepository, ReviewRepository reviewRepository, ReviewFileRepository reviewFileRepository, ModelMapper modelMapper) {
        this.campaignReviewRepository = campaignReviewRepository;
        this.reviewRepository = reviewRepository;
        this.reviewFileRepository = reviewFileRepository;
        this.modelMapper = modelMapper;
    }

    public List<CombineReviewDTO> findAllReviews() {

        List<Campaign> reviewList = campaignReviewRepository.findAll();

        log.info("findAllReviews: " + reviewList);

        return reviewList.stream()
                .map(review -> modelMapper.map(review, CombineReviewDTO.class))
                .collect(Collectors.toList());
    }


    public CombineReviewDTO findCampaignByCampaignCode(Long campaignCode) {
        Campaign campaign = campaignReviewRepository.findById(campaignCode).orElseThrow(IllegalArgumentException::new);

        return modelMapper.map(campaign, CombineReviewDTO.class);


    }

    @Transactional
    public void registNewReview(ReviewDTO reviewDTO, MultipartFile imageFile) throws IOException {

        log.info("[ReviewService] registNewReview Start ===========================");

        Review insertReview = modelMapper.map(reviewDTO, Review.class);

        reviewRepository.save(insertReview);

        /////////////////////////////////////////////////////////
        ReviewFileDTO reviewFileDTO = new ReviewFileDTO();

        String imageName = UUID.randomUUID().toString().replace("-", "");
        String replaceFileName = null;
        int result = 0;


            replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, imageFile);

            reviewFileDTO.setFileSaveName(replaceFileName);
            reviewFileDTO.setFileSavePath("http://localhost:3000" + IMAGE_DIR + "/" + imageFile.getOriginalFilename());
            reviewFileDTO.setFileExtension("PNG");

            log.info("[ReviewService] registNewReview Image name : " + replaceFileName);
            log.info("[ReviewService] registNewReview result : " + reviewDTO);
            log.info("[ReviewService] registNewReviewFile result : " + reviewFileDTO);

            ReviewFile insertReview1 = modelMapper.map(reviewFileDTO, ReviewFile.class);
            reviewFileRepository.save(insertReview1);

            result = 1;

            System.out.println("check");
            FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);



        log.info("[ReviewService] registReview End ===================");
    }

    public List<CombineReviewDTO> findReviewsBySearchFilter(String searchFilter) {
        List<Review> filteredReviewList = reviewRepository.findFilteredReviews(searchFilter);

        return filteredReviewList.stream()
                .map(filteredReviews -> modelMapper.map(filteredReviews, CombineReviewDTO.class))
                .collect(Collectors.toList());
    }

    /*public String uploadFiles(MultipartFile file) {

        ReviewFileDTO reviewFileDTO = new ReviewFileDTO();
        reviewFileDTO.setFileOriginName("http://localhost:3000/public/reviewImgs/" + file.getOriginalFilename());
        reviewFileDTO.setFileOriginPath("somePath");
        reviewFileDTO.setFileSaveName(file.getOriginalFilename());
        reviewFileDTO.setFileSavePath("http://localhost:3000/public/reviewImgs/" + file.getOriginalFilename());
        reviewFileDTO.setFileExtension(file.getContentType());

        ReviewFile insertReview = modelMapper.map(reviewFileDTO, ReviewFile.class);
        reviewFileRepository.save(insertReview);

        return insertReview.getFileSavePath();
    }*/
}
