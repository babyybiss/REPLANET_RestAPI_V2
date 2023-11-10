package metaint.replanet.rest.point.repository;

import metaint.replanet.rest.auth.repository.MemberRepository;
import metaint.replanet.rest.point.dto.ExchangeDTO;
import metaint.replanet.rest.point.dto.MemberDTO;
import metaint.replanet.rest.point.dto.PointFileDTO;
import metaint.replanet.rest.point.entity.Exchange;
import metaint.replanet.rest.point.entity.Member;
import metaint.replanet.rest.point.entity.PointFile;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.PropertyNameInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@SpringBootTest
public class pointRepositoryTest {

    @Autowired
    private ExchangeRepository exchangeRepository;

    @Autowired
    private PointFileRepository pointFileRepository;

    @Autowired
    private MemberPointRepository memberRepository;

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

        //when
        List<Exchange> allExchanges = exchangeRepository.findAll();

        //then
        Assertions.assertNotNull(allExchanges);
        System.out.println("전체 신청 목록 결과 : " + allExchanges);
    }

    @Test
    @DisplayName("특정 회원의 전환 신청 목록 전체 조회 테스트")
    void testSelectMemberAllExchange(){
        //given
        int memberCode = 1;

        //when
        String sql = "SELECT e.exchange_code, e.exchange_date, e.title, e.status, f.file_path " +
                "FROM tbl_point_exchange e " +
                "JOIN tbl_point_file f ON e.exchange_code = f.application_code " +
                "WHERE e.member_code = ?1";

        List<Object[]> memberExchanges = entityManager.createNativeQuery(sql)
                .setParameter(1, memberCode)
                .getResultList();
        List<Map<String, Object>> memberExchangeList = new ArrayList<>();

        String[] columnNames = {
                "exchange_code", "exchange_date", "title", "status", "file_path"
        };

        for(Object[] exchangeList : memberExchanges){
            Map<String, Object> exchangeListMap = new HashMap<>();
            for(int i = 0; i<columnNames.length; i++){
                exchangeListMap.put(columnNames[i], exchangeList[i]);
            }
            memberExchangeList.add(exchangeListMap);
        }

        //then
        Assertions.assertNotNull(memberExchangeList);
        System.out.print("회원의 조회 결과 : ");
        for(Map<String, Object> list : memberExchangeList) {
            System.out.println(list);
        }
    }

    @Test
    @DisplayName("포인트 전환 신청 목록 조건 조회 테스트")
    void testSelectExchangeByStatus(){
        //given
        String status = "대기";

        //when
        List<Exchange> exchangeListByStatus = exchangeRepository.findByStatus(status);

        //then
        Assertions.assertNotNull(exchangeListByStatus);
        System.out.println("조건 조회 결과 : " + exchangeListByStatus);
    }

    @Test
    @DisplayName("전환 신청 상세 정보 조회 테스트")
    void testSelectExchangeDetail(){
        //given
        int exchangeCode = 8;

        //when
        String sql = "SELECT e.title, e.exchange_date, e.status, e.points, e.return_detail, f.file_path, m.member_id, m.member_name " +
                "FROM tbl_point_exchange e " +
                "JOIN tbl_point_file f ON e.exchange_code = f.application_code " +
                "JOIN tbl_member m ON e.member_code = m.member_code " +
                "WHERE e.exchange_code = ?1";

        Object[] detailResult = (Object[]) entityManager.createNativeQuery(sql)
                .setParameter(1, exchangeCode)
                .getSingleResult();

        String[] columNames = {
                "title", "exchange_date", "status", "points", "return_detail", "file_path", "member_id", "member_name"
        };

        Map<String, Object> detailResultMap = new HashMap<>();
        for(int i = 0; i<columNames.length; i++){
            detailResultMap.put(columNames[i], detailResult[i]);
        }

        //then
        Assertions.assertNotNull(detailResultMap);
        System.out.println("상세조회결과 : " + detailResultMap);
    }

    @Test
    @DisplayName("포인트 전환 신청 승인 테스트")
    void testExchangeApproval(){
        //given
        int exchangeCode = 25;
        int points = 10000;
        String status = "승인";
        // 실제 코드에서는 ExchangeDTO로 파라미터를 받아와서 build

        //when
        Exchange approveExchange = exchangeRepository.findById(exchangeCode).get();
        approveExchange = approveExchange.points(points)
                .status(status)
                .builder();
        exchangeRepository.save(approveExchange);

        Member memberPoint = memberRepository.findById(approveExchange.getMemberCode()).get();
        System.out.println("적립 전 보유 포인트 : " + memberPoint.getCurrentPoint());
        memberPoint = memberPoint.currentPoint(memberPoint.getCurrentPoint() + approveExchange.getPoints())
                .builder();
        memberRepository.save(memberPoint);

        //then
        Assertions.assertNotNull(approveExchange);
        System.out.println("신청 상태 확인 : " + approveExchange.getStatus());
        System.out.println("승인 포인트 확인 : " + approveExchange.getPoints());

        Assertions.assertNotNull(memberPoint);
        System.out.println("현재 포인트 확인 : " + memberPoint.getCurrentPoint());
    }

    @Test
    @DisplayName("포인트 전환 신청 반려 테스트")
    void testExchangeRejection(){
        //given
        int exchangeCode = 18;
        String returnDetail = "잘못된 파일";
        String status = "반려";
        // 실제 코드에서는 ExchangeDTO로 파라미터를 받아와서 build

        //when
        Exchange rejectExchange = exchangeRepository.findById(exchangeCode).get();
        rejectExchange = rejectExchange.returnDetail(returnDetail)
                .status(status)
                .builder();
        exchangeRepository.save(rejectExchange);

        //then
        Assertions.assertNotNull(rejectExchange);
        System.out.println("신청 상태 확인 : " + rejectExchange.getStatus());
        System.out.println("반려 사유 확인 : " + rejectExchange.getReturnDetail());
    }

    @Test
    @DisplayName("특정 회원 포인트 적립 / 사용 내역 조회")
    void testSelectMemberPoints(){
        //given

        //when


        //then
    }


}
