package com.fk.dropwizard.multitenancy.hibernate.dao;

import com.fk.dropwizard.multitenancy.TenantResolver;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractMultitenantDAO<T> extends AbstractDAO<T> {
    private static Logger logger = LoggerFactory.getLogger(AbstractMultitenantDAO.class);

    private TenantResolver tenantResolver;

    /**
     * Creates a new DAO with a given session provider.
     *
     * @param sessionFactory a session provider
     */
    public AbstractMultitenantDAO(SessionFactory sessionFactory, TenantResolver tenantResolver) {
        super(sessionFactory);
        this.tenantResolver = tenantResolver;
    }

    @Override
    protected Session currentSession() {
        Session session = super.currentSession();
        addTenantFilterToSession(session);
        return session;
    }

    private void addTenantFilterToSession(Session session) {
        String tenantId = tenantResolver.resolve();
        logger.info("MultitenantDAO: Set tenant_id in session filter:" + tenantId);
        session.enableFilter("tenant").setParameter("tenant_id", tenantId);
    }
}
