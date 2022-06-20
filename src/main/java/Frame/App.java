package Frame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.table.TableStringConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import Classes.Drivers;
import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import static com.itextpdf.text.Image.MIDDLE;
import static java.awt.Frame.MAXIMIZED_BOTH;
import SQL.*;

public class App {
    Drivers_con ControlDR;
    Winners_con ControlWin;
    App(){
        show();
    }
	private JFrame manager;
	private DefaultTableModel model;
	private JButton add;
    private JPanel searchPanel;
	private JButton delete;
	private JButton edit;
	private JButton print;
	private JToolBar toolBarHorizontal;
	private JScrollPane scroll;
	protected JTable drivers;
	private JComboBox comboBox;
	private JButton search;
	private JTextField textSearch;
     // Диалоговое окно редактирования данных
    private EditDialogApp dialog;
     // Диалоговое окно добавления данных
    private AddDialogApp dialogAdd;
	private static final Logger log = Logger.getLogger("App.class");
	private static final String SWT = null;
	public static class Util {
		public static boolean isEmpty(String value) {
			return value == null || "".equals(value);
		}
		public static int sum(int x, int y) {
			return x + y;
		}}
    class MyException extends Exception {
        public MyException(){
            super("You don't enter the name");
        }
    }
    private void checkName (JTextField pName) throws MyException,NullPointerException
    {
	    String sName = pName.getText();
	    if (sName.contains("Name search")) throw new MyException();
	    if (sName.length() == 0) throw new NullPointerException();
    } 
    public void saveFile() {
    	FileDialog save = new FileDialog(manager, "Save data", FileDialog.SAVE);
    	save.setFile("team.txt");
    	save.setVisible(true);
    	String fileName = save.getDirectory() + save.getFile();
    	if(fileName == null) return;
    }
	public void makeReport()
	{
		makeXml();
		Document document = new Document(PageSize.A4, 50, 50, 50, 50);

		PdfPTable t = new PdfPTable(4);
		try {
			PdfWriter.getInstance(document, new FileOutputStream("otchet.pdf"));
		} 
		catch (FileNotFoundException e) {
		e.printStackTrace();
		} 
		catch (DocumentException e) {
		e.printStackTrace();
		}

		BaseFont bfComic = null;

		try {
		bfComic = BaseFont.createFont("C:\\Users\\artem\\eclipse-workspace\\ManagerTeam\\fonts\\arial.ttf" ,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

		} catch (DocumentException e1) {
		e1.printStackTrace();
		} catch (IOException e1) {
		e1.printStackTrace();

		}
		Font font1 = new Font(bfComic, 12);
		t.addCell(new PdfPCell(new Phrase("team", font1)));
		t.addCell(new PdfPCell(new Phrase("name", font1)));
		t.addCell(new PdfPCell(new Phrase("lastname", font1)));
		t.addCell(new PdfPCell(new Phrase("score", font1)));
		for(int i = 0; i < model.getRowCount(); i++){
		t.addCell(new Phrase((String) model.getValueAt(i,0),font1));
		t.addCell(new Phrase((String) model.getValueAt(i,1),font1));
		t.addCell(new Phrase((String) model.getValueAt(i,2),font1));
		t.addCell(new Phrase((String) model.getValueAt(i,3),font1));
		}
		document.open();
		try {
		document.add(t);
		} catch (DocumentException e) {
		e.printStackTrace();
		}
		document.close();
		
			PrintWriter pw = null;
			try {
			pw = new PrintWriter(new FileWriter("otchet.html"));

			} catch (IOException e) {
			e.printStackTrace();
			}
			pw.println("<TABLE BORDER><TR><TH>team<TH>name<TH>lastname<TH>score<TH></TR>");
			for(int i = 0; i < model.getRowCount(); i++) {
			pw.println("<TR><TD>" + (String) model.getValueAt(i,0) + "<TD>" + (String) model.getValueAt(i,1) + "<TD>" + (String) model.getValueAt(i,2) + "<TD>" + (String) model.getValueAt(i,3)+ "<TD>");
			}
			pw.close();
	}
    public  void read(String filename){
        try{
        	BufferedReader reader = new BufferedReader(new FileReader(filename));
        	int rows = model.getRowCount();
        	for (int i = 0; i < rows; i++) model.removeRow(0); // Очистка таблицы
        	String team;
        	do {
        	team = reader.readLine();
        	if(team != null)
        	{ String name = reader.readLine();
        	String lastname = reader.readLine();
        	String score = reader.readLine();
        	model.addRow(new String[]{team, name, lastname, score}); // Запись строки в таблицу
        	}
        	} while(team != null);
        	reader.close();
        	} catch (FileNotFoundException e) {e.printStackTrace();} // файл не найден
        	 catch (IOException e) {e.printStackTrace();}

    }
    public void write(String filename){
        try{
        	BufferedWriter writer = new BufferedWriter (new FileWriter(filename));
        	for (int i = 0; i < model.getRowCount(); i++) // Для всех строк
        	for (int j = 0; j < model.getColumnCount(); j++) // Для всех столбцов
        	{writer.write ((String) model.getValueAt(i, j)); // Записать значение из ячейки
        	writer.write("\n"); // Записать символ перевода каретки
        	}
        	writer.close();
        	 }
        	catch(IOException e) // Ошибка записи в файл
        	{ e.printStackTrace(); }
    }
    public void makeXml() {
        try {
            // Создание парсера документа
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            // Создание пустого документа
            org.w3c.dom.Document doc = builder.newDocument();
            // Создание корневого элемента team и добавление его в документ
            Node team = doc.createElement("team");
            doc.appendChild(team);
            // Создание дочерних элементов team и присвоение значений атрибутам
            for (int i = 0; i < model.getRowCount(); i++) {
                Element driver = doc.createElement("player");
                team.appendChild(driver);
                driver.setAttribute("team", (String)model.getValueAt(i, 0));
                driver.setAttribute("name", (String)model.getValueAt(i, 1));
                driver.setAttribute("lastname", (String)model.getValueAt(i, 2));
                driver.setAttribute("score", (String)model.getValueAt(i, 3));
            }
            try {
                // Создание преобразователя документа
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                // Создание файла с именем team.xml для записи документа
                java.io.FileWriter fw = new FileWriter("team.xml");
                // Запись документа в файл
                trans.transform(new DOMSource((Node) doc), new StreamResult(fw));
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }	
        	
    }
    //выгрузка из xml
    public void loadXML(){
        try{
            // Создание парсера документа
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            org.w3c.dom.Document doc = builder.newDocument();
            // Чтение документа из файла
            JFileChooser fileChooser = new JFileChooser("C:\\Users\\vova_\\eclipse-workspace\\Manager_of_Team");
            int ret = fileChooser.showDialog(null, "Open file");
            if (ret == JFileChooser.APPROVE_OPTION) {
                doc = dBuilder.parse(fileChooser.getSelectedFile());
                // Нормализация документа
                doc.getDocumentElement().normalize();
                // Получение списка элементов с именем player
                NodeList nlplayer = doc.getElementsByTagName("player");
                // Цикл просмотра списка элемента и запись данных в таблицу
                for (int temp = 0; temp < nlplayer.getLength(); temp++) {
                    // Выбор очередного элемента списка
                    Node elem = nlplayer.item(temp);
                    // Получение списка атрибутов документа
                    NamedNodeMap attrs = elem.getAttributes();
                    // Чтение атрибутов элемента
                    String team = attrs.getNamedItem("team").getNodeValue();
                    String name = attrs.getNamedItem("name").getNodeValue();
                    String lastname = attrs.getNamedItem("lastname").getNodeValue();
                    String score = attrs.getNamedItem("score").getNodeValue();
                    // Запись данных в таблицу
                    model.addRow(new String[]{team, name, lastname, score});
                }
            }
        }
        catch (ParserConfigurationException e){e.printStackTrace();}
        // Обработка ошибки парсера при чтении данных из XML-файла
        catch (SAXException e){e.printStackTrace();}
        catch (IOException e){e.printStackTrace();}
    }

    public void loadDrivers() {
        if (manager.isAncestorOf(scroll)) manager.remove(scroll);
        model = new DefaultTableModel(ControlDR.show(), Data_columns.columns_dr);
        drivers = new JTable(model);
        drivers.setIntercellSpacing(new Dimension(0,1));
        drivers.setRowHeight(drivers.getRowHeight()+10);
        drivers.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        scroll = new JScrollPane(drivers);
        manager.add(scroll, BorderLayout.CENTER);
        manager.add(searchPanel, BorderLayout.SOUTH);
        manager.revalidate();
        manager.repaint();
    }

	public void show() {

        ControlDR = new Drivers_con();
        ControlWin = new Winners_con();

        add = new JButton("Add", new ImageIcon("./images/add.png"));
        delete = new JButton("Delete", new ImageIcon("./images/delete.png"));
        edit = new JButton("Edit", new ImageIcon("./images/edit.png"));
        print = new JButton("Print",new ImageIcon("./images/print.png"));
        add.setToolTipText("Add information about drivers");
        delete.setToolTipText("Delete information about drivers");
        edit.setToolTipText("Edit information about drivers");
        print.setToolTipText("Print information about drivers");
        manager = new JFrame("Team");
        manager.setSize(640, 480);
        manager.setVisible(true);
        manager.setExtendedState(MAXIMIZED_BOTH);
        manager.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);;
        manager.setLayout(new BorderLayout());
        toolBarHorizontal = new JToolBar("Toolbar");
        toolBarHorizontal.add(add);
        toolBarHorizontal.add(delete);
        toolBarHorizontal.add(edit);
        toolBarHorizontal.add(print);
		manager.setLayout(new BorderLayout());
		manager.add(toolBarHorizontal, BorderLayout.NORTH);
		String [] columns = {"Id","Team", "Name", "Lastname", "Score"};
		String [][] data = ControlDR.show();
                /*{{"Team RFR", "Daniel", "Lex", "27"}, {"MClaren", "Julian", "Vandorn", "12"}, {"Team RFR", "Paul", "Pendox", "122"}, {"MClaren", "Otto", "Parker", "32"}, {"MClaren", "Kleo", "Dornelli", "76"}};*/
        comboBox = new JComboBox(new String[]{"Id", "Team", "Name", "Lastname", "Score"});
        textSearch = new JTextField();
        textSearch.setColumns(20);
        search = new JButton("Search");
        manager.getRootPane().setDefaultButton(search);
// remove the binding for pressed
        manager.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ENTER"), "none");
// retarget the binding for released
        manager.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("released ENTER"), "press");
        searchPanel = new JPanel();
        searchPanel.add(comboBox);
        searchPanel.add(textSearch);
        searchPanel.add(search);
        manager.add(searchPanel,BorderLayout.SOUTH);
		model = new DefaultTableModel(data, columns);
		drivers = new JTable(model);
		scroll = new JScrollPane(drivers);
		manager.add(scroll, BorderLayout.CENTER);
        drivers.setIntercellSpacing(new Dimension(0,1));
        drivers.setRowHeight(drivers.getRowHeight()+10);
        drivers.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        manager.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        manager.addWindowListener(new WindowListener() {
	        public void windowOpened(WindowEvent e) {
	        }
	        public void windowClosing(WindowEvent e) {
	            Object[] options = { "Yes", "No!" };
                int n = JOptionPane.showOptionDialog(e.getWindow(), "Are you sure you want to close this window?","Confirmation", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                if (n == 0) {
                    e.getWindow().setVisible(false);
                }
	        }
	        public void windowClosed(WindowEvent e) {
	        }
	        public void windowIconified(WindowEvent e) {
	        }
	        public void windowDeiconified(WindowEvent e) {
	        }
	        public void windowActivated(WindowEvent e) {
	        }
            public void windowDeactivated(WindowEvent e) {
            }
	    });
        add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JTextField team = new JTextField(20);
                JTextField name = new JTextField(20);
                JTextField lastname = new JTextField(20);
                JTextField score = new JTextField(20);
                JPanel myPanel = new JPanel();
                myPanel.add(new JLabel("Team:"));
                myPanel.add(team);
                myPanel.add(new JLabel("Name:"));
                myPanel.add(name);
                myPanel.add(new JLabel("Lastname:"));
                myPanel.add(lastname);
                myPanel.add(new JLabel("Score:"));
                myPanel.add(score);
                int result = JOptionPane.showConfirmDialog(null, myPanel,
                        "Add a new driver", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    try {
                        checkName(name);
                        Drivers dr = new Drivers();
                        dr.setTeam((team.getText()));
                        dr.setName(name.getText());
                        dr.setLast_name(lastname.getText());
                        dr.setScore(score.getText());
                        ControlDR.addToDB(dr);
                        JOptionPane.showMessageDialog(null, "Data is successfully added!");
                    } catch (MyException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    } catch (NullPointerException ex) {
                        JOptionPane.showMessageDialog(null, ex.toString());
                    }

                }
                loadDrivers();
            }
        });

        search.addActionListener((e) -> {
            if (model.getRowCount() != 0) {
                if (!textSearch.getText().isEmpty())
                    log.info("Starting a new keyword search: " + textSearch.getText());
                else
                    log.info("Reset search keyword");
                TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(((DefaultTableModel) model));
                sorter.setStringConverter(new TableStringConverter() {
                    public String toString(TableModel model, int row, int column) {
                        return model.getValueAt(row, column).toString().toLowerCase();
                    }
                });
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + textSearch.getText().toLowerCase()));
                drivers.setRowSorter(sorter);
            }
        });
        print.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                log.info("Start Print listener");
                makeReport();
                Document document = new Document(PageSize.A4, 20, 20, 20, 20);
                try {
                    PdfWriter.getInstance(document, new FileOutputStream("Inform.pdf"));
                    BaseFont bfComic;
                    bfComic = BaseFont.createFont("fonts\\arial.ttf" ,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                    Font font = new Font(bfComic, 18, Font.BOLD);
                    Font font1 = new Font(bfComic, 14);
                    Font font2 = new Font(bfComic, 10);
                    Paragraph header = new Paragraph("Information about drivers", font);
                    header.setAlignment(MIDDLE);
                    Paragraph cm = new Paragraph("\nCount of Drivers: "+drivers.getRowCount(), font1);
                    Paragraph[] eff = new Paragraph[drivers.getRowCount()];
                    for (int i = 0; i < drivers.getRowCount(); i++) {
                        eff[i] = new Paragraph("Driver: " + ControlDR.getDriver1(i)+ " " + ControlDR.getDriver2(i)+ "--->" + "Scores: " + ControlDR.getScore(i) + "\n", font1);
                    }
                    document.open();
                    try {
                        document.add(header);
                        document.add(cm);
                        for (int i = 0; i < drivers.getRowCount(); i++) {
                            document.add(eff[i]);
                        }
                    } catch (DocumentException ex) {
                        JOptionPane.showMessageDialog(null, ex.getMessage());
                    }
                    document.close();
                        JOptionPane.showMessageDialog(null, "Data successfully uploaded to Inform.pdf!");
                }
                catch (DocumentException | IOException ex) {
                    JOptionPane.showMessageDialog(null, ex.getMessage());
                }
            }
        });
        manager.setVisible(true);
        delete.addActionListener((e) -> {
            log.info("Start Delete listener");
            if (drivers.getRowCount() > 0) {
                if (drivers.getSelectedRow() != -1) {
                    try {
                        int id = Integer.parseInt(model.getValueAt(drivers.getSelectedRow(), 0).toString());
                        Object[] options = { "Yes", "No!" };
                        int n = JOptionPane.showOptionDialog(manager, "Are you sure you want to delete this row?","Confirmation", JOptionPane.YES_NO_OPTION,
                                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
                        if (n == 0) {
                            ControlDR.delete(id);
                            ControlWin.deleteD(id);
                            model.removeRow(drivers.convertRowIndexToModel(drivers.getSelectedRow()));
                            JOptionPane.showMessageDialog(manager, "You removed the line");
                        }
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Mistake");
                    }
                } else {
                    log.info("Row not selected");
                    JOptionPane.showMessageDialog(null, "You have not selected a line to delete");
                }
            } else {
                log.info("There are no entries in the window");
                JOptionPane.showMessageDialog(null, "There are no entries in this window. Nothing to delete");
            }
        });
        edit.addActionListener((e) -> {
            log.info("Start Edit listener");
            if (model.getRowCount() != 0) {
                if (drivers.getSelectedRow() != -1) {
                    dialog = new EditDialogApp(manager, App.this, "Editing");
                    dialog.setVisible(true);
                    int n = model.getColumnCount();
                    int currentRow = drivers.getSelectedRow();
                    TextField[] fields = new TextField[n - 1];
                    System.out.println(model.getValueAt(currentRow, 0));
                    System.out.println(model.getValueAt(currentRow, 1));
                    for (int i = 0; i < n - 1; i++) {
                        manager.add(new Label(model.getColumnName(i + 1)));
                        manager.add(fields[i] = new TextField((String) model.getValueAt(currentRow, i + 1), 20));
                    }
                    boolean edited = false;
                    int id = Integer.parseInt(model.getValueAt(currentRow, 0).toString());
                    if (ControlDR.edit(id, fields)) edited = true;
                    if (edited) {
                        loadDrivers();
                    }
                } else {
                    log.info("Row not selected");
                    JOptionPane.showMessageDialog(null, "Row not selected. Nothing to edit");
                }
            } else {
                log.info("There are no entries in this window.");
                JOptionPane.showMessageDialog(null, "There are no entries in this window. Nothing to edit");
            }
        });
        
		}
	    public void addR(String[] arr){
	        model.addRow(arr);
	    }
}