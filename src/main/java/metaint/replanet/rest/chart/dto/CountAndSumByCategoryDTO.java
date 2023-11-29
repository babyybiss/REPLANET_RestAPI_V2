package metaint.replanet.rest.chart.dto;

public class CountAndSumByCategoryDTO {

    private String campaignCategory;
    private int campaigns; // 캠페인 수 카운트
    private int sumCurrentBudget;
    private int sumGoalBudget;
    private int displaySumCurrentBudget;
    private int sumExpectBudget; // 목표 모금액까지 남은 액수
    private double progress;

    public CountAndSumByCategoryDTO() {
    }

    public CountAndSumByCategoryDTO(String campaignCategory, int campaigns, int sumCurrentBudget, int sumGoalBudget, int displaySumCurrentBudget, int sumExpectBudget, double progress) {
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

    public int getDisplaySumCurrentBudget() {
        return displaySumCurrentBudget;
    }

    public void setDisplaySumCurrentBudget(int displaySumCurrentBudget) {
        this.displaySumCurrentBudget = displaySumCurrentBudget;
    }

    public int getSumExpectBudget() {
        return sumExpectBudget;
    }

    public void setSumExpectBudget(int sumExpectBudget) {
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
