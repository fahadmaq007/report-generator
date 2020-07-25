package com.maqs.reportgenerator.exception;


/**
 * Represents the service layer exception.
 * 
 * @author niraj.gupta
 * 
 */
public class ServiceException extends Exception {

 
  /**
	 * 
	 */
	private static final long serialVersionUID = 8130834998742492354L;

/**
   * Constructs the exception with the message.
   * 
   * @param message exception message
   */
  public ServiceException(String message) {
    this(message, null);
  }

  /**
   * Constructs with the supplied message & the cause.
   * 
   * @param message exception message
   * @param cause Cause of the exception
   */
  public ServiceException(String message, Throwable cause) {
    super(message, cause);
  }

  /**
   * Constructs with the cause.
   * 
   * @param cause Cause of the exception
   */
  public ServiceException(Throwable cause) {
    this(cause.getMessage(), cause);
  }
}
