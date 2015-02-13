package com.fk.dropwizard.multitenancy;

import com.fk.dropwizard.multitenancy.exception.InvalidTenantException;
import com.google.inject.Inject;
import com.google.inject.Provider;

public class TenantResolver {

    @Inject
    Provider<Tenant> tenantProvider;

    public String resolve() throws InvalidTenantException {
        Tenant tenant = tenantProvider.get();
        if (tenant.getTenantId() != null){
            return tenant.getTenantId();
        } else {
            throw new InvalidTenantException();
        }
    }
}
