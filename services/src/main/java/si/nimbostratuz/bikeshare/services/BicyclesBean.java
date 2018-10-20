package si.nimbostratuz.bikeshare.services;

import si.nimbostratuz.bikeshare.models.entities.Bicycle;

import javax.enterprise.context.ApplicationScoped;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;

@ApplicationScoped
public class BicyclesBean {

    @PersistenceContext(unitName = "bikeshare-catalogue-jpa")
    private EntityManager em;

    public List<Bicycle> getAll() {

        return new LinkedList<>();
    }
}
