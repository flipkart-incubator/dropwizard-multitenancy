## dropwizard-multitenancy

dropwizard-multitenancy adds multitenancy support to dropwizard resources, for sharing the same database table across multiple tenants.

### Usage

* Add dropwizard-multitenancy
```
<dependency>
  <groupId>com.fk.dropwizard.multitenancy</groupId>
  <artifactId>dropwizard-multitenancy</artifactId>
  <version>0.0.1</version>
</dependency>
```
* Register TenantFilter as a Jersey filter in Application's run method
```
environment.jersey().getResourceConfig().getContainerRequestFilters().add(getTenantFilter());

private TenantFilter getTenantFilter() {
    return getInjector().getInstance(TenantFilter.class);
}
```
* Add Hibernate Filter annotations to entity to filter by tenant_id in select queries.
```
@Entity
@FilterDef(name = "tenant", parameters = {@ParamDef(name = "tenant_id", type = "string")}, defaultCondition = ":tenant_id = tenant_id")
@Filter(name = "tenant")
public class Project {
}
```
* Mark Entity as TenantEntity to set tenantId in insert/update queries
```
@Entity
public class Project implements TenantEntity {
    @JsonProperty
    @Column(name = "tenant_id")
    private String tenantId;

    @Override
    public String getTenantId() {
        return tenantId;
    }

    @Override
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }
}
```
* Extend DAO from AbstractMultitenantDAO instead of AbstractDAO, to enable tenant filter in currentSession
```
public class ProjectDAO extends AbstractMultitenantDAO<Project> {
    @Inject
    public ProjectDAO(SessionFactory sessionFactory, TenantResolver tenantResolver) {
        super(sessionFactory, tenantResolver);
    }
}
```
* For injecting tenantId in insert queries, create MultitenantHibernateBundle (includes MultitenantHibernateInterceptor) instead of ScanningHibernateBundle

### How it works

1. Tenant id is passed as header param (ex: tenantId)
2. TenantFilter is a Jersey filter which stores tenantId in a request scoped variable
3. tenantId is implicitly set as a [Hibernate filter](http://docs.jboss.org/hibernate/orm/4.3/manual/en-US/html/ch19.html#objectstate-filters) in currentSession of DAOs which extend from AbstractMultitenantDAO
4. Entities which have tenant_id column and define hibernate filter will automatically scope records for tenant in Find/Criteria queries.
5. For insert queries, it provides a Multitenant [Hibernate interceptor](http://docs.jboss.org/hibernate/orm/4.3/manual/en-US/html/ch14.html), which automatically sets tenantId property during onSave

Multitenancy constructs in this library are based on [this blog](http://blog.lunatech.com/2011/03/04/play-framework-writing-multitenancy-application-hibernate-filters) and [this gist](https://gist.github.com/yunspace/618eaa54eec1a726ae21)

### Assumptions

1. Hibernate is used. Please send a pull request for JDBI or JPA compatible ORMs.
2. Guice is used for dependency injection. Please send a pull request for other DI frameworks.

### Example App

[dropwizard-multitenancy-example](https://github.com/sathish316/dropwizard-multitenancy-example) contains a full-fledged example on how to use this library in a dropwizard application.

