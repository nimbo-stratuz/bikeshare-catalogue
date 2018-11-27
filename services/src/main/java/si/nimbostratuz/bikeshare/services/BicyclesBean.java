package si.nimbostratuz.bikeshare.services;

import lombok.extern.java.Log;
import si.nimbostratuz.bikeshare.models.entities.Bicycle;
import si.nimbostratuz.bikeshare.services.configuration.BikeshareConfig;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.List;

@Log
@RequestScoped
public class BicyclesBean {

    @PersistenceContext(unitName = "bikeshare-catalogue-jpa")
    private EntityManager em;

    @Inject
    private RentalsService rentalsService;

    @Inject
    private BikeshareConfig bikeshareConfig;

    public List<Bicycle> getAll() {

        TypedQuery<Bicycle> query = em.createNamedQuery("Bicycle.getAll", Bicycle.class);

        return query.getResultList();
    }

    public Bicycle get(Integer bicycleId) {

        Bicycle bicycle = em.find(Bicycle.class, bicycleId);

        if (bicycle == null) {
            throw new NotFoundException("Bicycle with id " + bicycleId + " not found");
        }

        if (bikeshareConfig.getNRentalsIncluded() > 0) {
            bicycle.setRentals(rentalsService.getLastRentalsForBicycle(bicycleId, bikeshareConfig.getNRentalsIncluded())
                                             .orElse(null));
        }

        return bicycle;
    }

    public Bicycle getBySmartLockUUID(String smartLockUUID) {

        TypedQuery<Bicycle> query = em.createNamedQuery("Bicycle.findBySmartLockUUID", Bicycle.class);
        query.setParameter("smartLockUUID", smartLockUUID);

        List<Bicycle> resultList = query.getResultList();

        if (!resultList.isEmpty()) {
            return resultList.get(0);
        } else {
            throw new NotFoundException("Bicycle with smartLockUUID " + smartLockUUID + " not found");
        }
    }

    public Bicycle create(Bicycle bicycle) {

        // Workaround
        bicycle.setId(null);

        try {
            beginTx();

            bicycle.setDateAdded(Instant.now());
            bicycle.setAvailable(false);

            em.persist(bicycle);

            commitTx();
        } catch (Exception e) {
            rollbackTx();
            log.throwing(BicyclesBean.class.getName(), "create", e);
            throw new BadRequestException();
        }

        return bicycle;
    }

    public Bicycle update(Integer id, Bicycle bicycle) {

        Bicycle originalBicycle = this.get(id);

        try {
            beginTx();
            bicycle.setId(originalBicycle.getId());
            bicycle = em.merge(bicycle);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
            log.throwing(BicyclesBean.class.getName(), "update", e);
        }

        return bicycle;
    }

    public void delete(Integer id) {

        Bicycle bicycle = this.get(id);

        try {
            beginTx();
            em.remove(bicycle);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
            log.throwing(BicyclesBean.class.getName(), "delete", e);
        }
    }

    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}
