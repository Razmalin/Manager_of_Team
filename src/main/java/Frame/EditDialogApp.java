package Frame;
import javax.swing.*;
public class EditDialogApp extends AppDialog{
    @Override
    public void progress(App parent) {
        setVisible(false);
        int row = parent.drivers.getSelectedRow();
        parent.drivers.setValueAt(team.getText(), row, 1);
        parent.drivers.setValueAt(name.getText(), row, 2);
        parent.drivers.setValueAt(lastname.getText(), row, 3);
        parent.drivers.setValueAt(score.getText(), row, 4);
        parent.makeXml();
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Editing is completed"));
    }

    @Override
    public void init(App parent) {
        int row = parent.drivers.getSelectedRow();
        team = new JTextField(parent.drivers.getValueAt(row, 1).toString(), 20);
        name = new JTextField(parent.drivers.getValueAt(row, 2).toString(), 20);
        lastname = new JTextField(parent.drivers.getValueAt(row, 3).toString(), 20);
        score = new JTextField(parent.drivers.getValueAt(row, 4).toString(), 20);
        checker(0, team);
        checker(1, name);
        checker(2, lastname);
        checker(3, score);
    }

    public EditDialogApp(JFrame owner, App parent, String title){
        super(owner,parent,title);
    };
}
