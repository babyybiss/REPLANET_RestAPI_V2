package metaint.replanet.rest.pay;

import metaint.replanet.rest.pay.entity.Pay;
import metaint.replanet.rest.pay.repository.PayRepository;
import metaint.replanet.rest.pay.service.KakaoPayService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.persistence.EntityNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
public class KakaoPayServiceTest {

    @Autowired
    private KakaoPayService kakaoPayService;

    @MockBean
    private PayRepository payRepository;

    @DisplayName("임의의 payCode를 통해서 Pay를 조회하는 메소드 테스트")
    @Test
    public void testGetPayByPayCode() {
        // given
        String payCode = "123";

        // when
        Pay mockPay = Pay.builder().payCode(123).build();

        when(payRepository.findByPayCode(123)).thenReturn(mockPay);

        // then
        Pay resultPay = kakaoPayService.getPayByPayCode(payCode);
        assertThat(resultPay).isEqualTo(mockPay);
    }

    @DisplayName("존재하지 않는 payCode 값을 넣으면 null 반환하는지 테스트")
    @Test
    public void testGetPayByPayCodeNotFound() {
        // given
        String payCode = "999";
        // when
        when(payRepository.findByPayCode(999)).thenReturn(null);

        // then
        assertThrows(EntityNotFoundException.class, () -> {
            kakaoPayService.getPayByPayCode(payCode);
        });
    }
}
