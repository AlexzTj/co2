package co2.domain;

import lombok.Getter;

public class NotFoundException extends RuntimeException {

    @Getter
    private Error error;

    public NotFoundException(Error error) {
        this.error = error;
    }
}
