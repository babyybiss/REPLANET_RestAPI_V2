package metaint.replanet.rest.privacy.controller;

import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.privacy.dto.MemberDTO;
import metaint.replanet.rest.privacy.service.PrivacyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/")
public class PrivacyController {

    private PrivacyService privacyService;

    public PrivacyController(PrivacyService privacyService){
        this.privacyService = privacyService;
    }

    @PutMapping("provideInfo")
    public ResponseEntity<?> updateMemberPrivacy(@RequestBody MemberDTO memberDTO){

        int result = 0;
        log.info("memberDTO 확인 : " + memberDTO);
        log.info("privacyStatus 확인 : " + memberDTO.getPrivacyStatus());
        if(memberDTO.getPrivacyStatus() == 'Y'){
            result = privacyService.updateMemberPrivacy(memberDTO);
            if(result == 1){
                return ResponseEntity.status(HttpStatus.OK).body("동의 처리 완료");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("처리 중 서버 오류 발생");
            }
        } else if (memberDTO.getPrivacyStatus() == 'N'){
            result = privacyService.withdrawMemberPrivacy(memberDTO);
            if(result == 1){
                return ResponseEntity.status(HttpStatus.OK).body("철회 처리 완료");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("처리 중 서버 오류 발생");
            }
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("신청 처리 중 오류 발생");
    }

    @GetMapping("privacyStatus/{memberCode}")
    public ResponseEntity<?> selectPrivacyStatus(@PathVariable int memberCode){

        log.info("멤버코드 확인 : " + memberCode);
        char privacyStatus = privacyService.selectPrivacyStatus(memberCode);
        if(privacyStatus != 0){
            return ResponseEntity.status(HttpStatus.OK).body(privacyStatus);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("처리 중 서버 오류 발생");
    }

    @DeleteMapping("users/{memberCode}")
    public ResponseEntity<?> deleteUser(@PathVariable int memberCode,
                                        @RequestParam String password){

        log.info("멤버코드 확인 : " + memberCode);
        log.info("비밀번호 확인 : " + password);

        int verify = privacyService.verifyPassword(memberCode, password);

        if(verify == 1){
            int result = privacyService.deleteUser(memberCode);
            if(result == 2){
                return ResponseEntity.status(HttpStatus.OK).body("탈퇴 처리 완료");
            } else if(result == 1){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("repository 단계에서 오류 발생");
            } else if(result == 0){
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("service 단계에서 오류 발생");
            }
        } else if(verify == 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("WrongPwd");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호를 검증하지 못했습니다.");
    }

    @GetMapping("users/verifying/{memberCode}")
    public ResponseEntity<?> verifyUserPwd(@PathVariable int memberCode, @RequestParam(value = "userPwd") String password){

        log.info("멤버코드 확인 : " + memberCode);
        log.info("비밀번호 확인 : " + password);

        int verify = privacyService.verifyPassword(memberCode, password);
        if(verify == 1){
                return ResponseEntity.status(HttpStatus.OK).body("탈퇴 처리 완료");
        } else if(verify == 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("WrongPwd");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호를 검증하지 못했습니다.");
    }

    @PutMapping("users/{memberCode}")
    public ResponseEntity<?> modifyUser(@PathVariable int memberCode, @RequestParam(value = "memberName") String memberName,
                                        @RequestParam(value = "password") String password, @RequestParam(value = "phone") String phone){

        log.info("멤버코드 확인 : " + memberCode);
        log.info("비밀번호 확인 : " + password);
        log.info("이름 확인 : " + memberName);
        log.info("전화번호 확인 : " + phone);

        MemberDTO modified = new MemberDTO();
        modified.setMemberCode(memberCode);
        modified.setPassword(password);
        modified.setPhone(phone);
        modified.setMemberName(memberName);

        try {
            privacyService.modifyUser(modified);
            return ResponseEntity.status(HttpStatus.OK).body("정보 수정 완료");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("service 단계에서 오류 발생");
        }
    }
}
