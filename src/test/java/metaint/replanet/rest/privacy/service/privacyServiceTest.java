package metaint.replanet.rest.privacy.service;

import metaint.replanet.rest.org.dto.OrgRequestDTO;
import metaint.replanet.rest.org.service.OrgService;
import metaint.replanet.rest.privacy.dto.MemberDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class privacyServiceTest {

    @Mock
    PrivacyService privacyService;

    @Mock
    OrgService orgService;


    @Test
    @DisplayName("개인정보 제공 동의 service 테스트")
    void updateMemberPrivacyTest(){
        //given
        MemberDTO member = new MemberDTO();
        member.setMemberCode(1);
        member.setPrivacyStatus('Y');
        member.setResidentNum("1234561234567");

        //when
        int result = privacyService.updateMemberPrivacy(member);

        //then
        Assertions.assertEquals(result, 1);
    }

    @Test
    @DisplayName("기부처 기존 정보 가져오기 service 테스트")
    void selectOrgInformationTest(){
        //given
        int memberCode = 9;

        //when
        List<Map<String, Object>> orgInformation = orgService.selectOrgInformation(memberCode);

        //then
        Assertions.assertNotNull(orgInformation);
        System.out.println("기존 정보 확인 : " + orgInformation);
    }

    @Test
    @DisplayName("비밀번호 확인하기 service 테스트")
    void verifyPasswordTest(){
        //given
        int memberCode = 9;
        String password = "a123456!";

        //when
        int verify = orgService.verifyPassword(memberCode, password);

        //then
        Assertions.assertNotNull(verify);
        System.out.println("비밀번호가 맞다면 1을, 틀리다면 2를! " + verify);
    }

    @Test
    @DisplayName("org 프로필 업데이트 service 테스트")
    void updateOrgProfileTest(){
        //given

        //when
        OrgRequestDTO orgUpdate = new OrgRequestDTO();
        orgUpdate.setFileOriginName("fileOriginName");
        orgUpdate.setFileSaveName("fileSaveName");
        orgUpdate.setFileSavePath("FILE_DIR");
        orgUpdate.setFileExtension("fileExtension");
        orgUpdate.setOrgCode(9);
        orgUpdate.setOrgDescription("orgDescription".substring(1, "orgDescription".length()-1));

        //then
        Assertions.assertDoesNotThrow(() -> orgService.updateOrgInfo(orgUpdate));
    }

    @Test
    @DisplayName("org 정보 업데이트 service 테스트")
    void updateOrgInfoTest(){
        //given

        //when
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberId("memberEmail");
        memberDTO.setPassword("password");
        memberDTO.setMemberName("memberName".substring(1, "memberName".length()-1));
        memberDTO.setPhone("phone");
        memberDTO.setMemberCode(9);

        //then
        Assertions.assertDoesNotThrow(() -> orgService.updateMemberInfo(memberDTO));
    }

    @Test
    @DisplayName("org 탈퇴 신청 service 테스트")
    void updateOrgWithdrawTest(){
        //given

        //when
        OrgRequestDTO orgRequest = new OrgRequestDTO();
        orgRequest.setOrgCode(9);
        orgRequest.setWithdrawReqDate(new Date());
        orgRequest.setWithdrawReason("withdrawReason");

        //then
        Assertions.assertDoesNotThrow(() -> orgService.updateOrgWithdraw(orgRequest));
    }

}
