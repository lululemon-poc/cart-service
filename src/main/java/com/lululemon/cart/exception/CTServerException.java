package com.lululemon.cart.exception;

public class CTServerException extends RuntimeException {
  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new CommerceTools exception with message.
   *
   * @param message the message
   */
  public CTServerException(String message) {
    super(message);
  }

  /**
   * Instantiates a new CommerceTools exception with message and cause.
   *
   * @param message   the message
   * @param throwable the cause
   */
  public CTServerException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
