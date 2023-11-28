package metaint.replanet.rest.org.service;

import metaint.replanet.rest.auth.entity.Member;
import metaint.replanet.rest.auth.repository.MemberRepository;
import metaint.replanet.rest.auth.util.SecurityUtil;
import metaint.replanet.rest.org.dto.OrgRequestDTO;
import metaint.replanet.rest.org.entity.Organization;
import metaint.replanet.rest.org.repository.OrgRepository;
import metaint.replanet.rest.privacy.dto.MemberDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrgService {

    private final MemberRepository memberRepository;
    private OrgRepository orgRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public OrgService(OrgRepository orgRepository, ModelMapper modelMapper,PasswordEncoder passwordEncoder, MemberRepository memberRepository){
        this.orgRepository = orgRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
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
            orgInformation.add(orgInformationMap);
        }

        return orgInformation;
    }

    public int verifyPassword(int memberCode, String orgPwd) {
        System.out.println("service 왔습니다~ 비밀번호 맞는지 확인합시다~");

        Member member = memberRepository.findById(Long.valueOf(memberCode)).get();
        if(passwordEncoder.matches(orgPwd, member.getPassword())){
            return 1;
        } else {
            return 0;
        }
    }

    public void updateOrgInformation(MemberDTO memberDTO, String orgDescription) {
        System.out.println("service 왔습니다~ 재단 정보 수정합시다~");

        Member memberM = memberRepository.findById((long) memberDTO.getMemberCode()).get();
        metaint.replanet.rest.privacy.entity.Organization organizationM = orgRepository.findById(memberDTO.getMemberCode()).get();

        if(organizationM != null && memberM != null){
            organizationM = organizationM.orgDescription(orgDescription).builder();
            orgRepository.save(organizationM);
            System.out.println("org 확인 : " + organizationM);

        }
         return;
    }
}
