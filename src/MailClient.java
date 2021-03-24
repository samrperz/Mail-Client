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

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
//import javax.mail.*;
//import javax.mail.Session;
//import javax.mail.MimeMessage;
//import javax.mail.internet.*;

import java.util.Properties;





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
	
	/* this is for the Authentication GUI */
	private String email;
	private String toEmail;
	private String password;
	
	private String[] host;
	private String smtpHost;
	private String port = "465";
	
	
	
	private JLabel userLabel = new JLabel("Email: ");
	private JTextField emailField = new JTextField("samrperz@gmail.com", 40);
	
	private JLabel passLabel = new JLabel("Password: ");
	private JPasswordField passField = new JPasswordField("rustycat1", 40);
	//passField.setEchoCar("*");
	
	private JLabel failLabel = new JLabel("");
	
	private JButton btLogin = new JButton("Login");
	
	
	
	/* The stuff for the GUI. */	

	private JLabel titLabel = new JLabel("Mail Client");
	
	//private JLabel fromLabel = new JLabel("From:");
	//private JTextField fromField = new JTextField("", 40);
	
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
	private JButton btBrowse = new JButton("Add Image");
	
	
	//for authentication GUI
	private JPanel userPanel = new JPanel(new BorderLayout());
	private JPanel passPanel = new JPanel(new BorderLayout());
	private JPanel failPanel = new JPanel(new BorderLayout());
	private JPanel loginPanel = new JPanel(new BorderLayout());
	private JPanel authField = new JPanel(new BorderLayout());
	
	
	//for messge GUI
	private JPanel titPanel = new JPanel(new BorderLayout());
	private JPanel ccPanel = new JPanel(new BorderLayout());
	private JPanel toPanel = new JPanel(new BorderLayout());
	private JPanel subjectPanel = new JPanel(new BorderLayout());
	private JPanel imgPanel = new JPanel(new BorderLayout());
	private JPanel messagePanel = new JPanel(new BorderLayout());
	private JPanel imgDisplayPanel = new JPanel(new BorderLayout());
	private JPanel fieldPanel = new JPanel(new GridLayout(0, 1));
	private JFrame frame = new JFrame("Mail Client");
	
	
//	private String toAddr = "samrperz@gmail.com";
//	private String fromAddr = "samrperz@gmail.com";
//	private String[] host = toAddr.split("@", 2);
	
	
	

	/** ------------added by me--------------
	 *  addes support for the image file
	 */
	File file;
	//BufferedImage img;
	JLabel iconLabel = new JLabel("Attachments:");
	
	//public static final String imgPath = "/C:/Users/samrp/Downloads/cat.jpg";
	private ImageIcon img;// = new ImageIcon(imgPath);
	private JLabel imgIcon = new JLabel(img);


	/**
	 * Create a new MailClient window with fields for entering all the relevant
	 * information (From, To, Subject, and message).
	 */
	@SuppressWarnings("deprecation")
	public MailClient() {
		super("Java Mailclient");
		/*
		 * Create panels for holding the fields. To make it look nice, create an
		 * extra panel for holding all the child panels.
		 */
		/* auth panel*/
		
		userPanel.add(userLabel, BorderLayout.NORTH);
		userPanel.add(emailField, BorderLayout.SOUTH);
		
		passPanel.add(passLabel, BorderLayout.NORTH);
		passPanel.add(passField, BorderLayout.SOUTH);
		
		btLogin.addActionListener(new Authenticate());
		loginPanel.add(btLogin);
		
		failPanel.add(failLabel);
		
		authField.add(userPanel, BorderLayout.NORTH);
		authField.add(passPanel, BorderLayout.CENTER);
		authField.add(failPanel, BorderLayout.SOUTH);
		
		add(authField, BorderLayout.NORTH);
		add(loginPanel, BorderLayout.SOUTH);
		
		pack();
		show();
	}

	static public void main(String argv[]) {
		new MailClient();
	}
	
	
	// change so it checks for the properties of an email address
	public Boolean checkValidAddress(String str) {
		if(str != "") {
			return true;
		}
		return false;
	}

	/* Handler for the Send-button. */
	class SendListener implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			toEmail = toField.getText();
			System.out.println("Sending mail to " + toEmail);
			
			System.out.println(email + "    " + password);
			//add check emails valid
			// check message valid
			//add cc to the mix
			
			//host = toAddr.split("@", 2);
			
			Properties props = System.getProperties();
			props.put("mail.smtp.host", smtpHost);
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.socketFactory.port", port);
			//props.put("mail.smtp.transport.class", "com.sun.mail.smtp.SSLTransport");
			props.put("mail.smtp.ssl.enable", "true");
			//props.put("mail.smtp.starttls.enable", "true");
			//props.put("mail.smtp.starttls.enable", props)
			
			
			
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					System.out.println(password);
					return new PasswordAuthentication(email, password);
				}
			});
			
			//System.out.println(session.getProperty("password"));
			session.setDebug(true);
			try {
				Message message= new MimeMessage(session);
				message.setFrom(new InternetAddress(email));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(toField.getText()));
				if(ccField.getText() != "") {
					message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccField.getText()));
				}
				
				message.setSubject(subjectField.getText());
				message.setText(messageText.getText());
				System.out.println(message.getAllHeaders());
				
				System.out.println(" - Message Ready: sending...");
				Transport.send(message);

				System.out.println("Message set successfully");
				
			} catch(MessagingException e) {
				System.out.println("Failed to Send");
				e.printStackTrace();
				return;
			}

		}
	}

	/* Clear the fields on the GUI. */
	class ClearListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			System.out.println("Clearing fields");
			//fromField.setText("");
			toField.setText("");
			subjectField.setText("");
			imgField.setText("");
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
			ccField.setText("hello");
			imgField.setText("/C:/Users/samrp/Downloads/cat.jpg");
//			String imgName = imgField.getText();
//			File file = new File(imgName);
//			if(file.exists()) {
//				img = new ImageIcon(imgField.getText());// = new ImageIcon(imgPath);
//				imgIcon.setLabelFor(img);
//			}
//			
//			
//			img = new ImageIcon(imgField.getText());// = new ImageIcon(imgPath);
//			imgIcon.setLabelFor(img);// = new JLabel(img);
			/**
			 * fill out with code to recieve an image
			 */
			//JFileChooser browser = new JFileChooser(imgPath);
//			try {
//				String imgName = imgField.getText();
//				File file = new File(imgName);
//				if(file.exists()) {
//					
//				}
//				
//				//JLabel iconLabel = new JLabel("Attachments:");
//				
//				//String imgPath = "/C:/Users/samrp/Downloads/cat.jpg";
//				img = new ImageIcon(imgName);
//				imgIcon = new JLabel(img);
//				
//				imgDisplayPanel.add(iconLabel, BorderLayout.NORTH);
//				imgDisplayPanel.add(imgIcon, BorderLayout.WEST);
//				//try {
//					//file = new File(imgField.getText());
//					//file = new File("C:/Users/samrp/Downloads/cat.jpg");
//					//img = ImageIO.read(file);
//					
//					
//					
//					//JFrame imgFrame = new JFrame("Images");
//					//JPanel img1 = new JPanel(new BorderLayout());
//					//JLabel icon = new JLabel(new ImageIcon(img));
//					
//					//img1.add(icon);
//					//imgFrame.add(img1);
//					//pack();
//					//show();
//					//
//
//				//} catch (IOException e1) {
//					// TODO Auto-generated catch block
//				//}
//				
//				
//			} catch (Exception e) {
//				System.out.println("Bad File")
//			}
			
		
			
		}
		
	}
	
	class Authenticate implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			/**
			 * start out with an authentication screen
			 * Move the main body of the app over here
			 * 
			 * this stores username and password, and reveals the main app when send is hit
			 */
			
			email = emailField.getText();
			password = passField.getText();
			
			userPanel.setVisible(false);
			passPanel.setVisible(false);
			loginPanel.setVisible(false);
			authField.setVisible(false);
			
			//String toAddr = "samrperz@gmail.com";
			//fromAddr = "samrperz@gmail.com";
			String address = email;
			host = address.split("@", 2);
			smtpHost = "smtp." + host[1];
			System.out.println(smtpHost);
			
//			String smtpHost = "smpt." + host[1];
//			String port = "465";
			//ccField.setText(smtpHost);
			
			
			//toField.setText("samrperz@gmail.com");
			//subjectField.setText("test");
			//messageText.setText("testing");
			
			
			//if(check is login is good, then create the Client GUI){
				//parse the smtp server from email
			
				imgDisplayPanel.add(iconLabel, BorderLayout.NORTH);
				imgDisplayPanel.add(imgIcon, BorderLayout.WEST);
	
				titPanel.add(titLabel, BorderLayout.CENTER);
	
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
				messagePanel.add(imgDisplayPanel);
				
				fieldPanel.add(titPanel);
				fieldPanel.add(ccPanel);
				fieldPanel.add(toPanel);
				fieldPanel.add(subjectPanel);
				fieldPanel.add(imgPanel);
	
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
				add(fieldPanel, BorderLayout.NORTH);
				add(messagePanel, BorderLayout.CENTER);
				add(buttonPanel, BorderLayout.SOUTH);
				pack();
				show();
			//}
			
			
			//TODO reveal the rest of app
			
		}
		
		
		
	}
}







/* Check that we have the local mailserver */
//InetAddress addr;
//try {
//	addr = InetAddress.getLocalHost();
//} catch (UnknownHostException e) {
//	System.out.println("Can't determine host");
//	return;
//}
//if ((serverField.getText()).equals("")) {
//	System.out.println("Need name of local mailserver!");
//	return;
//}

/* Check that we have the sender and recipient. */
//if ((fromField.getText()).equals("")) {
//	System.out.println("Need sender!");
//	return;
//}
//if ((toField.getText()).equals("")) {
//	System.out.println("Need recipient!");
//	return;
//}

/* Create the message */
//Message mailMessage = new Message(addr.getHostName(),
//		toField.getText(), subjectField.getText(),
//		messageText.getText());

/*
 * Check that the message is valid, i.e., sender and recipient
 * addresses look ok.
 */
//if (!mailMessage.isValid()) {
//	return;
//}

/*
 * Create the envelope, open the connection and try to send the
 * message.
 */
//try {
//	Envelope envelope = new Envelope(mailMessage,
//			"londo.ad.stetson.edu");
//
//	SMTPConnection connection = new SMTPConnection(envelope);
//	connection.send(envelope);
//	connection.close();
//} catch (IOException error) {
//	System.out.println("Sending failed: " + error);
//	return;
//}
//System.out.println("Mail sent succesfully!");



// https://stackoverflow.com/questions/13512612/browse-for-image-file-and-display-it-using-java-swing
// https://www.programcreek.com/java-api-examples/java.awt.Image
// https://www.javatpoint.com/example-of-sending-email-using-java-mail-api-through-gmail-server
// https://www.javatpoint.com/example-of-sending-email-using-java-mail-api-through-gmail-server
// https://netcorecloud.com/tutorials/send-email-in-java-using-gmail-smtp/
