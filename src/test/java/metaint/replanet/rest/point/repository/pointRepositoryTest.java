package metaint.replanet.rest.point.repository;

import metaint.replanet.rest.auth.entity.Member;
import metaint.replanet.rest.auth.repository.MemberRepository;
import metaint.replanet.rest.point.dto.ExchangeDTO;
import metaint.replanet.rest.point.dto.MemberDTO;
import metaint.replanet.rest.point.dto.PointFileDTO;
import metaint.replanet.rest.point.entity.Exchange;
import metaint.replanet.rest.point.entity.PointFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class pointRepositoryTest {

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private PointFileRepository pointFileRepository;

    @Autowired
    private MemberRepository memberRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private final ModelMapper modelMapper;

    @Autowired
    public pointRepositoryTest(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Test
    @DisplayName("포인트 전환 신청 정보 저장 테스트")
    void testInsertExchnage(){
        //given
        ExchangeDTO exchange = new ExchangeDTO();
        exchange.setExchangeDate(new Date());
        exchange.setTitle("신청 정보 저장 테스트~");
        exchange.setMemberCode(88);

        //when
        Exchange savedExchange = exchangeRepository.save(modelMapper.map(exchange, Exchange.class));

        PointFileDTO newFile = new PointFileDTO();
        newFile.setFileOriginName("파일 정보 저장 테스트~");
        newFile.setFileSaveName("바뀐 이름~");
        newFile.setFilePath("저장 경로~");
        newFile.setFileExtension("확장자~");
        newFile.setApplicationCode(savedExchange.getExchangeCode());

        PointFile savedFile = pointFileRepository.save(modelMapper.map(newFile, PointFile.class));

        //then
        Assertions.assertNotEquals(savedExchange.getExchangeCode(), 0);
        Assertions.assertEquals(exchange.getMemberCode(), savedExchange.getMemberCode());

        Assertions.assertNotEquals(savedFile.getApplicationCode(), 0);
        Assertions.assertEquals(newFile.getFileOriginName(), savedFile.getFileOriginName());
        Assertions.assertEquals(savedExchange.getExchangeCode(), savedFile.getApplicationCode());
    }

    @Test
    @DisplayName("포인트 전환 신청 목록 전체 조회 테스트")
    void testSelectAllExchanges(){
        //given
        ExchangeDTO exchange1 = new ExchangeDTO();
        exchange1.setExchangeDate(new Date());
        exchange1.setTitle("전체 목록 조회 테스트1~");
        exchange1.setMemberCode(88);

        ExchangeDTO exchange2 = new ExchangeDTO();
        exchange2.setExchangeDate(new Date());
        exchange2.setTitle("전체 목록 조회 테스트2~");
        exchange2.setMemberCode(88);

        //when
        exchangeRepository.save(modelMapper.map(exchange1, Exchange.class));
        exchangeRepository.save(modelMapper.map(exchange2, Exchange.class));

        List<Exchange> allExchanges = exchangeRepository.findAll();

        //then
        Assertions.assertNotNull(allExchanges);
    }

    @Test
    @DisplayName("특정 회원의 전환 신청 목록 전체 조회 테스트")
    void testSelectMemberAllExchange(){
        //given
        ExchangeDTO exchange1 = new ExchangeDTO();
        exchange1.setExchangeDate(new Date());
        exchange1.setTitle("회원 신청 목록 테스트1~");
        exchange1.setMemberCode(77);

        ExchangeDTO exchange2 = new ExchangeDTO();
        exchange2.setExchangeDate(new Date());
        exchange2.setTitle("회원 신청 목록 테스트2~");
        exchange2.setMemberCode(88);

        //when
        Exchange savedExchange1 = exchangeRepository.save(modelMapper.map(exchange1, Exchange.class));
        exchangeRepository.save(modelMapper.map(exchange2, Exchange.class));

        List<Exchange> memberAllExchanges = exchangeRepository.findByMemberCode(savedExchange1.getMemberCode());

        //then
        Assertions.assertNotNull(memberAllExchanges);
    }

    @Test
    @DisplayName("포인트 전환 신청 목록 조건 조회 테스트")
    void testSelectExchangeByStatus(){
        //given
        ExchangeDTO exchange1 = new ExchangeDTO();
        exchange1.setExchangeDate(new Date());
        exchange1.setTitle("신청 목록 조건 조회 테스트1~");
        exchange1.setStatus("대기");
        exchange1.setMemberCode(77);

        ExchangeDTO exchange2 = new ExchangeDTO();
        exchange2.setExchangeDate(new Date());
        exchange2.setTitle("신청 목록 조건 조회 테스트2~");
        exchange2.setStatus("승인");
        exchange2.setMemberCode(88);

        ExchangeDTO exchange3 = new ExchangeDTO();
        exchange3.setExchangeDate(new Date());
        exchange3.setTitle("신청 목록 조건 조회 테스트3~");
        exchange3.setStatus("반려");
        exchange3.setMemberCode(99);

        //when
        exchangeRepository.save(modelMapper.map(exchange1, Exchange.class));
        exchangeRepository.save(modelMapper.map(exchange2, Exchange.class));
        exchangeRepository.save(modelMapper.map(exchange3, Exchange.class));

        List<Exchange> exchangeListByStatus = exchangeRepository.findByStatus("대기");

        //then
        Assertions.assertNotNull(exchangeListByStatus);
    }

    @Test
    @DisplayName("전환 신청 상세 정보 조회 테스트")
    void testSelectExchangeDetail(){
        //given
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldMatchingEnabled(true);

        MemberDTO member = new MemberDTO();
        member.setMemberId("testuser01");
        member.setMemberName("유유정");
        member.setPassword("010-0000-0000");
        member.setJoinDate(new Date());
        Member theMember = modelMapper.map(member, Member.class);
        memberRepository.save(theMember);

        ExchangeDTO exchange = new ExchangeDTO();
        exchange.setExchangeDate(new Date());
        exchange.setTitle("신청 정보 저장 테스트~");
        exchange.setMemberCode((theMember.getId()).intValue());
        Exchange savedExchange = exchangeRepository.save(modelMapper.map(exchange, Exchange.class));

        PointFileDTO newFile = new PointFileDTO();
        newFile.setFileOriginName("파일 정보 저장 테스트~");
        newFile.setFileSaveName("바뀐 이름~");
        newFile.setFilePath("저장 경로~");
        newFile.setFileExtension("확장자~");
        newFile.setApplicationCode(savedExchange.getExchangeCode());
        pointFileRepository.save(modelMapper.map(newFile, PointFile.class));

        //when
        String sql = "SELECT e.title, e.exchange_date, e.status, e.points, e.return_detail, f.file_path, m.member_id, m.member_name " +
                "FROM tbl_point_exchange e " +
                "JOIN tbl_point_file f ON e.exchange_code = f.application_code " +
                "JOIN tbl_member m ON e.member_code = m.member_code " +
                "WHERE e.exchange_code = ?1";

        Object detailResult = entityManager.createNativeQuery(sql)
                .setParameter(1, savedExchange.getExchangeCode())
                .getSingleResult();

        //then
        Assertions.assertNotNull(detailResult);
        System.out.println("상세조회결과 : " + detailResult);
    }


}
