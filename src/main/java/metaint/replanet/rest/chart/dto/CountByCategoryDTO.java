package metaint.replanet.rest.chart.dto;

public class CountByCategoryDTO {

    private String campaignCategory;
    private int campaigns; // 캠페인 수 카운트

    public CountByCategoryDTO() {
    }

    public CountByCategoryDTO(String campaignCategory, int campaigns) {
        this.campaignCategory = campaignCategory;
        this.campaigns = campaigns;
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

    @Override
    public String toString() {
        return "CountByCategoryDTO{" +
                "campaignCategory='" + campaignCategory + '\'' +
                ", campaigns=" + campaigns +
                '}';
    }
}
