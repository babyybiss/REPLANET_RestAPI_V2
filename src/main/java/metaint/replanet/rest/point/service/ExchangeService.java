package metaint.replanet.rest.point.service;

import metaint.replanet.rest.point.dto.ExchangeDTO;
import metaint.replanet.rest.point.dto.PointFileDTO;
import metaint.replanet.rest.point.entity.Exchange;
import metaint.replanet.rest.point.entity.PointFile;
import metaint.replanet.rest.point.repository.ExchangeRepository;
import metaint.replanet.rest.point.repository.PointFileRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class ExchangeService {

    private final ExchangeRepository exchangeRepository;

    private final PointFileRepository pointFileRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public ExchangeService(ExchangeRepository exchangeRepository, PointFileRepository pointFileRepository, ModelMapper modelMapper) {
        this.exchangeRepository = exchangeRepository;
        this.pointFileRepository = pointFileRepository;
        this.modelMapper = modelMapper;
    }

    public int insertExchange(ExchangeDTO newExchange) {
        Exchange savedExchange = exchangeRepository.save(modelMapper.map(newExchange, Exchange.class));
        int exchangeCode = savedExchange.getExchangeCode();

        return exchangeCode;
    }

    public int insertPointFile(PointFileDTO newFile) {
        PointFile savedFile = pointFileRepository.save(modelMapper.map(newFile, PointFile.class));
        int applicationCode = savedFile.getApplicationCode();

        //실제로 쓰이지는 않지만 테스트코드를 위해 반환
        return applicationCode;
    }

    public List<Exchange> selectAllExchanges() {
        List<Exchange> allExchanges = exchangeRepository.findAll();

        return allExchanges;
    }

    public List<Exchange> selectMemberAllExchange(int memberCode) {
        List<Exchange> memberAllExchange = exchangeRepository.findByMemberCode(memberCode);

        return memberAllExchange;
    }
}
