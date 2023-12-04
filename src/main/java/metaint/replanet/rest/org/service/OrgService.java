package metaint.replanet.rest.org.service;


import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.auth.entity.MemberRole;

import metaint.replanet.rest.auth.repository.MemberRepository;

import metaint.replanet.rest.org.dto.OrgRequestDTO;
import metaint.replanet.rest.org.entity.Organization;
import metaint.replanet.rest.org.repository.OrgMemberRepository;
import metaint.replanet.rest.org.repository.OrgRepository;


import metaint.replanet.rest.pay.entity.Member;

import metaint.replanet.rest.privacy.dto.MemberDTO;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@Service
public class OrgService {


    private final OrgRepository orgRepository;

    private final OrgMemberRepository orgMemberRepository;




    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public OrgService(OrgRepository orgRepository, OrgMemberRepository orgMemberRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder){
        this.orgRepository = orgRepository;
        this.orgMemberRepository = orgMemberRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;

    }


    // 기부처 등록
    @Transactional
    public void orgRegist(OrgRequestDTO orgRequestDTO) {
        System.out.println(orgRequestDTO);

        // 권한 등록

        Organization org = modelMapper.map(orgRequestDTO, Organization.class);
        System.out.println(org);

        //orgRepository.save(org);


        //Organization org = orgRequestDTO.toOrganization(passwordEncoder);
        //return orgRepository.save(org);



    }


    public List<Map<String, Object>> getOrgList() {
        log.info("[getOrgList()] =============================================");
        log.info("service 왔습니다~ org 리스트 가져가세요~");

        MemberRole memberRole = MemberRole.ROLE_ORG;

        List<Object[]> allOrgs = orgMemberRepository.findAllByMemberRole(memberRole);
        List<Map<String, Object>> orgList = new ArrayList<>();

        for(Object[] orgs : allOrgs){
            Map<String, Object> orgListMap = new HashMap<>();
            orgListMap.put("memberCode", orgs[0]);
            orgListMap.put("memberEmail", orgs[1]);
            orgListMap.put("memberName", orgs[2]);
            orgListMap.put("joinDate", orgs[3]);
            orgListMap.put("phone", orgs[4]);
            orgListMap.put("withdrawDate", orgs[5]);
            orgListMap.put("wReqDate", orgs[6]);
            orgListMap.put("wReqReason", orgs[7]);

            orgList.add(orgListMap);
        }
        log.info("[getOrgList() orgList] : " + orgList);

        return orgList;
}

    public List<Map<String, Object>> selectOrgInformation(int memberCode) {
        System.out.println("service 왔습니다~ 재단 기존 정보 가져가세요~");

        List<Object[]> orgInfo = orgRepository.selectOrgInformation(memberCode);
        List<Map<String, Object>> orgInformation = new ArrayList<>();
        for(Object[] info : orgInfo){
            Map<String, Object> orgInformationMap = new HashMap<>();
            orgInformationMap.put("orgEmail", info[0]);
            orgInformationMap.put("orgName", info[1]);
            orgInformationMap.put("phone", info[2]);
            orgInformationMap.put("description", info[3]);
            orgInformationMap.put("fileName", info[4]);
            orgInformation.add(orgInformationMap);
        }

        return orgInformation;
    }

    public int verifyPassword(int memberCode, String orgPwd) {
        System.out.println("service 왔습니다~ 비밀번호 맞는지 확인합시다~");

        Member member = orgMemberRepository.findById((long) memberCode).get();

        if(passwordEncoder.matches(orgPwd, member.getPassword())){
            return 1;
        } else {
            return 0;
        }
    }

    public void updateMemberInfo(MemberDTO memberDTO) {
        System.out.println("service 왔습니다~ 재단 회원 정보 수정합시다~");

        Member memberM = orgMemberRepository.findById((long) memberDTO.getMemberCode()).get();
        memberM = memberM.toBuilder().password(passwordEncoder.encode(memberDTO.getPassword()))
                                    .memberName(memberDTO.getMemberName())
                                    .phone(memberDTO.getPhone())
                                    .build();
        orgMemberRepository.save(memberM);
        System.out.println("member 업데이트 확인 : " + memberM);
    }

    public void updateOrgInfo(OrgRequestDTO orgRequestDTO){
        System.out.println("service 왔습니다~ 재단 프로필 정보 수정합시다~");

        Organization organizationM = orgRepository.findById(orgRequestDTO.getOrgCode()).get();

        if(orgRequestDTO.getFileOriginName() != null){
            organizationM = organizationM.toBuilder().fileOriginName(orgRequestDTO.getFileOriginName())
                        .fileExtension(orgRequestDTO.getFileExtension())
                        .fileSaveName(orgRequestDTO.getFileSaveName())
                        .fileSavePath(orgRequestDTO.getFileSavePath())
                        .orgDescription(orgRequestDTO.getOrgDescription())
                        .build();
        } else {
            organizationM = organizationM.toBuilder()
                    .orgDescription(orgRequestDTO.getOrgDescription())
                    .build();
        }
        orgRepository.save(organizationM);
        System.out.println("org 업데이트 확인 : " + organizationM);
    }

    @Transactional
    public int deleteOrgByMemberCode(Long memberCode) {
        log.info("[deleteOrgByMemberCode()] ==================================== " + memberCode);
        log.info("[deleteOrgByMemberCode() memberCode] : " + memberCode);

        int result = orgMemberRepository.deleteOrgMemberByMemberCode(memberCode);
        log.info("[deleteOrgByMemberCode() result] : " + result);

        return result;
  }

    public void updateOrgWithdraw(OrgRequestDTO newRequest) {
        System.out.println("service 왔습니다~ 탈퇴 신청 정보 업데이트 합시다~");

        Organization organizationW = orgRepository.findById(newRequest.getOrgCode()).get();

        organizationW = organizationW.toBuilder()
                                    .withdrawReqDate(newRequest.getWithdrawReqDate())
                                    .withdrawReason(newRequest.getWithdrawReason())
                                    .build();

        orgRepository.save(organizationW);

        System.out.println("org 탈퇴 신청 확인 : " + organizationW);
    }
}
