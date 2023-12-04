package metaint.replanet.rest.privacy.repository;

import metaint.replanet.rest.org.entity.Organization;
import metaint.replanet.rest.org.repository.OrgMemberRepository;
import metaint.replanet.rest.org.repository.OrgRepository;
import metaint.replanet.rest.privacy.dto.MemberDTO;
import metaint.replanet.rest.privacy.entity.Donation;
import metaint.replanet.rest.privacy.entity.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@SpringBootTest
public class privacyRepositoryTest {

    @Mock
    private PrivacyRepository privacyRepository;

    @Mock
    private OrgRepository orgRepository;

    @Mock
    private OrgMemberRepository orgMemberRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDonationRepository donationRepository;

    @Test
    @DisplayName("개인정보 제공 동의 repository 테스트")
    void updateMemberPrivacyTest(){
        //given
        MemberDTO member = new MemberDTO();
        member.setMemberCode(1);
        member.setPrivacyStatus('Y');
        member.setResidentNum("1234561234567");

        //when
        Member memberPrivacy = privacyRepository.findById(member.getMemberCode()).get();
        memberPrivacy = memberPrivacy.toBuilder()
                .privacyStatus(member.getPrivacyStatus())
                .residentNum(member.getResidentNum())
                .build();
        privacyRepository.save(memberPrivacy);

        //then
        Assertions.assertNotNull(memberPrivacy);
    }

    @Test
    @DisplayName("기부처 기존 정보 가져오기 repository 테스트")
    void selectOrgInformationTest() {
        //given
        int memberCode = 9;

        //when
        List<Object[]> orgInfo = orgRepository.selectOrgInformation(memberCode);
        List<Map<String, Object>> orgInformation = new ArrayList<>();
        for(Object[] info : orgInfo){
            Map<String, Object> orgInformationMap = new HashMap<>();
            orgInformationMap.put("orgEmail", info[0]);
            orgInformationMap.put("password", info[1]);
            orgInformationMap.put("orgName", info[2]);
            orgInformationMap.put("phone", info[3]);
            orgInformationMap.put("description", info[4]);
            orgInformation.add(orgInformationMap);
        }

        //then
        Assertions.assertNotNull(orgInformation);
        System.out.println("기부처 정보 확인해라 ~ : " + orgInformation);
    }

    @Test
    @DisplayName("비밀번호 확인하기 repository 테스트")
    void verifyPasswordTest() {
        //given
        int memberCode = 9;
        String password = "a123456!";

        //when
        metaint.replanet.rest.pay.entity.Member member = orgMemberRepository.findById((long) memberCode).get();
        boolean verify = passwordEncoder.matches(password, member.getPassword());

        //then
        Assertions.assertNotNull(verify);
        System.out.println("비밀번호가 맞고 틀리고의 결과가 나오나요? " + verify);
    }

    @Test
    @DisplayName("org 프로필 업데이트 repository 테스트")
    void updateOrgProfileTest(){
        //given
        int memberCode = 9;

        //when
        Optional<Organization> organizationOptional = orgRepository.findById(memberCode);

        organizationOptional.ifPresent(organizationM -> {
//            /*이미지 변경이 포함될 때*/
//            organizationM = organizationM.toBuilder().fileOriginName("fileOriginName")
//                    .fileExtension("fileExtension")
//                    .fileSaveName("fileSaveName")
//                    .fileSavePath("FILE_DIR")
//                    .orgDescription("orgDescription")
//                    .build();
            /*이미지 변경이 없을 때*/
             organizationM = organizationM.toBuilder()
                    .orgDescription("orgDescription")
                    .build();

            orgRepository.save(organizationM);

        //then
        Assertions.assertEquals("orgDescription", organizationM.getOrgDescription());
        });
    }

    @Test
    @DisplayName("org 정보 업데이트 repository 테스트")
    void updateOrgInfoTest(){
        //given
        int memberCode = 9;

        //when
        Optional<metaint.replanet.rest.pay.entity.Member> memberOptional = orgMemberRepository.findById((long) memberCode);

        memberOptional.ifPresent(memberM -> {
            memberM = memberM.toBuilder().memberEmail("memberEmail")
                    .password(passwordEncoder.encode("password"))
                    .memberName("memberName")
                    .phone("phone")
                    .build();

            orgMemberRepository.save(memberM);

        //then
        Assertions.assertEquals("memberName", memberM.getMemberName());
        });
    }

    @Test
    @DisplayName("org 탈퇴 신청 repository 테스트")
    void updateOrgWithdrawTest(){
        //given
        int orgCode = 9;

        //when
        Optional<Organization> organizationOptional = orgRepository.findById(orgCode);

        organizationOptional.ifPresent(organization -> {
            organization = organization.toBuilder()
                    .withdrawReqDate(new Date())
                    .withdrawReason("withdrawReason")
                    .build();
            orgRepository.save(organization);

        //then
        Assertions.assertEquals("withdrawReason", organization.getWithdrawReason());
        });
    }

    @Test
    @DisplayName("user 탈퇴 요청 repository 테스트")
    void deleteUserTest(){
        //given
        int memberCode = 1;

        //when
        Member memberW = privacyRepository.findById(memberCode).get();

        if(memberW != null){
            List<Donation> donations = donationRepository.findByMemberCode(memberCode);
            if(donations.size() > 0){
                memberW = memberW.toBuilder()
                        .memberEmail("withdrawal")
                        .password("withdrawal")
                        .phone("withdrawal")
                        .currentPoint(0)
                        .privacyStatus('N')
                        .residentNum("withdrawal")
                        .provider("withdrawal")
                        .providerId("withdrawal")
                        .withdraw('Y')
                        .withdrawDate(new Date())
                        .build();
                privacyRepository.save(memberW);
            } else {
                memberW = memberW.toBuilder()
                        .withdraw('Y')
                        .withdrawDate(new Date())
                        .build();
                privacyRepository.save(memberW);
            }
        }

        //then
        Assertions.assertEquals('Y', memberW.getWithdraw());
    }

    @Test
    @DisplayName("user 정보 수정 service 테스트")
    void modifyUserTest(){
        //given
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberCode(1);
        memberDTO.setPassword("password");
        memberDTO.setPhone("phone");
        memberDTO.setMemberName("memberName");

        //when
        Optional<Member> memberOptional = privacyRepository.findById(memberDTO.getMemberCode());
        memberOptional.ifPresent(member -> {
            member = member.toBuilder()
                    .password(memberDTO.getPassword())
                    .memberName(memberDTO.getMemberName())
                    .phone(memberDTO.getPhone())
                    .build();
            privacyRepository.save(member);

        //then
        Assertions.assertEquals(memberDTO.getPassword(), member.getPassword());
        });
    }
}
