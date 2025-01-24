package com.github.garodriguezlp.irimo.service.exception;

public class ServiceNotFoundException extends RuntimeException {

  private final String serviceIdentity;

  public ServiceNotFoundException(String serviceIdentity) {
    super("No service found for identity: " + serviceIdentity);
    this.serviceIdentity = serviceIdentity;
  }

  public String getServiceIdentity() {
    return serviceIdentity;
  }
}
