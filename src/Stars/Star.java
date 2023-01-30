package Stars;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */




import Joueur.Joueur;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 *
 * @author Chaimae El Mahdaj
 */
public class Star extends Point {

	private int numero;

	private int proprio;
	
	private boolean isDefinitif;

	private BufferedImage bf;

	private File urlBgrise = new File("C:\\Users\\Chaimae El Mahdaj\\Desktop\\Miniprogetjava nim\\GameNIMStars\\Photos\\sgrise.png");

	private File urlBbleu = new File("C:\\Users\\Chaimae El Mahdaj\\Desktop\\Miniprogetjava nim\\GameNIMStars\\Photos\\sbleu.png");

	private File urlBrouge = new File("C:\\Users\\Chaimae El Mahdaj\\Desktop\\Miniprogetjava nim\\GameNIMStars\\Photos\\srouge.png");
	
	private File urlBverte = new File("C:\\Users\\Chaimae El Mahdaj\\Desktop\\Miniprogetjava nim\\GameNIMStars\\Photos\\sverte.png");

	/**
	 * Construit une etoile.
	 * @param x coordonnée x
	 * @param y coordonnée y
	 */
	public Star(int x, int y) {
		super(x, y);
		proprio = Joueur.personne;
		try {
			bf = ImageIO.read(urlBgrise);
		} catch (IOException e) {
			e.printStackTrace();
		}
		isDefinitif = false;
	}

	/**
	 * Construit une etoile.
	 * @param x coordonnée x
	 * @param y coordonnée y
	 * @param numero numéro de l'etoile
	 */
	public Star(int x, int y, int numero) {
		this(x, y);
		this.numero = numero;
	}

	/**
	 * Permet de savoir si le pointeur de la souris est dans l'etoile.
	 * @param x coordonnée x
	 * @param y coordonnée y
	 * @return true si un clic s'est effectué dans l'etoile
	 */
	public boolean isInStar(int x, int y) {
		boolean ret = false;
		int myX = super.x;
		int myY = super.y;
		if ((x >= myX) && (x <= (myX + 22)) && (y >= myY) && (y <= (myY + 22))) {
			ret = true;
		}
		return ret;
	}

	/**
	 * Retourne le numero de l'etoile.
	 * @return Numéro de l'etoile
	 */
	public int getNumero() {
		return numero;
	}

	/**
	 * Retourne le proprietaire de l'etoile.
	 * @return Le proprietaire de l'etoile
	 */
	public int getProprio() {
		return proprio;
	}

	/**
	 * Change le proprietaire de l'etoile.
	 * @param p nouveau proprietaire
	 */
	public void setProprio(int p) {
		proprio = p;
		switch (p) {
		case Joueur.personne:
			try {
				bf = ImageIO.read(urlBgrise);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case Joueur.hjoueur:
			try {
				bf = (isDefinitif==true)?ImageIO.read(urlBbleu):ImageIO.read(urlBverte);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case Joueur.AIjoueur:
			try {
				bf = ImageIO.read(urlBrouge);
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}

	/**
	 * Retourne l'image de l'etoile.
	 * @return Image de l'etoile (BufferedImage)
	 */
	public BufferedImage getImage() {
		return bf;
	}
	
	/**
	 * Permet de savoir si cette etoile est definitive.
	 * @return true si l'etoile ne peut plus etre deplacée
	 */
	public boolean isDefinitif() {
		return isDefinitif;
	}
	
	/**
	 * Permet de changer le caractere definitif d'une etoile.
	 * @param b true si l'etoile est definitive, false dans l'autre cas
	 */
	public void setDefinitif(boolean b) {
		isDefinitif = b;
		try {
			bf = (isDefinitif==true)?ImageIO.read(urlBbleu):ImageIO.read(urlBverte);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
