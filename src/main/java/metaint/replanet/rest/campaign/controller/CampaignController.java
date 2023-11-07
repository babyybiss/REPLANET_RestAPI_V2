package metaint.replanet.rest.campaign.controller;

import metaint.replanet.rest.campaign.entity.CampaignDescription;
import metaint.replanet.rest.campaign.service.CampaignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/")
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
    @GetMapping("/done")
    public List<CampaignDescription> campaignListDone(){
        return campaignService.findCampaignListDone();
    }

    @GetMapping("/campaign/{campaignCode}")
    public CampaignDescription campaignDetail(@PathVariable int campaignCode){
        return campaignService.findCampaign(campaignCode);

    }
    
}
