package metaint.replanet.rest.pay.entity;

import lombok.*;
import metaint.replanet.rest.campaign.entity.Organization;

import javax.persistence.*;
import java.time.LocalDate;
@Entity(name = "pay_campaign_description")
@Table(name = "tbl_campaign_description")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class CampaignDescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "campaign_code")
    private int campaignCode;
    @Column(name = "campaign_title")
    private String campaignTitle;
    @Column(name = "campaign_content")
    private String campaignContent;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name = "campaign_category")
    private String campaignCategory;
    @Column(name = "current_budget")
    private int currentBudget;
    @Column(name = "goal_budget")
    private int goalBudget;
    @ManyToOne
    @JoinColumn(name = "org_code")
    private Organization organization; // 기부처 코드

}
