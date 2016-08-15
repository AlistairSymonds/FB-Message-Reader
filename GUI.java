package fbMessageReaderGUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import java.awt.Color;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JList;

public class GUI {
	
	public static File file = null;
	
	private JFrame frmFacebookMessageReader;
	private final JCheckBox createStatsToggle = new JCheckBox("Generate statistics report");
	private JTextField dayStart;
	private JTextField monthStart;
	private JTextField yearStart;
	private JTextField dayFinish;
	private JTextField monthFinish;
	private JTextField yearFinish;
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmFacebookMessageReader.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmFacebookMessageReader = new JFrame();
		frmFacebookMessageReader.setTitle("Facebook Message Reader");
		frmFacebookMessageReader.setBounds(100, 100, 722, 362);
		frmFacebookMessageReader.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		springLayout.putConstraint(SpringLayout.WEST, createStatsToggle, 10, SpringLayout.WEST, frmFacebookMessageReader.getContentPane());
		frmFacebookMessageReader.getContentPane().setLayout(springLayout);
		
	
		
		
		createStatsToggle.setSelected(true);
		frmFacebookMessageReader.getContentPane().add(createStatsToggle);
		
		JTextPane filePathText = new JTextPane();
		filePathText.setText("Please select a messages file");
		springLayout.putConstraint(SpringLayout.NORTH, filePathText, 11, SpringLayout.NORTH, frmFacebookMessageReader.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, filePathText, -10, SpringLayout.EAST, frmFacebookMessageReader.getContentPane());
		filePathText.setToolTipText("Path of file which will be scanned");
		filePathText.setEditable(false);
		frmFacebookMessageReader.getContentPane().add(filePathText);
		
		JTextPane statusText = new JTextPane();
		springLayout.putConstraint(SpringLayout.NORTH, statusText, -118, SpringLayout.NORTH, createStatsToggle);
		springLayout.putConstraint(SpringLayout.SOUTH, statusText, -6, SpringLayout.NORTH, createStatsToggle);
		springLayout.putConstraint(SpringLayout.EAST, statusText, 0, SpringLayout.EAST, filePathText);
		statusText.setEditable(false);
		frmFacebookMessageReader.getContentPane().add(statusText);
		
		
		JButton btnScan = new JButton("Parse messages file");
		springLayout.putConstraint(SpringLayout.WEST, statusText, 0, SpringLayout.WEST, btnScan);
		springLayout.putConstraint(SpringLayout.SOUTH, createStatsToggle, -6, SpringLayout.NORTH, btnScan);
		btnScan.setEnabled(false);
		springLayout.putConstraint(SpringLayout.NORTH, btnScan, -40, SpringLayout.SOUTH, frmFacebookMessageReader.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnScan, 10, SpringLayout.WEST, frmFacebookMessageReader.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, btnScan, -10, SpringLayout.SOUTH, frmFacebookMessageReader.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnScan, 0, SpringLayout.EAST, filePathText);
		
		
		
		JButton btnNewButton = new JButton("Select messages file");
		springLayout.putConstraint(SpringLayout.WEST, filePathText, 10, SpringLayout.EAST, btnNewButton);
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton, 11, SpringLayout.NORTH, frmFacebookMessageReader.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton, 10, SpringLayout.WEST, frmFacebookMessageReader.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, btnNewButton, 31, SpringLayout.NORTH, frmFacebookMessageReader.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnNewButton, 145, SpringLayout.WEST, frmFacebookMessageReader.getContentPane());
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(frmFacebookMessageReader);
				if (result == JFileChooser.APPROVE_OPTION) {
				    file = fileChooser.getSelectedFile();
				    System.out.println("Selected file: " + file.getAbsolutePath());
					filePathText.setText(file.getAbsolutePath());
					btnScan.setEnabled(true);
				}
				
			}
		});
		frmFacebookMessageReader.getContentPane().add(btnNewButton);
		
		JTextPane txtpnMessagesWillBe = new JTextPane();
		springLayout.putConstraint(SpringLayout.WEST, txtpnMessagesWillBe, 10, SpringLayout.WEST, frmFacebookMessageReader.getContentPane());
		txtpnMessagesWillBe.setBackground(Color.LIGHT_GRAY);
		txtpnMessagesWillBe.setEditable(false);
		txtpnMessagesWillBe.setText("Messages will be outputted to the \"threads\" folder in the same folder as this program");
		springLayout.putConstraint(SpringLayout.NORTH, txtpnMessagesWillBe, 6, SpringLayout.SOUTH, filePathText);
		frmFacebookMessageReader.getContentPane().add(txtpnMessagesWillBe);
		
		JLabel lblDateFilters = new JLabel("Date Filters:");
		springLayout.putConstraint(SpringLayout.NORTH, lblDateFilters, 6, SpringLayout.SOUTH, txtpnMessagesWillBe);
		springLayout.putConstraint(SpringLayout.WEST, lblDateFilters, 0, SpringLayout.WEST, createStatsToggle);
		frmFacebookMessageReader.getContentPane().add(lblDateFilters);
		
		dayStart = new JTextField();
		dayStart.setToolTipText("DD");
		springLayout.putConstraint(SpringLayout.NORTH, dayStart, 6, SpringLayout.SOUTH, txtpnMessagesWillBe);
		springLayout.putConstraint(SpringLayout.WEST, dayStart, 4, SpringLayout.EAST, lblDateFilters);
		springLayout.putConstraint(SpringLayout.EAST, dayStart, 27, SpringLayout.EAST, lblDateFilters);
		dayStart.setText("1");
		frmFacebookMessageReader.getContentPane().add(dayStart);
		dayStart.setColumns(10);
		
		JLabel label = new JLabel("/");
		springLayout.putConstraint(SpringLayout.NORTH, label, 6, SpringLayout.SOUTH, txtpnMessagesWillBe);
		springLayout.putConstraint(SpringLayout.WEST, label, 6, SpringLayout.EAST, dayStart);
		springLayout.putConstraint(SpringLayout.EAST, label, 10, SpringLayout.EAST, dayStart);
		frmFacebookMessageReader.getContentPane().add(label);
		
		monthStart = new JTextField();
		monthStart.setToolTipText("MM");
		springLayout.putConstraint(SpringLayout.NORTH, monthStart, 6, SpringLayout.SOUTH, txtpnMessagesWillBe);
		springLayout.putConstraint(SpringLayout.WEST, monthStart, 7, SpringLayout.EAST, label);
		springLayout.putConstraint(SpringLayout.EAST, monthStart, 30, SpringLayout.EAST, label);
		monthStart.setText("1");
		frmFacebookMessageReader.getContentPane().add(monthStart);
		monthStart.setColumns(10);
		
		yearStart = new JTextField();
		yearStart.setToolTipText("YYYY");
		springLayout.putConstraint(SpringLayout.NORTH, yearStart, 6, SpringLayout.SOUTH, txtpnMessagesWillBe);
		yearStart.setText("1970");
		frmFacebookMessageReader.getContentPane().add(yearStart);
		yearStart.setColumns(10);
		
		JLabel label_1 = new JLabel("/");
		springLayout.putConstraint(SpringLayout.WEST, yearStart, 5, SpringLayout.EAST, label_1);
		springLayout.putConstraint(SpringLayout.EAST, yearStart, 42, SpringLayout.EAST, label_1);
		springLayout.putConstraint(SpringLayout.NORTH, label_1, 6, SpringLayout.SOUTH, txtpnMessagesWillBe);
		springLayout.putConstraint(SpringLayout.WEST, label_1, 6, SpringLayout.EAST, monthStart);
		frmFacebookMessageReader.getContentPane().add(label_1);
		
		JLabel lblTo = new JLabel("to");
		springLayout.putConstraint(SpringLayout.WEST, lblTo, 6, SpringLayout.EAST, yearStart);
		springLayout.putConstraint(SpringLayout.SOUTH, lblTo, 0, SpringLayout.SOUTH, lblDateFilters);
		frmFacebookMessageReader.getContentPane().add(lblTo);
		
		dayFinish = new JTextField();
		dayFinish.setToolTipText("DD");
		springLayout.putConstraint(SpringLayout.NORTH, dayFinish, 6, SpringLayout.SOUTH, txtpnMessagesWillBe);
		springLayout.putConstraint(SpringLayout.WEST, dayFinish, 5, SpringLayout.EAST, lblTo);
		springLayout.putConstraint(SpringLayout.EAST, dayFinish, 28, SpringLayout.EAST, lblTo);
		dayFinish.setText("1");
		dayFinish.setColumns(10);
		frmFacebookMessageReader.getContentPane().add(dayFinish);
		
		monthFinish = new JTextField();
		monthFinish.setToolTipText("MM");
		springLayout.putConstraint(SpringLayout.NORTH, monthFinish, 6, SpringLayout.SOUTH, txtpnMessagesWillBe);
		monthFinish.setText("1");
		monthFinish.setColumns(10);
		frmFacebookMessageReader.getContentPane().add(monthFinish);
		
		JLabel label_3 = new JLabel("/");
		springLayout.putConstraint(SpringLayout.NORTH, label_3, 6, SpringLayout.SOUTH, txtpnMessagesWillBe);
		springLayout.putConstraint(SpringLayout.WEST, label_3, 6, SpringLayout.EAST, monthFinish);
		springLayout.putConstraint(SpringLayout.EAST, label_3, -425, SpringLayout.EAST, frmFacebookMessageReader.getContentPane());
		frmFacebookMessageReader.getContentPane().add(label_3);
		
		yearFinish = new JTextField();
		yearFinish.setToolTipText("YYYY");
		springLayout.putConstraint(SpringLayout.NORTH, yearFinish, 6, SpringLayout.SOUTH, txtpnMessagesWillBe);
		springLayout.putConstraint(SpringLayout.WEST, yearFinish, 6, SpringLayout.EAST, label_3);
		springLayout.putConstraint(SpringLayout.EAST, yearFinish, 43, SpringLayout.EAST, label_3);
		yearFinish.setText("2025");
		yearFinish.setColumns(10);
		frmFacebookMessageReader.getContentPane().add(yearFinish);
		
		JLabel label_2 = new JLabel("/");
		springLayout.putConstraint(SpringLayout.WEST, monthFinish, 6, SpringLayout.EAST, label_2);
		springLayout.putConstraint(SpringLayout.EAST, monthFinish, 29, SpringLayout.EAST, label_2);
		springLayout.putConstraint(SpringLayout.NORTH, label_2, 6, SpringLayout.SOUTH, txtpnMessagesWillBe);
		springLayout.putConstraint(SpringLayout.WEST, label_2, 6, SpringLayout.EAST, dayFinish);
		frmFacebookMessageReader.getContentPane().add(label_2);
		
		
		btnScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String filePath = "";
				try{
					filePath = file.getAbsolutePath();
				} catch (NullPointerException e) {
					System.out.println("File not selected");
				}
				statusText.setText("Error in scanning, please close all open files outputted by the program");
				SimpleDateFormat ft = new SimpleDateFormat("dd MM yyyy");
				String startStr = (dayStart.getText() + " " + monthStart.getText() + " " + yearStart.getText());
				String finishStr = (dayFinish.getText() + " " + monthFinish.getText() + " " + yearFinish.getText());
				Date startDate;
				Date finishDate;
				try {
					startDate = ft.parse(startStr);
					finishDate = ft.parse(finishStr);
					String outDetails = MainFBParse.scanAll(filePath, createStatsToggle.isSelected(), startDate.getTime(), finishDate.getTime());
					statusText.setText(outDetails);
				} catch (ParseException e) {
					statusText.setText("Error parsing date, please use valid values");
				}
				

			}
		});
		frmFacebookMessageReader.getContentPane().add(btnScan);
		
		
		
	}
	
	
}
