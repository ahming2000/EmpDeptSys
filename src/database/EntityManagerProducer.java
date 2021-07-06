package database;

import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EntityManagerProducer {
    @Produces
    @PersistenceContext(unitName = "EmpDeptSys")
    @PostgresDatabase
    private EntityManager em;

    public EntityManagerProducer() {
        super();
    }
}
