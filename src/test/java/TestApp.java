import org.junit.*;
import Classes.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class TestApp {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("new_schema");
    EntityManager em = emf.createEntityManager();

    @Test
    public void TestAddReader(){
        Drivers dr = new Drivers();
        dr.setTeam("Tachki");
        dr.setName("Molnia");
        dr.setLast_name("McQueen");
        dr.setScore("999");
        em.getTransaction().begin();
        em.persist(dr);
        em.getTransaction().commit();
        Assert.assertNotNull(em.find(Drivers.class, dr.getId()));
    }
}
