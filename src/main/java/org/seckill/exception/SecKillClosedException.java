package org.seckill.exception;

public class SecKillClosedException extends SecKillException {

    public SecKillClosedException(String message) {
        super(message);
    }

    public SecKillClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}
