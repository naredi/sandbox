package se.xalan.misc;
import javax.swing.*;


import java.awt.Container;
import java.awt.event.*;
import java.awt.*;
import java.util.StringTokenizer;

public class Pilot extends JFrame {

    /**
     * 
     */
    private static final long serialVersionUID = 2378954155859184600L;
    private JButton openButton;
    private JButton removeButton;

    private JTextField fileTextField;
    private JComboBox<String> nameTextCombo;

    private JTextField typeText;
    private JTextField startText;
    private JTextField endText;
    private JList<PilotElementTemplate> list;
    private PilotController controller;

    private Container contentPane;
    private JMenuBar menuBar;
    private JMenu fileMenu, editMenu;
    private JMenu openMenu, saveMenu;
    private JMenuItem openTemplateItem, saveTemplateItem;
    private JMenuItem closeItem, addItem, removeItem;

    private String tagName;
    private String description;

    public Pilot() {

	controller = new PilotController(this);
	Font font = new Font("Monospaced", Font.PLAIN, 14);
	contentPane = getContentPane();

	setSize(600, 600);
	setResizable(false);
	setLocation(200, 50);
	setTitle("CSV-converter");

	/********** Menu ***************/

	menuBar = new JMenuBar();
	setJMenuBar(menuBar);

	fileMenu = new JMenu("File");
	editMenu = new JMenu("Edit");
	menuBar.add(fileMenu);
	menuBar.add(editMenu);

	openMenu = new JMenu("Open");
	openTemplateItem = new JMenuItem("XSD");
	openTemplateItem.addActionListener(new OpenTemplateListener());
	openMenu.add(openTemplateItem);
	fileMenu.add(openMenu);

	saveMenu = new JMenu("Save");
	saveTemplateItem = new JMenuItem("XSD");
	saveTemplateItem.addActionListener(new SaveTemplateListener());
	saveMenu.add(saveTemplateItem);
	fileMenu.add(saveMenu);

	fileMenu.addSeparator();

	closeItem = new JMenuItem("Exit");
	closeItem.addActionListener(new CloseListener());
	fileMenu.add(closeItem);

	addItem = new JMenuItem("Add");
	addItem.addActionListener(new AddListener());
	editMenu.add(addItem);

	removeItem = new JMenuItem("Remove");
	removeItem.addActionListener(new RemoveListener());
	editMenu.add(removeItem);

	/*********** South panel ********************/

	openButton = new JButton("Run"); // byta namn på variabler + lyssnare?
	openButton.addActionListener(new OpenListener());

	fileTextField = new JTextField(35);
	fileTextField.setEditable(false);

	JPanel topPanel = new JPanel();
	topPanel.add(openButton);
	topPanel.add(fileTextField);
	contentPane.add(topPanel, "South");

	/************* Input panel ***********************/

	JPanel inputPanel = new JPanel();
	inputPanel.setBorder(BorderFactory.createEmptyBorder(20, // top
		30, // left
		20, // bottom
		20)// right
		);
	inputPanel.setLayout(new BorderLayout(20, 0));

	JPanel labelPanel = new JPanel();
	labelPanel.add(new JLabel("Element name"));
	labelPanel.add(new JLabel("Element type"));
	labelPanel.add(new JLabel("Start position"));
	labelPanel.add(new JLabel("End position"));
	labelPanel.setLayout(new GridLayout(4, 1));
	labelPanel.setBorder(BorderFactory.createEmptyBorder(0, // top
		0, // left
		0, // bottom
		0)// right
		);

	JPanel textFieldPanel = new JPanel();
	textFieldPanel.setLayout(new GridLayout(4, 1));
	textFieldPanel.setBorder(BorderFactory.createEmptyBorder(0, // top
		0, // left
		0, // bottom
		100)// right
		);

	nameTextCombo = new JComboBox<String>();
	nameTextCombo.setBackground(new Color(230, 230, 230));
	nameTextCombo.setEditable(false);
	nameTextCombo.setFont(font);
	nameTextCombo.addItem("transId    Transaction number");
	nameTextCombo.addItem("currency   Valuta");
	nameTextCombo.addItem("date       Date of issue");
	nameTextCombo.addItem("cardType   Card issuer");
	nameTextCombo.addItem("country    Country");
	nameTextCombo.addItem("cardNbr    Card number");
	nameTextCombo.addItem("cvx2       CVX2 code");
	nameTextCombo.addActionListener(new AddListener());
	textFieldPanel.add(nameTextCombo);

	typeText = new JTextField();
	typeText.setEditable(false);
	textFieldPanel.add(typeText);

	startText = new JTextField();
	startText.setEditable(false);
	textFieldPanel.add(startText);

	endText = new JTextField();
	endText.setEditable(false);
	textFieldPanel.add(endText);

	inputPanel.add(labelPanel, BorderLayout.WEST);
	inputPanel.add(textFieldPanel, BorderLayout.CENTER);

	JPanel buttonPanel = new JPanel();
	buttonPanel.setLayout(new GridLayout(1, 2));
	buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, // top
		80, // left
		0, // bottom
		80)// right
		);

	removeButton = new JButton("Remove");
	removeButton.addActionListener(new RemoveListener());
	buttonPanel.add(removeButton);
	inputPanel.add(buttonPanel, BorderLayout.SOUTH);
	contentPane.add(inputPanel, "North");

	/****************** Center panel ****************************/

	list = new JList<PilotElementTemplate>();
	list.setFont(font);
	list.setVisibleRowCount(10);

	JPanel centerPanel = new JPanel();
	centerPanel.setLayout(new BorderLayout());
	JLabel titleRow = new JLabel("ELEMENT NAME         DESCRIPTION                     START END");
	titleRow.setFont(font);
	centerPanel.add(titleRow, "North");
	centerPanel.add(new JScrollPane(list), "Center");
	contentPane.add(centerPanel, "Center");

	/************* Misc ***********************************/

	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setVisible(true);

    }

    class OpenListener implements ActionListener {
	public void actionPerformed(ActionEvent event) {
	    controller.doIt("txt", "Textfil");
	}
    }

    class AddListener implements ActionListener {

	boolean cardNumberAdded = false;

	public void actionPerformed(ActionEvent event) {
	    String type = "";
	    String start = "";
	    String end = "";
	    description = "";
	    @SuppressWarnings("unchecked")
	    JComboBox<String> sourceOfEvent = (JComboBox<String>) event.getSource();
	    String comboItem = (String) sourceOfEvent.getSelectedItem();
	    StringTokenizer tokenizer = new StringTokenizer(comboItem);
	    tagName = tokenizer.nextToken();
	    while (tokenizer.hasMoreTokens())
		description += (tokenizer.nextToken() + " ");

	    if (tagName.equals("transId")) {
		type = "Text";
		start = "1";
		end = "12";
	    } else if (tagName.equals("currency")) {
		type = "Text";
		start = "13";
		end = "14";
	    } else if (tagName.equals("date")) {
		type = "Date";
		start = "15";
		end = "22";
	    } else if (tagName.equals("cardType")) {
		type = "Text";
		start = "23";
		end = "47";
	    } else if (tagName.equals("country")) {
		type = "Text";
		start = "48";
		end = "49";
	    } else if (tagName.equals("cardNbr")) {
		type = "Text";
		start = "50";
		end = "65";
		cardNumberAdded = true;
	    } else if (tagName.equals("cvx2")) {
		if (!cardNumberAdded) {
		    java.awt.Toolkit.getDefaultToolkit().beep();
		    showDialog("To use this parameter\n" + "the card number is needed.\n" + "Please add.");
		    return;
		}
		type = "cvx2Code";
		start = "66";
		end = "69";
		cardNumberAdded = false;
	    }
	    typeText.setText(type);
	    startText.setText(start);
	    endText.setText(end);
	    controller.addElementTemplate();
	}
    }

    class RemoveListener implements ActionListener {
	public void actionPerformed(ActionEvent event) {
	    controller.remove(list.getSelectedIndex());
	}
    }

    class CloseListener implements ActionListener {
	public void actionPerformed(ActionEvent event) {
	    System.exit(0);
	}
    }

    class SaveTemplateListener implements ActionListener {
	public void actionPerformed(ActionEvent event) {
	    controller.saveTemplate("ptf", "Saved templates");
	}
    }

    class OpenTemplateListener implements ActionListener {
	public void actionPerformed(ActionEvent event) {
	    controller.openTemplate("ptf", "Saved templates");
	}
    }

    private void showDialog(String str) {
	JOptionPane.showMessageDialog(this, str);
    }

    public String getDescription() {
	return description;
    }

    public String getTagName() {
	return tagName;
    }

    public String getTypeText() {
	return typeText.getText();
    }

    public int getStart() {
	int i = -1;
	try {
	    i = Integer.parseInt(startText.getText());
	} catch (Exception e) {
	}
	return i;
    }

    public int getEnd() {
	int i = -1;
	try {
	    i = Integer.parseInt(endText.getText());
	} catch (Exception e) {
	}
	return i;
    }

    public void setFileText(String s) {
	fileTextField.setText(s);
    }

    public void updateList(PilotElementTemplate[] arr) {
	list.setListData(arr);
    }

    public static void main(String[] args) {
	new Pilot();

	// Sätt L&F för gällande plattform
	try {
	    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	} catch (UnsupportedLookAndFeelException ulfe) {
	    System.out.println("Fel vid skapande av SystemLAF: " + ulfe);
	} catch (ClassNotFoundException cnfe) {
	    System.out.println("Fel vid skapande av SystemLAF: " + cnfe);
	} catch (InstantiationException ie) {
	    System.out.println("Fel vid skapande av SystemLAF: " + ie);
	} catch (IllegalAccessException iae) {
	    System.out.println("Fel vid skapande av SystemLAF: " + iae);
	}
    }
}
