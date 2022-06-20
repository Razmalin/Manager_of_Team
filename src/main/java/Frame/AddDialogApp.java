package Frame;
import javax.swing.*;

public class AddDialogApp extends AppDialog {
    @Override
    public void progress(App parent) {
        setVisible(false);
        String[] arr = {team.getText(),name.getText(),lastname.getText(), score.getText()};
        parent.addR(arr);
        JOptionPane.showMessageDialog(null, "You have added a player \""+arr[1]+" "+arr[2]+"\"");
    }

    @Override
    public void init(App parent) {
       team = new JTextField(20);
       name= new JTextField(20);
       lastname = new JTextField(20);
       score = new JTextField(20);
    }

    public AddDialogApp(JFrame owner, App parent, String title){
        super(owner,parent,title);
    }
}
