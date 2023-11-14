package metaint.replanet.rest.pay.controller;

import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.auth.jwt.TokenProvider;
import metaint.replanet.rest.pay.dto.pay.DonationAmountDTO;
import metaint.replanet.rest.pay.dto.pay.KakaoPayApprovalVO;
import metaint.replanet.rest.pay.entity.Donation;
import metaint.replanet.rest.pay.entity.Member;
import metaint.replanet.rest.pay.entity.Pay;
import metaint.replanet.rest.pay.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import lombok.Setter;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class PayController {

    @Setter(onMethod_ = @Autowired)
    private PayService payService;

    @Autowired
    private TokenProvider tokenProvider;


    @GetMapping("/kakaoPay")
    public void kakaoPayGet() {}

    @PostMapping("/kakaoPay/{campaignCode}")
    public String kakaoPay(@RequestBody DonationAmountDTO amount,
                           @PathVariable String campaignCode,
                           @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        log.info("[POST /kakaoPay] -------------------------------------");
        log.info("[/kakaoPay cashAmount] : " + amount.getCashAmount());
        log.info("[/kakaoPay pointAmount] : " + amount.getPointAmount());
        log.info("[/kakaoPay finalAmount] : " + amount.getFinalAmount());
        log.info("[/kakaoPay campaign] : " + campaignCode);

        String token = extractToken(authorizationHeader);
        Authentication authentication = tokenProvider.getAuthentication(token);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String memberCode = userDetails.getUsername();
        log.info("[/kakaoPay memberCode] : " + memberCode);

        String redirectUrl = payService.kakaoPayReady(amount, campaignCode, memberCode);

        return "redirect:" + redirectUrl;
    }

    @PostMapping("/pointDonation/{campaignCode}")
    public ResponseEntity<Map<String, Integer>> pointDonation(@RequestBody DonationAmountDTO amount,
                                                              @PathVariable String campaignCode,
                                                              @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {
        log.info("[POST /pointDonation] -------------------------------------");
        log.info("[/pointDonation cashAmount] : " + amount.getCashAmount()); // 0일거임
        log.info("[/pointDonation pointAmount] : " + amount.getPointAmount());
        log.info("[/pointDonation finalAmount] : " + amount.getFinalAmount());
        log.info("[/pointDonation campaignCode] : " + campaignCode);

        String token = extractToken(authorizationHeader);
        Authentication authentication = tokenProvider.getAuthentication(token);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String memberCode = userDetails.getUsername();
        log.info("[/kakaoPay memberCode] : " + memberCode);

        int payCode = payService.postPointDonation(amount, campaignCode, memberCode);
        log.info("[GET /pointDonation] payCode : " + payCode);

        Map<String, Integer> response = new HashMap<>();
        response.put("payCode", payCode);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/kakaoPaySuccess")
    public void kakaoPaySuccess(@RequestParam("pg_token") String pg_token,
                                @RequestParam("pointAmount") String pointAmount,
                                @RequestParam("campaignCode") String campaignCode,
                                @RequestParam("memberCode") String memberCode,
                                HttpServletResponse response) {
        log.info("[GET /kakaoPaySuccess]-------------------------------------");
        log.info("[GET /pg_token] : " + pg_token);
        log.info("[GET /pointAmount] : " + pointAmount);
        log.info("[GET /campaignCode] : " + campaignCode);

        KakaoPayApprovalVO info = payService.kakaoPayInfo(pg_token, pointAmount, campaignCode, memberCode);

        log.info("[GET /kakaoPaySuccess] info.getPayCode() : " + info.getPayCode());

        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", "http://localhost:3000/campaign/" + campaignCode + "/donations/success?number=" + info.getPayCode());
    }

    @GetMapping("/kakaoPayCancle")
    public void kakaoPayCancel(HttpServletResponse response, @RequestParam("campaignCode") String campaignCode) {
        log.info("[GET /kakaoPayCancle]-------------------------------------");
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", "http://localhost:3000/campaign/" + campaignCode + "/donations/cancel");
    }

    @GetMapping("/kakaoPaySuccessFail")
    public void kakaoPaySuccessFail(HttpServletResponse response, @RequestParam("campaignCode") String campaignCode) {
        log.info("[GET /kakaoPaySuccessFail]-------------------------------------");
        response.setStatus(HttpServletResponse.SC_FOUND);
        response.setHeader("Location", "http://localhost:3000/campaign/" + campaignCode + "/donations/fail");
    }

    @GetMapping("/paysByMemberWithDate")
    public ResponseEntity<List<Pay>> getPays(@RequestParam(required = false) String startDate,
                                             @RequestParam(required = false) String endDate,
                                             @RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        log.info("[GET /pays] ----------------------------------------------");
        log.info("[GET /pays startDate] : " + startDate);
        log.info("[GET /pays endDate] : " + endDate);

        String token = extractToken(authorizationHeader);
        Authentication authentication = tokenProvider.getAuthentication(token);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String memberCode = userDetails.getUsername();
        log.info("[/kakaoPay memberCode] : " + memberCode);

        List<Pay> payList;

        if (startDate != null && endDate != null) {
            payList = payService.getPaysByDateRange(startDate, endDate, memberCode);
        } else {
            payList = payService.getPaysByMember(memberCode);
        }

        log.info("[/pays payList] : " + payList);
        log.info("[/pays payList.size()] : " + payList.size());

        return ResponseEntity.ok(payList);
    }

    @GetMapping("/donations/payCode={payCode}")
    public ResponseEntity<Pay> getDonationByTid(@PathVariable String payCode) {
        Pay pay = payService.getPayByPayCode(payCode);
        log.info("GET /donations/{payTid} pay : " + pay);

        if (pay != null) {
            Donation donation = pay.getRefDonation();
            if (donation != null) {
                return new ResponseEntity<>(pay, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/users/point")
    public ResponseEntity<Member> getPointByMember(@RequestHeader(name = HttpHeaders.AUTHORIZATION) String authorizationHeader) {

        String token = extractToken(authorizationHeader);
        Authentication authentication = tokenProvider.getAuthentication(token);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String memberCode = userDetails.getUsername();
        log.info("[/kakaoPay memberCode] : " + memberCode);

        Member member = payService.getPointByMember(memberCode);
        log.info("GET /users/point member.getCurrentPoint() : " + member.getCurrentPoint());

        if (member != null) {
            return new ResponseEntity<>(member, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
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
}
