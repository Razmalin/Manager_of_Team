package Frame;
import javax.swing.*;
public class EditDialogRace extends RaceDialog{
    @Override
    public void progress(RaceApp parent) {
        setVisible(false);
        int row = parent.matches.getSelectedRow();
        parent.matches.setValueAt(track.getText(), row, 0);
        parent.matches.setValueAt(distance.getText(), row, 1);
        parent.matches.setValueAt(data.getText(), row, 2);
        parent.makeXml();
        SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(null, "Editing is comlited"));
    }

    @Override
    public void init(RaceApp parent) {
        int row = parent.matches.getSelectedRow();
        track = new JTextField(parent.matches.getValueAt(row, 0).toString(), 20);
        distance = new JTextField(parent.matches.getValueAt(row, 1).toString(), 20);
        data = new JTextField(parent.matches.getValueAt(row, 2).toString(), 20);
        checker(0, track);
        checker(1, distance);
        checker(2, data);
    }

    public EditDialogRace(JFrame owner, RaceApp parent, String title){
        super(owner,parent,title);
    };
}
