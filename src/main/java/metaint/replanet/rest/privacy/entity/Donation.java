package metaint.replanet.rest.privacy.entity;

import lombok.*;
import metaint.replanet.rest.campaign.entity.CampaignDescription;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "privacyDonation")
@Table(name = "tbl_donation")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder(toBuilder = true)
public class Donation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "donation_code")
    private int donationCode;

    @Column(name = "donation_date_time")
    private LocalDateTime donationDateTime;

    @Column(name = "donation_point")
    private int donationPoint;

    @Column(name = "member_code")
    private int memberCode;

    @JoinColumn(name = "campaign_code")
    @ManyToOne
    private CampaignDescription refCampaign;
}
