import java.io.*;

//import java.net.*;
import java.awt.*;
import java.awt.event.*;

//import javax.imageio.stream.ImageInputStream;
//import java.awt.image.BufferedImage;

//import javax.imageio.ImageIO;
import javax.swing.*;
//import javax.swing.border.Border;
//import javax.swing.border.Border;
import javax.swing.filechooser.FileSystemView;

//import java.net.InetAddress;
//import java.net.UnknownHostException;
import java.io.File;
import java.io.IOException;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeBodyPart;
//import javax.mail.*;
//import javax.mail.Session;
//import javax.mail.MimeMessage;
//import javax.mail.internet.*;

import java.util.ArrayList;
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
	
	//variables for authenticating and sending messages
	private String email;
	private String toEmail;
	private String password;
	private String[] host;
	private String smtpHost;
	private String port = "465";
	private ArrayList<File> files = new ArrayList<File>();
	
	private JLabel userLabel = new JLabel("Email: ");
	private JTextField emailField = new JTextField("", 40);
	
	private JLabel passLabel = new JLabel("Password: ");
	private JPasswordField passField = new JPasswordField("", 40);
	
	private JLabel failLabel = new JLabel("");
	
	private JButton btLogin = new JButton("Login");
	
	

	//for authentication GUI
	private JPanel userPanel = new JPanel(new BorderLayout());
	private JPanel passPanel = new JPanel(new BorderLayout());
	private JPanel failPanel = new JPanel(new BorderLayout());
	private JPanel loginPanel = new JPanel(new BorderLayout());
	private JPanel authField = new JPanel(new BorderLayout());
	
	
	//for message  GUI
	private JLabel titLabel = new JLabel("Mail Client");
	
	private JLabel ccLabel = new JLabel("CC: ");
	private JTextField ccField = new JTextField("", 40);
	
	private JLabel toLabel = new JLabel("To:");
	private JTextField toField = new JTextField("", 40);
	
	private JLabel subjectLabel = new JLabel("Subject:");
	private JTextField subjectField = new JTextField("", 40);
	
	private JLabel imgLabel = new JLabel("ImagePath: ");
	private JTextField imgField  = new JTextField("", 40);
	
	private JLabel messageLabel = new JLabel("Message:");
	private JTextArea messageText = new JTextArea(10, 40);

	private JButton btSend = new JButton("Send");
	private JButton btClear = new JButton("Clear");
	private JButton btQuit = new JButton("Quit");
	private JButton btBrowse = new JButton("Add Image");
	
	private JPanel titPanel = new JPanel(new BorderLayout());
	private JPanel ccPanel = new JPanel(new BorderLayout());
	private JPanel toPanel = new JPanel(new BorderLayout());
	private JPanel subjectPanel = new JPanel(new BorderLayout());
	private JPanel imgPanel = new JPanel(new BorderLayout());
	private JPanel messagePanel = new JPanel(new BorderLayout());
	private JPanel imgDisplayPanel = new JPanel(new BorderLayout());
	private JPanel fieldPanel = new JPanel(new GridLayout(0, 1));
	private Panel buttonPanel = new Panel(new GridLayout(1, 0));
	private JFrame frame = new JFrame("Mail Client");	

	
	private JLabel iconLabel = new JLabel("Attachments:");
	private JLabel imgIcon = new JLabel();


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
		
		btLogin.addActionListener(new AuthenticateListener());
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
			
			Properties props = System.getProperties();
			props.put("mail.smtp.host", smtpHost);
			props.put("mail.smtp.port", port);
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.socketFactory.port", port);
			props.put("mail.smtp.ssl.enable", "true");		
			
			
			Session session = Session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					System.out.println(password);
					return new PasswordAuthentication(email, password);
				}
			});
			
			//session.setDebug(true);
			try {
				MimeMessage message= new MimeMessage(session);
				
				
				message.setFrom(new InternetAddress(email));
				message.addRecipient(Message.RecipientType.TO, new InternetAddress(toField.getText()));
//				if(ccField.getText() != null) {
//					message.addRecipient(Message.RecipientType.CC, new InternetAddress(ccField.getText()));
//				}
				
				message.setSubject(subjectField.getText());
				//message.setText(messageText.getText());
				
				MimeMultipart parts = new MimeMultipart();
//				System.out.println(message.getAllHeaders());
				MimeBodyPart text = new MimeBodyPart();
				text.setText(messageText.getText());
				parts.addBodyPart(text);
				
				for(int i = 0; i < files.size(); i++) {
					try {
						MimeBodyPart attach = new MimeBodyPart();
						attach.attachFile(files.get(i));
						parts.addBodyPart(attach);
					} catch(IOException e) {
						System.out.println("Error attaching file");
					}
				}
				
				message.setContent(parts);
				
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
			String imgName = imgField.getText();
			File file = new File(imgName);
			files.add(file);
			
			Icon icon = FileSystemView.getFileSystemView().getSystemIcon(file);
			
			iconLabel = new JLabel(file.getPath(), icon, SwingConstants.LEFT);
			
			imgDisplayPanel.add(iconLabel);
			
			pack();
			show();			
		}
	}
	
	class AuthenticateListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			imgField.setText("/C:/Users/samrp/Downloads/");
			
			email = emailField.getText();
			password = passField.getText();
			
			userPanel.setVisible(false);
			passPanel.setVisible(false);
			loginPanel.setVisible(false);
			authField.setVisible(false);
			
			String address = email;
			host = address.split("@", 2);
			smtpHost = "smtp." + host[1];
			//System.out.println(smtpHost);
			
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
			messagePanel.add(imgDisplayPanel, BorderLayout.CENTER);
			messagePanel.add(messageText, BorderLayout.SOUTH);
			
			
			fieldPanel.add(titPanel);
			fieldPanel.add(ccPanel);
			fieldPanel.add(toPanel);
			fieldPanel.add(subjectPanel);
			fieldPanel.add(imgPanel);

			frame.add(fieldPanel);

			
			btSend.addActionListener(new SendListener());
			btClear.addActionListener(new ClearListener());
			btQuit.addActionListener(new QuitListener());
			btBrowse.addActionListener(new AddImageListener());
			
			buttonPanel.add(btSend);
			buttonPanel.add(btClear);
			buttonPanel.add(btQuit);
			buttonPanel.add(btBrowse);

			add(fieldPanel, BorderLayout.NORTH);
			add(messagePanel, BorderLayout.CENTER);
			add(buttonPanel, BorderLayout.SOUTH);
			pack();
			show();			
		}
	}
}


// https://stackoverflow.com/questions/13512612/browse-for-image-file-and-display-it-using-java-swing
// https://www.programcreek.com/java-api-examples/java.awt.Image
// https://www.javatpoint.com/example-of-sending-email-using-java-mail-api-through-gmail-server
// https://www.javatpoint.com/example-of-sending-email-using-java-mail-api-through-gmail-server
// https://netcorecloud.com/tutorials/send-email-in-java-using-gmail-smtp/
