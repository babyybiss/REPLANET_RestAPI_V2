package metaint.replanet.rest.point.entity;

import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

@DynamicInsert
@Entity
@Table(name = "tbl_point_exchange")
public class Exchange {

    @Id
    @Column(name = "exchange_code")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int exchangeCode;

    @Column(name = "exchange_date")
    private Date exchangeDate;

    @Column(name = "title")
    private String title;

    @Column(name = "status")
    private String status;

    @Column(name = "return_detail")
    private String returnDetail;

    @Column(name = "points")
    private int points;

    @Column(name = "member_code")
    private int memberCode;

    public Exchange(){}

    public int getExchangeCode() {
        return exchangeCode;
    }

    @Override
    public String toString() {
        return "Exchange{" +
                "exchangeCode=" + exchangeCode +
                ", exchangeDate=" + exchangeDate +
                ", title='" + title + '\'' +
                ", status='" + status + '\'' +
                ", returnDetail='" + returnDetail + '\'' +
                ", points=" + points +
                ", memberCode=" + memberCode +
                '}';
    }
}
