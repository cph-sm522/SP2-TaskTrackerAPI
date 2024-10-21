package dat.exceptions;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {
    private final int StatusCode;

    public ApiException(int StatusCode, String message) {
        super(message);
        this.StatusCode = StatusCode;
    }
}
