package Stars;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Chaimae El Mahdaj
 */


import Joueur.Moteur;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Observable;
import java.util.Observer;
import javax.swing.Icon;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 *
 * @author Chaimae El Mahdaj
 */
public class MyGameStars extends JFrame implements Observer{
    private StarScreen starScreen;

	private JButton btNew, btPlay, btQuit,btNbConf;

	private static MyGameStars mg;

	private Moteur moteur;

	private ImageIcon iconWizard = new ImageIcon("C:\\Users\\Chaimae El Mahdaj\\Desktop\\Miniprogetjava nim\\GameNIMStars\\Photos\\wizard.png");

	/**
	 * Construit une nouvelle instance du jeu.
	 * (Singleton).
	 */
	public MyGameStars() {
		super("Jeu de Nim ");
		createComponents();
		placeComponents();
		initBehaviours();
		setLocation(0, 0);
		setResizable(false);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(getParent());
		setVisible(true);
	}

	/**
	 * Recupere l'instance de jeu.
	 * @return Instance de jeu
	 */
	public static MyGameStars getInstance() {
		if (mg == null) {
			mg = new MyGameStars();
		}
		return mg;
	}

	/**
	 * Initialise les listeners et observers.
	 *
	 */
	private void initBehaviours() {
		// action sur le bouton nouvelle partie
		btNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				newGame();
				// moteur.createTree();
			}
		});
		// acion sur le bouton play
		btPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// on indique au moteur que le joueur a valider son(ses) coup(s)
				moteur.jouer();
			}
		});
		//action sur le bouton quitter
		btQuit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, "Merci d'avoir jouer au jeu de NIM ;)", "Jeu de l'étoile",
						JOptionPane.INFORMATION_MESSAGE);
				System.exit(1);
			}
		});
		//	action sur le bouton NbConf
		btNbConf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				moteur.createTree();
			}
		});
		// ajout de l'observer
		starScreen.addObserver(moteur);
	}

	/**
	 * Demarre une nouvelle partie.
	 *
	 */
	private void newGame() {
		// ici le code de fin de partie
		Object[] possibilities = { "3", "5", "7", "9", "11", "13","15","17" };
		String nbEtoilesStr= null;
			nbEtoilesStr = (String) JOptionPane.showInputDialog(null,
				"Choisissez le nombre d'étoiles:\n",
				"Jeu de NIM", JOptionPane.PLAIN_MESSAGE, (Icon) iconWizard, possibilities, "3");
		if(nbEtoilesStr==null) return;

		int nbEtoiles = new Integer(nbEtoilesStr).intValue();

		Object[] options = { "Commencer"};
		int minmax = JOptionPane.showOptionDialog(null,
				"Bienvenue à jeu de NIM bonne chance",
				"Jeu de NIM", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, // the titles of
				// buttons
				options[0]); // default button title
		int typeIA = (minmax == 0) ? Moteur.MOD_1 : null;
		setVisible(false);
		getContentPane().remove(starScreen);
		starScreen.deleteObserver(moteur);
		moteur = new Moteur(this, nbEtoiles, typeIA);
		starScreen = new StarScreen(moteur);
		getContentPane().add(starScreen, BorderLayout.CENTER);
		// ajout de l'observer
		starScreen.addObserver(moteur);
		repaint();
		repaintStarScreen();
		setVisible(true);
	}

	/**
	 * Place graphiquement les composants.
	 *
	 */
	private void placeComponents() {
		setLayout(new BorderLayout());
		Container c = getContentPane();
		c.add(starScreen, BorderLayout.CENTER);
		JPanel p = new JPanel();
		p.setBackground(new Color(255, 255, 255));
		p.add(btPlay);
		p.add(btNew);
		p.add(btNbConf);
		p.add(btQuit);
		c.add(p, BorderLayout.SOUTH);

	}

	/**
	 * Crée les composants.
	 *
	 */
	private void createComponents() {
		// ici le code de fin de partie
		Object[] possibilities = { "3", "5", "7", "9", "11", "13","15","17"};
		String nbEtoilesStr= null;
		do {
			nbEtoilesStr = (String) JOptionPane.showInputDialog(null,
				"Choisissez le nombre d'étoiles:\n",
				"Jeu de NIM", JOptionPane.PLAIN_MESSAGE, (Icon) iconWizard, possibilities, "3");
		} while(nbEtoilesStr==null);
		int nbEtoiles = new Integer(nbEtoilesStr).intValue();

		Object[] options = { "Commencer"};
		int minmax = JOptionPane.showOptionDialog(null,
				"Bienvenue à jeu de NIM bonne chance",
				"Jeu de NIM", JOptionPane.YES_NO_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, // the titles of
				// buttons
				options[0]); // default button title
		int typeIA = (minmax == 0) ? Moteur.MOD_1: null;
		moteur = new Moteur(this, nbEtoiles,typeIA);
		starScreen = new StarScreen(moteur);
		btNew = new JButton("Nouvelle partie");
		btPlay = new JButton("Jouer");
		btQuit = new JButton("Quitter");
		btNbConf = new JButton("Taille arbre");
		btPlay.setEnabled(false);
	}

	/**
	 * Main principal.
	 * @param args pas d'arguments !
	 */
	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				MyGameStars.getInstance();
			}
		});
	}

	/**
	 * Observation du starScreen.
     * @param arg0
     * @param arg1
	 */
    @Override
	public void update(Observable arg0, Object arg1) {
		if(arg1.getClass()==Boolean.class){
			btPlay.setEnabled(((Boolean) arg1).booleanValue());
		}else {
			newGame();
		}
	}

	/**
	 * Repaint l'ecran d'étoiles.
	 *
	 */
	public void repaintStarScreen() {
		starScreen.repaint();
	}
}

