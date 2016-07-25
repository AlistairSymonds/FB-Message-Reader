package fbMessageReaderGUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.SpringLayout;
import java.awt.Color;

public class GUI {
	
	public static File file = null;
	
	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
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
		frame = new JFrame();
		frame.setBounds(100, 100, 702, 193);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		frame.getContentPane().setLayout(springLayout);
		
		JTextPane filePathText = new JTextPane();
		filePathText.setText("Please select a messages file");
		springLayout.putConstraint(SpringLayout.NORTH, filePathText, 11, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, filePathText, -10, SpringLayout.EAST, frame.getContentPane());
		filePathText.setToolTipText("Path of file which will be scanned");
		filePathText.setEditable(false);
		frame.getContentPane().add(filePathText);
		
		JTextPane statusText = new JTextPane();
		springLayout.putConstraint(SpringLayout.NORTH, statusText, -76, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, statusText, 0, SpringLayout.EAST, filePathText);
		statusText.setEditable(false);
		frame.getContentPane().add(statusText);
		
		
		JButton btnScan = new JButton("Parse messages file");
		btnScan.setEnabled(false);
		springLayout.putConstraint(SpringLayout.WEST, statusText, 0, SpringLayout.WEST, btnScan);
		springLayout.putConstraint(SpringLayout.SOUTH, statusText, -6, SpringLayout.NORTH, btnScan);
		springLayout.putConstraint(SpringLayout.NORTH, btnScan, -40, SpringLayout.SOUTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnScan, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, btnScan, -10, SpringLayout.SOUTH, frame.getContentPane());
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
				String outDetails = MainFBParse.scanAll(filePath);
				statusText.setText(outDetails);
			}
		});
		frame.getContentPane().add(btnScan);
		
		JButton btnNewButton = new JButton("Select messages file");
		springLayout.putConstraint(SpringLayout.WEST, filePathText, 10, SpringLayout.EAST, btnNewButton);
		springLayout.putConstraint(SpringLayout.NORTH, btnNewButton, 11, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, btnNewButton, 10, SpringLayout.WEST, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.SOUTH, btnNewButton, 31, SpringLayout.NORTH, frame.getContentPane());
		springLayout.putConstraint(SpringLayout.EAST, btnNewButton, 145, SpringLayout.WEST, frame.getContentPane());
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fileChooser.showOpenDialog(frame);
				if (result == JFileChooser.APPROVE_OPTION) {
				    file = fileChooser.getSelectedFile();
				    System.out.println("Selected file: " + file.getAbsolutePath());
					filePathText.setText(file.getAbsolutePath());
					btnScan.setEnabled(true);
				}
				
			}
		});
		frame.getContentPane().add(btnNewButton);
		
		JTextPane txtpnMessagesWillBe = new JTextPane();
		txtpnMessagesWillBe.setBackground(Color.LIGHT_GRAY);
		txtpnMessagesWillBe.setEditable(false);
		txtpnMessagesWillBe.setText("Messages will be outputted to the \"threads\" folder in the same folder as this program");
		springLayout.putConstraint(SpringLayout.NORTH, txtpnMessagesWillBe, 6, SpringLayout.SOUTH, filePathText);
		springLayout.putConstraint(SpringLayout.WEST, txtpnMessagesWillBe, 0, SpringLayout.WEST, statusText);
		frame.getContentPane().add(txtpnMessagesWillBe);
		
		
	}
}
