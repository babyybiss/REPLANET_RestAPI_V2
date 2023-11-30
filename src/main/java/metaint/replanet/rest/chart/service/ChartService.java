package metaint.replanet.rest.chart.service;

import metaint.replanet.rest.chart.dto.CountAndSumByCategoryDTO;

import metaint.replanet.rest.chart.dto.CountAndSumByMonthlyDTO;
import metaint.replanet.rest.chart.dto.DonationByTimeDTO;
import metaint.replanet.rest.chart.repository.ChartRepository;
import metaint.replanet.rest.chart.repository.DonationChartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChartService {
    private final ChartRepository chartRepository;
    private final DonationChartRepository donationChartRepository;


    @Autowired
    public ChartService(ChartRepository chartRepository, DonationChartRepository donationChartRepository) {
        this.chartRepository = chartRepository;
        this.donationChartRepository = donationChartRepository;
    }

    // 캠페인 수 카운트 조회
    public int countCampaign() {

        int countResult = (int) chartRepository.count();

        return countResult;
    }

    // 카테고리별 캠페인 수 카운트, 현재모금액 합계, 목표모금액 합계 조회
    public List<CountAndSumByCategoryDTO> countAndSumByCampaignCategory() {

        List<Object[]> resultList = chartRepository.countAndSumByCategory();

        return resultList.stream()
                .map(row -> {
                    String campaignCategory = (String) row[0];
                    int campaigns = ((Number) row[1]).intValue();
                    long sumCurrentBudget = ((Number) row[2]).longValue();
                    long sumGoalBudget = ((Number) row[3]).longValue();
                    long displaySumCurrentBudget = ((Number) row[4]).longValue();
                    long sumExpectBudget = ((Number) row[5]).longValue();
                    double progress = ((Number) row[6]).doubleValue();

                    CountAndSumByCategoryDTO dto = new CountAndSumByCategoryDTO();
                    dto.setCampaignCategory(campaignCategory);
                    dto.setCampaigns(campaigns);
                    dto.setSumCurrentBudget(sumCurrentBudget);
                    dto.setSumGoalBudget(sumGoalBudget);
                    dto.setDisplaySumCurrentBudget(displaySumCurrentBudget);
                    dto.setSumExpectBudget(sumExpectBudget);
                    dto.setProgress(progress);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    // 당해 캠페인 수 카운트, 현재모금액 합계, 목표모금액 합계 조회
    public List<CountAndSumByMonthlyDTO> countAndSumByCurrentYear() {
        List<Object[]> resultList = chartRepository.countAndSumByCurrentYear();

        return resultList.stream()
                .map(row -> {
                    int fullYear = ((Number) row[0]).intValue();
                    int monthly = ((Number) row[1]).intValue();
                    int campaigns = ((Number) row[2]).intValue();
                    long sumCurrentBudget = ((Number) row[3]).longValue();
                    long sumGoalBudget = ((Number) row[4]).longValue();
                    long displaySumCurrentBudget = ((Number) row[5]).longValue();
                    long sumExpectBudget = ((Number) row[6]).longValue();
                    double progress = ((Number) row[7]).doubleValue();

                    CountAndSumByMonthlyDTO dto = new CountAndSumByMonthlyDTO();
                    dto.setFullYear(fullYear);
                    dto.setMonthly(monthly);
                    dto.setCampaigns(campaigns);
                    dto.setSumCurrentBudget(sumCurrentBudget);
                    dto.setSumGoalBudget(sumGoalBudget);
                    dto.setDisplaySumCurrentBudget(displaySumCurrentBudget);
                    dto.setSumExpectBudget(sumExpectBudget);
                    dto.setProgress(progress);

                    return dto;
                })
                .collect(Collectors.toList());
    }


    // 전해 캠페인 수 카운트, 현재모금액 합계, 목표모금액 합계 조회
    public List<CountAndSumByMonthlyDTO> countAndSumByPreviousYear() {
        List<Object[]> resultList = chartRepository.countAndSumByPreviousYear();

        return resultList.stream()
                .map(row -> {
                    int fullYear = ((Number) row[0]).intValue();
                    int monthly = ((Number) row[1]).intValue();
                    int campaigns = ((Number) row[2]).intValue();
                    long sumCurrentBudget = ((Number) row[3]).longValue();
                    long sumGoalBudget = ((Number) row[4]).longValue();
                    long displaySumCurrentBudget = ((Number) row[5]).longValue();
                    long sumExpectBudget = ((Number) row[6]).longValue();
                    double progress = ((Number) row[7]).doubleValue();

                    CountAndSumByMonthlyDTO dto = new CountAndSumByMonthlyDTO();
                    dto.setFullYear(fullYear);
                    dto.setMonthly(monthly);
                    dto.setCampaigns(campaigns);
                    dto.setSumCurrentBudget(sumCurrentBudget);
                    dto.setSumGoalBudget(sumGoalBudget);
                    dto.setDisplaySumCurrentBudget(displaySumCurrentBudget);
                    dto.setSumExpectBudget(sumExpectBudget);
                    dto.setProgress(progress);

                    return dto;
                })
                .collect(Collectors.toList());
    }



    // 시간별 캠페인에 기부된 포인트 및 기부 모금액 조회
    public List<DonationByTimeDTO> selectDonationByTime() {
        List<Object[]> resultList = donationChartRepository.donateByTime();

        return resultList.stream()
                .map(row -> {
                    int campaignCode = ((Number) row[0]).intValue();
                    Date donationDate = (Date) row[1];
                    long donationPoint = ((Number) row[2]).longValue();
                    long payAmount = ((Number) row[3]).longValue();
                    long donationAmount = ((Number) row[4]).longValue();
                    String campaignCategory = (String) row[5];

                    DonationByTimeDTO dto = new DonationByTimeDTO();
                    dto.setCampaignCode(campaignCode);
                    dto.setDonationDate(donationDate);
                    dto.setDonationPoint(donationPoint);
                    dto.setPayAmount(payAmount);
                    dto.setDonationAmount(donationAmount);
                    dto.setCampaignCategory(campaignCategory);
                    return dto;
                })
                .collect(Collectors.toList());
    }
}
