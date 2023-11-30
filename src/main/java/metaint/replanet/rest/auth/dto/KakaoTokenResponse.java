package metaint.replanet.rest.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KakaoTokenResponse {
    private String access_token;
    private String refresh_token;
    private long expires_in;
}
