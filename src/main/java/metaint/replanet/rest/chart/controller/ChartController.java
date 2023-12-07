package metaint.replanet.rest.chart.controller;


import io.swagger.annotations.ApiOperation;
import metaint.replanet.rest.chart.service.ChartService;
import metaint.replanet.rest.common.ResponseMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/charts")
public class ChartController {

    private final ChartService chartService;

    @Autowired
    public ChartController(ChartService chartService) {
        this.chartService = chartService;
    }

    @ApiOperation(value = "통계 조회 요청", notes = "현재 등록된 캠페인의 카테고리를 기준으로한 데이터 통계를 조회합니다.", tags = {"통계 리스트 조회"})
    @GetMapping("/series/byCategory")
    public ResponseEntity<ResponseMessageDTO> selectCategoryDataResult() {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("categoryData", chartService.countAndSumByCampaignCategory());
        ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "조회성공!", responseMap);

        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    @ApiOperation(value = "통계 조회 요청", notes = "현재 등록된 캠페인의 당해를 기준으로한 데이터 통계를 조회합니다.", tags = {"통계 리스트 조회"})
    @GetMapping("/series/byCurrentYear")
    public ResponseEntity<ResponseMessageDTO> selectCurrentYearDataResult() {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("currentYearData" , chartService.countAndSumByCurrentYear());

        ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "조회성공!", responseMap);

        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

    @ApiOperation(value = "통계 조회 요청", notes = "현재 등록된 캠페인의 월 및 시간을 기준으로한 데이터 통계를 조회합니다.", tags = {"통계 리스트 조회"})
    @GetMapping("/series/byDonationTime")
    public ResponseEntity<ResponseMessageDTO> selectDonationDataResult() {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(new MediaType("application","json", Charset.forName("UTF-8")));

        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("donationByTimeData", chartService.selectDonationByTime());

        ResponseMessageDTO responseMessage = new ResponseMessageDTO(HttpStatus.OK, "조회성공!", responseMap);

        return new ResponseEntity<>(responseMessage, headers, HttpStatus.OK);
    }

}
