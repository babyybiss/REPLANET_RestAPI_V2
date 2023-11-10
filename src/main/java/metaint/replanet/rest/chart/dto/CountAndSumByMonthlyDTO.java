package metaint.replanet.rest.chart.dto;

import java.time.LocalDate;

public class CountAndSumByMonthlyDTO {

    private String monthly;
    private int campaigns; // 캠페인 수 카운트
    private int sumCurrentBudget;
    private int sumGoalBudget;

    public CountAndSumByMonthlyDTO() {
    }

    public CountAndSumByMonthlyDTO(String monthly, int campaigns, int sumCurrentBudget, int sumGoalBudget) {
        this.monthly = monthly;
        this.campaigns = campaigns;
        this.sumCurrentBudget = sumCurrentBudget;
        this.sumGoalBudget = sumGoalBudget;
    }

    public String getMonthly() {
        return monthly;
    }

    public void setMonthly(String monthly) {
        this.monthly = monthly;
    }

    public int getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(int campaigns) {
        this.campaigns = campaigns;
    }

    public int getSumCurrentBudget() {
        return sumCurrentBudget;
    }

    public void setSumCurrentBudget(int sumCurrentBudget) {
        this.sumCurrentBudget = sumCurrentBudget;
    }

    public int getSumGoalBudget() {
        return sumGoalBudget;
    }

    public void setSumGoalBudget(int sumGoalBudget) {
        this.sumGoalBudget = sumGoalBudget;
    }

    @Override
    public String toString() {
        return "CountAndSumByMonthlyDTO{" +
                "monthly='" + monthly + '\'' +
                ", campaigns=" + campaigns +
                ", sumCurrentBudget=" + sumCurrentBudget +
                ", sumGoalBudget=" + sumGoalBudget +
                '}';
    }
}
