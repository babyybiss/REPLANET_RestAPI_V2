package metaint.replanet.rest.chart.dto;

import java.time.LocalDate;

public class CountByMonthlyDTO {

    private LocalDate startDate;
    private int campaigns;

    public CountByMonthlyDTO() {
    }

    public CountByMonthlyDTO(LocalDate startDate, int campaigns) {
        this.startDate = startDate;
        this.campaigns = campaigns;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getCampaigns() {
        return campaigns;
    }

    public void setCampaigns(int campaigns) {
        this.campaigns = campaigns;
    }

    @Override
    public String toString() {
        return "CountByMonthlyDTO{" +
                "startDate=" + startDate +
                ", campaigns=" + campaigns +
                '}';
    }
}
