package metaint.replanet.rest.chart.dto;

public class CountAndSumByMonthlyDTO {

    private int fullYear;
    private int monthly;
    private int campaigns;
    private long sumCurrentBudget;
    private long sumGoalBudget;
    private long displaySumCurrentBudget;
    private long sumExpectBudget;
    private double progress;

    public CountAndSumByMonthlyDTO() {
    }

    public CountAndSumByMonthlyDTO(int fullYear, int monthly, int campaigns, long sumCurrentBudget, long sumGoalBudget, long displaySumCurrentBudget, long sumExpectBudget, double progress) {
        this.fullYear = fullYear;
        this.monthly = monthly;
        this.campaigns = campaigns;
        this.sumCurrentBudget = sumCurrentBudget;
        this.sumGoalBudget = sumGoalBudget;
        this.displaySumCurrentBudget = displaySumCurrentBudget;
        this.sumExpectBudget = sumExpectBudget;
        this.progress = progress;
    }

    public int getFullYear() {
        return fullYear;
    }

    public void setFullYear(int fullYear) {
        this.fullYear = fullYear;
    }

    public int getMonthly() {
        return monthly;
    }

    public void setMonthly(int monthly) {
        this.monthly = monthly;
    }

    public int getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(int campaigns) {
        this.campaigns = campaigns;
    }

    public long getSumCurrentBudget() {
        return sumCurrentBudget;
    }

    public void setSumCurrentBudget(long sumCurrentBudget) {
        this.sumCurrentBudget = sumCurrentBudget;
    }

    public long getSumGoalBudget() {
        return sumGoalBudget;
    }

    public void setSumGoalBudget(long sumGoalBudget) {
        this.sumGoalBudget = sumGoalBudget;
    }

    public long getDisplaySumCurrentBudget() {
        return displaySumCurrentBudget;
    }

    public void setDisplaySumCurrentBudget(long displaySumCurrentBudget) {
        this.displaySumCurrentBudget = displaySumCurrentBudget;
    }

    public long getSumExpectBudget() {
        return sumExpectBudget;
    }

    public void setSumExpectBudget(long sumExpectBudget) {
        this.sumExpectBudget = sumExpectBudget;
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "CountAndSumByMonthlyDTO{" +
                "fullYear=" + fullYear +
                ", monthly=" + monthly +
                ", campaigns=" + campaigns +
                ", sumCurrentBudget=" + sumCurrentBudget +
                ", sumGoalBudget=" + sumGoalBudget +
                ", displaySumCurrentBudget=" + displaySumCurrentBudget +
                ", sumExpectBudget=" + sumExpectBudget +
                ", progress=" + progress +
                '}';
    }
}
