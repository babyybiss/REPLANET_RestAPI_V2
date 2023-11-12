package metaint.replanet.rest.campaign.controller;

import metaint.replanet.rest.campaign.dto.CampaignDescFileDTO;
import metaint.replanet.rest.campaign.dto.CampaignDescriptionDTO;
import metaint.replanet.rest.campaign.entity.CampaignDescription;
import metaint.replanet.rest.campaign.entity.Donation;
import metaint.replanet.rest.campaign.service.CampaignService;
import metaint.replanet.rest.point.dto.PointFileDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public List<CampaignDescription> main() {
        return campaignService.findCampaignList();
    }

    // 종료 조회
    @GetMapping("done")
    public List<CampaignDescription> campaignListDone() {
        return campaignService.findCampaignListDone();
    }

    @GetMapping("{campaignCode}")
    public CampaignDescription campaignDetail(@PathVariable int campaignCode) {
        System.out.println(campaignCode);
        return campaignService.findCampaign(campaignCode);
    }

    // 캠페인 등록
    @PostMapping("campaigns")
    public ResponseEntity<?> campaignRegist(@RequestBody CampaignDescriptionDTO campaign,
                                            MultipartFile imageFile) {

        if (campaign != null && imageFile != null) {

            int campaignCode = campaignService.registCampaign(campaign);
            campaignService.registImage(imageFile,campaignCode);

        }
        return ResponseEntity.status(HttpStatus.OK).body("등록 성공");
    }

    // 캠페인 삭제 기부내역 없을 경우에만
    @DeleteMapping("campaigns/{campaignCode}")
    public String campaignDelete(int campaignCode){

        CampaignDescription campaign = campaignService.findCampaign(campaignCode);

        int currentBudget = campaign.getCurrentBudget();
        if (currentBudget > 0 ){
            System.out.println("모금액 있어서 삭제 안됨");
            return "삭제불가";
        }else {
            campaignService.deleteCampaign(campaignCode);
            System.out.println("삭제됨");

        }
        return "삭제완료";
    }

    // 캠페인 수정 기부내역 없을 경우에만
    @PutMapping("campaigns/{campaignCode}")
    public String campaignModify(@RequestBody CampaignDescriptionDTO campaignDTO,
                                 MultipartFile imageFile){

        CampaignDescription campaign = campaignService.findCampaign(campaignDTO.getCampaignCode());

        int currentBudget = campaign.getCurrentBudget();
        LocalDateTime endDate = campaign.getEndDate();
        LocalDateTime currentDate = LocalDateTime.now();
        System.out.println(currentBudget + "현재모금");
        if ((currentBudget > 0) && (endDate.isAfter(currentDate))){
            System.out.println("모금액 있어서 수정 안됨");
            return "수정불가";
        }else {
            campaignService.modifyCampaign(campaignDTO,imageFile);
            System.out.println("수정됨");

        }
        return "수정완료";
    }
    // 기부내역 조회
    @GetMapping("campaigns/{campaignCode}/donations")
    public List<Donation> donationList (int campaignCode){
        return campaignService.findDonationList(campaignCode);
    }
}
