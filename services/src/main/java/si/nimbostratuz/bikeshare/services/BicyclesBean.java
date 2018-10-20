package si.nimbostratuz.bikeshare.services;

import com.kumuluz.ee.rest.utils.JPAUtils;
import si.nimbostratuz.bikeshare.models.entities.Bicycle;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import java.util.List;

@ApplicationScoped
public class BicyclesBean {

    @Inject
    private EntityManager em;

    public List<Bicycle> getAll() {

        return JPAUtils.queryEntities(em, Bicycle.class);
    }

    public Bicycle getBicycle(Integer bicycleId) {

        Bicycle bicycle = em.find(Bicycle.class, bicycleId);

        if (bicycle == null) {
            throw new NotFoundException();
        }

        return bicycle;
    }

}
