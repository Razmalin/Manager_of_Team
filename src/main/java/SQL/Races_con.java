package SQL;

import Classes.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Races_con {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("new_schema");
    EntityManager em = emf.createEntityManager();

    public boolean addToDB(Races r){
        em.getTransaction().begin();
        em.persist(r);
        em.getTransaction().commit();
        return true;
    }
    public Races getById(int id){
        em.getTransaction().begin();
        Races r = em.find(Races.class, id);
        em.getTransaction().commit();
        return r;
    }
    public boolean delete(int id){
        Races r = getById(id);
        em.getTransaction().begin();
        em.remove(r);
        em.getTransaction().commit();
        Races r1 = getById(id);
        if (r1 == null) return true;
        else return false;
    }
    public String[][] show(){
        em.getTransaction().begin();
        List<Races> rc = em.createQuery("SELECT rc from Races rc").getResultList();
        String[][] races_data = new String[rc.size()][4];
        for (int i = 0; i < rc.size(); i++){
            races_data[i][0] = String.valueOf(rc.get(i).getId());
            races_data[i][1] = rc.get(i).getTrack();
            races_data[i][2] = rc.get(i).getDistance();
            races_data[i][3] = rc.get(i).getData();
        }
        em.getTransaction().commit();
        return races_data;
    }
    public boolean edit(int id, TextField[] fields){
        Races rc = getById(id);
        em.getTransaction().begin();
        rc.setTrack(fields[0].getText());
        rc.setDistance(fields[1].getText());
        rc.setData(fields[2].getText());
        em.getTransaction().commit();
        return true;
    }

    public String[][] PrintWinners(int id){
        em.getTransaction().begin();
        List<Winners> winners = em.createQuery("SELECT w from Winners w WHERE w.track_id="+id+" ORDER BY w.place").getResultList();
        String[][] MyWinners = new String[winners.size()][3];
        for (int i = 0; i < winners.size(); i++){
            Drivers dr = em.find(Drivers.class, winners.get(i).getDriver_id());
            MyWinners[i][0] = dr.getName();
            MyWinners[i][1] = dr.getLast_name();
            MyWinners[i][2] = String.valueOf(winners.get(i).getPlace());
        }
        em.getTransaction().commit();
        return MyWinners;
    }
}
