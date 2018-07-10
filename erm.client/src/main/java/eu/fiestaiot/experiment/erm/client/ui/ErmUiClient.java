package eu.fiestaiot.experiment.erm.client.ui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.awt.event.ActionEvent;

import eu.fiestaiot.experiment.erm.client.ErmClient;
import eu.fiestaiot.experiment.erm.client.SecureErmClient;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import java.awt.Color;
import javax.swing.JLabel;




public class ErmUiClient extends JPanel {
	
	final static Logger logger = LoggerFactory.getLogger(ErmUiClient.class);
	
//	private SecureErmClient ermClient;
	private ErmClient ermClient;

	private JFrame frmExperimentRegistryManagement;
	private JTextField fedSpecPathField;
	private JButton btnSaveFedspec;
	private JPanel panel_1;
	private JPanel panel_2;
	private JPanel panel_3;
	private JTextField userIDTextField;
	private JTextField experimentIDTextField;
	private JTextField serviceIDTextField;
	private JLabel lblServiceid;
	private JButton btnGetFISMO;
	private JTextField txtUseridForExpDescr;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ErmUiClient window = new ErmUiClient();
					window.frmExperimentRegistryManagement.setVisible(true);
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ErmUiClient() {
		
//		ermClient = new SecureErmClient();
		ermClient = new ErmClient();
		
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmExperimentRegistryManagement = new JFrame();
		frmExperimentRegistryManagement.setTitle("Experiment Registry Management Test Client");
		frmExperimentRegistryManagement.setBounds(100, 100, 573, 381);
		frmExperimentRegistryManagement.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmExperimentRegistryManagement.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("Welcome");
		btnNewButton.addActionListener(new BtnNewButtonActionListener());
		btnNewButton.setBounds(10, 35, 170, 23);
		frmExperimentRegistryManagement.getContentPane().add(btnNewButton);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Save FEDSpec from File", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(193, 18, 242, 74);
		frmExperimentRegistryManagement.getContentPane().add(panel);
		
		JButton btnOpenFedspec = new JButton("Open FEDSpec");
		btnOpenFedspec.addActionListener(new BtnOpenFedspecActionListener());
		
		btnSaveFedspec = new JButton("Save FEDSpec");
		btnSaveFedspec.addActionListener(new BtnSaveFedspecActionListener());
		
		fedSpecPathField = new JTextField();
		fedSpecPathField.setText("FedSpec Path");
		fedSpecPathField.setColumns(10);
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addComponent(btnOpenFedspec)
					.addPreferredGap(ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
					.addComponent(btnSaveFedspec, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))
				.addComponent(fedSpecPathField, GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addGroup(gl_panel.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnOpenFedspec)
						.addComponent(btnSaveFedspec))
					.addPreferredGap(ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
					.addComponent(fedSpecPathField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
		);
		panel.setLayout(gl_panel);
		
		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Get FEDSpec", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 129, 235, 74);
		frmExperimentRegistryManagement.getContentPane().add(panel_1);
		
		JLabel lblUserid = new JLabel("UserID");
		
		userIDTextField = new JTextField();
		userIDTextField.setText("userID");
		userIDTextField.setColumns(10);
		
		JButton btnGetFedspec = new JButton("Get FEDSpec");
		btnGetFedspec.addActionListener(new BtnGetFedspecActionListener());
		GroupLayout gl_panel_1 = new GroupLayout(panel_1);
		gl_panel_1.setHorizontalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_panel_1.createSequentialGroup()
					.addComponent(lblUserid)
					.addGap(18)
					.addGroup(gl_panel_1.createParallelGroup(Alignment.TRAILING)
						.addComponent(btnGetFedspec, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
						.addComponent(userIDTextField, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_panel_1.setVerticalGroup(
			gl_panel_1.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_1.createSequentialGroup()
					.addGroup(gl_panel_1.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUserid)
						.addComponent(userIDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnGetFedspec)
					.addContainerGap(25, Short.MAX_VALUE))
		);
		panel_1.setLayout(gl_panel_1);
		
		panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Get FEMO", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.setBounds(262, 129, 235, 74);
		frmExperimentRegistryManagement.getContentPane().add(panel_2);
		
		JLabel lblExpId = new JLabel("Exp ID");
		
		experimentIDTextField = new JTextField();
		experimentIDTextField.setText("expID");
		experimentIDTextField.setColumns(10);
		
		JButton btnGetFEMO = new JButton("Get FEMO");
		btnGetFEMO.addActionListener(new BtnGetFEMOActionListener());
		GroupLayout gl_panel_2 = new GroupLayout(panel_2);
		gl_panel_2.setHorizontalGroup(
			gl_panel_2.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addComponent(lblExpId, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_2.createParallelGroup(Alignment.LEADING)
						.addComponent(btnGetFEMO, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
						.addComponent(experimentIDTextField, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(41, Short.MAX_VALUE))
		);
		gl_panel_2.setVerticalGroup(
			gl_panel_2.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_panel_2.createSequentialGroup()
					.addGroup(gl_panel_2.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblExpId)
						.addComponent(experimentIDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnGetFEMO)
					.addGap(25))
		);
		panel_2.setLayout(gl_panel_2);
		
		panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Get FISMO", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3.setBounds(10, 214, 235, 74);
		frmExperimentRegistryManagement.getContentPane().add(panel_3);
		
		serviceIDTextField = new JTextField();
		serviceIDTextField.setText("serviceID");
		serviceIDTextField.setColumns(10);
		
		lblServiceid = new JLabel("ServiceID");
		
		btnGetFISMO = new JButton("Get FISMO");
		btnGetFISMO.addActionListener(new BtnGetFISMOActionListener());
		GroupLayout gl_panel_3 = new GroupLayout(panel_3);
		gl_panel_3.setHorizontalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addComponent(lblServiceid)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(serviceIDTextField, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(11, Short.MAX_VALUE))
				.addGroup(Alignment.TRAILING, gl_panel_3.createSequentialGroup()
					.addContainerGap(51, Short.MAX_VALUE)
					.addComponent(btnGetFISMO, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
		);
		gl_panel_3.setVerticalGroup(
			gl_panel_3.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel_3.createSequentialGroup()
					.addGap(0)
					.addGroup(gl_panel_3.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblServiceid)
						.addComponent(serviceIDTextField, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
					.addComponent(btnGetFISMO))
		);
		panel_3.setLayout(gl_panel_3);
		
		JButton btnSaveTestUserExperiment = new JButton("Save Test User Experiment");
		btnSaveTestUserExperiment.addActionListener(new BtnSaveTestUserExperimentActionListener());
		btnSaveTestUserExperiment.setBounds(10, 69, 170, 23);
		frmExperimentRegistryManagement.getContentPane().add(btnSaveTestUserExperiment);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Get User Exp Descriptions", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_4.setBounds(262, 214, 235, 74);
		frmExperimentRegistryManagement.getContentPane().add(panel_4);
		
		JLabel lblUserid_1 = new JLabel("UserID");
		
		JButton btnGetExpDescr = new JButton("Get Exp. Descr.");
		btnGetExpDescr.addActionListener(new BtnGetExpDescrActionListener());
		
		txtUseridForExpDescr = new JTextField();
		txtUseridForExpDescr.setText("userID");
		txtUseridForExpDescr.setColumns(10);
		GroupLayout gl_panel_4 = new GroupLayout(panel_4);
		gl_panel_4.setHorizontalGroup(
			gl_panel_4.createParallelGroup(Alignment.LEADING)
				.addGap(0, 235, Short.MAX_VALUE)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addComponent(lblUserid_1, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_panel_4.createParallelGroup(Alignment.LEADING)
						.addComponent(btnGetExpDescr, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE)
						.addComponent(txtUseridForExpDescr, GroupLayout.PREFERRED_SIZE, 162, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(18, Short.MAX_VALUE))
		);
		gl_panel_4.setVerticalGroup(
			gl_panel_4.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 74, Short.MAX_VALUE)
				.addGroup(gl_panel_4.createSequentialGroup()
					.addGroup(gl_panel_4.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblUserid_1)
						.addComponent(txtUseridForExpDescr, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnGetExpDescr)
					.addGap(25))
		);
		panel_4.setLayout(gl_panel_4);
	}
	private class BtnNewButtonActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			
			ermClient.welcomeMessage();
			
			
		}
	}
	private class BtnOpenFedspecActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			JFileChooser fc = new JFileChooser();
			
			FileNameExtensionFilter filter = new FileNameExtensionFilter("XML Files", "xml", "xml");
			fc.setFileFilter(filter);
			
			int returnVal = fc.showOpenDialog(ErmUiClient.this);
	        if (returnVal == JFileChooser.APPROVE_OPTION){
	            File file = fc.getSelectedFile();	        
	            fedSpecPathField.setText(file.getAbsolutePath());	            
	        } else {
	        	logger.debug("Open command cancelled by user." + "\n");
	        }			
		}
	}
	private class BtnSaveFedspecActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			try {
				String responseMsg = ermClient.saveFromFile2(fedSpecPathField.getText());
				if (responseMsg==null){
					JOptionPane.showMessageDialog(ErmUiClient.this, 
							"Error registering service. Check log.", 
							"Register service error.", 
							JOptionPane.ERROR_MESSAGE);
				}	else {
					JOptionPane.showMessageDialog(ErmUiClient.this, 
							"ResponseMsg: " + responseMsg, 
							"Registering service.", 
							JOptionPane.PLAIN_MESSAGE);
				}
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(ErmUiClient.this, 
						"Error opening file.", 
						"Error opening file.", 
						JOptionPane.ERROR_MESSAGE);
			}	


		}
	}
	private class BtnGetFedspecActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			ermClient.getALLUserExperimentsEntity(userIDTextField.getText());
			
		}
	}
	private class BtnGetFEMOActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			ermClient.getExperimentModelObjectEntity(experimentIDTextField.getText());
		}
	}
	private class BtnGetFISMOActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			ermClient.getExperimentServiceModelObjectEntity(serviceIDTextField.getText());
			
		}
	}
	private class BtnSaveTestUserExperimentActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			String responseMsg = ermClient.populateAndSaveTestUserExperiment();
			
			JOptionPane.showMessageDialog(ErmUiClient.this, 
					"responseMsg: " + responseMsg,
					"Registering service.",  
					JOptionPane.PLAIN_MESSAGE);
			
			
		}
	}
	private class BtnGetExpDescrActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			
			
			ermClient.getAllUserExperimentsDescreptions(txtUseridForExpDescr.getText());
			
		}
	}
}
