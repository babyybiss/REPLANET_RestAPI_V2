package metaint.replanet.rest.chart.dto;

public class GoalBudgetByCategoryDTO {

    private String campaignCategory;
    private int goalBudget;

    public GoalBudgetByCategoryDTO() {
    }

    public GoalBudgetByCategoryDTO(String campaignCategory, int goalBudget) {
        this.campaignCategory = campaignCategory;
        this.goalBudget = goalBudget;
    }

    public String getCampaignCategory() {
        return campaignCategory;
    }

    public void setCampaignCategory(String campaignCategory) {
        this.campaignCategory = campaignCategory;
    }

    public int getGoalBudget() {
        return goalBudget;
    }

    public void setGoalBudget(int goalBudget) {
        this.goalBudget = goalBudget;
    }

    @Override
    public String toString() {
        return "GoalBudgetByCategoryDTO{" +
                "campaignCategory='" + campaignCategory + '\'' +
                ", goalBudget=" + goalBudget +
                '}';
    }
}
