package com.fk.dropwizard.multitenancy.hibernate.bundle;

import com.fk.dropwizard.multitenancy.TenantResolver;
import com.fk.dropwizard.multitenancy.hibernate.interceptor.MultitenantInterceptor;
import io.dropwizard.hibernate.ScanningHibernateBundle;
import org.hibernate.cfg.Configuration;

public abstract class MultitenantHibernateBundle<T extends io.dropwizard.Configuration> extends ScanningHibernateBundle<T> {

    private final TenantResolver tenantResolver;

    protected MultitenantHibernateBundle(String pckg, TenantResolver tenantResolver) {
        super(pckg);
        this.tenantResolver = tenantResolver;
    }

    @Override
    protected void configure(Configuration configuration) {
        configuration.setInterceptor(new MultitenantInterceptor(tenantResolver));
    }
}
