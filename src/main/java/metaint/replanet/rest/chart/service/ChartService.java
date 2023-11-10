package metaint.replanet.rest.chart.service;



import metaint.replanet.rest.chart.dto.CountAndSumByCategoryDTO;
import metaint.replanet.rest.chart.dto.CountAndSumByMonthlyDTO;
import metaint.replanet.rest.chart.repository.ChartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChartService {
    private ChartRepository chartRepository;


    @Autowired
    public ChartService(ChartRepository chartRepository) {
        this.chartRepository = chartRepository;
    }

    /* ------------ ServiceTest start ------------- */

    // 캠페인 수 카운트 조회
    public int countCampaign() {

        int countResult = (int) chartRepository.count();

        return countResult;
    }

    // 카테고리별 캠페인 수 카운트, 현재모금액 합계, 목표모금액 합계 조회
    public List<CountAndSumByCategoryDTO> countAndSumByCampaignCategory() {

        List<Object[]> resultList = chartRepository.countAndSumByCategory();

        /*
        resultList.forEach( row -> {
            for(Object item : row) {
                System.out.print(item);
            }
            System.out.println();
        });
        */

        return resultList.stream()
                .map(row -> {
                    String campaignCategory = (String) row[0];
                    int campaigns = ((Number) row[1]).intValue();
                    int sumCurrentBudget = ((Number) row[2]).intValue();
                    int sumGoalBudget = ((Number) row[3]).intValue();

                    CountAndSumByCategoryDTO dto = new CountAndSumByCategoryDTO();
                    dto.setCampaignCategory(campaignCategory);
                    dto.setCampaigns(campaigns);
                    dto.setSumCurrentBudget(sumCurrentBudget);
                    dto.setSumGoalBudget(sumGoalBudget);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 당해 캠페인 수 카운트, 현재모금액 합계, 목표모금액 합계 조회
    public List<CountAndSumByMonthlyDTO> countAndSumByCurrentyear() {
        List<Object[]> resultList = chartRepository.countAndSumByCurrentyear();

        return resultList.stream()
                .map(row -> {
                    String monthly = (String) row[0];
                    int campaigns = ((Number) row[1]).intValue();
                    int sumCurrentBudget = ((Number) row[2]).intValue();
                    int sumGoalBudget = ((Number) row[3]).intValue();

                    CountAndSumByMonthlyDTO dto = new CountAndSumByMonthlyDTO();
                    dto.setMonthly(monthly);
                    dto.setCampaigns(campaigns);
                    dto.setSumCurrentBudget(sumCurrentBudget);
                    dto.setSumGoalBudget(sumGoalBudget);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 전해 캠페인 수 카운트, 현재모금액 합계, 목표모금액 합계 조회
    public List<CountAndSumByMonthlyDTO> countAndSumByPreviousyear() {
        List<Object[]> resultList = chartRepository.countAndSumByPreviousyear();

        return resultList.stream()
                .map(row -> {
                    String monthly = (String) row[0];
                    int campaigns = ((Number) row[1]).intValue();
                    int sumCurrentBudget = ((Number) row[2]).intValue();
                    int sumGoalBudget = ((Number) row[3]).intValue();

                    CountAndSumByMonthlyDTO dto = new CountAndSumByMonthlyDTO();
                    dto.setMonthly(monthly);
                    dto.setCampaigns(campaigns);
                    dto.setSumCurrentBudget(sumCurrentBudget);
                    dto.setSumGoalBudget(sumGoalBudget);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /* ------------ ServiceTest END ------------- */

}
