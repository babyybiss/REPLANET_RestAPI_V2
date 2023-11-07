package metaint.replanet.rest.pay.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import metaint.replanet.rest.pay.dto.CampaignDescriptionDTO;
import metaint.replanet.rest.pay.dto.DonationDTO;
import metaint.replanet.rest.pay.dto.MemberDTO;
import metaint.replanet.rest.pay.dto.pay.DonationAmountDTO;
import metaint.replanet.rest.pay.dto.pay.KakaoPayApprovalVO;
import metaint.replanet.rest.pay.dto.pay.KakaoPayReadyVO;
import metaint.replanet.rest.pay.entity.CampaignDescription;
import metaint.replanet.rest.pay.entity.Donation;
import metaint.replanet.rest.pay.entity.Member;
import metaint.replanet.rest.pay.entity.Pay;
import metaint.replanet.rest.pay.repository.DonationRepository;
import metaint.replanet.rest.pay.repository.PayRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;


@Service
@Slf4j
public class KakaoPay {

    private final DonationRepository donationRepository;
    private final PayRepository payRepository;
    private final ModelMapper modelMapper;

    public KakaoPay(DonationRepository donationRepository, PayRepository payRepository, ModelMapper modelMapper) {
        this.donationRepository = donationRepository;
        this.payRepository = payRepository;
        this.modelMapper = modelMapper;
    }
    private static final String HOST = "https://kapi.kakao.com";

    private KakaoPayReadyVO kakaoPayReadyVO;
    private KakaoPayApprovalVO kakaoPayApprovalVO;


    public String kakaoPayReady(DonationAmountDTO amount) {
        log.info("[kakaoPayReady()] ---------------------------------------- ");
        log.info("[kakaoPayReady() amount.getPointAmount()] : " + amount.getPointAmount());

        RestTemplate restTemplate = new RestTemplate();

        //임의 값 생성
        MemberDTO member = new MemberDTO();
        member.setMemberId("user01");
        CampaignDescriptionDTO campaign = new CampaignDescriptionDTO();
        campaign.setCampaignCode(1);
        campaign.setCampaignTitle("테스트테스트");

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "77683905e97c497707a05671168be68f");
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");

        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME"); // 가맹점코드 테스트에선 고정
        params.add("partner_order_id", String.valueOf(campaign.getCampaignCode())); // orderId
        params.add("partner_user_id", member.getMemberId()); // userId
        params.add("item_name", campaign.getCampaignTitle()); // 상품명
        params.add("quantity", "1"); // 수량
        params.add("total_amount", String.valueOf(amount.getFinalAmount())); // 총액
        params.add("tax_free_amount", "100"); // 상품 비과세 금액
        params.add("approval_url", "http://localhost:8001/kakaoPaySuccess?pointAmount=" + amount.getPointAmount());
        params.add("cancel_url", "http://localhost:8001/kakaoPayCancle");
        params.add("fail_url", "http://localhost:8001/kakaoPaySuccessFail");
        params.add("pointAmount", String.valueOf(amount.getPointAmount()));
        // 성공, 취소, 실패 페이지 만들어야함!

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);

        try {
            kakaoPayReadyVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/ready"), body, KakaoPayReadyVO.class);

            log.info("[kakaoPayReady() kakaoPayReadyVO] : " + kakaoPayReadyVO);

            return kakaoPayReadyVO.getNext_redirect_pc_url();
            // 기부하기 누르는 순간 얘들은 이미 다실행되고 카카오페이 결제페이지임!
            // 걍 다른 카카오페이API 영역에서 놀고있는거임

        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "/donations";
    }

    public KakaoPayApprovalVO kakaoPayInfo(String pg_token, String pointAmount) {

        log.info("[kakaoPayInfo()]............................................");

        RestTemplate restTemplate = new RestTemplate();

        // 서버로 요청할 Header
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + "77683905e97c497707a05671168be68f");
        headers.add("Accept", MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");

        //임의 값 생성
        MemberDTO member = new MemberDTO();
        member.setMemberId("user01");
        CampaignDescriptionDTO campaign = new CampaignDescriptionDTO();
        campaign.setCampaignCode(1);
        campaign.setCampaignTitle("테스트테스트");
        // 회원정보랑 캠페인정보 자리잡히면 이거 바꿔야함! 까먹으면 안됨!

        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME"); // 가맹점 코드 -> 테스트용 고정!
        params.add("tid", kakaoPayReadyVO.getTid()); // 결제 고유번호 -> 결제 준비 API 응답에 포함돼서 온거임 -> VO를 통해서 받아옴
        params.add("partner_order_id", String.valueOf(campaign.getCampaignCode())); // order_id 이건 어디서 들고와야하지?? -> 캠페인번호!! -> 뭐 dto나 엔티티 통해서 들고오던가 해야지 -> 현재 선택한 캠페인
        params.add("partner_user_id", member.getMemberId()); // 얘도 동일하게 -> 뭐 dto나 엔티티 통해서 들고오던가 해야지 -> 현재 로그인한 사용자!
        params.add("pg_token", pg_token);

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers);

        try {
            kakaoPayApprovalVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/approve"), body, KakaoPayApprovalVO.class);
            log.info("" + kakaoPayApprovalVO);

            if (kakaoPayApprovalVO != null && kakaoPayApprovalVO.getApproved_at() != null) {
                // 필요한 정보 추출
                String paymentCode = kakaoPayApprovalVO.getTid();
                Date date = kakaoPayApprovalVO.getApproved_at();
                Instant instant = date.toInstant();
                ZoneId zoneId = ZoneId.systemDefault(); // 또는 다른 시간대로 설정
                LocalDateTime donationDateTime = instant.atZone(zoneId).toLocalDateTime();

                Member refMember = Member.builder()
                        .memberCode(1).build();
                CampaignDescription refCampaign = CampaignDescription.builder()
                        .campaignCode(1).build();
                // 회원정보랑 캠페인정보 자리잡히면 이거 바꿔야함! 까먹으면 안됨!

                // Donation 엔티티 생성
                Donation newDonation = Donation.builder()
                                                .donationDateTime(donationDateTime)
                                                .donationPoint(Integer.parseInt(pointAmount))
                                                .refMember(refMember)  // Member 엔티티 객체
                                                .refCampaign(refCampaign)  // CampaignDescription 엔티티 객체
                                                .build();

                // builder() 통해서 값 넣기
                Pay newPay = Pay.builder()
                                .payAmount(kakaoPayApprovalVO.getAmount().getTotal())
                                .payTid(kakaoPayApprovalVO.getTid())
                                .refDonation(newDonation)
                                .build();

                // Donation 엔티티 저장
                donationRepository.save(newDonation);
                payRepository.save(newPay);
            }

            return kakaoPayApprovalVO;

        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public List<Donation> getDonation() {
        log.info("[getDonation()] -----------------------------------");

        List<Donation> donationList = donationRepository.findAll();

        log.info("[getDonation() donationList] : " + donationList);

        return donationList;
    }

    public Pay getPayByTid(String payTid) {
        log.info("[getPayByTid(String payTid)] -----------------------------------");

        Pay pay = payRepository.findByPayTid(payTid);

        if (pay != null) {
            return pay;
        } else {
            log.error("payTid에 해당하는 Pay를 찾을수없으요 payTid : " + payTid);
            throw new EntityNotFoundException("payTid에 해당하는 Pay를 찾을수없으요 payTid : " + payTid);
        }
    }
}
