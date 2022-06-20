package SQL;

import Classes.Drivers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.awt.*;
import java.util.List;

public class Drivers_con {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("new_schema");
    EntityManager em = emf.createEntityManager();

    public void addToDB(Drivers dr){
        em.getTransaction().begin();
        em.persist(dr);
        em.getTransaction().commit();
    }
    public String getDriver1(int id) {
        em.getTransaction().begin();
        List<Drivers> plr = em.createQuery("SELECT dr from Drivers dr").getResultList();
        String pl_data = plr.get(id).getName();
        em.getTransaction().commit();
        return pl_data;
    }
    public String getDriver2(int id) {
        em.getTransaction().begin();
        List<Drivers> plr = em.createQuery("SELECT dr from Drivers dr").getResultList();
        String pl_data = plr.get(id).getLast_name();
        em.getTransaction().commit();
        return pl_data;
    }
    public String getScore(int id) {
        em.getTransaction().begin();
        List<Drivers> plr = em.createQuery("SELECT dr from Drivers dr").getResultList();
        String pl_data = plr.get(id).getScore();
        em.getTransaction().commit();
        return pl_data;
    }
    public Drivers getById(int id){
        em.getTransaction().begin();
        Drivers pl = em.find(Drivers.class, id);
        em.getTransaction().commit();
        return pl;
    }
    public String[][] show() {
        em.getTransaction().begin();
        List<Drivers> plr = em.createQuery("SELECT dr from Drivers dr").getResultList();
        String[][] driver_data = new String[plr.size()][5];
        for (int i = 0; i < plr.size(); i++){
            driver_data[i][0] = String.valueOf(plr.get(i).getId());
            driver_data[i][1] = plr.get(i).getTeam();
            driver_data[i][2] = plr.get(i).getName();
            driver_data[i][3] = plr.get(i).getLast_name();
            driver_data[i][4] = plr.get(i).getScore();
        }
        em.getTransaction().commit();
        return driver_data;
    }
    public boolean edit(int id, TextField[] fields){
        Drivers pl = getById(id);
        em.getTransaction().begin();
        pl.setTeam((fields[0].getText()));
        pl.setName(fields[1].getText());
        pl.setLast_name(fields[2].getText());
        pl.setScore(fields[3].getText());
        em.getTransaction().commit();
        return true;
    }
    public boolean delete(int id){
        Drivers dr = getById(id);
        em.getTransaction().begin();
        em.remove(dr);
        em.getTransaction().commit();
        Drivers pl1 = getById(id);
        if (pl1 == null) return true;
        else return false;
    }
}
