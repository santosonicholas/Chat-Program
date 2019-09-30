import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;


import java.awt.Color;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingConstants;

public class ClientWindow extends JFrame implements Runnable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JTextField messageField;
	private JTextArea textArea;
	private DefaultCaret caret;
	private Thread run,listen;
	private boolean running = false;
	private Client client;
	private JLabel lblUser;
	
	private JMenuBar menuBar;
	private JMenu mnFile;
	private JMenuItem mntmExit;
	private JMenuItem mntmOnlineUsers;
	
	private OnlineUsers users;
	
	public ClientWindow(String name, String address, int port) {
		setTitle("CHAT PROGRAM JARKOM");
		client = new Client(name, address, port);
		boolean connect = client.openConnection(address);
		if(!connect) {
			System.err.println("Connection failed!");
			printToConsole("Connection failed!");
		}
		makeWindow();
		printToConsole("Attempt a connection to "+ address+" :"+ port + ", user: "+name);
		lblUser.setText(lblUser.getText()+name);
		String connection = "/c/" + name + "/e/";
		client.send(connection.getBytes());
		users = new OnlineUsers();
		running = true;
		run = new Thread(this, "Running");
		run.start();
		
	}
	
	private void makeWindow() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}catch (Exception e1) {
			e1.printStackTrace();
		}
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(727,546);
		
		setBounds(100, 100, 708, 476);
		
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnFile = new JMenu("File");
		menuBar.add(mnFile);
		

		mntmOnlineUsers = new JMenuItem("Online Users");
		mntmOnlineUsers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				users.setVisible(true);
			}
		});
		mnFile.add(mntmOnlineUsers);
		
		mntmExit = new JMenuItem("Disconnect");
		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String disconnect = "/d/" + client.getID() + "/e/";
				send(disconnect, false);
				running = false;
				client.quit();
				dispose();
			}
		});
		mnFile.add(mntmExit);
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		textArea = new JTextArea();
		textArea.setForeground(new Color(255, 255, 255));
		textArea.setBackground(new Color(0, 0, 0));
		textArea.setFont(new Font("Arial", Font.PLAIN, 14));
		textArea.setEditable(false);
		caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE); //if scroll, then type message go back to the new message
		
		JScrollPane scrollPane = new JScrollPane(textArea);
		getContentPane().add(scrollPane);
		
		lblUser = new JLabel("Your name is ");
		lblUser.setBackground(new Color(255, 228, 225));
		lblUser.setFont(new Font("Trajan Pro 3", Font.PLAIN, 15));
		scrollPane.setColumnHeaderView(lblUser);
		
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(192, 192, 192));
		getContentPane().add(panel, BorderLayout.SOUTH);
		
		messageField = new JTextField();
		messageField.setFont(new Font("Tahoma", Font.PLAIN, 13));
		messageField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					send(messageField.getText(),true);
				}
			}
		});
		panel.add(messageField);
		messageField.setColumns(55);
		
		JButton btnSend = new JButton("SEND");
		btnSend.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnSend.setForeground(new Color(50, 205, 50));
		btnSend.setBackground(new Color(0, 255, 0));
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				send(messageField.getText(), true);
			}
		});
		panel.add(btnSend);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(128, 128, 128));
		getContentPane().add(panel_1, BorderLayout.EAST);
		
		
		JButton btnDisconnect = new JButton("DISCONNECT");
		btnDisconnect.setFont(new Font("Tahoma", Font.BOLD, 11));
		btnDisconnect.setForeground(new Color(255, 0, 0));
		btnDisconnect.setBackground(new Color(220, 20, 60));
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String disconnect = "/d/" + client.getID() + "/e/";
				send(disconnect, false);
				running = false;
				client.quit();
				dispose();
			}
		});
		panel_1.add(btnDisconnect, BorderLayout.NORTH);
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				String disconnect = "/d/" + client.getID() + "/e/";
				send(disconnect, false);
				running = false;
				client.quit();
			}
		});
	
		setVisible(true);
		messageField.requestFocusInWindow();
	}
	public void run() {
		listen();
	}
	
	private void send(String message, boolean text) {
		if (message.equals("")) return;
		if (text) {
			message = client.getName() + ": " + message;
			message = "/m/" + message + "/e/";
			messageField.setText("");
		}
		client.send(message.getBytes());
	}
	
	public void listen() {
		listen = new Thread("Listen") {
			public void run() {
				while (running) {
					String message = client.receive();
					if (message.startsWith("/c/")) {
						client.setID(Integer.parseInt(message.split("/c/|/e/")[1]));
						printToConsole("Successfully connected to server! ID: " + client.getID());
					} else if (message.startsWith("/m/")) {
						String text = message.substring(3);
						text = text.split("/e/")[0];
						printToConsole(text);
					} else if (message.startsWith("/i/")) {
						String text = "/i/" + client.getID() + "/e/";
						send(text, false);
					} else if (message.startsWith("/u/")) {
						String[] u = message.split("/u/|/n/|/e/");
						users.update(Arrays.copyOfRange(u, 1, u.length - 1));
					}
				}
			}
		};
		listen.start();
	}
	
	
	
	private void printToConsole(String message) {
		textArea.append(message+"\n"); // \r
		textArea.setCaretPosition(textArea.getDocument().getLength());
		
	}

}
