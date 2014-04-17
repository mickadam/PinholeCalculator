import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
 
public class Fenetre extends JFrame {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = 8911186875883779876L;
	private JPanel pan = new JPanel();
    private JButton boutonclient = new JButton("Client");
    private JButton boutonserveur = new JButton("Serveur");
/*    private JTextField txtfield=new JTextField();
    private JLabel monlabel=new JLabel();
 */   
    public Fenetre(int x, int y){
    this.setTitle("Sélection du mode de communication");

    this.setSize(x, y);

    this.setLocationRelativeTo(null);

    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    pan.add(boutonclient);
    pan.add(boutonserveur);
    boutonclient.addActionListener(new BoutonListener());
    boutonserveur.addActionListener(new BoutonListener());
/*    pan.add(monlabel);
    monlabel.setText("monlabel");
    bouton.addActionListener(new BoutonListener());
    txtfield.setText("127.0.0.1");
    pan.add(txtfield);
*/
    this.setContentPane(pan);
    this.setVisible(true);}
    
    class BoutonListener  implements ActionListener{
    	 
        /**
         * Redéfinition de la méthode actionPerformed
         */

		@SuppressWarnings("unused")
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			System.out.println("OK");
			Fenetre.this.setVisible(false);
			Fenetre2 fenetre2 = new Fenetre2(200, 300, ((JButton) e.getSource()).getText());
				/*		    Fenetre.this.setSize(Fenetre.this.getWidth()*2,Fenetre.this.getHeight()*2);
*/
		}

       
}
    
}
