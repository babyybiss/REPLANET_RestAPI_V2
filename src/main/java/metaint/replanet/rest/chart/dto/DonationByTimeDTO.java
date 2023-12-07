package metaint.replanet.rest.chart.dto;

import java.time.LocalDateTime;
import java.util.Date;

public class DonationByTimeDTO {

    private Date donationDate;
    private long donationPoint;
    private long payAmount;
    private long donationAmount; // 사용포인트 + 결제금액

    public DonationByTimeDTO() {

    }

    public DonationByTimeDTO(Date donationDate, long donationPoint, long payAmount, long donationAmount) {
        this.donationDate = donationDate;
        this.donationPoint = donationPoint;
        this.payAmount = payAmount;
        this.donationAmount = donationAmount;
    }

    public Date getDonationDate() {
        return donationDate;
    }

    public void setDonationDate(Date donationDate) {
        this.donationDate = donationDate;
    }

    public long getDonationPoint() {
        return donationPoint;
    }

    public void setDonationPoint(long donationPoint) {
        this.donationPoint = donationPoint;
    }

    public long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(long payAmount) {
        this.payAmount = payAmount;
    }

    public long getDonationAmount() {
        return donationAmount;
    }

    public void setDonationAmount(long donationAmount) {
        this.donationAmount = donationAmount;
    }

    @Override
    public String toString() {
        return "DonationByTimeDTO{" +
                "donationDate=" + donationDate +
                ", donationPoint=" + donationPoint +
                ", payAmount=" + payAmount +
                ", donationAmount=" + donationAmount +
                '}';
    }
}
