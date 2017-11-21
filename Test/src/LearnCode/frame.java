package LearnCode;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.GridLayout;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JTextField;

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class frame implements ActionListener{

	private JFrame frame;
	private JTextField textField;
	JButton btnOk;
	String verificationCode;

	/**
	 * Launch the application.
	 */
/*	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame window = new frame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 */
	public frame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setContentPane(createPanel());
		frame.setVisible(true);
		frame.setBounds(100, 100, 581, 410);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));
		
	}
	
	public JPanel createPanel(){
		JPanel panel = new JPanel();
		panel.setVisible(true);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(123, 7, 300, 200);
		lblNewLabel.setIcon(new ImageIcon("C:\\Users\\qauser\\Desktop\\Verify_Picture_screenshot.jpg"));
		panel.add(lblNewLabel);
		
		JLabel label = new JLabel("");
		label.setBounds(400, 186, 0, 0);
		panel.add(label);
		
		JLabel lblTypeTheCharacters = new JLabel("Type the characters in the image ");
		lblTypeTheCharacters.setBounds(104, 218, 332, 23);
		lblTypeTheCharacters.setFont(new Font("Arial Black", Font.BOLD | Font.ITALIC, 16));
		panel.add(lblTypeTheCharacters);
		
		textField = new JTextField();
		textField.setBounds(220, 252, 89, 36);
		textField.setFont(new Font("Arial", Font.BOLD, 14));
		panel.add(textField);
		textField.setColumns(4);
		
		btnOk = new JButton("OK");
		btnOk.setFont(new Font("Arial", Font.BOLD, 32));
		btnOk.setBounds(204, 312, 125, 49);
		btnOk.addActionListener(this);
		panel.add(btnOk);
		
		return panel;
	}
	
    public void actionPerformed(ActionEvent e) {
    	
    	if(e.getSource() == btnOk){
    		
    		verificationCode = textField.getText();
    		
    		frame.setVisible(false);
    		frame.dispose();
    		
    		System.out.println(verificationCode);

    		
    	}

    }

}
