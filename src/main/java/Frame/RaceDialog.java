package Frame;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RaceDialog extends JDialog {
    protected JTextField track;
    protected JTextField distance;
    protected JTextField data;
    protected Boolean[] check = {false,false,false};
    private JButton ok = new JButton("Accept");
    private JButton cancel = new JButton("Cancel");
    private JLabel trackLab = new JLabel("Track");
    private JLabel distanceLab = new JLabel("Distance");
    private JLabel dataLab = new JLabel("Data");

	//Выполнение манипуляций с данными
     //@param parent Объект класса приложения
    public abstract void progress(RaceApp parent);
     //Инициализация
     // @param parent Объект класса приложения
    public abstract void init(RaceApp parent);

    /**
     * Основной конструктор
     *
     * @param owner  JFrame приложения
     * @param parent Объект класса приложения
     * @param title  Title окна
     * @return 
     */
    public RaceDialog(JFrame owner, RaceApp parent, String title) {
        super(owner, title, true);
        ok.setEnabled(false);
        // Инит кнопок
        init(parent);

        // Создание валидатора полей
        track.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checker(0, track);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checker(0, track);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        distance.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checker(1,distance);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checker(1,distance);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        data.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                checker(2,data);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                checker(2,data);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {

            }
        });

        ok.addActionListener((e) -> progress(parent));
        cancel.addActionListener((e) -> dispose());
        JPanel mainp = new JPanel();

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2, 2, 2));

        panel.setSize(700, 250);

        // adds to the GridLayout
        panel.add(trackLab);
        panel.add(track);
        panel.add(distanceLab);
        panel.add(distance);
        panel.add(dataLab);
        panel.add(data);
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
            r = Pattern.compile("^[A-Z][a-z]{1,20}\\s[A-Z][a-z]{1,20}$");
        else
            if (i == 1)
                r = Pattern.compile("^\\d{1,2},\\d{3}$");
        else
            r = Pattern.compile("^\\d{2}-\\d{2}-\\d{4}$");
        Matcher m = r.matcher(field.getText());
        if (m.matches()) {
            field.setBorder(BorderFactory.createLineBorder(Color.GREEN));
            check[i] = true;
        }
        else {
            field.setBorder(BorderFactory.createLineBorder(Color.RED));
            check[i] = false;
        }
        if(check[0] && check[1] && check[2])
            ok.setEnabled(true);
        else
            ok.setEnabled(false);
    }
}