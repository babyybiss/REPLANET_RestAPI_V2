package metaint.replanet.rest.campaign.controller;

import io.swagger.annotations.ApiOperation;
import metaint.replanet.rest.campaign.dto.*;
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
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

    // 기부처별 진행& 종료 리스트 조회
    @ApiOperation(value = "기부처 리스트 조회", notes = "기부처 리스트 조회 합니다", tags = {"기부처 리스트 조회"})
    @GetMapping("orgsList/{orgCode}")
    public ResponseEntity<ResponseMessageDTO> campaignListByOrg(@PathVariable int orgCode, @RequestParam String status) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        if (status.equals("ing") || status.equals("no") ) {
            List<CampaignDesOrgDTO> campaign = campaignService.findCampaignByOrg(orgCode);
            responseMap.put("campaignList", campaign);

            // 성공 메시지
            ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "진행중 조회성공!", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
        } else if (status.equals("done")) {
            List<CampaignDesOrgDTO> campaign = campaignService.findCampaignByOrgDone(orgCode);
            responseMap.put("campaignList", campaign);

            // 성공 메시지
            ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "종료 조회성공!", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
        }
        return null;
    }

    // 기부처별 캠페인 개수 조회
    @ApiOperation(value = "캠페인 개수 조회", notes = "캠페인 개수 조회 조회 합니다", tags = {"캠페인 개수 조회 조회"})
    @GetMapping("orgsCount/{orgCode}")
    public ResponseEntity<ResponseMessageDTO> getCampaignCount(@PathVariable int orgCode) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

            int ingCount = campaignService.getCampaignCount(orgCode);
            responseMap.put("ingCount", ingCount);

            int doneCount = campaignService.getCampaignCountDone(orgCode);
            responseMap.put("doneCount", doneCount);

            // 성공 메시지
            ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "개수 조회성공!", responseMap);
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
        responseMap.put("campaign", participation);

        ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "조회성공!", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);

    }

    // 캠페인 등록
    @ApiOperation(value = "캠페인 등록", notes = "캠펜 등록~", tags = {"캠페인 등록"})
    @PostMapping(name = "campaigns", consumes = MediaType.ALL_VALUE)
    public ResponseEntity<ResponseMessageDTO> campaignRegist(@ModelAttribute RequestCampaignDTO campaign, MultipartFile imageFile) throws IOException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();
        try{
        if (campaign != null && imageFile != null) {
            int campaignCode = campaignService.registCampaign(campaign);
            if (campaignCode == -1) {
                ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.BAD_GATEWAY, "과거로 회기 불가능", responseMap);
                return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_GATEWAY);
            }
            if (campaignCode == -2) {
                ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.BAD_GATEWAY, "10억 이상 모금은 불가능 합니다.", responseMap);
                return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_GATEWAY);
            }
            campaignService.registImage(imageFile, campaignCode);

        } else {
            if (campaign != null) {
                ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.BAD_GATEWAY, "사진이 없습니다.", responseMap);
                return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_GATEWAY);
            }
        }
        }catch (NumberFormatException e){
            ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.BAD_GATEWAY, "목표금액이 비었습니다.", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_GATEWAY);
        }catch (DateTimeException e){
            ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.BAD_GATEWAY, "마감일이 비었습니다.", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_GATEWAY);
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
            ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.BAD_GATEWAY, "모금액 있거나 마감일이 지나서 수정 안됩니다.", responseMap);
            return new ResponseEntity<>(responseMessage, headers, HttpStatus.BAD_GATEWAY);
        } else {
            int result = campaignService.modifyCampaign(campaignDTO, campaignCode, imageFile);
            if (result == -1) {
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
        }
        ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "성공!", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    // 하루 총 기부금액,횟수 조회
    @ApiOperation(value = "일일 기부 현황 조회", notes = "일일 기부현황 조회~", tags = {"기부현황"})
    @GetMapping("campaigns/donations/today")
    public ResponseEntity<ResponseMessageDTO>  getTodayDonations() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        Map<String, Object> responseMap = new HashMap<>();

        List<TodayDonationsDTO> todayDonation = campaignService.getTodayDonations();
        responseMap.put("todayDonation", todayDonation);

        ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "성공!", responseMap);
        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }
}

