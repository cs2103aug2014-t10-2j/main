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
import java.util.Stack;

/**
 * @author a0108553h
 */

public class GUI {
	
	private static final String EMPTY_STRING = "";

	private JFrame frmZombietask;
	private JTextField textField;
	private JLabel label;
	private JLabel lblNewLabel;
	private JScrollPane scrollPane;
	private JScrollPane scrollPane_1;
	private Stack<String> pastTextField;
	private Stack<String> futureTextField;
		
	/**
	 * Create the application.
	 */
	
	@SuppressWarnings("unchecked")
	public GUI() {
		
		pastTextField = new Stack<String>();
		futureTextField = new Stack<String>();
		
		frmZombietask = new JFrame();
		frmZombietask.setTitle("ZombieTask");
		frmZombietask.setBounds(100, 100, 560, 350);
		frmZombietask.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmZombietask.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Enter");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addCurrentTextToPastList();
				ZombieTask.userInput(textField.getText());
				textField.setText(EMPTY_STRING);
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
					addCurrentTextToPastList();
					ZombieTask.userInput(textField.getText());
					textField.setText(EMPTY_STRING);
					break;
				case java.awt.event.KeyEvent.VK_HOME:
					ZombieTask.userInput("view agenda");
					break;
				case java.awt.event.KeyEvent.VK_UP:
					if (!pastTextField.isEmpty()){
						futureTextField.push(textField.getText());
						textField.setText(pastTextField.pop());
					}
					break;
				case java.awt.event.KeyEvent.VK_DOWN:
					if (!futureTextField.isEmpty()){
						pastTextField.push(textField.getText());
						textField.setText(futureTextField.pop());
					}
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
	
	public void addCurrentTextToPastList(){
		pastTextField.push(textField.getText());
		if (!futureTextField.isEmpty()){
			futureTextField.clear();
		}
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
