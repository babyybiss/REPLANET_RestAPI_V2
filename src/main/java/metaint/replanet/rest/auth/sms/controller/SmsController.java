package metaint.replanet.rest.auth.sms.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.auth.dto.MemberRequestDto;
import metaint.replanet.rest.auth.sms.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Random;

@Slf4j
@RestController
@RequestMapping("/")
public class SmsController {
    @Autowired
    private SmsService smsService;
    private MemberRequestDto memberRequestDto;

    @PostMapping("/users/sms")
    public String sendSMS (@RequestBody String u_phone) throws JsonProcessingException {

        Random rnd  = new Random();
        StringBuffer buffer = new StringBuffer();
        for (int i=0; i<4; i++) {
            buffer.append(rnd.nextInt(10));
        }
        String smsCode = buffer.toString();
        log.info("[/users/sms 수신자 번호 u_phone] : " + u_phone);
        log.info("[/users/sms 인증 번호 smsCode] : " + smsCode);
        smsService.smsService(u_phone, smsCode);
        return smsCode;


    }


    @PostMapping("/users/smscode")
    public boolean checkSMS (@RequestBody String smsCode, HttpServletRequest request) {
        log.info(smsCode);
        String enteredSmsCode = (String) request.getAttribute(smsCode);
        if (enteredSmsCode == smsCode) {return true;}
        else {return false;}
    }

}