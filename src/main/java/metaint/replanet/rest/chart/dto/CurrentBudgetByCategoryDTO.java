package metaint.replanet.rest.chart.dto;

public class CurrentBudgetByCategoryDTO {

    private String campaignCategory;
    private int currentBudget;

    public CurrentBudgetByCategoryDTO() {
    }

    public CurrentBudgetByCategoryDTO(String campaignCategory, int currentBudget) {
        this.campaignCategory = campaignCategory;
        this.currentBudget = currentBudget;
    }

    public String getCampaignCategory() {
        return campaignCategory;
    }

    public void setCampaignCategory(String campaignCategory) {
        this.campaignCategory = campaignCategory;
    }

    public int getCurrentBudget() {
        return currentBudget;
    }

    public void setCurrentBudget(int currentBudget) {
        this.currentBudget = currentBudget;
    }

    @Override
    public String toString() {
        return "CurrentBudgetByCategoryDTO{" +
                "campaignCategory='" + campaignCategory + '\'' +
                ", currentBudget=" + currentBudget +
                '}';
    }
}
