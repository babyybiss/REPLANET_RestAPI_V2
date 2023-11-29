package metaint.replanet.rest.campaign.controller;

import io.swagger.annotations.ApiOperation;
import metaint.replanet.rest.campaign.dto.*;
import metaint.replanet.rest.campaign.entity.Campaign;
import metaint.replanet.rest.campaign.service.CampaignService;
import metaint.replanet.rest.common.ResponseMessageDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.time.LocalTime.now;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping(value = {"/", "campaigns"})
public class CampaignController {

    private CampaignService campaignService;
    private final ModelMapper modelMapper;

    @Autowired
    public CampaignController(CampaignService campaignService, ModelMapper modelMapper) {
        this.campaignService = campaignService;
        this.modelMapper = modelMapper;
    }

    // 진행 & 종료 조회
    @ApiOperation(value = "캠페인 조회", notes = "캠페인 진행 or 종료 조회합니다", tags = {"캠페인 리스트 조회"})
    @GetMapping()
    public ResponseEntity<ResponseMessageDTO> main(@RequestParam String status) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();
        if (status.equals("ing")) {
            List<CampaignDesOrgDTO> campaignList = campaignService.findCampaignList();
            responseMap.put("campaignList", campaignList);
            ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "조회성공!", responseMap);

            return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
        } else if (status.equals("done")) {
            List<CampaignDesOrgDTO> campaignList = campaignService.findCampaignListDone();
            responseMap.put("campaignList", campaignList);
            ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "조회성공!", responseMap);

            return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
        }
        return null;
    }

    // 상세 조회
    @ApiOperation(value = "캠페인 상세조회", notes = "캠페인 상세조회 합니다", tags = {"캠페인 상세 조회"})
    @GetMapping("{campaignCode}")
    public ResponseEntity<ResponseMessageDTO> campaignDetail(@PathVariable int campaignCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        CampaignDesOrgDTO campaign = campaignService.findCampaign(campaignCode);
        System.out.println(campaign + "ㅎㅇ");
        responseMap.put("campaign", campaign);

        // 성공 메시지
        ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "조회성공!", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    // 기부처별 리스트
    @ApiOperation(value = "기부처 리스트 조회", notes = "기부처 리스트합니다", tags = {"기부처 리스트 조회"})
    @GetMapping("orgsList/{orgCode}")
    public ResponseEntity<ResponseMessageDTO> campaignListByOrg(@PathVariable int orgCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        List<CampaignDesOrgDTO> campaign = campaignService.findCampaignByOrg(orgCode);
        responseMap.put("campaignList", campaign);

        // 성공 메시지
        ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "조회성공!", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    // 참여내역 조회
    @ApiOperation(value = "캠페인 참여내역 조회", notes = "참여내역 조회~", tags = {"참여내역 조회"})
    @GetMapping("donations/{campaignCode}")
    public ResponseEntity<ResponseMessageDTO> donationByCampaignCode(@PathVariable int campaignCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        List<ParticipationDTO> participation = campaignService.findparticipationList(campaignCode);
        System.out.println(participation + "ㅎㅇ1212");
        responseMap.put("campaign", participation);

        ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "조회성공!", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);

    }

//    // 카테고리별 & 기간별 캠페인 조회
//    @GetMapping("/category")
//    public List<Campaign> findCategoryByCampaignList (@RequestParam String selectedValue){
//        LocalDateTime currentDate = LocalDateTime.now();
//
//        if(selectedValue.equals("latest")){// 최신순 조회
//
//        } else if (selectedValue.equals("earliest")) { // 마감순 조회
//            return campaignService.findCampaignSort(currentDate);
//        }
//        return null;
//    }


    // 캠페인 등록
    @ApiOperation(value = "캠페인 등록", notes = "캠펜 등록~", tags = {"캠페인 등록"})
    @PostMapping(name = "campaigns", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<ResponseMessageDTO> campaignRegist(@ModelAttribute RequestCampaignDTO campaign, MultipartFile imageFile) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        if (campaign != null && imageFile != null) {
            int campaignCode = campaignService.registCampaign(campaign);

            if (campaignCode == -1) {
                ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.BAD_GATEWAY, "과거로 회기 불가능", responseMap);
                return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_GATEWAY);
            }
            if (campaignCode == -2) {
                ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.BAD_GATEWAY, "10억 이상은 좀.. ", responseMap);
                return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_GATEWAY);
            }
            campaignService.registImage(imageFile, campaignCode);

        } else {
            if (campaign != null) {
                ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.BAD_GATEWAY, "사진이 없습니다.", responseMap);
                return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_GATEWAY);
            }
        }
        ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "등록 완료!", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    // 캠페인 삭제 기부내역 없을 경우에만
    @ApiOperation(value = "캠페인 삭제", notes = "캠펜 삭제~", tags = {"캠페인 삭제"})
    @DeleteMapping(value = {"campaigns/{campaignCode}", "modify/{campaignCode}"})
    public ResponseEntity<ResponseMessageDTO> campaignDelete(@PathVariable int campaignCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        CampaignDesOrgDTO campaign = campaignService.findCampaign(campaignCode);

        int currentBudget = Integer.parseInt(campaign.getCurrentBudget());
        if (currentBudget > 0) {
            ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.BAD_GATEWAY, "모금액 있어서 삭제 불가능합니다.", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_GATEWAY);
        } else {
            // 결과 확인
            int result = campaignService.deleteCampaign(campaignCode);
            if (result == 1) {
                ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "삭제완료!", responseMap);
                return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
            } else if (result == 0) {
                ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.BAD_GATEWAY, "삭제 실패!", responseMap);
                return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_GATEWAY);
            }
        }
        return null;
    }

    // 캠페인 수정 기부내역 없을 경우에만
    @ApiOperation(value = "캠페인 수정", notes = "캠펜 수정~", tags = {"캠페인 수정"})
    @PutMapping("campaigns/{campaignCode}")
    public ResponseEntity<ResponseMessageDTO> campaignModify(@ModelAttribute RequestCampaignDTO campaignDTO, MultipartFile imageFile) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        int campaignCode = campaignDTO.getCampaignCode();
        CampaignDesOrgDTO campaign = campaignService.findCampaign(campaignCode);

        // 현재 모금액 확인
        int currentBudget = Integer.parseInt(campaign.getCurrentBudget());
        LocalDateTime endDate = campaign.getEndDate();
        LocalDateTime currentDate = LocalDateTime.now();

        System.out.println(currentBudget);
        if ((currentBudget > 0) || (endDate.isBefore(currentDate))) {
            System.out.println("모금액 있어서 수정 안됨");
            ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.BAD_GATEWAY, "모금액 있거나 마감일이 지나서 수정 안됩니다.", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_GATEWAY);
        } else {
            int result = campaignService.modifyCampaign(campaignDTO, campaignCode, imageFile);
            if (result == -1) {
                System.out.println("마감일은 현재날짜보다 커야함");
                ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.BAD_GATEWAY, "과거로 회기 불가능", responseMap);
                return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_GATEWAY);
            }
            if (result == -2) {
                ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.BAD_GATEWAY, "10억 이상은 좀.. ", responseMap);
                return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_GATEWAY);
            }
            if (imageFile != null) {
                campaignService.registImage(imageFile, campaignCode);
            }
            System.out.println("수정됨");
        }
        ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "조회성공!", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    // 하루 총 기부금액,횟수 조회
    @GetMapping("campaigns/donations/today")
    public List<TodayDonationsDTO> getTodayDonations() {
        return campaignService.getTodayDonations();
    }


}

