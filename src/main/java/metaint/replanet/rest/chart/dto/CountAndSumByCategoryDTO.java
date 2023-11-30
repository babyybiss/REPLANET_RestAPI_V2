package metaint.replanet.rest.chart.dto;

public class CountAndSumByCategoryDTO {

    private String campaignCategory;
    private int campaigns; // 캠페인 수 카운트
    private long sumCurrentBudget;
    private long sumGoalBudget;
    private long displaySumCurrentBudget;
    private long sumExpectBudget; // 목표 모금액까지 남은 액수
    private double progress;

    public CountAndSumByCategoryDTO() {
    }

    public CountAndSumByCategoryDTO(String campaignCategory, int campaigns, long sumCurrentBudget, long sumGoalBudget, long displaySumCurrentBudget, long sumExpectBudget, double progress) {
        this.campaignCategory = campaignCategory;
        this.campaigns = campaigns;
        this.sumCurrentBudget = sumCurrentBudget;
        this.sumGoalBudget = sumGoalBudget;
        this.displaySumCurrentBudget = displaySumCurrentBudget;
        this.sumExpectBudget = sumExpectBudget;
        this.progress = progress;
    }

    public String getCampaignCategory() {
        return campaignCategory;
    }

    public void setCampaignCategory(String campaignCategory) {
        this.campaignCategory = campaignCategory;
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
        return "CountAndSumByCategoryDTO{" +
                "campaignCategory='" + campaignCategory + '\'' +
                ", campaigns=" + campaigns +
                ", sumCurrentBudget=" + sumCurrentBudget +
                ", sumGoalBudget=" + sumGoalBudget +
                ", displaySumCurrentBudget=" + displaySumCurrentBudget +
                ", sumExpectBudget=" + sumExpectBudget +
                ", progress=" + progress +
                '}';
    }
}
