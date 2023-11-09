package metaint.replanet.rest.chart.service;


import metaint.replanet.rest.chart.dto.CampaignDescriptionDTO;
import metaint.replanet.rest.chart.dto.CountByCategoryDTO;
import metaint.replanet.rest.chart.dto.CurrentBudgetByCategoryDTO;
import metaint.replanet.rest.chart.entity.CampaignDescription;
import metaint.replanet.rest.chart.repository.ChartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChartService {
    private ModelMapper modelMapper;
    private ChartRepository chartRepository;


    @Autowired
    public ChartService(ModelMapper modelMapper, ChartRepository chartRepository) {
        this.modelMapper = modelMapper;
        this.chartRepository = chartRepository;
    }

    // 캠페인 수 카운트 조회
    public int countCampaign() {

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
    public List<CountByCategoryDTO> countCampaignByCampaignCategory() {

        List<Object[]> resultList = chartRepository.countByCategory();

        /*
        resultList.forEach( row -> {
            for(Object item : row) {
                System.out.print(item);
            }
            System.out.println();
        });
        */

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

    // 카테고리별 현재 모금액 합계 조회
    public List<CurrentBudgetByCategoryDTO> sumCurrentBudgetByCampaignCategory() {

        List<Object[]> resultList = chartRepository.sumCurrentBudgetByCategory();

        return resultList.stream()
                .map(row -> {
                    String campaignCategory = (String) row[0];
                    int currentBudget = ((Number) row[1]).intValue();

                    CurrentBudgetByCategoryDTO dto = new CurrentBudgetByCategoryDTO();
                    dto.setCampaignCategory(campaignCategory);
                    dto.setCurrentBudget(currentBudget);

                    return dto;
                })
                .collect(Collectors.toList());
    }


}
