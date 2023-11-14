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

import javax.persistence.EntityNotFoundException;
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
        log.info("[ReviewService] registNewReview : " + reviewDTO);
        Review insertReview = modelMapper.map(reviewDTO, Review.class);

        reviewRepository.save(insertReview);
        reviewRepository.flush();

        /////////////////////////////////////////////////////////
        Long reviewCode = reviewRepository.findByCampaignCode(reviewDTO.getCampaignCode());

        //System.out.println("what is the fucking reviewCode??" + reviewCode);
        ReviewFileDTO reviewFileDTO = new ReviewFileDTO();

        String imageName = UUID.randomUUID().toString().replace("-", "");
        String replaceFileName = null;
        int result = 0;

        replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, imageFile);

        reviewFileDTO.setReviewCode(reviewCode);
        reviewFileDTO.setFileSaveName(replaceFileName);
        reviewFileDTO.setFileSavePath("http://localhost:3000" + IMAGE_DIR + "/" + imageFile.getOriginalFilename());
        reviewFileDTO.setFileExtension("PNG");

        reviewFileDTO.setFileOriginName(imageFile.getOriginalFilename());
        reviewFileDTO.setFileOriginPath(IMAGE_DIR);


        log.info("[ReviewService] registNewReview Image name : " + replaceFileName);
        log.info("[ReviewService] registNewReview result : " + reviewDTO);
        log.info("[ReviewService] registNewReviewFile result : " + reviewFileDTO);

        ReviewFile insertReview1 = modelMapper.map(reviewFileDTO, ReviewFile.class);
        reviewFileRepository.save(insertReview1);

        result = 1;

        System.out.println("check");
        //FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);



        log.info("[ReviewService] registReview End ===================");
/*            replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, imageFile);

            reviewFileDTO.setFileOriginName(imageFile.getOriginalFilename());
            reviewFileDTO.setFileOriginPath(IMAGE_DIR);
            reviewFileDTO.setFileSaveName(replaceFileName);
            reviewFileDTO.setFileSavePath("http://localhost:3000" + IMAGE_DIR + "/" + imageFile.getOriginalFilename());
            reviewFileDTO.setFileExtension("PNG");
            reviewFileDTO.setReviewCode(reviewCode);

            log.info("[ReviewService] registNewReview replaceFileName : " + replaceFileName);
            log.info("[ReviewService] registNewReview result : " + reviewDTO);
            log.info("[ReviewService] registNewReviewFile result : " + reviewFileDTO);

            ReviewFile insertReview1 = modelMapper.map(reviewFileDTO, ReviewFile.class);
            reviewFileRepository.save(insertReview1);

            result = 1;

            System.out.println("check");
            FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);

        log.info("[ReviewService] registReview End ===================");*/
    }

    public List<CombineReviewDTO> findReviewsBySearchFilter(String searchFilter) {
        List<Review> filteredReviewList = reviewRepository.findFilteredReviews(searchFilter);

        return filteredReviewList.stream()
                .map(filteredReviews -> modelMapper.map(filteredReviews, CombineReviewDTO.class))
                .collect(Collectors.toList());
    }

    public ReviewFileDTO getThumbnailPath(Long reviewCode) {

        ReviewFile reviewFile = reviewFileRepository.findByReviewCode(reviewCode);

        log.info("reviewFile path?? : " + reviewFile);

        return modelMapper.map(reviewFile, ReviewFileDTO.class);
    }

    @Transactional
    public void modifyReview(ReviewDTO reviewDTO, MultipartFile imageFile) {

        log.info("[ProductService] updateReview Start ===================================");
        log.info("[ProductService] productDTO : " + reviewDTO);

        String replaceFileName = null;
        int result = 0;

        try {

            Review review = reviewRepository.findById(reviewDTO.getReviewCode()).get();
            ReviewFile replaceableReview = reviewFileRepository.findByReviewCode(reviewDTO.getReviewCode());
            String replaceableImgPath = replaceableReview.getFileSaveName();
            log.info("what are we deleting? : " + replaceableImgPath);

            List<ReviewFile> reviewFileList = review.getReviewFileList();

            if(!reviewFileList.isEmpty()) {
                String fileOriginpath = reviewFileList.get(0).getFileOriginPath();

                System.out.println("FileOriginpath: " + fileOriginpath);

                review = review.reviewCode(reviewDTO.getReviewCode())
                        .reviewTitle(reviewDTO.getReviewTitle())
                        .description(reviewDTO.getDescription()).build();

                if(imageFile != null) {
                    String imageName = UUID.randomUUID().toString().replace("-", "");
                    replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, imageFile);

                    ////////////////////////////


                    ReviewFile reviewFile = reviewFileRepository.findByReviewCode(reviewDTO.getReviewCode());

                    //ReviewFileDTO reviewFileDTO = new ReviewFileDTO();

                    reviewFile = reviewFile.fileSaveName(replaceFileName).build();

                    boolean isDelete = FileUploadUtils.deleteFile(IMAGE_DIR, replaceableImgPath);
                    log.info("[Update Review] isDelete : " + isDelete);

                    log.info("[Update Review] InserFileName : " + replaceFileName);
                }
            } else {
                ReviewFile reviewFile = reviewFileRepository.findByReviewCode(reviewDTO.getReviewCode());
                reviewFile = reviewFile.fileSaveName(replaceableImgPath).build();

                System.out.println("No files in the reviewFileList");
            }
        } catch (Exception e) {
            System.out.println("Error occurred!");
        }


    }

    @Transactional
    public void deleteReview(Long reviewCode, Long revFileCode) {

        ReviewFile replaceableReview = reviewFileRepository.findByReviewCode(reviewCode);
        String deleteImgPath = replaceableReview.getFileSaveName();

        boolean isDelete = FileUploadUtils.deleteFile(IMAGE_DIR, deleteImgPath);
        log.info("[Update Review] isDelete : " + isDelete);

        reviewFileRepository.deleteByRevFileCode(revFileCode);
        //reviewFileRepository.deleteByReviewCode(revFileCode);
        reviewRepository.deleteById(reviewCode);
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
