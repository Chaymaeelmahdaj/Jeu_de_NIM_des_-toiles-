package Joueur;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author Chaimae El Mahdaj
 */
public class Joueur {
	public final static int personne = 0;

	public final static int hjoueur = 1;

	public final static int AIjoueur = 2;

	private int joueur;

	/**
	 * Construit une nouveau joueur.
	 * @param j Le type de joueur
	 */
	public Joueur(int j) {
		joueur = j;
	}

	/**
	 * Retourne le type de joueur.
	 * @return Le type de joueur
	 */
	public int getType() {
		return joueur;
	}
}

