package metaint.replanet.rest.bookmark.dto;

import lombok.*;
import metaint.replanet.rest.campaign.dto.CampaignDesOrgDTO;
import metaint.replanet.rest.campaign.dto.MemberDTO;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class BookmarkRegistDTO {

    private int bookmarkCode;
    private int memberCode;
    private int campaignCode;
}
