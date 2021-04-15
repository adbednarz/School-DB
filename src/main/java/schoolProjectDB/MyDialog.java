package schoolProjectDB;

import java.awt.Cursor;
import java.awt.Color;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class MyDialog extends JDialog implements ActionListener {

	private JPanel jPanel;	
	private JLabel userLabel;	
	private JLabel passLabel;
	private JTextField userField;
	private JPasswordField passField;	
	private JButton logButton;	
	private JLabel wrongInput;
	private GUI parent;
	
	public MyDialog(GUI parentFrame) {
	
		super(parentFrame, "Szkoła", true);
		this.parent = parentFrame;
		jPanel = new JPanel(new GridBagLayout());
		
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = new Insets(10, 10, 10, 10);
		
		userLabel = new JLabel("Użytkownik:");
		
		constraints.gridx = 0;
		constraints.gridy = 0;
		
		jPanel.add(userLabel, constraints);
		
		passLabel = new JLabel("Hasło:");
		
		constraints.gridx = 0;
		constraints.gridy = 1;
		
		jPanel.add(passLabel, constraints);
		
		userField = new JTextField(17);
		userField.setFont(new Font("Serif", Font.BOLD, 11));
		
		constraints.gridx = 1;
		constraints.gridy = 0;
		
		jPanel.add(userField, constraints);
		
		passField = new JPasswordField(20);
		
		constraints.gridy = 1;
		
		jPanel.add(passField, constraints);

		
		logButton = new JButton("Zaloguj");
		
		constraints.gridx = 0;	
		constraints.gridy = 2;	
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.CENTER;
		
		logButton.addActionListener(this);
		jPanel.add(logButton, constraints);
		
		
		Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
		logButton.setCursor(cursor); 
		
		wrongInput = new JLabel();
		
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		constraints.anchor = GridBagConstraints.CENTER;
	
		jPanel.add(wrongInput, constraints);
	
		jPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Logowanie"));
		add(jPanel);
		pack();
		setLocationRelativeTo(null);
	}

	@Override
	public void actionPerformed(ActionEvent event) {

		String usernameInput = userField.getText();
		String passwordInput = String.valueOf(passField.getPassword());
	
		if (event.getSource() == logButton) {
			if (performAuthentication(usernameInput, passwordInput)) {
				dispose();
			} else {
				wrongInput.setForeground(Color.RED);
				wrongInput.setText("Niepoprawne dane, spróbuj ponownie.");
				pack();
			}
		}
	}
	
	public boolean performAuthentication(String usernameInput, String passwordInput) {
		try {
			parent.setDatabaseConnector(new DatabaseConnector("root", "Alahaski2"));
		}catch(Exception e) {
			parent.setUser("root");
			return true;
		}
		parent.setUser("root");
		return true;
	}
}
