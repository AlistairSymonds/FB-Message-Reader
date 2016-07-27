package fbMessageReaderGUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import java.awt.Color;
import javax.swing.JCheckBox;

public class GUI {
	
	public static File file = null;
	
	private JFrame frmFacebookMessageReader;
	private final JCheckBox createStatsToggle = new JCheckBox("Generate statistics report");

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
		
		btnScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String filePath = "";
				try{
					filePath = file.getAbsolutePath();
				} catch (NullPointerException e) {
					System.out.println("File not selected");
				}
				statusText.setText("Parsing messages file, please be patient, this may take a minute...");
				String outDetails = MainFBParse.scanAll(filePath, createStatsToggle.isSelected());
				statusText.setText(outDetails);
			}
		});
		frmFacebookMessageReader.getContentPane().add(btnScan);
		
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
		
		
		
	}
}
