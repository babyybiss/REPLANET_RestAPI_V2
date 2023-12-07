package metaint.replanet.rest.chart.service;

import metaint.replanet.rest.chart.dto.CountAndSumByCategoryDTO;

import metaint.replanet.rest.chart.dto.CountAndSumByMonthlyDTO;
import metaint.replanet.rest.chart.dto.DonationByTimeDTO;
import metaint.replanet.rest.chart.repository.ChartRepository;
import metaint.replanet.rest.chart.repository.DonationChartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    // 카테고리별 캠페인 통계 조회
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

    // 당해 캠페인 통계 조회
    public List<CountAndSumByMonthlyDTO> countAndSumByCurrentYear() {
        List<Object[]> resultList = chartRepository.countAndSumByCurrentYear();

        return resultList.stream()
                .map(row -> {
                    int fullYear = ((Number) row[0]).intValue();
                    int monthly = ((Number) row[1]).intValue();
                    int allCampaigns = ((Number) row[2]).intValue();
                    int childCampaigns = ((Number) row[3]).intValue();
                    int olderCampaigns = ((Number) row[4]).intValue();
                    int etcCampaigns = ((Number) row[5]).intValue();
                    int animalCampaigns = ((Number) row[6]).intValue();
                    int natureCampaigns = ((Number) row[7]).intValue();
                    long sumCurrentBudget = ((Number) row[8]).longValue();
                    long sumGoalBudget = ((Number) row[9]).longValue();
                    long displaySumCurrentBudget = ((Number) row[10]).longValue();
                    long sumExpectBudget = ((Number) row[11]).longValue();
                    double progress = ((Number) row[12]).doubleValue();

                    CountAndSumByMonthlyDTO dto = new CountAndSumByMonthlyDTO();
                    dto.setFullYear(fullYear);
                    dto.setMonthly(monthly);
                    dto.setAllCampaigns(allCampaigns);
                    dto.setChildCampaigns(childCampaigns);
                    dto.setOlderCampaigns(olderCampaigns);
                    dto.setEtcCampaigns(etcCampaigns);
                    dto.setAnimalCampaigns(animalCampaigns);
                    dto.setNatureCampaigns(natureCampaigns);
                    dto.setSumCurrentBudget(sumCurrentBudget);
                    dto.setSumGoalBudget(sumGoalBudget);
                    dto.setDisplaySumCurrentBudget(displaySumCurrentBudget);
                    dto.setSumExpectBudget(sumExpectBudget);
                    dto.setProgress(progress);

                    return dto;
                })
                .collect(Collectors.toList());
    }


    // 전해 캠페인 통계 조회
    public List<CountAndSumByMonthlyDTO> countAndSumByPreviousYear() {
        List<Object[]> resultList = chartRepository.countAndSumByPreviousYear();

        return resultList.stream()
                .map(row -> {
                    int fullYear = ((Number) row[0]).intValue();
                    int monthly = ((Number) row[1]).intValue();
                    int allCampaigns = ((Number) row[2]).intValue();
                    int childCampaigns = ((Number) row[3]).intValue();
                    int olderCampaigns = ((Number) row[4]).intValue();
                    int etcCampaigns = ((Number) row[5]).intValue();
                    int animalCampaigns = ((Number) row[6]).intValue();
                    int natureCampaigns = ((Number) row[7]).intValue();
                    long sumCurrentBudget = ((Number) row[8]).longValue();
                    long sumGoalBudget = ((Number) row[9]).longValue();
                    long displaySumCurrentBudget = ((Number) row[10]).longValue();
                    long sumExpectBudget = ((Number) row[11]).longValue();
                    double progress = ((Number) row[12]).doubleValue();

                    CountAndSumByMonthlyDTO dto = new CountAndSumByMonthlyDTO();
                    dto.setFullYear(fullYear);
                    dto.setMonthly(monthly);
                    dto.setAllCampaigns(allCampaigns);
                    dto.setChildCampaigns(childCampaigns);
                    dto.setOlderCampaigns(olderCampaigns);
                    dto.setEtcCampaigns(etcCampaigns);
                    dto.setAnimalCampaigns(animalCampaigns);
                    dto.setNatureCampaigns(natureCampaigns);
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return resultList.stream()
                .map(row -> {
                    String donationDateString = (String) row[0];
                    long donationPoint = ((Number) row[1]).longValue();
                    long payAmount = ((Number) row[2]).longValue();
                    long donationAmount = ((Number) row[3]).longValue();

                    /*
                    LocalDateTime donationDate = LocalDateTime.parse(donationDateString, formatter);
                    */
                    Date donationDate = stringToDate(donationDateString);

                    DonationByTimeDTO dto = new DonationByTimeDTO();
                    dto.setDonationDate(donationDate);
                    dto.setDonationPoint(donationPoint);
                    dto.setPayAmount(payAmount);
                    dto.setDonationAmount(donationAmount);
                    return dto;
                })
                .collect(Collectors.toList());
    }


    private Date stringToDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
