package metaint.replanet.rest.point.service;

import metaint.replanet.rest.auth.entity.Member;
import metaint.replanet.rest.auth.repository.MemberRepository;
import metaint.replanet.rest.point.dto.ExchangeDTO;
import metaint.replanet.rest.point.dto.MemberDTO;
import metaint.replanet.rest.point.dto.PointFileDTO;
import metaint.replanet.rest.point.entity.Exchange;
import metaint.replanet.rest.point.entity.PointFile;
import metaint.replanet.rest.point.repository.ExchangeRepository;
import metaint.replanet.rest.point.repository.PointFileRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;

@SpringBootTest
public class pointServiceTest {

    @Autowired
    ExchangeService exchangeService;

    private final ModelMapper modelMapper;

    @Autowired
    public pointServiceTest(ModelMapper modelMapper) {
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
        int savedExchangeCode = exchangeService.insertExchange(exchange);

        PointFileDTO newFile = new PointFileDTO();
        newFile.setFileOriginName("파일 정보 저장 테스트~");
        newFile.setFileSaveName("바뀐 이름~");
        newFile.setFilePath("저장 경로~");
        newFile.setFileExtension("확장자~");
        newFile.setApplicationCode(savedExchangeCode);
        int savedFileApplicationCode = exchangeService.insertPointFile(newFile);

        //then
        Assertions.assertNotEquals(savedExchangeCode, 0);
        Assertions.assertNotEquals(savedFileApplicationCode, 0);
        Assertions.assertEquals(savedExchangeCode, savedFileApplicationCode);
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
        exchangeService.insertExchange(exchange1);
        exchangeService.insertExchange(exchange2);
        List<Exchange> allExchanges = exchangeService.selectAllExchanges();

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
        exchangeService.insertExchange(exchange1);
        exchangeService.insertExchange(exchange2);
        List<Exchange> memberAllExchange =  exchangeService.selectMemberAllExchange(77);

        //then
        Assertions.assertNotNull(memberAllExchange);
    }


}
