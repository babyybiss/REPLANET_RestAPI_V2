package metaint.replanet.rest.org.service;

import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.auth.entity.MemberRole;
import metaint.replanet.rest.org.dto.OrgRequestDTO;
import metaint.replanet.rest.org.entity.Organization;
import metaint.replanet.rest.org.repository.OrgMemberRepository;
import metaint.replanet.rest.org.repository.OrgRepository;
import metaint.replanet.rest.pay.entity.Member;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        orgRepository.save(org);


        //Organization org = orgRequestDTO.toOrganization(passwordEncoder);
        //return orgRepository.save(org);



    }

    public List<Member> getOrgList() {
        log.info("[getOrgList()] =============================================");

        MemberRole memberRole = MemberRole.ROLE_ORG;

        List<Member> orgList = orgMemberRepository.findAllByMemberRole(memberRole);

        log.info("[getOrgList() orgList] : " + orgList);

        return orgList;
    }
}
