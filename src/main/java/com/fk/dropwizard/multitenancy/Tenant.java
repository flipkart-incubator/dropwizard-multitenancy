package com.fk.dropwizard.multitenancy;

import com.google.inject.servlet.RequestScoped;

@RequestScoped
public class Tenant {
    private String tenantId;

    public void setTenantId( final String tenantId ){
        this.tenantId = tenantId;
    }

    public String getTenantId(){
        return tenantId;
    }
}