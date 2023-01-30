package Stars;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import Stars.Star;
import Joueur.Moteur;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 *
 * @author Chaimae El Mahdaj
 */
public class StarScreen extends JPanel {

	private final static int cWidth = 900;

	private final static int cHeight = 600;

	private final static int cTotal =600;

	private int nbStars;

	private double deg;
	private BufferedImage fond;

	private boolean firstDraw;

	private ObservableInterne obs;

	private Moteur moteur;

	/**
	 * Construit un nouvel ecran d'etoiles.
	 * @param m Moteur de jeu
	 */
	public StarScreen(Moteur m) {
		// ajout de l'osbservable
		obs = new ObservableInterne();
		// on doit remettre a zero le tableau de boules
		firstDraw = true;
		// nombre de boules
		this.nbStars = m.getNbStars();// nbStars;
		// calcul des degrés pour la rotation
		deg = cTotal / m.getNbStars();// nbStars;
		// Enregistrement du moteur de jeu
		moteur = m;
		// chargement des images
		try {
			fond = ImageIO.read(new File( "C:\\Users\\Chaimae El Mahdaj\\Desktop\\Miniprogetjava nim\\GameNIMStars\\Photos\\background.jpg"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Pointeur de souris
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		// taille de la fenêtre
		setPreferredSize(new Dimension(900,600));
		initBehaviours();
	}

	/**
	 * Initialise les listeners.
	 *
	 */
	private void initBehaviours() {
		this.addMouseListener(new MouseAdapter() {
                        @Override
			public void mouseClicked(MouseEvent arg0) {
				Point p = new Point(arg0.getX(), arg0.getY());
				obs.notifyObs(p);
				repaint();
			}
		});
	}

	/**
	 * Permet d'ajouter un observer.
     * @param observer
	 */
	public void addObserver(Observer observer) {
		assert observer != null : "Observer ne peut être null";
		obs.addObserver(observer);
	}

	/**
	 * Permet de supprimer un observer.
     * @param observer
	 */
	public void deleteObserver(Observer observer) {
		assert observer != null : "Observer ne peut être null";
		obs.deleteObserver(observer);
        }

	/**
	 * Paint les etoiles et le fond.
	 */
        @Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		double x = 0;
		double y = -270;
		double xp, yp;
		double degC;
		xp = x;
		yp = y;
		Graphics2D g2d = (Graphics2D) g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
				RenderingHints.VALUE_COLOR_RENDER_QUALITY);
		g2d.drawImage(fond, 0, 0, cWidth, cHeight, this);
		g2d.setColor(Color.WHITE);
		g2d.drawString("Temps : " + moteur.getSearchTime() + " ms", 650, 25);
		if (moteur.getModeIA() == Moteur.MOD_1) {
			g2d.drawString("Algorithme de NIM ", 650, 40);
		}
		g2d.drawString("Noeuds : " + moteur.getNbNoeuds(), 650, 55);
		/*
		 * g2d.drawOval(cDWidth - 5, cDHeight, cWidth - (2 * cDWidth) - 13,
		 * cHeight - (2 * cDHeight) - 13);
		 */
		g2d.translate(280, 280);
		// ajustement du coefficient de rotation (util sur grandes valeurs)
		if (nbStars == 17)
			degC = (Math.PI * deg +0.5 ) / 300;
		else if (nbStars == 15)
			degC = (Math.PI * deg) / 290;
		else
			degC = (Math.PI * deg + (nbStars / 5)) / 280;
		for (int i = 0; i < nbStars; i++){ 
			if (firstDraw) { // on dessine pour la premiere fois
				// creation d'une boule
				Star s = new Star((int) xp + 280, (int) yp + 280, i);
				// on sauvegarde les coordonnées du point en cours
				moteur.getStars().add(s);
			} else { // creation des lignes interétoiles
				g2d.setColor(Color.GRAY);
				if (i <= (nbStars / 2)) {
					int pos1, pos2;
					int mid = nbStars / 2;
					pos1 = mid + i;
					pos2 = mid + i + 1;
					g2d.translate(-280, -280);
					if (pos2 == nbStars) {
						g2d.drawLine(((Star) moteur.getStars().get(i)).x + 12,
								((Star) moteur.getStars().get(i)).y + 12,
								((Star) moteur.getStars().get(pos1)).x + 12,
								((Star) moteur.getStars().get(pos1)).y + 12);
					} else {
						g2d.drawLine(((Star) moteur.getStars().get(i)).x + 12,
								((Star) moteur.getStars().get(i)).y + 12,
								((Star) moteur.getStars().get(pos1)).x + 12,
								((Star) moteur.getStars().get(pos1)).y + 12);
						g2d.drawLine(((Star) moteur.getStars().get(i)).x + 12,
								((Star) moteur.getStars().get(i)).y + 12,
								((Star) moteur.getStars().get(pos2)).x + 12,
								((Star) moteur.getStars().get(pos2)).y + 12);
					}
					g2d.translate(280, 280);

				}
			}
			// on dessine le point
			// g2d.drawRect((int) xp, (int) yp, 22, 22);
			g2d.setColor(Color.RED);
			g2d.drawString(String.valueOf(i), (int) xp, (int) yp);
			g2d.setColor(Color.BLACK);
			// on dessine la boule
			g2d.drawImage(((Star) moteur.getStars().get(i)).getImage(),
					(int) xp, (int) yp, 22, 22, this);
			// rotation des points
			xp = x * Math.cos(degC) - y * Math.sin(degC);
			yp = x * Math.sin(degC) + y * Math.cos(degC);
			x = xp;
			y = yp;

		}
		if (firstDraw) {
			firstDraw = false;
			repaint();
			// System.out.println("Creation du tableau d'étoiles terminée !");
		}
	}

	/**
	 * Observable interne redéfinie pour faire un setChanged().
	 * 
	 * @author Spawnrider
	 */
	private class ObservableInterne extends Observable {
		/**
		 * Notifie les obersvers.
		 */
		public void notifyObs() {
			setChanged();
			notifyObservers();
		}

		/**
		 * Notifie les observers avec un message.
		 * 
		 * @param arg
		 *            message.
		 */
		public void notifyObs(Point arg) {
			setChanged();
			notifyObservers(arg);
		}
	}

}

