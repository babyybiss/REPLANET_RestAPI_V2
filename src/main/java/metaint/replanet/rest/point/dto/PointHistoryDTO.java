package metaint.replanet.rest.point.dto;

import lombok.*;

import java.util.Date;

public interface PointHistoryDTO {

    Date getChangeDate();
    String getContent();
    int getChangePoint();
    int getRemainingPoint();
}
