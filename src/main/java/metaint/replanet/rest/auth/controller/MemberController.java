package metaint.replanet.rest.auth.controller;

import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.auth.dto.*;
import metaint.replanet.rest.auth.entity.Member;
import metaint.replanet.rest.auth.exception.BadRequestException;
import metaint.replanet.rest.auth.service.MemberService;
import metaint.replanet.rest.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/auth")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/password")
    public ResponseEntity<MemberResponseDto> setMemberPassword(@RequestBody ChangePasswordRequestDto request) {
        return ResponseEntity.ok(memberService.changeMemberPassword(request.getExPassword(), request.getNewPassword()));
    }


    @PostMapping("/emailcheck/{email}")
    public ResponseEntity<?> checkEmailDuplication(@PathVariable String email) throws BadRequestException {
        System.out.println("test" + email);

        try {
            if (!memberService.existsByEmail(email)) {
                return ResponseEntity.ok(email + "은(는) 사용 가능한 이메일입니다.");

            } else {
                return new ResponseEntity(
                        "이미 사용 중인 이메일입니다.",
                        HttpStatus.BAD_REQUEST);
            }
        } catch(Exception e) {
            return new ResponseEntity(
                    "API 오류",
                    HttpStatus.MULTI_STATUS);
        }

    }

    @PostMapping("/phonecheck/{phone}")
    public ResponseEntity<?> checkPhoneDuplication(@PathVariable String phone) throws BadRequestException {
        System.out.println("test" + phone);

        try {
            if (!memberService.existsByPhone(phone)) {
                return ResponseEntity.ok(phone + "은(는) 사용 가능한 번호입니다.");

            } else {
                return new ResponseEntity(
                        "이미 사용 중인 번호입니다.",
                        HttpStatus.BAD_REQUEST);
            }
        } catch(Exception e) {
            return new ResponseEntity(
                    "API 오류",
                    HttpStatus.MULTI_STATUS);
        }

    }

    @GetMapping("/emailfind/{phone}")
    public String findEmailByPhone(@PathVariable String phone, Model model) {
        Member member = memberService.findEmailByPhone(phone);

        model.addAttribute("email", member.getEmail());

        log.info(phone);
        log.info(member.getEmail());

        return member.getEmail();

    }

    @GetMapping("/phonefind/{email}")
    public String findPhoneByEmail(@PathVariable String email, Model model) {
        Member member = memberService.findPhoneByEmail(email);

        model.addAttribute("phone", member.getPhone());

        log.info(email);
        log.info(member.getPhone());

        return member.getPhone();

    }


//    @PostMapping("findPw")
//    public String modifyPassword(MemberDto memberDto) {
//        memberService.modifyPassword(memberDto);
//        return "redirect:/auth/login";
//    }


}