package Frame;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AppDialog extends JDialog {
    protected JTextField team;
    protected JTextField name;
    protected JTextField lastname;
    protected JTextField score;
    protected Boolean[] check = {false,false,false,false};
    private JButton ok = new JButton("Accept");
    private JButton cancel = new JButton("Cancel");
    private JLabel teamLab = new JLabel("Team");
    private JLabel namLab = new JLabel("Name");
    private JLabel lstnamLab = new JLabel("Lastname");
    private JLabel scrLab = new JLabel("Score");

	//Выполнение манипуляций с данными
     //@param parent Объект класса приложения
    public abstract void progress(App parent);
     //Инициализация
     // @param parent Объект класса приложения
    public abstract void init(App parent);

    /**
     * Основной конструктор
     *
     * @param owner  JFrame приложения
     * @param parent Объект класса приложения
     * @param title  Title окна
     * @return 
     */
    public AppDialog(JFrame owner, App parent, String title) {
        super(owner, title, true);
        ok.setEnabled(false);
        // Инит кнопок
        init(parent);

        // Создание валидатора полей
        team.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checker(0, team);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checker(0, team);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        name.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checker(1,name);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checker(1,name);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        lastname.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checker(2,lastname);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checker(2,lastname);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        score.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checker(3, score);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checker(3, score);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });
        ok.addActionListener((e) -> progress(parent));
        cancel.addActionListener((e) -> dispose());
        JPanel mainp = new JPanel();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(8, 2, 2, 2));

        panel.setSize(700, 250);

        // adds to the GridLayout
        panel.add(teamLab);
        panel.add(team);
        panel.add(namLab);
        panel.add(name);
        panel.add(lstnamLab);
        panel.add(lastname);
        panel.add(scrLab);
        panel.add(score);
        mainp.add(panel);
        add(BorderLayout.CENTER, mainp);
        JPanel but = new JPanel();
        but.add(ok);
        but.add(cancel);
        add(BorderLayout.SOUTH, but);
        setLocation(400, 250);
        setSize(800, 300);
        this.getRootPane().setDefaultButton(ok);
// remove the binding for pressed
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ENTER"), "none");
// retarget the binding for released
        this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("released ENTER"), "press");
    }

    protected void checker(int i, JTextField field){
        Pattern r;
        if (i == 0)
            r = Pattern.compile("^.{3,20}$");
        else
            if (i == 1)
                r = Pattern.compile("^[A-Z][a-z]{1,20}$");
        else
            if (i == 2)
                r = Pattern.compile("^[A-Z][a-z]{1,20}$");
        else
            r = Pattern.compile("^[1234567890]{1,3}$");
        Matcher m = r.matcher(field.getText());
        if (m.matches()) {
            field.setBorder(BorderFactory.createLineBorder(Color.GREEN));
            check[i] = true;
        }
        else {
            field.setBorder(BorderFactory.createLineBorder(Color.RED));
            check[i] = false;
        }
        if(check[0] && check[1] && check[2] && check[3])
            ok.setEnabled(true);
        else
            ok.setEnabled(false);
	}
        
}
