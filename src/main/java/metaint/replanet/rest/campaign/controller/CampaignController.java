package metaint.replanet.rest.campaign.controller;

import metaint.replanet.rest.campaign.dto.CampaignDescriptionDTO;
import metaint.replanet.rest.campaign.entity.CampaignDescription;
import metaint.replanet.rest.campaign.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping(value = {"/","campaigns"})
public class CampaignController {

    private CampaignService campaignService;

    @Autowired
    public CampaignController(CampaignService campaignService){
        this.campaignService = campaignService;
    }

    // 진행중 조회 
    @GetMapping()
    public List<CampaignDescription>  main(){
        return campaignService.findCampaignList();
    }
    // 종료 조회
    @GetMapping("done")
    public List<CampaignDescription> campaignListDone(){
        return campaignService.findCampaignListDone();
    }

    @GetMapping("{campaignCode}")
    public CampaignDescription campaignDetail(@PathVariable int campaignCode){
        System.out.println(campaignCode);
        return campaignService.findCampaign(campaignCode);
    }

    // 캠페인 등록
    @PostMapping("campaigns")
    public ResponseEntity<?> campaignRegist(@RequestBody CampaignDescriptionDTO campaign){
        // 목표금액 , 제거
        String goalBudger =  campaign.getGoalBudget().replaceAll(",","");
        campaign.setGoalBudget(goalBudger);

        // 현재 날짜
        LocalDateTime getStartDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String startDate = getStartDate.format(formatter);
        campaign.setStartDate(startDate);



        System.out.println( startDate.getClass().getName() +" : 스타트데이트");
        System.out.println( campaign.getEndDate().getClass().getName() +" : 스타트데이트");



        System.out.println(campaign + "여기는?!!");


        campaignService.registCampaign(campaign);
        return ResponseEntity.status(HttpStatus.OK).body("등록 성공");
    }
    
}
