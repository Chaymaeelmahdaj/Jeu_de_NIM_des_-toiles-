package Joueur;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Chaimae El Mahdaj
 */
public class Configuration{

	private HashMap config;
	
	/**
	 * Constructeur par défaut.
	 */
	public Configuration() {
		config = new HashMap();
	}
	
	/**
	 * Construit une nouvelle configuration a partir d'une Hashmap.
	 * @param hm HashMap provenant d'une configuration
	 */
	public Configuration(HashMap hm) {
		config = new HashMap();
		config.putAll(hm);
	}
	
	/**
	 * Construit une nouvelle configuration a partir d'un nombre d'etoiles.
	 * @param nbEtoile Nombre d'etoiles
	 */
	public Configuration(int nbEtoile) {
		config = new HashMap();
		config.put(new Integer(nbEtoile), new Integer(1));
	}

	/**
	 * Recupere une configuration de jeu.
	 * @return Hashmap : configuration de jeu
	 */
	public HashMap getConfig() {
		return config;
	}
	
	/**
	 * Permet de savoir si une configuration est finale ou pas.
	 * @return true si la configuration est finale
	 */
	public boolean isFinale() {
		HashMap hm = getConfig();
		if(hm.size()==1){
			if(hm.containsKey(new Integer(0))){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Représente sous forme de chaine, une configuration.
	 */
	public String toString() {
		String str = "";
		Iterator it = config.keySet().iterator();	
		while(it.hasNext()) {
			Integer i = (Integer)it.next();
			//System.out.println(" Valeur : "+i.toString());
			int val = ((Integer)config.get(i)).intValue();
			str += val+i.toString()+".";
		}
		str = str.substring(0, str.length()-1);
		if(isFinale()){
			str+=" FINALE";
		}
		return str;
	}
}

