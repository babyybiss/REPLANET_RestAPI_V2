package metaint.replanet.rest.auth.controller;

import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.auth.dto.ChangePasswordRequestDto;
import metaint.replanet.rest.auth.dto.MemberRequestDto;
import metaint.replanet.rest.auth.dto.MemberResponseDto;
import metaint.replanet.rest.auth.exception.BadRequestException;
import metaint.replanet.rest.auth.service.MemberService;
import metaint.replanet.rest.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

}