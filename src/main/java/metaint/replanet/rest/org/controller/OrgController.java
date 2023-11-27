package metaint.replanet.rest.org.controller;

import metaint.replanet.rest.org.service.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping()
public class OrgController {

    private OrgService orgService;
    @Autowired
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

}
