package com.nv.test.jms.publisher.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import javax.jms.JMSException;
import javax.naming.NamingException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

import com.nv.test.jms.sender.QueueSend;
import com.nv.test.utils.ExceptionUtils;
import com.nv.test.utils.TextUtils;

/**
 * @author naveenvemulapalli
 *
 */
public class JMSQueuePublisher extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JFrame frmXmlMessagePublisher;
	private JTextField textJNDIFactory;
	private JTextField textJMSFactory;
	private JTextField textQueue;
	private JTextField textCorrelationId;
	private JFileChooser fc;
	private JTextField textFilePath;
	private JTextField textServerUrl;
	private JCheckBox chckbxNewCheckBox;
	private final Action action = new SwingAction();
	private JButton btnPublish;
	private QueueSendTask task;
	private JFrame logFrame;
	private JPanel contentPane;
	private JButton btnNewButton;
	private JTextPane textPane;

	private JPanel progressMsgContentPanel;
	private JLabel lblPublising;
	private JFrame progMsgFrame;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JMSQueuePublisher window = new JMSQueuePublisher();
					window.frmXmlMessagePublisher.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public JMSQueuePublisher() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frmXmlMessagePublisher = new JFrame();
		frmXmlMessagePublisher
				.setTitle("XML Message Publisher for Oracle Weblogic Server");
		frmXmlMessagePublisher.setBounds(4, 123, 587, 348);
		frmXmlMessagePublisher.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmXmlMessagePublisher.getContentPane().setLayout(null);

		JLabel lblXmlToBe = new JLabel("XML to be published*");
		lblXmlToBe.setBounds(33, 72, 122, 14);
		frmXmlMessagePublisher.getContentPane().add(lblXmlToBe);

		textJNDIFactory = new JTextField();
		textJNDIFactory.setText("weblogic.jndi.WLInitialContextFactory");
		textJNDIFactory.setBounds(163, 131, 302, 20);
		frmXmlMessagePublisher.getContentPane().add(textJNDIFactory);
		textJNDIFactory.setColumns(10);

		textJMSFactory = new JTextField();
		textJMSFactory.setBounds(163, 162, 302, 20);
		frmXmlMessagePublisher.getContentPane().add(textJMSFactory);
		textJMSFactory.setColumns(10);

		textQueue = new JTextField();
		textQueue.setBounds(163, 193, 302, 20);
		frmXmlMessagePublisher.getContentPane().add(textQueue);
		textQueue.setColumns(10);

		JLabel lblNewLabel = new JLabel("JNDI Factory*");
		lblNewLabel.setBounds(33, 134, 104, 14);
		frmXmlMessagePublisher.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("JMS Factory*");
		lblNewLabel_1.setBounds(33, 165, 77, 14);
		frmXmlMessagePublisher.getContentPane().add(lblNewLabel_1);

		JLabel lblQueue = new JLabel("JMS Queue*");
		lblQueue.setBounds(33, 196, 77, 14);
		frmXmlMessagePublisher.getContentPane().add(lblQueue);

		btnNewButton = new JButton("Load File");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Set up the file chooser.
				if (fc == null) {
					fc = new JFileChooser();

				}
				// Show it.
				int returnVal = fc.showDialog(JMSQueuePublisher.this, "Open");

				// Process the results.
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();

					// Set the File Path to the text field
					textFilePath.setText(file.getAbsolutePath());

				} else {
					// System.out.println("cancelled by user.");
				}
				// Reset the file chooser for the next time it's shown.
				fc.setSelectedFile(null);
			}
		});
		btnNewButton.setBounds(475, 68, 94, 23);
		frmXmlMessagePublisher.getContentPane().add(btnNewButton);

		btnPublish = new JButton("Publish");
		btnPublish.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String serverUrl;
				String jndiFactory;
				String jmsFactory;
				String queue;
				String correlationId;
				String fileLocation;

				fileLocation = textFilePath.getText();
				serverUrl = textServerUrl.getText();
				jndiFactory = textJNDIFactory.getText();
				jmsFactory = textJMSFactory.getText();
				queue = textQueue.getText();
				correlationId = textCorrelationId.getText();

				// Add some field check!
				if (fileLocation.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Input File cannot be empty", "File",
							JOptionPane.ERROR_MESSAGE);
				}

				if (serverUrl.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Server URL cannot be empty", "Server URL",
							JOptionPane.ERROR_MESSAGE);
				}

				if (jmsFactory.equals("")) {
					JOptionPane.showMessageDialog(null,
							"JMS Factory Name cannot be empty",
							"JMS Factory Name", JOptionPane.ERROR_MESSAGE);
				}

				if (queue.equals("")) {
					JOptionPane.showMessageDialog(null,
							"JMS Queue Name cannot be empty", "JMS Queue Name",
							JOptionPane.ERROR_MESSAGE);
				}

				else {

					// Disable all controls (except Exit & About)
					btnPublish.setEnabled(false);
					textFilePath.setEnabled(false);
					textServerUrl.setEnabled(false);
					textJNDIFactory.setEnabled(false);
					textJMSFactory.setEnabled(false);
					textQueue.setEnabled(false);
					textCorrelationId.setEnabled(false);
					chckbxNewCheckBox.setEnabled(false);
					btnNewButton.setEnabled(false);
					// Either of the Following options can be used to tell that
					// something is happening in the background
					// Option-1: Activate Wait cursor
					// frmXmlMessagePublisher.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					// Option-2: Show a dialog box
					progMsgFrame.setVisible(true);
					// Create a task and execute that will do the actual
					// operation/activity
					task = new QueueSendTask(fileLocation, serverUrl,
							jndiFactory, jmsFactory, queue, correlationId);
					task.execute();
				}
			}
		});

		btnPublish.setBounds(222, 271, 91, 23);
		frmXmlMessagePublisher.getContentPane().add(btnPublish);

		textFilePath = new JTextField();
		textFilePath.setBounds(165, 69, 302, 20);
		frmXmlMessagePublisher.getContentPane().add(textFilePath);
		textFilePath.setColumns(10);

		JLabel lblCorrelationId = new JLabel("Correlation ID");
		lblCorrelationId.setBounds(33, 227, 77, 14);
		frmXmlMessagePublisher.getContentPane().add(lblCorrelationId);

		textCorrelationId = new JTextField();
		textCorrelationId.setBounds(163, 224, 302, 20);
		frmXmlMessagePublisher.getContentPane().add(textCorrelationId);
		textCorrelationId.setColumns(10);

		JButton btnNewButton_1 = new JButton("About");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane
						.showMessageDialog(
								null,
								"XML Message Publisher for Oracle Weblogic Server "
										+ "\n===developed by=== "
										+ "\nNaveen Vemulapalli (naveenvemulapalli@yahoo.com)",
								"About", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnNewButton_1.setBounds(500, 298, 69, 14);
		frmXmlMessagePublisher.getContentPane().add(btnNewButton_1);

		JLabel lblServerUrl = new JLabel(" Server URL*");
		lblServerUrl.setBounds(30, 101, 77, 14);
		frmXmlMessagePublisher.getContentPane().add(lblServerUrl);

		textServerUrl = new JTextField();
		textServerUrl.setBounds(163, 100, 302, 20);
		frmXmlMessagePublisher.getContentPane().add(textServerUrl);
		textServerUrl.setColumns(10);

		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Any of the below two will work!
				// frmXmlMessagePublisher.dispose();
				frmXmlMessagePublisher.dispatchEvent(new WindowEvent(
						frmXmlMessagePublisher, WindowEvent.WINDOW_CLOSING));
			}
		});
		btnExit.setBounds(373, 271, 91, 23);
		frmXmlMessagePublisher.getContentPane().add(btnExit);

		chckbxNewCheckBox = new JCheckBox("Show Log Window");
		chckbxNewCheckBox.setAction(action);
		chckbxNewCheckBox.setBounds(33, 271, 134, 23);
		frmXmlMessagePublisher.getContentPane().add(chckbxNewCheckBox);

		JLabel lblXmlMessagePublisher = new JLabel(
				"XML Message Publisher for Oracle Weblogic Server");
		lblXmlMessagePublisher
				.setFont(new Font("Arial Narrow", Font.PLAIN, 23));
		lblXmlMessagePublisher.setBounds(23, 11, 444, 28);
		frmXmlMessagePublisher.getContentPane().add(lblXmlMessagePublisher);

		logFrame = new JFrame();

		logFrame.setTitle("Log Window");
		// setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		logFrame.setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);

		textPane = new JTextPane();
		textPane.setEditable(false);

		 DefaultCaret caret = (DefaultCaret)textPane.getCaret();
		 caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		 
		scrollPane.setViewportView(textPane);

		// logFrame.setVisible(true);

		// Progress Message
		// Add Frame
		progMsgFrame = new JFrame();
		progMsgFrame.setTitle("Progress");
		// --set location of the frame
		progMsgFrame.setBounds(400, 300, 233, 113);
		// --Put it on top of other windows
		progMsgFrame.setAlwaysOnTop(true);
		// --Do not show it by default
		progMsgFrame.setVisible(false);

		// Add Panel
		progressMsgContentPanel = new JPanel();
		progressMsgContentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		progressMsgContentPanel.setLayout(new BorderLayout(0, 0));

		// Add label to panel
		lblPublising = new JLabel("Publishing Message...");
		progressMsgContentPanel.add(lblPublising);

		// Add panel to frame
		progMsgFrame.setContentPane(progressMsgContentPanel);

	}

	class QueueSendTask extends SwingWorker<Void, Void> {

		String fileLocation;
		String serverUrl;
		String jndiFactory;
		String jmsFactory;
		String queue;
		String correlationId;

		public QueueSendTask(String strfileLocation, String strServerUrl,
				String strJndiFactory, String strJmsFactory, String strQueue,
				String strCorrelationId) {
			// TODO Auto-generated constructor stub
			
			fileLocation = strfileLocation;
			serverUrl = strServerUrl;
			jndiFactory = strJndiFactory; 
			jmsFactory = strJmsFactory; 
			queue = strQueue;
			correlationId = strCorrelationId;
								
		}

		protected Void doInBackground() throws Exception {

			try {

				TextUtils.appendToPane(textPane, "\n"
						+ new Timestamp(new java.util.Date().getTime())
						+ " - Sending message to queue " + queue, Color.BLUE);

				QueueSend.sendToQueue(fileLocation, serverUrl, jndiFactory,
						jmsFactory, queue, correlationId);

				TextUtils.appendToPane(textPane, "\n"
						+ new Timestamp(new java.util.Date().getTime())
						+ " - Message is successfully sent", Color.GREEN);

			} catch (NamingException nme) {
				TextUtils.appendToPane(textPane,
						"\nMessage could not be sent - Error: "
								+ ExceptionUtils.getStackTrace(nme), Color.RED);
			} catch (JMSException jmse) {
				TextUtils
						.appendToPane(textPane,
								"\nMessage could not be sent - Error: "
										+ ExceptionUtils.getStackTrace(jmse),
								Color.RED);
			} catch (IOException ioe) {
				TextUtils.appendToPane(textPane,
						"\nMessage could not be sent - Error: "
								+ ExceptionUtils.getStackTrace(ioe), Color.RED);
			}

			return null;
		}

		public void done() {
			// Enable all controls
			btnPublish.setEnabled(true);
			textFilePath.setEnabled(true);
			textServerUrl.setEnabled(true);
			textJNDIFactory.setEnabled(true);
			textJMSFactory.setEnabled(true);
			textQueue.setEnabled(true);
			textCorrelationId.setEnabled(true);
			chckbxNewCheckBox.setEnabled(true);
			btnNewButton.setEnabled(true);
			// Either of the following can be done:
			// Option-1: Disable wait cursor
			// frmXmlMessagePublisher.setCursor(null);
			// Option-2: Disappear the frame showing that message is being
			// published
			progMsgFrame.setVisible(false);

		}

	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Show Log Window");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}

		public void actionPerformed(ActionEvent e) {

			if (chckbxNewCheckBox.isSelected()) {
				// frame = new LogsWindow();
				logFrame.setBounds(614, 123, 587, 348);
				logFrame.setVisible(true);
			} else {
				logFrame.dispose();
			}
		}
	}

}
