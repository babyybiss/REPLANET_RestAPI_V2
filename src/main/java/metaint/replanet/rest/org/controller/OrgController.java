package metaint.replanet.rest.org.controller;


import metaint.replanet.rest.org.dto.OrgRequestDTO;
import metaint.replanet.rest.org.entity.Organization;
import metaint.replanet.rest.org.service.OrgService;
import metaint.replanet.rest.pay.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.privacy.dto.MemberDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@CrossOrigin("http://localhost:3000/")
@RestController
public class OrgController {

    private final OrgService orgService;

    public OrgController (OrgService orgService){
        this.orgService = orgService;
    }
    
    // 기부처 등록 (관리자 간단 정보)
    public void orgRegist(){}
    // 기부처 프로필 등록 ( 소개, 연락처, 사진 등)

    public void orgUpdateInfo(){}

    // 기부처 캠페인 등록
    public void campaignRegist(){

    }



    @GetMapping("/orgs")
    public ResponseEntity<List<Member>> getOrgs() {
        // 유효성 체크한다고 하면 현재 로그인한 놈의 ROLE_ADMIN인지 확인하는 정도 
        List<Member> orgList = orgService.getOrgList();

        return new ResponseEntity<>(orgList, HttpStatus.OK);

    }

    @GetMapping("orgInfo/{memberCode}")
    public ResponseEntity<?> selectOrgInformation(@PathVariable int memberCode,
                                                  @RequestParam String orgPwd){

        log.info("기부처 코드 확인 : " + memberCode);
        log.info("비밃번호 넘어왔는지 확인 : " + orgPwd);
        int verify = orgService.verifyPassword(memberCode, orgPwd);
        log.info("verify는 " + verify);
        if(verify == 1){
            List<Map<String, Object>> orgInformation = orgService.selectOrgInformation(memberCode);
            log.info("정보 확인~ " + orgInformation);
            return ResponseEntity.status(HttpStatus.OK).body(orgInformation);
        } else if(verify == 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 일치하지 않습니다.");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("처리 중 오류가 발생했습니다.");
    }

    @PostMapping("orgModify/{memberCode}")
    public ResponseEntity<?> updateOrgInformation(@PathVariable int memberCode, @RequestPart(value = "file", required = false)MultipartFile orgImg, @RequestPart(value = "orgDescription") String orgDescription,
                                                  @RequestPart(value = "memberEmail") String memberEmail, @RequestPart(value = "password") String password,
                                                  @RequestPart(value = "memberName") String memberName, @RequestPart(value = "phone") String phone){

        log.info("기부처 코드 확인 : " + memberCode);
        log.info("기부처 소개 넘어왔는지 확인 : " + orgDescription);
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId(memberEmail);
        memberDTO.setPassword(password);
        memberDTO.setMemberName(memberName.substring(1, memberName.length()-1));
        memberDTO.setPhone(phone);
        memberDTO.setMemberCode(memberCode);
        log.info("기부처 정보 넘어왔는지 확인 : " + memberDTO);

        if(orgImg != null){
            //이미지가 바뀌었을 때
            log.info("기부처 이미지 넘어왔는지 확인 : " + orgImg);
            try{
                String fileOriginName = orgImg.getOriginalFilename();
                String fileExtension = fileOriginName.substring(fileOriginName.lastIndexOf("."));
                String fileSaveName = UUID.randomUUID().toString().replaceAll("-", "") + fileExtension;
                String FILE_DIR = null;
                Path rootPath;
                if (FileSystems.getDefault().getSeparator().equals("/")) {
                    // Unix-like system (MacOS, Linux)
                    Path filePath1 = Paths.get("/REPLANET_React_V2/public/orgImgs").toAbsolutePath();
                    rootPath = Paths.get("/User").toAbsolutePath();
                    Path relativePath = rootPath.relativize(filePath1);
                    FILE_DIR = String.valueOf(relativePath);
                } else {
                    // Windows
                    Path filePath2 = Paths.get("/dev/metaint/REPLANET_React_V2/public/orgImgs").toAbsolutePath();
                    rootPath = Paths.get("C:\\").toAbsolutePath();
                    Path relativePath = rootPath.resolve(filePath2);
                    FILE_DIR = String.valueOf(relativePath);
                }
                log.info("저장 경로 확인 : " + FILE_DIR);
                OrgRequestDTO orgUpdate = new OrgRequestDTO();
                orgUpdate.setFileOriginName(fileOriginName);
                orgUpdate.setFileSaveName(fileSaveName);
                orgUpdate.setFileSavePath(FILE_DIR);
                orgUpdate.setFileExtension(fileExtension);
                orgUpdate.setOrgCode(memberCode);
                orgUpdate.setOrgDescription(orgDescription.substring(1, orgDescription.length()-1));

                try {
                    File directory = new File(FILE_DIR);
                    if (!directory.exists()) {
                        directory.mkdirs();
                        log.info("저장경로가 존재하지 않아 새로 생성되었습니다.");
                    }
                    File pf = new File(FILE_DIR + "/" + fileSaveName);
                    orgImg.transferTo(pf);

                    orgService.updateOrgInfo(orgUpdate);
                    orgService.updateMemberInfo(memberDTO);

                    return ResponseEntity.status(HttpStatus.OK).body("수정 완료");
                } catch (IOException e){
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("처리 중 오류");
                }
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("처리 중 오류");
            }
        } else {
            //이미지 변경 없을 때
            try{
                OrgRequestDTO orgUpdate = new OrgRequestDTO();
                orgUpdate.setOrgCode(memberCode);
                orgUpdate.setOrgDescription(orgDescription.substring(1, orgDescription.length()-1));

                orgService.updateOrgInfo(orgUpdate);
                orgService.updateMemberInfo(memberDTO);

                return ResponseEntity.status(HttpStatus.OK).body("수정 완료");
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("처리 중 오류");
            }
        }
    }
}
