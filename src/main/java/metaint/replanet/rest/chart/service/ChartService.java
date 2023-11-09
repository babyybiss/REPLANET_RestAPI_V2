package metaint.replanet.rest.chart.service;


import metaint.replanet.rest.chart.dto.CampaignDescriptionDTO;
import metaint.replanet.rest.chart.dto.CountByCategoryDTO;
import metaint.replanet.rest.chart.entity.CampaignDescription;
import metaint.replanet.rest.chart.repository.ChartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChartService {
    private final ModelMapper modelMapper;
    private final ChartRepository chartRepository;

    @Autowired
    public ChartService(ModelMapper modelMapper, ChartRepository chartRepository) {
        this.modelMapper = modelMapper;
        this.chartRepository = chartRepository;
    }

    // 캠페인 수 카운트 조회
    public int CountCampaign() {

        int countResult = (int) chartRepository.count();

        return countResult;
    }

    // 캠페인 전체 조회
    public List<CampaignDescriptionDTO> findCampaignList() {

        List<CampaignDescription> campaignList = chartRepository.findAll();

        return campaignList.stream()
                .map(campaign -> modelMapper.map(campaign, CampaignDescriptionDTO.class))
                .collect(Collectors.toList());
    }

    // 카테고리별 캠페인 수 카운트 조회
    public List<CountByCategoryDTO> CountCampaignByCampaignCategory() {

        List<Object[]> resultList = chartRepository.countByCategory();

        resultList.forEach( row -> {
            for(Object item : row) {
                System.out.print(item);
            }
            System.out.println();
        });

        return resultList.stream()
                .map( row -> {
                    String campaignCategory = (String) row[0];
                    int campaigns = ((Number) row[1]).intValue();

                    CountByCategoryDTO dto = new CountByCategoryDTO();
                    dto.setCampaignCategory(campaignCategory);
                    dto.setCampaigns(campaigns);

                    return dto;
                })
                .collect(Collectors.toList());
    }







}
