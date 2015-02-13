package com.fk.dropwizard.multitenancy.hibernate.interceptor;

import com.fk.dropwizard.multitenancy.TenantResolver;
import com.fk.dropwizard.multitenancy.config.MultitenantConfig;
import com.fk.dropwizard.multitenancy.hibernate.TenantEntity;
import com.google.inject.Inject;
import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;

public class MultitenantInterceptor extends EmptyInterceptor {

    private TenantResolver tenantResolver;

    @Inject
    public MultitenantInterceptor(TenantResolver tenantResolver) {
        this.tenantResolver = tenantResolver;
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if ( entity instanceof TenantEntity) {
            for ( int i=0; i<propertyNames.length; i++ ) {
                if (MultitenantConfig.TENANT_PROPERTY.equals( propertyNames[i] ) ) {
                    state[i] = tenantResolver.resolve();
                    return true;
                }
            }
        }
        return false;
    }
}
