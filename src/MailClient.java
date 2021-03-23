/**
 * TODO:
 * 
 * Get the smtp server name from the email inputs
 * 
 * System.getProperty("user.dir");
 */



import java.io.*;
//import java.net.*;
import java.awt.*;
import java.awt.event.*;

//import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
//import javax.swing.border.Border;
import javax.swing.border.Border;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.File;
import java.io.IOException;
//import javax.mail;
//import javax.mail.internet.*;





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

	private JLabel titLabel = new JLabel("Mail Client");
	
	private JLabel fromLabel = new JLabel("From:");
	private JTextField fromField = new JTextField("", 40);
	
	private JLabel ccLabel = new JLabel("CC: ");
	private JTextField ccField = new JTextField("", 40);
	
	private JLabel toLabel = new JLabel("To:");
	private JTextField toField = new JTextField("", 40);
	
	private JLabel subjectLabel = new JLabel("Subject:");
	private JTextField subjectField = new JTextField("", 40);
	
	private JLabel imgLabel = new JLabel("ImagePath: ");// C:/Users/samrp/Downloads/...
	private JTextField imgField  = new JTextField("", 40);
	
	private JLabel messageLabel = new JLabel("Message:");
	private JTextArea messageText = new JTextArea(10, 40);

	private JButton btSend = new JButton("Send");
	private JButton btClear = new JButton("Clear");
	private JButton btQuit = new JButton("Quit");
	private JButton btBrowse = new JButton("Browse");

	/** ------------added by me--------------
	 *  addes support for the image file
	 */
	File file;
	BufferedImage img;
	JLabel icon = new JLabel();
	private static final String imgPath = "/C:/Users/samrp/Downloads";
	


	/**
	 * Create a new MailClient window with fields for entering all the relevant
	 * information (From, To, Subject, and message).
	 */
	@SuppressWarnings("deprecation")
	public MailClient() {
		super("Java Mailclient");
		// InetAddress addr;
		// try {
		// 	addr = InetAddress.getLocalHost();
		// 	serverField.setText(addr.getHostName());
		// 	fromField.setText(addr.getHostAddress());
		// } catch (UnknownHostException e) {
		// 	System.out.println("Can't determine host");
		// }

		/* --------------added-----------------------*/
		


		/*
		 * Create panels for holding the fields. To make it look nice, create an
		 * extra panel for holding all the child panels.
		 */
		
		


		//JPanel serverPanel = new JPanel(new BorderLayout());
		JPanel titPanel = new JPanel(new BorderLayout());
		JPanel fromPanel = new JPanel(new BorderLayout());
		JPanel ccPanel = new JPanel(new BorderLayout());
		JPanel toPanel = new JPanel(new BorderLayout());
		JPanel subjectPanel = new JPanel(new BorderLayout());
		JPanel imgPanel = new JPanel(new BorderLayout());
		JPanel messagePanel = new JPanel(new BorderLayout());
		JPanel imgDisplayPanel = new JPanel(new BorderLayout());

		JLabel icon = new JLabel();
		imgDisplayPanel.add(icon, BorderLayout.WEST);

		titPanel.add(titLabel, BorderLayout.CENTER);

		fromPanel.add(fromLabel, BorderLayout.NORTH);
		fromPanel.add(fromField, BorderLayout.SOUTH);

		ccPanel.add(ccLabel, BorderLayout.NORTH);
		ccPanel.add(ccField, BorderLayout.SOUTH);

		toPanel.add(toLabel, BorderLayout.NORTH);
		toPanel.add(toField, BorderLayout.SOUTH);
		
		subjectPanel.add(subjectLabel, BorderLayout.NORTH);
		subjectPanel.add(subjectField, BorderLayout.SOUTH);
		
		imgPanel.add(imgLabel, BorderLayout.NORTH);
		imgPanel.add(imgField, BorderLayout.SOUTH);
		
		messagePanel.add(messageLabel, BorderLayout.NORTH);
		messagePanel.add(messageText, BorderLayout.SOUTH);
		
		JPanel fieldPanel = new JPanel(new GridLayout(0, 1));
		fieldPanel.add(titPanel);
		fieldPanel.add(fromPanel);
		fieldPanel.add(ccPanel);
		fieldPanel.add(toPanel);
		fieldPanel.add(subjectPanel);
		fieldPanel.add(imgPanel);
		//fieldPanel.add(imgDisplayPanel);


		JFrame frame = new JFrame("Mail Client");
		frame.add(fieldPanel);

		/*
		 * Create a panel for the buttons and add listeners to the buttons.
		 */
		Panel buttonPanel = new Panel(new GridLayout(1, 0));
		btSend.addActionListener(new SendListener());
		btClear.addActionListener(new ClearListener());
		btQuit.addActionListener(new QuitListener());
		btBrowse.addActionListener(new AddImageListener());
		buttonPanel.add(btSend);
		buttonPanel.add(btClear);
		buttonPanel.add(btQuit);
		buttonPanel.add(btBrowse);

		/* Add, pack, and show. */
		//add(titPanel, BorderLayout.NORTH);
		add(fieldPanel, BorderLayout.NORTH);
		add(messagePanel, BorderLayout.CENTER);
		//add(imgDisplayPanel, BorderLayout.CENTER);
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
			InetAddress addr;
			try {
				addr = InetAddress.getLocalHost();
			} catch (UnknownHostException e) {
				System.out.println("Can't determine host");
				return;
			}
			//if ((serverField.getText()).equals("")) {
			//	System.out.println("Need name of local mailserver!");
			//	return;
			//}

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
			Message mailMessage = new Message(addr.getHostName(),
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
						"londo.ad.stetson.edu");

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

	/* -------added by me---------- */
	class AddImageListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			/**
			 * fill out with code to recieve an image
			 */
			//JFileChooser browser = new JFileChooser(imgPath);
			//try {
				
				try {
					file = new File(imgField.getText());
					img = ImageIO.read(file);
					
					JFrame imgFrame = new JFrame("Images");
					JPanel img1 = new JPanel(new BorderLayout());
					JLabel icon = new JLabel(new ImageIcon(img));
					
					img1.add(icon);
					imgFrame.add(img1);
					pack();
					show();
					

				} catch (IOException e1) {
					// TODO Auto-generated catch block
				}
				
				
			//} catch (Exception e) {
				
			//}
			
		
			
		}
		
	}
}



// https://stackoverflow.com/questions/13512612/browse-for-image-file-and-display-it-using-java-swing
