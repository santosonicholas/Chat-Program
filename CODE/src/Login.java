import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField txtName;
	private JTextField txtAddress;
	private JLabel lblIpAddress;
	private JTextField txtPort;
	private JLabel lblPort;
	private JLabel ipDesc;
	private JLabel portDesc;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Login() {
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		setResizable(false);
		
		setTitle("LOGIN FORM CHAT PROGRAM JARKOM");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 391, 438);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(135, 206, 235));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		txtName = new JTextField();
		txtName.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					String name = txtName.getText();
					String address = txtAddress.getText();
					int port = Integer.parseInt(txtPort.getText());
					login(name, address, port);
				}
			}
		});
		txtName.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtName.setBounds(139, 56, 188, 32);
		contentPane.add(txtName);
		txtName.setColumns(10);
		
		JLabel lblName = new JLabel("Name :");
		lblName.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblName.setBounds(28, 59, 64, 23);
		contentPane.add(lblName);
		
		txtAddress = new JTextField("127.0.0.1");
		txtAddress.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtAddress.setColumns(10);
		txtAddress.setBounds(139, 144, 188, 32);
		txtAddress.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					String name = txtName.getText();
					String address = txtAddress.getText();
					int port = Integer.parseInt(txtPort.getText());
					login(name, address, port);
				}
			}
		});
		contentPane.add(txtAddress);
		
		lblIpAddress = new JLabel("IP Address :");
		lblIpAddress.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblIpAddress.setBounds(28, 147, 101, 23);
		contentPane.add(lblIpAddress);
		
		txtPort = new JTextField("8888");
		txtPort.setFont(new Font("Tahoma", Font.PLAIN, 15));
		txtPort.setColumns(10);
		txtPort.setBounds(139, 235, 188, 32);
		txtPort.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					String name = txtName.getText();
					String address = txtAddress.getText();
					int port = Integer.parseInt(txtPort.getText());
					login(name, address, port);
				}
			}
		});
		contentPane.add(txtPort);
		
		lblPort = new JLabel("Port :");
		lblPort.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblPort.setBounds(28, 242, 75, 14);
		contentPane.add(lblPort);
		
		ipDesc = new JLabel("ex. 127.0.0.1, 127.0.0.2, etc");
		ipDesc.setFont(new Font("Tahoma", Font.ITALIC, 12));
		ipDesc.setBounds(149, 183, 169, 14);
		contentPane.add(ipDesc);
		
		portDesc = new JLabel("ex. 8888");
		portDesc.setFont(new Font("Tahoma", Font.ITALIC, 12));
		portDesc.setBounds(200, 278, 64, 14);
		contentPane.add(portDesc);
		
		JButton btnLogin = new JButton("LOGIN");
		btnLogin.setFont(new Font("Cambria", Font.BOLD, 15));
		btnLogin.setForeground(new Color(0, 0, 0));
		btnLogin.setBackground(new Color(0, 255, 255));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String name = txtName.getText();
				String address = txtAddress.getText();
				int port = Integer.parseInt(txtPort.getText());
				login(name, address, port);
			}
		});
		btnLogin.setBounds(102, 325, 177, 23);
		contentPane.add(btnLogin);
	}
	
	private void login(String name, String address, int port) { //LOGIN
		dispose();
		new ClientWindow(name, address, port);
	}
}
