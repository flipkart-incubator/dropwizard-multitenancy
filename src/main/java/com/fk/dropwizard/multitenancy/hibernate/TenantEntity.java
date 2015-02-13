package com.fk.dropwizard.multitenancy.hibernate;

public interface TenantEntity {
    public String getTenantId();

    public void setTenantId(String tenantId);
}
