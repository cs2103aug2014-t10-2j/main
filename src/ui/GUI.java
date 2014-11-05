package ui;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import zombietask.ZombieTask;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.KeyboardFocusManager;

import javax.swing.SwingConstants;

import java.awt.Color;
import java.util.Collections;

public class GUI {
	
	private static final String EMPTY_STRING = "";

	private JFrame frmZombietask;
	private JTextField textField;
	private JLabel label;
	private JLabel lblNewLabel;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
		

	/**
	 * Create the application.
	 */
	public GUI() {
		frmZombietask = new JFrame();
		frmZombietask.setTitle("ZombieTask");
		frmZombietask.setBounds(100, 100, 560, 350);
		frmZombietask.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmZombietask.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Enter");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ZombieTask.userInput(textField.getText());
			}
		});
		btnNewButton.setBounds(445, 277, 89, 23);
		frmZombietask.getContentPane().add(btnNewButton);
		
		textField = new JTextField();
		textField.setBounds(10, 280, 425, 20);
		frmZombietask.getContentPane().add(textField);
		textField.setColumns(10);
		textField.setFocusTraversalKeys(
                KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
                Collections.EMPTY_SET);
		textField.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(java.awt.event.KeyEvent evt) {
				switch (evt.getKeyCode()){
				case java.awt.event.KeyEvent.VK_TAB:
					ZombieTask.userInput("help " + textField.getText());
					break;
				case java.awt.event.KeyEvent.VK_ENTER:
					ZombieTask.userInput(textField.getText());
					textField.setText(EMPTY_STRING);
					break;
				case java.awt.event.KeyEvent.VK_HOME:
					ZombieTask.userInput("view agenda");
					break;
				default:
					break;
				}
		        
			}
		});
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 67, 522, 208);
		frmZombietask.getContentPane().add(scrollPane);
		
		label = new JLabel("");
		label.setVerticalAlignment(SwingConstants.TOP);
		scrollPane.setViewportView(label);
		label.setFont(new Font("Courier New", Font.BOLD, 12));
		
		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 11, 522, 45);
		frmZombietask.getContentPane().add(scrollPane_1);
		
		lblNewLabel = new JLabel("");
		lblNewLabel.setForeground(Color.LIGHT_GRAY);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		scrollPane_1.setViewportView(lblNewLabel);
		lblNewLabel.setFont(new Font("Courier New", Font.BOLD, 12));
	}
	
	public void modifyLabelText(String str) {
		label.setText(str);
	}
	
	public void modifyUpperLabel(String str) {
		lblNewLabel.setText(str);
	}
	
	public void closeWindow() {
		System.exit(0);
	}
	
	public JFrame getFrmZombietask() {
		return frmZombietask;
	}

	public void setFrmZombietask(JFrame frmZombietask) {
		this.frmZombietask = frmZombietask;
	}
}
