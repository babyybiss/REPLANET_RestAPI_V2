package metaint.replanet.rest.auth.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KakaoTokenRequest {
    private String access_token;
    private long expires_in;
    private String refresh_token;
    private long refresh_token_expires_in;
    private String scope;
    private String token_type;
}
