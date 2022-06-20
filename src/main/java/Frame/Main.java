package Frame;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.swing.*;

import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Main {
     public static void main(String[] args) {
          JFrame window = new JFrame("Main window");
          JButton playerWindow = new JButton("Drivers", new ImageIcon("./images/player.png"));
          JButton raceWindow = new JButton("Races", new ImageIcon("./images/match.png"));
          JButton refWindow = new JButton("Open program info", new ImageIcon("./images/info.png"));

          EntityManagerFactory emf = Persistence.createEntityManagerFactory("new_schema");
          EntityManager em = emf.createEntityManager();
          em.getTransaction().begin();
          System.out.println("Start Hibernate super cool connection");
          em.getTransaction().commit();

          playerWindow.addActionListener((e) -> new App());
          raceWindow.addActionListener((e) -> new RaceApp());
          refWindow.addActionListener((e) -> new Ref(window, "Reference"));
          window.setLayout(new FlowLayout());
          JPanel mainp = new JPanel();
          JPanel panel = new JPanel();
          panel.setLayout(new GridLayout(3, 1));
          panel.setSize(350, 100);
          // adds to the GridLayout
          panel.add(playerWindow);
          panel.add(raceWindow);
          panel.add(refWindow);
          mainp.add(panel);
          window.add(BorderLayout.CENTER, mainp);
          window.setSize(400, 380);
          window.setLocation(500, 200);
          window.setVisible(true);
          window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          window.addWindowListener(new WindowListener() {
               public void windowOpened(WindowEvent e) {
               }

               public void windowClosing(WindowEvent e) {
                    Object[] options = {"Yes", "No!"};
                    int n = JOptionPane.showOptionDialog(e.getWindow(), "Are you sure you want to close the program?", "Confirmation", JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                    if (n == 0) {
                         e.getWindow().setVisible(false);
                    }
               }

               @Override
               public void windowClosed(WindowEvent e) {
               }

               @Override
               public void windowIconified(WindowEvent e) {
               }

               @Override
               public void windowDeiconified(WindowEvent e) {
               }

               @Override
               public void windowActivated(WindowEvent e) {
               }

               @Override
               public void windowDeactivated(WindowEvent e) {
               }
          });
     }
}