import java.io.*;
//import java.net.*;
import java.awt.*;
import java.awt.event.*;


import javax.swing.*;
import java.net.InetAddress;
import java.net.UnknownHostException;



/* $Id: MailClient.java,v 1.7 1999/07/22 12:07:30 kangasha Exp $ */

/**
 * A simple mail client with a GUI for sending mail.
 * 
 * @author Jussi Kangasharju
 */
public class MailClient extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/* The stuff for the GUI. */
	// private Button btSend = new Button("Send");
	// private Button btClear = new Button("Clear");
	// private Button btQuit = new Button("Quit");
	
	private JLabel serverLabel = new JLabel("Local mailserver:");
	private JTextField serverField = new JTextField("", 40);
	private JLabel fromLabel = new JLabel("From:");
	private JTextField fromField = new JTextField("", 40);
	private JLabel toLabel = new JLabel("To:");
	private JTextField toField = new JTextField("", 40);
	private JLabel subjectLabel = new JLabel("Subject:");
	private JTextField subjectField = new JTextField("", 40);
	private JLabel messageLabel = new JLabel("Message:");
	private JTextArea messageText = new JTextArea(10, 40);
	private JButton btSend = new JButton("Send");
	private JButton btClear = new JButton("Clear");
	private JButton btQuit = new JButton("Quit");

	/**
	 * Create a new MailClient window with fields for entering all the relevant
	 * information (From, To, Subject, and message).
	 */
	@SuppressWarnings("deprecation")
	public MailClient() {
		super("Java Mailclient");
		InetAddress addr;
		try {
			addr = InetAddress.getLocalHost();
			serverField.setText(addr.getHostName());
			fromField.setText(addr.getHostAddress());
		} catch (UnknownHostException e) {
			//TODO: handle exception
		}
		

		/*
		 * Create panels for holding the fields. To make it look nice, create an
		 * extra panel for holding all the child panels.
		 */
		try {
			
		} catch (Exception e) {
			//TODO: handle exception
		}
		


		JPanel serverPanel = new JPanel(new BorderLayout());
		JPanel fromPanel = new JPanel(new BorderLayout());
		JPanel toPanel = new JPanel(new BorderLayout());
		JPanel subjectPanel = new JPanel(new BorderLayout());
		JPanel messagePanel = new JPanel(new BorderLayout());
		serverPanel.add(serverLabel, BorderLayout.WEST);
		serverPanel.add(serverField, BorderLayout.CENTER);
		fromPanel.add(fromLabel, BorderLayout.WEST);
		fromPanel.add(fromField, BorderLayout.CENTER);
		toPanel.add(toLabel, BorderLayout.WEST);
		toPanel.add(toField, BorderLayout.CENTER);
		subjectPanel.add(subjectLabel, BorderLayout.WEST);
		subjectPanel.add(subjectField, BorderLayout.CENTER);
		messagePanel.add(messageLabel, BorderLayout.NORTH);
		messagePanel.add(messageText, BorderLayout.CENTER);
		Panel fieldPanel = new Panel(new GridLayout(0, 1));
		fieldPanel.add(serverPanel);
		fieldPanel.add(fromPanel);
		fieldPanel.add(toPanel);
		fieldPanel.add(subjectPanel);

		JFrame frame = new JFrame("Mail Client");
		frame.add(fieldPanel);

		/*
		 * Create a panel for the buttons and add listeners to the buttons.
		 */
		Panel buttonPanel = new Panel(new GridLayout(1, 0));
		btSend.addActionListener(new SendListener());
		btClear.addActionListener(new ClearListener());
		btQuit.addActionListener(new QuitListener());
		buttonPanel.add(btSend);
		buttonPanel.add(btClear);
		buttonPanel.add(btQuit);

		/* Add, pack, and show. */
		add(fieldPanel, BorderLayout.NORTH);
		add(messagePanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		pack();
		show();
	}

	static public void main(String argv[]) {
		new MailClient();
	}

	/* Handler for the Send-button. */
	class SendListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.out.println("Sending mail");

			/* Check that we have the local mailserver */
			if ((serverField.getText()).equals("")) {
				System.out.println("Need name of local mailserver!");
				return;
			}

			/* Check that we have the sender and recipient. */
			if ((fromField.getText()).equals("")) {
				System.out.println("Need sender!");
				return;
			}
			if ((toField.getText()).equals("")) {
				System.out.println("Need recipient!");
				return;
			}

			/* Create the message */
			Message mailMessage = new Message(fromField.getText(),
					toField.getText(), subjectField.getText(),
					messageText.getText());

			/*
			 * Check that the message is valid, i.e., sender and recipient
			 * addresses look ok.
			 */
			if (!mailMessage.isValid()) {
				return;
			}

			/*
			 * Create the envelope, open the connection and try to send the
			 * message.
			 */
			try {
				Envelope envelope = new Envelope(mailMessage,
						serverField.getText());

				SMTPConnection connection = new SMTPConnection(envelope);
				connection.send(envelope);
				connection.close();
			} catch (IOException error) {
				System.out.println("Sending failed: " + error);
				return;
			}
			System.out.println("Mail sent succesfully!");
		}
	}

	/* Clear the fields on the GUI. */
	class ClearListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Clearing fields");
			fromField.setText("");
			toField.setText("");
			subjectField.setText("");
			messageText.setText("");
		}
	}

	/* Quit. */
	class QuitListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}