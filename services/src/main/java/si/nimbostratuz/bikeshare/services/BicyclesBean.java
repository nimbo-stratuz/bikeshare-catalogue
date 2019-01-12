package si.nimbostratuz.bikeshare.services;

import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import si.nimbostratuz.bikeshare.models.common.AuthenticatedUser;
import si.nimbostratuz.bikeshare.models.entities.Bicycle;
import si.nimbostratuz.bikeshare.services.configuration.BikeshareConfig;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.NotFoundException;
import java.time.Instant;
import java.util.List;

@Log
@RequestScoped
public class BicyclesBean {

    private static final Logger log = LogManager.getLogger(BicyclesBean.class.getName());

    @Inject
    private EntityManager em;

    @Inject
    private RentalsService rentalsService;

    @Inject
    private BikeshareConfig bikeshareConfig;

    @Inject
    private AuthenticatedUser authenticatedUser;

    public List<Bicycle> getAll() {

        TypedQuery<Bicycle> query = em.createNamedQuery("Bicycle.getAll", Bicycle.class);

        return query.getResultList();
    }

    public Bicycle getClosest(Double latitude, Double longitude) {

        TypedQuery<Bicycle> query = em.createNamedQuery("Bicycle.findClosest", Bicycle.class);

        query.setParameter("latitude", latitude);
        query.setParameter("longitude", longitude);

        List<Bicycle> bicycles = query.getResultList();

        if (bicycles.size() == 0) {
            throw new NotFoundException("No closest bicycle");
        }

        return bicycles.get(0);
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

            bicycle.setOwnerId(authenticatedUser.getId());
            bicycle.setDateAdded(Instant.now());
            bicycle.setAvailable(true);

            em.persist(bicycle);
            commitTx();

            return bicycle;

        } catch (Exception e) {
            rollbackTx();
            log.error("create", e);
            throw new BadRequestException("Could not create bicycle ");
        }
    }

    public Bicycle update(Integer id, Bicycle bicycle) {

        Bicycle originalBicycle = this.get(id);

        if (!authenticatedUser.owns(originalBicycle)) {
            throw new ForbiddenException("Bicycle not owned by authenticated user.");
        }

        try {
            beginTx();

            bicycle.setId(originalBicycle.getId());
            bicycle.setOwnerId(originalBicycle.getOwnerId());

            bicycle = em.merge(bicycle);
            commitTx();

            return bicycle;

        } catch (Exception e) {
            rollbackTx();
            log.error("update", e);
            throw new BadRequestException("Could not update bicycle with id " + id);
        }
    }

    public void delete(Integer id) {

        Bicycle bicycle = this.get(id);

        if (!authenticatedUser.owns(bicycle)) {
            throw new ForbiddenException("Bicycle not owned by authenticated user.");
        }

        try {
            beginTx();
            em.remove(bicycle);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
            log.error("delete", e);
            throw new BadRequestException("Could not delete bicycle with id " + id);
        }
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        } else {
            log.warn("Call to beginTx while there is an active transaction.");
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        } else {
            log.warn("Call to commitTx while there is no active transactions.");
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        } else {
            log.warn("Call to rollbackTx while there is no active transactions.");
        }
    }
}
