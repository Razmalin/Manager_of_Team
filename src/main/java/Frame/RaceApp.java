package Frame;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
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
import Classes.Races;
import SQL.Races_con;
import SQL.Winners_con;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.xml.sax.SAXException;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Font;
import com.itextpdf.text.DocumentException;
import static java.awt.Frame.MAXIMIZED_BOTH;


public class RaceApp {
	Races_con ControlRC;
	Winners_con ControlWin;
	RaceApp(){
        show();
    }
	private JFrame match;
	private JButton seeWinners;
	private static final Logger log = Logger.getLogger("RaceApp.class");
	private DefaultTableModel model;
	private JButton add;
	private JButton delete;
	private JButton edit;
	private JPanel searchPanel;
	private JToolBar toolBar;
	private JScrollPane scroll;
	protected JTable matches;
	private JComboBox comboBox;
	private JButton search;
	private JTextField textSearch;
	private EditDialogRace dialog;
    // Диалоговое окно добавления данных
   private AddRaceDialog dAdd;
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
    	FileDialog save = new FileDialog(match, "Save data", FileDialog.SAVE);
    	save.setFile("races.txt");
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
			PdfWriter.getInstance(document, new FileOutputStream("otcheteraces.pdf"));
		} 
		catch (FileNotFoundException e) {
		e.printStackTrace();
		} 
		catch (DocumentException e) {
		e.printStackTrace();
		}

		BaseFont bfComic = null;

		try {
		bfComic = BaseFont.createFont("C:\\Users\\artem\\eclipse-workspace\\Manager_of_Team\\fonts\\arial.ttf" ,BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

		} catch (DocumentException e1) {
		e1.printStackTrace();
		} catch (IOException e1) {
		e1.printStackTrace();

		}
		Font font1 = new Font(bfComic, 12);
		t.addCell(new PdfPCell(new Phrase("track", font1)));
		t.addCell(new PdfPCell(new Phrase("distance", font1)));
		t.addCell(new PdfPCell(new Phrase("data", font1)));
		for(int i = 0; i < model.getRowCount(); i++){
		t.addCell(new Phrase((String) model.getValueAt(i,0),font1));
		t.addCell(new Phrase((String) model.getValueAt(i,1),font1));
		t.addCell(new Phrase((String) model.getValueAt(i,2),font1));
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
			pw = new PrintWriter(new FileWriter("otchetraces.html"));

			} catch (IOException e) {
			e.printStackTrace();
			}
			pw.println("<TABLE BORDER><TR><TH>track<TH>distance<TH>data<TH></TR>");
			for(int i = 0; i < model.getRowCount(); i++) {
			pw.println("<TR><TD>" + (String) model.getValueAt(i,0) + "<TD>" + (String) model.getValueAt(i,1) + "<TD>" + (String) model.getValueAt(i,2) + "<TD>");
			}
			pw.close();
	}
    public  void read(String filename){
        try{
        	BufferedReader reader = new BufferedReader(new FileReader(filename));
        	int rows = model.getRowCount();
        	for (int i = 0; i < rows; i++) model.removeRow(0); // Очистка таблицы
        	String track;
        	do {
        	track = reader.readLine();
        	if(track != null)
        	{ String data = reader.readLine();
        	String distance = reader.readLine();
        	model.addRow(new String[]{track, distance, data}); // Запись строки в таблицу
        	}
        	} while(track != null);
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
                Element match = doc.createElement("race");
                team.appendChild(match);
                match.setAttribute("team", (String)model.getValueAt(i, 0));
				match.setAttribute("distance", (String)model.getValueAt(i, 1));
                match.setAttribute("data", (String)model.getValueAt(i, 2));
            }
            try {
                // Создание преобразователя документа
                Transformer trans = TransformerFactory.newInstance().newTransformer();
                // Создание файла с именем team.xml для записи документа
                java.io.FileWriter fw = new FileWriter("races.xml");
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
            JFileChooser fileChooser = new JFileChooser("C:\\Users\\artem\\eclipse-workspace\\Manager_of_Team");
            int ret = fileChooser.showDialog(null, "Open file");
            if (ret == JFileChooser.APPROVE_OPTION) {
                doc = dBuilder.parse(fileChooser.getSelectedFile());
                // Нормализация документа
                doc.getDocumentElement().normalize();
                // Получение списка элементов с именем player
                NodeList nlmatch = doc.getElementsByTagName("race");
                // Цикл просмотра списка элемента и запись данных в таблицу
                for (int temp = 0; temp < nlmatch.getLength(); temp++) {
                    // Выбор очередного элемента списка
                    Node elem = nlmatch.item(temp);
                    // Получение списка атрибутов документа
                    NamedNodeMap attrs = elem.getAttributes();
                    // Чтение атрибутов элемента
                    String team = attrs.getNamedItem("team").getNodeValue();
					String distance = attrs.getNamedItem("distance").getNodeValue();
                    String data = attrs.getNamedItem("data").getNodeValue();
                    // Запись данных в таблицу
                    model.addRow(new String[]{team, distance, data});
                }
            }
        }
        catch (ParserConfigurationException e){e.printStackTrace();}
        // Обработка ошибки парсера при чтении данных из XML-файла
        catch (SAXException e){e.printStackTrace();}
        catch (IOException e){e.printStackTrace();}
    }

	public void loadRaces() {
		if (match.isAncestorOf(scroll)) match.remove(scroll);
		model = new DefaultTableModel(ControlRC.show(), Data_columns.columns_rc);
		matches = new JTable(model);
		scroll = new JScrollPane(matches);
		matches.setIntercellSpacing(new Dimension(0,1));
		matches.setRowHeight(matches.getRowHeight()+10);
		matches.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		match.add(scroll, BorderLayout.CENTER);
		match.add(searchPanel, BorderLayout.SOUTH);
		match.revalidate();
		match.repaint();
	}

	public void show() {
		ControlWin = new Winners_con();
		ControlRC = new Races_con();
		seeWinners = new JButton("See winners of the track");
        add = new JButton("Add", new ImageIcon("./images/add.png"));
        delete = new JButton("Delete", new ImageIcon("./images/delete.png"));
        edit = new JButton("Edit", new ImageIcon("./images/edit.png"));
        add.setToolTipText("Add information about races");
        delete.setToolTipText("Delete information about races");
        edit.setToolTipText("Edit information about races");
		match = new JFrame("Race");
		match.setSize(640, 480);
		match.setVisible(true);
		match.setExtendedState(MAXIMIZED_BOTH);
		match.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        toolBar = new JToolBar("Toolbar");
        toolBar.add(add);
        toolBar.add(delete);
        toolBar.add(edit);
		match.setLayout(new BorderLayout());
		match.add(toolBar, BorderLayout.NORTH);
		String [] columns = {"Id", "Track", "Distance [km]", "Data"};
		String [][] data = ControlRC.show();
				//{{"Dubai Autodrome", "4,574","21-02-2010"}, {"Korea Circuit", "4,574", "23-12-2011"}, {"Igora Drive", "4,086","01-02-2012"}, {"Sochi Autodrome", "5,848","12-08-2018"}, {"Shanghai Autodrome", "5,451","16-10-2015"}, {"Moscow Raceway", "3,955","01-05-2020"}};
		comboBox = new JComboBox(new String[]{"Id", "Track", "Distance [km]", "Data"});
        textSearch = new JTextField();
        textSearch.setColumns(20);
        search = new JButton("Search");
        match.getRootPane().setDefaultButton(search);
// remove the binding for pressed
        match.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("ENTER"), "none");
// retarget the binding for released
        match.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke("released ENTER"), "press");
        searchPanel = new JPanel();
        searchPanel.add(comboBox);
        searchPanel.add(textSearch);
        searchPanel.add(search);
		searchPanel.add(seeWinners);
        match.add(searchPanel,BorderLayout.SOUTH);
		model = new DefaultTableModel(data, columns);
		matches = new JTable(model);
		scroll = new JScrollPane(matches);
		match.add(scroll, BorderLayout.CENTER);
		match.setVisible(true);
        matches.setIntercellSpacing(new Dimension(0,1));
        matches.setRowHeight(matches.getRowHeight()+10);
        matches.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        match.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        match.addWindowListener(new WindowListener() {
	        public void windowOpened(WindowEvent e) {
	
	        }
	        @Override
	        public void windowClosing(WindowEvent e) {
	            Object[] options = { "Yes", "No!" };
                int n = JOptionPane.showOptionDialog(e.getWindow(), "Are you sure you want to close this window?","Confirmation", JOptionPane.YES_NO_OPTION,
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
        search.addActionListener((e) -> {
            if (model.getRowCount() != 0) {
                TableRowSorter<TableModel> sorter = new TableRowSorter<TableModel>(((DefaultTableModel) model));
                sorter.setStringConverter(new TableStringConverter() {
                    public String toString(TableModel model, int row, int column) {
                        return model.getValueAt(row, column).toString().toLowerCase();
                    }
                });
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + textSearch.getText().toLowerCase()));
                matches.setRowSorter(sorter);
            }
        });
		add.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JTextField track = new JTextField(20);
				JTextField distance = new JTextField(20);
				JTextField data = new JTextField(20);
				JPanel myPanel = new JPanel();
				myPanel.add(new JLabel("Track:"));
				myPanel.add(track);
				myPanel.add(new JLabel("Distance:"));
				myPanel.add(distance);
				myPanel.add(new JLabel("Data:"));
				myPanel.add(data);
				int result = JOptionPane.showConfirmDialog(null, myPanel,
						"Add a new Race", JOptionPane.OK_CANCEL_OPTION);
				if (result == JOptionPane.OK_OPTION) {
					try {
						checkName(track);
						Races r = new Races();
						r.setTrack(track.getText());
						r.setDistance(distance.getText());
						r.setData(data.getText());
						ControlRC.addToDB(r);
						JOptionPane.showMessageDialog(null, "Data is successfully added!");
					} catch (MyException ex) {
						JOptionPane.showMessageDialog(null, ex.getMessage());
					} catch (NullPointerException ex) {
						JOptionPane.showMessageDialog(null, ex.toString());
					}

				}
				loadRaces();
			}
		});
		match.setVisible(true);
		delete.addActionListener((e) -> {
			if (matches.getRowCount() > 0) {
				if (matches.getSelectedRow() != -1) {
					try {
						int id = Integer.parseInt(model.getValueAt(matches.getSelectedRow(), 0).toString());
						Object[] options = { "Yes", "No!" };
						int n = JOptionPane.showOptionDialog(match, "Are you sure you want to delete this row?","Confirmation", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
						if (n == 0) {
							ControlRC.delete(id);
							ControlWin.deleteT(id);
							model.removeRow(matches.convertRowIndexToModel(matches.getSelectedRow()));
							JOptionPane.showMessageDialog(match, "You removed the line");
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(null, "Mistake");
					}
				} else {
					JOptionPane.showMessageDialog(null, "You have not selected a line to delete");
				}
			} else {
				JOptionPane.showMessageDialog(null, "There are no entries in this window. Nothing to delete");
			}
		});
		edit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
					JPanel panel = new JPanel();
					int n = model.getColumnCount();
					int currentRow = matches.getSelectedRow();
					TextField[] fields = new TextField[n-1];
					int k = 1;
					for (int i = 0; i < n-1; i++) {
						panel.add(new Label(model.getColumnName(i+1)));
						panel.add(fields[i] = new TextField((String) model.getValueAt(currentRow, i+1), 20));
					}

					int input = JOptionPane.showConfirmDialog(match, panel, "Edit:"
							, JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
					if (input == JOptionPane.OK_OPTION) {
						int confirm = JOptionPane.OK_OPTION;
						for (int i = 0; i < n-1; i++) {
							if (fields[i].getText().equals("") || fields[i] == null) {
								confirm = JOptionPane.showConfirmDialog(null,
										"Not all fields are filled, they'll be empty. Do you want to continue?",
										"Confirm", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
								if (confirm != JOptionPane.OK_OPTION) break;
							}
						}
						if (confirm == JOptionPane.OK_OPTION) {
							boolean edited = false;
							int id = Integer.parseInt(model.getValueAt(currentRow,0).toString());
							if(ControlRC.edit(id, fields)) edited = true;
							if (edited) {
								loadRaces();
								JOptionPane.showMessageDialog(null, "Data is successfully updated!");
							}
							else JOptionPane.showMessageDialog(null, "Error while updating!", "Error",
									JOptionPane.ERROR_MESSAGE);
						}
					}
			}
		});

		seeWinners.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int race_id = Integer.parseInt(model.getValueAt(matches.getSelectedRow(), 0).toString());
				String[][] winners = ControlRC.PrintWinners(race_id);
				String[] hat = new String[]{"Name","Lastname","Place"};
				DefaultTableModel modelWinner = new DefaultTableModel(winners, hat);
				JTable tableWinner = new JTable(modelWinner);
				JScrollPane scrollBook = new JScrollPane(tableWinner);
				scrollBook.setPreferredSize(new Dimension(500,100));
				JOptionPane.showMessageDialog(null, scrollBook, "Winners of the track", JOptionPane.PLAIN_MESSAGE);
			}
		});

        match.setVisible(true);
		}
	    public void addR(String[] arr){
	        model.addRow(arr);
	    }
}
