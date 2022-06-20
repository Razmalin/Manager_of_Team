package Frame;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class AddRaceDialog extends RaceDialog {
   @Override
   public void progress(RaceApp parent) {
       setVisible(false);
       String[] arr = {track.getText(),distance.getText(), data.getText()};
       parent.addR(arr);
       JOptionPane.showMessageDialog(null, "You have added a match \""+arr[0]+" "+arr[2]+"\"");
   }

   @Override
   public void init(RaceApp parent) {
      track = new JTextField(20);
      distance  = new JTextField(20);
      data= new JTextField(20);
   }

   public AddRaceDialog(JFrame owner, RaceApp parent, String title){
       super(owner,parent,title);
   }
}
