package metaint.replanet.rest.privacy.service;

import metaint.replanet.rest.privacy.dto.MemberDTO;
import metaint.replanet.rest.privacy.entity.Donation;
import metaint.replanet.rest.privacy.entity.Member;
import metaint.replanet.rest.privacy.repository.PrivacyRepository;
import metaint.replanet.rest.privacy.repository.UserDonationRepository;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PrivacyService {

    private StringEncryptor stringEncryptor;
    private final PrivacyRepository privacyRepository;
    private final UserDonationRepository donationRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PrivacyService(PrivacyRepository privacyRepository, StringEncryptor stringEncryptor, UserDonationRepository donationRepository, PasswordEncoder passwordEncoder){
        this.privacyRepository = privacyRepository;
        this.stringEncryptor = stringEncryptor;
        this.donationRepository = donationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String encryptResidentNum(String residentNumber){
        return stringEncryptor.encrypt(residentNumber);
    }

    public String descryptResidentNum(String encryptedResidentNumber){
        return stringEncryptor.decrypt(encryptedResidentNumber);
    }

    public int updateMemberPrivacy(MemberDTO memberDTO) {
        System.out.println("service로 오는 것 확인");
        Member updatePrivacy = privacyRepository.findById(memberDTO.getMemberCode()).get();
        System.out.println("updatePrivacy 확인 : " + updatePrivacy);

        if(updatePrivacy != null){
            updatePrivacy = updatePrivacy.toBuilder()
                    .privacyStatus(memberDTO.getPrivacyStatus())
                    .residentNum(encryptResidentNum(memberDTO.getResidentNum()))
                    .build();
            privacyRepository.save(updatePrivacy);

            System.out.println("동의 여부 확인 : " + updatePrivacy.getPrivacyStatus());
            System.out.println("암호화된 주민번호 확인 : " + updatePrivacy.getResidentNum());
            System.out.println("복호화된 주민번호 확인 : " + descryptResidentNum(updatePrivacy.getResidentNum()));

            return 1;
        }
        return 0;
    }

    public int withdrawMemberPrivacy(MemberDTO memberDTO) {
        System.out.println("service로 오는 것 확인");
        Member withdrawPrivacy = privacyRepository.findById(memberDTO.getMemberCode()).get();
        System.out.println("updatePrivacy 확인 : " + withdrawPrivacy);

        if(withdrawPrivacy != null){
            withdrawPrivacy = withdrawPrivacy.toBuilder()
                    .privacyStatus(memberDTO.getPrivacyStatus())
                    .residentNum(null)
                    .build();
            privacyRepository.save(withdrawPrivacy);

            System.out.println("동의 여부 확인 : " + withdrawPrivacy.getPrivacyStatus());
            System.out.println("주민번호 초기화 확인 : " + withdrawPrivacy.getResidentNum());

            return 1;
        }
        return 0;
    }

    public char selectPrivacyStatus(int memberCode) {
        System.out.println("service로 오는 것 확인");
        Member memberStatus = privacyRepository.findById(memberCode).get();
        System.out.println("memberStatus 확인 : " + memberStatus);
        return memberStatus.getPrivacyStatus();
    }

    public int deleteUser(int memberCode) {
        System.out.println("PrivacyService deleteUser start================");
        Member memberW = privacyRepository.findById(memberCode).get();
        if(memberW != null){
            List<Donation> donations = donationRepository.findByMemberCode(memberCode);
            System.out.println("회원 탈퇴 시 기부 내역 있는지 확인: " + donations);
            if(donations.size() > 0){
                memberW = memberW.toBuilder()
                        .memberEmail("withdrawal")
                        .provider("withdrawal")
                        .providerId("withdrawal")
                        .currentPoint(0)
                        .withdraw("Y")
                        .withdrawDate(new Date())
                        .build();
                try{
                    privacyRepository.save(memberW);
                    return 2;
                } catch (Exception e){
                    e.printStackTrace();
                    return 1;
                }
            } else {
                memberW = memberW.toBuilder()
                        .memberEmail("withdrawal")
                        .password("withdrawal")
                        .phone("withdrawal")
                        .joinDate(null)
                        .currentPoint(0)
                        .privacyStatus('N')
                        .residentNum(null)
                        .provider("withdrawal")
                        .providerId("withdrawal")
                        .withdraw("Y")
                        .withdrawDate(new Date())
                        .build();
                try{
                    privacyRepository.save(memberW);
                    return 2;
                } catch (Exception e){
                    e.printStackTrace();
                    return 1;
                }
            }
        }
        return 0;
    }

    public int verifyPassword(int memberCode, String password) {
        System.out.println("PrivacyService verifyPassword start================");
        Member member = privacyRepository.findById(memberCode).get();
        if(passwordEncoder.matches(password, member.getPassword())){
            return 1;
        } else {
            return 0;
        }
    }

    public void modifyUser(MemberDTO memberDTO) {
        System.out.println("PrivacyService modifyUser start================");
        Member member = privacyRepository.findById(memberDTO.getMemberCode()).get();
        member= member.toBuilder()
                .password(passwordEncoder.encode(memberDTO.getPassword()))
                .memberName(memberDTO.getMemberName())
                .phone(memberDTO.getPhone())
                .build();
        privacyRepository.save(member);
    }
}
