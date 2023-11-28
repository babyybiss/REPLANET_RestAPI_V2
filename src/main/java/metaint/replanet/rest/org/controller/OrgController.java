package metaint.replanet.rest.org.controller;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@CrossOrigin("http://localhost:3000")
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


}
