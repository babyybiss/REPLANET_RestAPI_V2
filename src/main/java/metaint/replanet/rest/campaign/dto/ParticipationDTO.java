package metaint.replanet.rest.campaign.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ParticipationDTO {
    private int payCode;
    private int payAmount;
    private DonationDTO donation;
}
