package se.xalan.misc;

import javax.swing.*;


import java.awt.Container;
import java.awt.event.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: folke
 * Date: 2005-jul-17
 * Time: 01:24:24
 * To change this template use File | Settings | File Templates.
 */
public class GuiClass extends JFrame{

    private JButton convertButton;

    private JTextField fileTextField;
    private JComboBox nameTextCombo;
    private JList list;
    private FileConverter controller;

    private Container contentPane;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenu convertMenu;
    private JMenuItem convertTemplateItem;
    private JMenuItem closeItem;

    public GuiClass(){

        controller = new FileConverter(this);

        setSize(600, 600);
        setResizable(false);
        setLocation(200,50);
        setTitle("Deltagar-generator");
        contentPane = getContentPane();
        Font font = new Font("Monospaced", Font.PLAIN, 14);


        /**********Menyn***************/
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        fileMenu = new JMenu("Arkiv");
        menuBar.add(fileMenu);

        convertMenu = new JMenu("Öppna");
        convertTemplateItem = new JMenuItem("Konvertera...");
        convertTemplateItem.addActionListener(new OpenListener());

        closeItem = new JMenuItem("Avsluta");
        closeItem.addActionListener(new CloseListener());

        fileMenu.add(convertMenu);
        fileMenu.addSeparator();
        fileMenu.add(closeItem);

        convertMenu.add(convertTemplateItem);


        /***********Southpanel********************/

        convertButton = new JButton("Utförknapp..."); //
        convertButton.addActionListener(new OpenListener());
        fileTextField = new JTextField(35);
        fileTextField.setEditable(false);
        JPanel topPanel = new JPanel();
        topPanel.add(convertButton);
        topPanel.add(fileTextField);
        contentPane.add(topPanel, "South");


        /*************Inputpanel***********************/
        JPanel inputPanel = new JPanel();
        inputPanel.setBorder(BorderFactory.createEmptyBorder(
                20, //top
                30, //left
                20, //bottom
                20 )//right
        );
        inputPanel.setLayout(new BorderLayout(20,0));
        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(4,1));
        labelPanel.setBorder(BorderFactory.createEmptyBorder(
                0, //top
                0, //left
                0, //bottom
                0 )//right
        );
        labelPanel.add(new JLabel("Inladdningsvariabel"));
        labelPanel.add(new JLabel("Typ av data"));
        labelPanel.add(new JLabel("Tab-läge"));
        JPanel textFieldPanel = new JPanel();
        textFieldPanel.setLayout(new GridLayout(4,1));
        textFieldPanel.setBorder(BorderFactory.createEmptyBorder(
                0, //top
                0, //left
                0, //bottom
                100 )//right
        );
        //nameText = new JTextField();
        //textFieldPanel.add(nameText);
        nameTextCombo = new JComboBox();
        nameTextCombo.setBackground(new Color(230, 230, 230));
        nameTextCombo.setEditable(false);
        nameTextCombo.setFont(font);
        nameTextCombo.addItem("twinnr     Tvillingnummer");
        nameTextCombo.addItem("pairid     Par-ID (twinnr minus tvab)");
        nameTextCombo.addItem("tvab       Individ-ID (1 eller 2)");
        nameTextCombo.addItem("personnr   Personnummer");
        nameTextCombo.addItem("birthday   Födelsedatum");
        nameTextCombo.addItem("last_first_name surname, christian_name");
        nameTextCombo.addItem("christian_name Förnamn");
        nameTextCombo.addItem("middle_name Mellannamn");
        nameTextCombo.addItem("surname    Efternamn");
        nameTextCombo.addItem("co_address Care/off-adress");
        nameTextCombo.addItem("fo_address Specialadress");
        nameTextCombo.addItem("address    Gatuadress");
        nameTextCombo.addItem("postalCode Postnummer");
        nameTextCombo.addItem("city       Postort");
        nameTextCombo.addItem("countyCode Län");
        nameTextCombo.addItem("council    Kommun");
        nameTextCombo.addItem("parish     Församling");
        nameTextCombo.addItem("country    Nationalitet");
        nameTextCombo.addItem("address_source Adresskälla");
        nameTextCombo.addItem("telephone  Telefon");
        nameTextCombo.addItem("dead       Död");
        nameTextCombo.addItem("death_date Dödsdatum");
        nameTextCombo.addItem("sex        Kön");

        nameTextCombo.addItem("zygosity   Tvillingrelation");
        nameTextCombo.addItem("ptvab     ");
        nameTextCombo.addItem("rantvab   ");
        nameTextCombo.addItem("prantvab   ");
        nameTextCombo.addItem("kinship_no1  ");
        nameTextCombo.addItem("kinship_relation1      ");
        nameTextCombo.addItem("kinship_no2      ");
        nameTextCombo.addItem("kinship_relation2   ");
        nameTextCombo.addItem("civilstatus  Civilstånd");
        nameTextCombo.addItem("latest_match_code  ");
        nameTextCombo.addItem("eff_date   ");
        nameTextCombo.addItem("xpnr   ");
        nameTextCombo.addItem("nottwin    ");
/*
nameTextCombo.addActionListener(new AddListener());
*/
        textFieldPanel.add(nameTextCombo);

        JTextField typeText = new JTextField();
        textFieldPanel.add(typeText);
        typeText.setEditable(false);

        JTextField tabText = new JTextField();
        textFieldPanel.add(tabText);
        tabText.setEditable(false);

        inputPanel.add(labelPanel,BorderLayout.WEST);
        inputPanel.add(textFieldPanel,BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1,2));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(
                10, //top
                80, //left
                0, //bottom
                80 )//right
        );
        //      addButton = new JButton("Lägg till");
        //      addButton.addActionListener(new AddListener());
        inputPanel.add(buttonPanel,BorderLayout.SOUTH);
        contentPane.add(inputPanel, "North");


        /******************Centerpanel****************************/
        //Font font = new Font("Monospaced", Font.PLAIN, 14);
        list = new JList();
        list.setFont(font);
        list.setVisibleRowCount(10);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BorderLayout());
        JLabel titleRow = new JLabel("INLADDNINGSVARIABEL       TYP AV DATA        TAB-LÄGE");
        titleRow.setFont(font);
        centerPanel.add(titleRow,"North");
        centerPanel.add(new JScrollPane(list), "Center");
        contentPane.add(centerPanel, "Center");

        /*************Övrigt***********************************/

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    class OpenListener implements ActionListener{
        public void actionPerformed(ActionEvent eve){
            controller.openInfile("txt", "Inmatningsfil");
        }
    }



    class CloseListener implements ActionListener{
        public void actionPerformed(ActionEvent eve){
            System.exit(0);
        }
    }

    public void setFileText(String s){
        fileTextField.setText(s);
    }



    public void updateList(Object[] arr){
        list.setListData(arr);
    }

    public static void main(String[] args){
        new GuiClass();

// Sätt L&F för gällande plattform
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch(UnsupportedLookAndFeelException ulfe) {
            System.out.println("Fel vid skapande av SystemLAF: " + ulfe);
        } catch(ClassNotFoundException cnfe) {
            System.out.println("Fel vid skapande av SystemLAF: " + cnfe);
        } catch(InstantiationException ie) {
            System.out.println("Fel vid skapande av SystemLAF: " + ie);
        }catch(IllegalAccessException iae) {
            System.out.println("Fel vid skapande av SystemLAF: " + iae);
        }
    }
}
