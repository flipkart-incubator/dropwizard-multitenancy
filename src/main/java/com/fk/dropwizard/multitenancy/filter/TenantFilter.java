package com.fk.dropwizard.multitenancy.filter;

import com.fk.dropwizard.multitenancy.Tenant;
import com.fk.dropwizard.multitenancy.config.MultitenantConfig;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@javax.ws.rs.ext.Provider
public class TenantFilter implements ContainerRequestFilter {
    private static Logger logger = LoggerFactory.getLogger(TenantFilter.class);

    private final Provider<Tenant> tenantProvider;

    @Inject
    public TenantFilter(Provider<Tenant> tenantProvider) {
        this.tenantProvider = tenantProvider;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        String tenantId = request.getHeaderValue(MultitenantConfig.TENANT_HEADER);
        Tenant tenant = tenantProvider.get();
        tenant.setTenantId(tenantId);
        logger.info("TenantFilter: Set tenantId in Provider:" + tenantId);
        return request;
    }
}
