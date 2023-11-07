package metaint.replanet.rest.pay.controller;

import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.pay.dto.pay.DonationAmountDTO;
import metaint.replanet.rest.pay.dto.pay.KakaoPayApprovalVO;
import metaint.replanet.rest.pay.entity.Donation;
import metaint.replanet.rest.pay.entity.Pay;
import metaint.replanet.rest.pay.service.KakaoPay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.Setter;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class SampleController {

    @Setter(onMethod_ = @Autowired)
    private KakaoPay kakaopay;


    @GetMapping("/kakaoPay")
    public void kakaoPayGet() {
        System.out.println("GetMapping /kakaoPay ㅎㅇㅎㅇ");
    }

    @PostMapping("/kakaoPay")
    public String kakaoPay(@RequestBody DonationAmountDTO amount) {
        log.info("[POST /kakaoPay] -------------------------------------");
        log.info("[/kakaoPay cashAmount] : " + amount.getCashAmount());
        log.info("[/kakaoPay pointAmount] : " + amount.getPointAmount());
        log.info("[/kakaoPay finalAmount] : " + amount.getFinalAmount());
        // RequestBody에 담아온 기부액수를 들고와서 확인하는거

        String redirectUrl = kakaopay.kakaoPayReady(amount);

        return "redirect:" + redirectUrl;
    }

    @GetMapping("/kakaoPaySuccess")
    public ModelAndView kakaoPaySuccess(@RequestParam("pg_token") String pg_token,
                                        @RequestParam("pointAmount") String pointAmount,
                                        ModelAndView mv) {
        log.info("[GET /kakaoPaySuccess]-------------------------------------");
        log.info("[/kakaoPaySuccess pg_token] : " + pg_token);
        log.info("[/kakaoPaySuccess pointAmount] : " + pointAmount);

        // 얘는 카카오페이API에서 결제완료를 누르면 요청이 되는페이지임

        KakaoPayApprovalVO info = kakaopay.kakaoPayInfo(pg_token, pointAmount);

        log.info("[/kakaoPaySuccess info.getTid()] : " + info.getTid());
        mv.setViewName("redirect:http://localhost:3000/donations/success?payTid=" + info.getTid());

        return mv;
    }

    @GetMapping("/kakaoPayCancle")
    public ModelAndView kakaoPayCancel() {
        log.info("[GET /kakaoPayCancle]-------------------------------------");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:http://localhost:3000/donations/cancel");
        // 취소할때 걍 메인으로 보내버릴지 생각해보기
        return mv;
    }

    @GetMapping("/kakaoPaySuccessFail")
    public ModelAndView kakaoPaySuccessFail() {
        log.info("[GET /kakaoPaySuccessFail]-------------------------------------");
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:http://localhost:3000/donations/fail");
        // 결제 실패시 이동할 화면 정하기!
        return mv;
    }

    @GetMapping("/donations")
    public ResponseEntity<List<Donation>> getAllDonations(ModelAndView mv) {

        log.info("[GET /donations] ----------------------------------------------");

        List<Donation> donationList = kakaopay.getDonation();
        log.info("[/donations donationList] : " + donationList);

        mv.addObject("donationList", donationList);

        return ResponseEntity.ok(donationList);
    }

    @GetMapping("/donations/{payTid}")
    public ResponseEntity<Pay> getDonationByTid(@PathVariable String payTid) {
        Pay pay = kakaopay.getPayByTid(payTid);

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

}
