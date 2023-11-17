package metaint.replanet.rest.campaign.controller;

import metaint.replanet.rest.campaign.dto.CampaignDescFileDTO;
import metaint.replanet.rest.campaign.dto.CampaignDescriptionDTO;
import metaint.replanet.rest.campaign.dto.TodayDonationsDTO;
import metaint.replanet.rest.campaign.entity.CampaignAndFile;
import metaint.replanet.rest.campaign.entity.CampaignDescription;
import metaint.replanet.rest.campaign.entity.Donation;
import metaint.replanet.rest.campaign.entity.Pay;
import metaint.replanet.rest.campaign.service.CampaignService;
import metaint.replanet.rest.point.dto.PointFileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping(value = {"/", "campaigns"})
public class CampaignController {

    private CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService) {
        this.campaignService = campaignService;
    }

    // 진행중 조회 
    @GetMapping()
    public List<CampaignAndFile> main() {
        return campaignService.findCampaignList();
    }

    // 종료 조회
    @GetMapping("done")
    public List<CampaignAndFile> campaignListDone() {
        return campaignService.findCampaignListDone();
    }

    // 상세 조회
    @GetMapping("{campaignCode}")
    public CampaignAndFile campaignDetail(@PathVariable int campaignCode) {
        System.out.println(campaignCode);
        return campaignService.findCampaign(campaignCode);
    }
    // 기부내역 조회
    @GetMapping("donations/{campaignCode}")
    public List<Pay> donationByCampaignCode(@PathVariable int campaignCode) {
        System.out.println(campaignCode+"ㅇㅇㅇ");
        return campaignService.findparticipationList(campaignCode);
    }


    // 캠페인 등록
    @PostMapping(name = "campaigns", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<?> campaignRegist(@ModelAttribute CampaignDescriptionDTO campaign, MultipartFile imageFile) throws IOException {

        System.out.println(campaign);
        System.out.println(imageFile);
        if (campaign != null && imageFile != null) {
            int campaignCode = campaignService.registCampaign(campaign);

            if (campaignCode == -1) {
                System.out.println("마감일은 현재날짜보다 커야함");
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("마감일은 현재날짜보다 커야함");
            }
            if (campaignCode == -2) {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("10억 이상은 좀.. ");
            }
            campaignService.registImage(imageFile, campaignCode);

        } else {
            if (campaign != null) {
                System.out.println("여기지?");
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("사진 없음");
            } else if (imageFile != null) {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("내용 없음");
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body("등록 성공");
    }

    // 캠페인 삭제 기부내역 없을 경우에만
    @DeleteMapping("campaigns/{campaignCode}")
    public ResponseEntity<?> campaignDelete(@PathVariable int campaignCode) {

        System.out.println(campaignCode + "시작 캠펜");
        CampaignAndFile campaign = campaignService.findCampaign(campaignCode);

        System.out.println(campaign + " 여기 지나갑니다");
        int currentBudget = campaign.getCurrentBudget();
        if (currentBudget > 0) {
            System.out.println("모금액 있어서 삭제 안됨");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body("모금액 있어서 삭제 안됨");
        } else {
            campaignService.deleteCampaign(campaignCode);
            System.out.println("삭제됨");
            return ResponseEntity.status(HttpStatus.OK).body("삭제됨");
        }
    }

    // 캠페인 수정 기부내역 없을 경우에만
    @PutMapping("campaigns/{campaignCode}")
    public ResponseEntity<?> campaignModify(@ModelAttribute CampaignDescriptionDTO campaignDTO, MultipartFile imageFile) {

        System.out.println(imageFile + "임지");
        int campaignCode = campaignDTO.getCampaignCode();

        CampaignAndFile campaign = campaignService.findCampaign(campaignCode);

        // 현재 모금액 확인
        int currentBudget = campaign.getCurrentBudget();
        LocalDateTime endDate = campaign.getEndDate();
        LocalDateTime currentDate = LocalDateTime.now();

        if ((currentBudget > 0) && (endDate.isAfter(currentDate))) {
            System.out.println("모금액 있어서 수정 안됨");
            return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("모금액 있거나 마감일이 지나서 수정 안됨");
        } else {
            int result = campaignService.modifyCampaign(campaignDTO, campaignCode, imageFile);
            if (result == -1) {
                System.out.println("마감일은 현재날짜보다 커야함");
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("마감일은 현재날짜보다 커야함");
            }
            if (result == -2) {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("10억 이상은 좀.. ");
            }
            if (imageFile != null) {
                System.out.println("이거 언제?");
                campaignService.registImage(imageFile, campaignCode);

            }
            System.out.println("수정됨");
        }
        return ResponseEntity.status(HttpStatus.OK).body("성공");
        //return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공")
    }

    // 하루 총 기부금액,횟수 조회
    @GetMapping("donations/today")
    public List<TodayDonationsDTO> getTodayDonations(){

        return campaignService.getTodayDonations();
    }




}

