import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Fenetre2 extends JFrame {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7989616760467843646L;
	
	private JPanel pan = new JPanel();
	private JTextField fieldhost=new JTextField();
	private JLabel labelhost=new JLabel();

	private JTextArea areareponse=new JTextArea("Réponse du serveur",20,10);
	private JTextField fieldtrame=new JTextField("Trame à envoyer au serveur");
	private JTextField fieldport=new JTextField();
	private JLabel labelport=new JLabel();

	private JButton boutonconnect = new JButton();
	private JButton boutonquit = new JButton("Quit");
	private JButton boutonsend = new JButton("Send");
	private EchoClient client;
	private String mode;
	private Echoserver serv;

	public Fenetre2(int x, int y, String passedmode){
		mode=passedmode;
		this.setTitle("Mode "+mode);

		this.setSize(x, y);

		this.setLocationRelativeTo(null);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		boutonconnect.setText("Démarrer le "+mode);
		pan.add(labelhost);
		pan.add(fieldhost);
		pan.add(labelport);
		pan.add(fieldport);
		pan.add(boutonconnect);
		pan.add(fieldtrame);
		pan.add(boutonsend);
		pan.add(areareponse);
		pan.add(boutonquit);

		labelhost.setText("IP de l'hôte");
		labelport.setText("N° de port");
		boutonconnect.addActionListener(new BoutonListener());
		boutonquit.addActionListener(new BoutonListener());
		boutonsend.addActionListener(new BoutonListener());
		fieldport.setText("80");
		fieldhost.setText("127.0.0.1");
		
		if(mode == "Serveur"){
			fieldhost.setEnabled(false);
			serv = new Echoserver();
		}else{
			client = new EchoClient();
		}

		this.setContentPane(pan);
		this.setVisible(true);
	}

	class BoutonListener  implements ActionListener{

		/**
		 * Redéfinition de la méthode actionPerformed
		 */

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getSource() == boutonconnect){
				if (mode=="Client"){
					client.port=Integer.parseInt(fieldport.getText());
					client.serveur=fieldhost.getText();
					client.connect();
				}
				if (mode=="Serveur"){
					System.out.println("fieldport.getText() = " +fieldport.getText()); 
		            serv.port=Integer.parseInt(fieldport.getText());
					serv.start();
					boutonsend.setEnabled(false);
				}
				boutonconnect.setEnabled(false);
			}

			if(e.getSource() == boutonquit){
				try{
					if(mode == "Serveur"){
						serv.askEnd=true;
						EchoClient exitClient = new EchoClient();
						exitClient.connect();
						exitClient.close();
					}else{ 
						client.close();
					}
				}
				catch(Exception e1) 
				{ 
					e1.printStackTrace(); 
				} 
				Fenetre2.this.dispose();					
			}

			if(e.getSource() == boutonsend){
				if(mode == "Client"){
					areareponse.setText(areareponse.getText()+"\n"+client.send(Fenetre2.this.fieldtrame.getText()));
				}
			}

		}
	}
}