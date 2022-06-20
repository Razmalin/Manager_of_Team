package SQL;

import Classes.Drivers;
import Classes.Winners;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.awt.*;
import java.util.List;

public class Winners_con {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("new_schema");
    EntityManager em = emf.createEntityManager();

    public void deleteD(int id) {
        em.getTransaction().begin();
        List <Winners> win = em.createQuery("SELECT w FROM Winners w WHERE w.driver_id="+id).getResultList();
        for (Winners w : win){
            em.remove(w);
        }
        em.getTransaction().commit();
    }

    public void deleteT(int id) {
        em.getTransaction().begin();
        List <Winners> win = em.createQuery("SELECT w FROM Winners w WHERE w.track_id="+id).getResultList();
        for (Winners w : win){
            em.remove(w);
        }
        em.getTransaction().commit();
    }
}
