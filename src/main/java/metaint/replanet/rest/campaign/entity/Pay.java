package metaint.replanet.rest.campaign.entity;
import lombok.*;

import javax.persistence.*;

@Entity(name = "campaign_pay")
@Table(name = "tbl_pay")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder(toBuilder = true)
public class Pay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pay_code")
    private int payCode;
    @Column(name = "pay_amount")
    private int payAmount;
    @ManyToOne
    @JoinColumn(name = "donation_code")
    private Donation donation;
}
