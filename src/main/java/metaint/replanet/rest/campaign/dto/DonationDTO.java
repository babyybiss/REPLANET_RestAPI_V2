package metaint.replanet.rest.campaign.dto;

import lombok.*;
import metaint.replanet.rest.pay.entity.CampaignDescription;
import metaint.replanet.rest.pay.entity.Member;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DonationDTO {
    private int donationCode;
    private LocalDateTime donationDateTime;
    private int donationPoint;
    private MemberDTO member;
    private int campaignCode;

}
