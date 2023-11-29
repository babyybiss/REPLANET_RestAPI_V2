package metaint.replanet.rest.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "error.bad")
public class BadRequestException extends RuntimeException {
    public String BadRequestException(String errorMsg) {
        errorMsg = "입력값 불일치";
        return errorMsg;
    }
}