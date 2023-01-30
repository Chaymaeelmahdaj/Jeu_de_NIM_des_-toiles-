package Joueur;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */





import Stars.MyGameStars;
import Stars.Star;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;

/**
 *
 * @author Chaimae El Mahdaj
 */
public class Moteur extends Observable implements Observer {

	private ArrayList tabBoule;

	private final int nbMaxCoups =3;

	private int numCoup;

	private MyGameStars mgs;

	private int nbEtoiles;

	private int numeroConf;

	private int modeIA;

	private long SearchTime;

	public final static int MOD_1 = 1;

	

	/**
	 * Construit un nouveau moteur de jeu.
	 * 
	 * @param m
	 *            MyGameStars principal
	 * @param nbE
	 *            Nombre d'étoiles
	 */
	private Moteur(MyGameStars m, int nbE) {
		tabBoule = new ArrayList();
		mgs = m;
		numCoup = 0;
		numeroConf = 0;
		nbEtoiles = nbE;
		modeIA = MOD_1;
		deleteObservers();
		this.addObserver(m);
	}

	/**
	 * Construit un nouveau moteur de jeu.
	 * 
	 * @param m
	 *            MyGameStars principal
	 * @param nbE
	 *            Nombre d'étoiles
	 * @param typeIA
	 *            Algorithme utilisé pour le moteur de jeu
	 */
	public Moteur(MyGameStars m, int nbE, int typeIA) {
		this(m, nbE);
		// changement du mode IA : mod1 ou mod2
		modeIA = typeIA;
	}

	/**
	 * Permet au moteur de jeu de savoir quelle etoile à été jouée.
	 */
	public void update(Observable arg0, Object arg1) {
		Point p = (Point) arg1;
		for (int i = 0; i < tabBoule.size(); i++) {
			Star s = (Star) tabBoule.get(i);
			if (s.isInStar(p.x, p.y)) {
                            switch (s.getProprio()) {
				case Joueur.personne:
					if (numCoup >= nbMaxCoups) {
						// nombre maxi de coups atteint !
						showMessage("Vous avez déjà séléctionné deux étoiles !");
						return;
					}
					// on verifie que le coup est valide quand le joueur
					// va selectionner sa deuxieme etoile
					if (numCoup == 1) {
						int pos = 0;
						Star sIn = null;
						for (int j = 0; j < tabBoule.size(); j++) {
							sIn = (Star) tabBoule.get(j);
							if (!sIn.isDefinitif()&& sIn.getProprio() == Joueur.hjoueur) {
								pos = j;
							}
						}
						// on a trouvé la boule d'avant, on verifie la deuxieme
						if (pos == 0) { // cas gauche
							if ((s.getNumero() != 1) & (s.getNumero() != (nbEtoiles -1))) {
								showMessage("L'étoile ne peut pas être séléctionnée !");
								return; // on annule la selection
							}
						} else {
							if (pos == nbEtoiles - 1) { // cas droit
								if (s.getNumero() != 0
										&& s.getNumero() != pos -1) {
									showMessage("L'étoile ne peut pas être séléctionnée !");
									return; // on annule la selection
								}
							} else {
								if ((s.getNumero() != pos -1 )
										&& (s.getNumero() != pos +1)) {
									showMessage("L'étoile ne peut pas être séléctionnée !");
									return; // on annule la selection
								}
							}
						}
					}
					// Changement du proprietaire de la boule
					s.setProprio(Joueur.hjoueur);
					// on remet la boule dans le tableau
					tabBoule.set(i, s);
					// on notifie que le joueur a jouer un coup valide
					setChanged();
					notifyObservers(new Boolean("true"));
					// on ajoute un coup
					numCoup++;
					break;
				case Joueur.hjoueur:
					if (s.isDefinitif()) {
						showMessage("Cette étoile vous appartient !");
						return;
					}
					// Changement du proprietaire de la boule
					s.setProprio(Joueur.personne);
					// on remet la boule dans le tableau
					tabBoule.set(i, s);
					// on enleve un coup
					numCoup--;
					// on notifie que le joueur a jouer un coup valide
					setChanged();
					if (numCoup == 0) {
						notifyObservers(new Boolean("false"));
					} else {
						notifyObservers(new Boolean("true"));
					}
					break;
				case Joueur.AIjoueur:
					showMessage("Cette étoile appartient à l'ordinateur !");
					break;
				}
			}
		}
	}

	/**
	 * Permet de jouer un coup pour le joueur et pour l'ia.
	 *
	 */
	public void jouer() {
		// on remet à zéro le compteur de coups !
		numCoup = 0;
		// on notifie qu'il ne peut plus appuyez sur play
		setChanged();
		notifyObservers(new Boolean("false"));
		// initilisation variables
		int tabPos[] = new int[3];
		int pos = 0;
		Star s = null;
		// initialisation des coups vides
		tabPos[0] = -1;
		tabPos[1] = -1;
                tabPos[2] = -1;
		// recuperation de la liste de coups
		for (int i = 0; i < getNbStars(); i++) {
			s = (Star) getStars().get(i);
			if (!s.isDefinitif() && s.getProprio() == Joueur.hjoueur) {
				tabPos[pos++] = i;
				s.setDefinitif(true);
				getStars().set(i, s);
			}
		}
		// on repaint le starScreen
		mgs.repaintStarScreen();
		// suite des actions..
		pos = 0;
		/*
		 * if (tabPos[1] != -1) { System.out.println("Le joueur à séléctionné 2
		 * étoiles N°" + tabPos[0] + " et N°" + tabPos[1]); } else {
		 * System.out.println("Le joueur à séléctionné l'étoile N°" +
		 * tabPos[0]); }
		 */
		// On evalue le coup du joueur
		int tab[] = new int[tabBoule.size()];
		// on recupere le jeu actuel
		boolean finDePartie = true;
		for (int i = 0; i < tabBoule.size(); i++) {
			tab[i] = ((Star) tabBoule.get(i)).getProprio();
			if (tab[i] == Joueur.personne)
				finDePartie = false;
		}
		if (finDePartie) {
			showMessage("Vous avez gagné !");
			newGame();
		}

		// on créer une configuration avec le jeu actuel
		// sauvegarde du decalage ici
		int decalage = getDecalage(tab);
		/*
		 * Configuration cJoueur = getConf(tab);
		 * System.out.println("Configuration JOUEUR : " + cJoueur + " Eval " +
		 * minmax(cJoueur, Joueur.joueurMin));
		 */

		//Numero de configuration (compteur)
		numeroConf = 0;
		// on crer une configuration pour l'IA
		long start = System.currentTimeMillis();
		int[] tabIA = getAgoodConfFor(tab);
		SearchTime = System.currentTimeMillis() - start;
		/*
		 * System.out.println("Temps de recherche du coup : " + duree + " ms");
		 * Configuration cIA = getConf(tabIA); System.out.println("Configuration
		 * IA : " + cIA + " EVal " + minmax(cJoueur, Joueur.joueurMax));
		 */
		// on refait le decalage ici
		int[] tabDecal = decalTab(tabIA, decalage);
		drawTab(tabDecal);
	}

	/**
	 * Permet de décaler un tableau d'etoile.
	 * Cela remet les etoiles au bon endroit pour les dessiner.
	 * @param tab Tableau de proprietaires
	 * @param decalage Indice de décalage
	 * @return tableau de proprietaires décaléss
	 */
	private int[] decalTab(int[] tab, int decalage) {
		String str = "";
		int[] newTab = new int[tab.length];
		for (int i = 0; i < tab.length; i++) {
			str += tab[i];
		}
		str = "";

		// on ajoute les données apres la position de decalage
		for (int i = tab.length - decalage; i < tab.length; i++) {
			str += tab[i];
		}
		// on ajoute les données avant la position de decalage
		for (int i = 0; i < tab.length - decalage; i++) {
			str += tab[i];
		}
		// on recrée notre tableau
		for (int i = 0; i < tab.length; i++) {
			switch (str.charAt(i)) {
			case '1':
				newTab[i] = 1;
				break;
			case '2':
				newTab[i] = 2;
				break;
			default:
				newTab[i] = 0;
				break;
			}
		}
		return newTab;
	}

	/**
	 * Prépare et dessine le tableau d'étoiles.
	 * @param tab Tableau d'étoiles à dessiner
	 */
	private void drawTab(int[] tab) {
		boolean finPartie = true;
		for (int i = 0; i < getNbStars(); i++) {
			if (tab[i] != Joueur.personne) {
				Star s = (Star) getStars().get(i);
				s.setDefinitif(true);
				s.setProprio(tab[i]);
				getStars().set(i, s);
			} else {
				Star s = (Star) getStars().get(i);
				s.setDefinitif(false);
				s.setProprio(tab[i]);
				getStars().set(i, s);
				finPartie = false; // ce n'est pas une configuration finale
			}
		}
		if (finPartie == true) {
			showMessage("L'ordinateur à gagné !");
			newGame();
		}
		mgs.repaintStarScreen();
	}

	/**
	 * Lance une nouvelle partie.
	 */
	private void newGame() {
		setChanged();
		notifyObservers("NEW");
	}

	/**
	 * Retourne une configuration optimale pour l'IA.
	 * @param tab configuration de jeu actuelle
	 * @return configuration de jeu après coup(s) joué(s) par l'IA
	 */
	private int[] getAgoodConfFor(int[] tab) {
		int val;
		Configuration c;
		tab = getConfTab(tab);
		// IA sur un coup
		for (int i = 0; i < tab.length; i++) {
			if (tab[i] == Joueur.personne) {
				tab[i] = 2; // joue un coup ici
				c = getConf(tab);
				val = (modeIA == MOD_1) ? NIM(c, Joueur.hjoueur): null;
				if (val == -1) {
					return tab;
				} else {
					tab[i] = 0;
				}
			}
		}

		tab = getConfTab(tab);
		// IA sur deux coups
		for (int i = 0; i < tab.length - 1; i++) {
			if (tab[i] == Joueur.personne && tab[i + 1] == Joueur.personne) {
				tab[i] = 2; // joue un coup ici
				tab[i + 1] = 2; // on joue un 2eme coup ici
				c = getConf(tab);
				val = (modeIA == MOD_1) ? NIM(c, Joueur.hjoueur):null;
				if (val == -1) {
					return tab;
				} else {
					tab[i + 1] = 0;
					tab[i] = 0;
				}
			}
		}
		showMessage("Aucune solution trouvée pour l'IA");
		return tab;
	}

	/**
	 * Crée l'arbre de jeu.
	 *
	 */
	public void createTree() {
		numeroConf = 2;
		Configuration c = new Configuration();
		addValue(c.getConfig(), nbEtoiles - 1, 1);
		addValue(c.getConfig(), 0, 1);
		createTree(c, 1);
		Configuration cBis = new Configuration();
		addValue(cBis.getConfig(), nbEtoiles - 2, 1);
		addValue(cBis.getConfig(), 0, 2);
		createTree(cBis, 1);
		showMessage("Nombre de configurations totales non symétriques : "
				+ numeroConf);
	}

	/**
	 * Appelée par createTree, cela crée récursivement l'arbre de jeu.
	 * @param conf Configuration de jeu à un instant T.
	 * @param level Niveau de l'arbre.
	 */
	public void createTree(Configuration conf, int level) {
		HashMap hm = conf.getConfig();
		HashMap hmChild, hmChildTwo;
		Configuration confChild, confChildTwo;
		Iterator it = hm.keySet().iterator();
		if (conf.isFinale()) {
			return;
		}
		while (it.hasNext()) {
			int n = ((Integer) it.next()).intValue();
			switch (n) {
			case 0:
				break;
			case 1:
				hmChild = new Configuration(hm).getConfig();
				removeValue(hmChild, 1, 1);
				addValue(hmChild, 0, 1);
				numeroConf++;
				confChild = new Configuration(hmChild);
				// System.out.println(confChild.toString() + " N°" + numeroConf
				// + " Lvl°" + level);
				createTree(confChild, level + 1);
				break;
			default:
				// cas ou le joueur joue une seule etoile
				for (int i = 0; i < (int) Math.ceil(n / 2.0); i++) {
					hmChild = new Configuration(hm).getConfig();
					removeValue(hmChild, n, 1);
					int c = n - (i + 1);

					if (i == 0) {
						addValue(hmChild, c, 1);
					} else {
						if (i == (int) Math.ceil(n / 2.0)) {
							addValue(hmChild, i, 2);
						} else {
							addValue(hmChild, c, 1);
							addValue(hmChild, i, 1);
						}
					}
					addValue(hmChild, 0, 1);
					numeroConf++;
					confChild = new Configuration(hmChild);
					// System.out.println(confChild.toString() + " N°"
					// + numeroConf + " Lvl°" + level);
					createTree(confChild, level + 1);
				}
				// cas ou le joueur joue deux etoiles successives
				for (int i = 0; i < (int) Math.floor(n / 2.0); i++) {
					hmChildTwo = new Configuration(hm).getConfig();
					removeValue(hmChildTwo, n, 1); // on consomme une chaine
					int c = n - (i + 2);

					if (i == 0) {
						addValue(hmChildTwo, c, 1); // on ajoute une chaine c
					} else {
						if (i == (int) Math.floor(n / 2.0)) {
							addValue(hmChildTwo, i, 2);
						} else {
							addValue(hmChildTwo, c, 1);
							addValue(hmChildTwo, i, 1);
						}
					}
					if (n == 2)
						addValue(hmChildTwo, 0, 1);
					else
						addValue(hmChildTwo, 0, 2);
					numeroConf++;
					confChildTwo = new Configuration(hmChildTwo);
					// System.out.println(confChildTwo.toString() + " N°"
					// + numeroConf + " Lvl°" + level+" DOUBLE");
					createTree(confChildTwo, level + 1);
				}
			}
		}
	}

	/**
	 * Algorithme minmax d'IA.
	 * @param conf Configuration a un instant T
	 * @param minmax Evaluation a un instant T
	 * @return Evaluation de la configuration
	 */
	public int NIM(Configuration conf, int mod1) {
		HashMap hm = conf.getConfig();
		HashMap hmChild, hmChildTwo;
		Configuration confChild, confChildTwo;
		Iterator it = hm.keySet().iterator();
		int vret1, vret2, vret3, vMax;
		vMax = mod1 == Joueur.hjoueur ? -1 : 1;
		int minmaxSuivant = mod1 == Joueur.hjoueur ? Joueur.AIjoueur
				: Joueur.hjoueur;
		if (conf.isFinale()) {
			if (mod1 == Joueur.hjoueur) {
				return -1;
			} else {
				return 1;
			}
		}
		while (it.hasNext()) {
			int n = ((Integer) it.next()).intValue();
			switch (n) {
			case 0:
				break;
			case 1:
				hmChild = new Configuration(hm).getConfig();
				removeValue(hmChild, 1, 1);
				addValue(hmChild, 0, 1);
				numeroConf++;
				confChild = new Configuration(hmChild);
				// System.out.println(confChild.toString() + " N°" +
				// numeroConf);
				vret1 = NIM(confChild, minmaxSuivant);
				// System.out.println("Eval1 : "+vret1);
				if (mod1 == Joueur.hjoueur) {
					vMax = Math.max(vMax, vret1);
				} else {
					vMax = Math.min(vMax, vret1);
				}
				break;
			default:
				// cas ou le joueur joue une seule etoile
				for (int i = 0; i < (int) Math.ceil(n / 2.0); i++) {
					hmChild = new Configuration(hm).getConfig();
					removeValue(hmChild, n, 1);
					int c = n - (i + 1);

					if (i == 0) {
						addValue(hmChild, c, 1);
					} else {
						if (i == (int) Math.ceil(n / 2.0)) {
							addValue(hmChild, i, 2);
						} else {
							addValue(hmChild, c, 1);
							addValue(hmChild, i, 1);
						}
					}
					addValue(hmChild, 0, 1);
					numeroConf++;
					confChild = new Configuration(hmChild);
					// System.out.println(confChild.toString() + " N°"+
					// numeroConf);
					vret2 = NIM(confChild, minmaxSuivant);
					// System.out.println("Eval1bis : "+vret2);
					if (mod1 == Joueur.hjoueur) {
						vMax = Math.max(vMax, vret2);
					} else {
						vMax = Math.min(vMax, vret2);
					}
				}
				// cas ou le joueur joue deux etoiles successives
				for (int i = 0; i < (int) Math.floor(n / 2.0); i++) {
					hmChildTwo = new Configuration(hm).getConfig();
					removeValue(hmChildTwo, n, 1); // on consomme une chaine
					int c = n - (i + 2);

					if (i == 0) {
						addValue(hmChildTwo, c, 1); // on ajoute une chaine c
					} else {
						if (i == (int) Math.floor(n / 2.0)) {
							addValue(hmChildTwo, i, 2);
						} else {
							addValue(hmChildTwo, c, 1);
							addValue(hmChildTwo, i, 1);
						}
					}
					if (n == 2)
						addValue(hmChildTwo, 0, 1);
					else
						addValue(hmChildTwo, 0, 2);
					numeroConf++;
					confChildTwo = new Configuration(hmChildTwo);
					// System.out.println(confChildTwo.toString() + " N°" +
					// numeroConf +" DOUBLE");
					vret3 = NIM(confChildTwo, minmaxSuivant);
					// System.out.println("Eval2 : "+vret3);
					if (mod1 == Joueur.hjoueur) {
						vMax = Math.max(vMax, vret3);
					} else {
						vMax = Math.min(vMax, vret3);
					}
				}
			}
		}
		// System.out.println("Configuration : "+conf+" Eval: "+vMax);
		return vMax;
	}

	/**
	 * Retourne une configuration d'un tableau d'etoile.
	 * @param tab Tableau d'etoile
	 * @return Configuration correspondante
	 */
	private Configuration getConf(int[] tab) {
		String str = "";
		for (int i = 0; i < tab.length; i++) {
			str += tab[i];
		}
		int pos = str.indexOf('1');
		String strFinale = str.substring(pos);
		for (int j = 0; j < pos; j++) {
			strFinale += tab[j];
		}
		// System.out.println("configuration : " + strFinale);
		return new Configuration(getHashMapConfig(strFinale));
	}

	/**
	 * Récupere l'indice de décalage d'une configuration.
	 * @param tab Tableau d'étoiles
	 * @return indice de décalage
	 */
	private int getDecalage(int[] tab) {
		String str = "";
		for (int i = 0; i < tab.length; i++) {
			str += tab[i];
		}
		return str.indexOf('1');
	}

	/**
	 * Retourne une configuration dans un ordre pratique pour la recherche.
	 * @param tab Configuration de jeu à optimiser
	 * @return Configuration de jeu optimisée
	 */
	private int[] getConfTab(int[] tab) {
		String str = "";
		for (int i = 0; i < tab.length; i++) {
			str += tab[i];
		}
		int pos = str.indexOf('1');
		/*
		 * int pos2 = str.indexOf('2'); if(pos2!=-1) { pos = Math.min(pos,
		 * pos2); }
		 */
		String strFinale = str.substring(pos);
		for (int j = 0; j < pos; j++) {
			strFinale += tab[j];
		}
		for (int i = 0; i < tab.length; i++) {
			switch (strFinale.charAt(i)) {
			case '1':
				tab[i] = 1;
				break;
			case '2':
				tab[i] = 2;
				break;
			default:
				tab[i] = 0;
				break;
			}
		}
		return tab;
	}

	/*
	 * Retourne une Hashmap à partir d'une chaine de caractéres.
	 */
	private HashMap getHashMapConfig(String tab) {
		int size = 0;
		HashMap hm = new HashMap();
		for (int i = 0; i < tab.length(); i++) {
			if (tab.charAt(i) == '1' || tab.charAt(i) == '2') {
				if (size > 0) {
					addValue(hm, size, 1);
					size = 0;
				}
				addValue(hm, 0, 1);
			} else {
				size++;
			}
		}
		if (size > 0) {
			addValue(hm, size, 1);
		}
		return hm;
	}

	/**
	 * Enleve une valeur d'une HashMap.
	 * @param hm Hashmap
	 * @param cle Cle de la valeur
	 * @param valeur valeur en elle-meme
	 */
	private void removeValue(HashMap hm, int cle, int valeur) {
		int val = ((Integer) hm.get(new Integer(cle))).intValue();

		if (val - valeur <= 0) {
			hm.remove(new Integer(cle));
		} else {
			hm.put(new Integer(cle), new Integer(val - valeur));
		}
	}

	/**
	 * Ajoute une valeur d'une HashMap.
	 * @param hm Hashmap
	 * @param cle Cle de la valeur
	 * @param valeur valeur en elle-meme
	 */
	private void addValue(HashMap hm, int cle, int valeur) {
		int val = 0;
		if (hm.containsKey(new Integer(cle))) {
			val = ((Integer) hm.get(new Integer(cle))).intValue();
		}
		hm.put(new Integer(cle), new Integer(val + valeur));
	}

	/**
	 * Affiche un message à l'écran.
	 * @param s Message
	 */
	private void showMessage(String s) {
		JOptionPane.showMessageDialog(null, s, "Partie",
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Retourne une arrayList d'étoile.
	 * @return Liste d'etoiles
	 */
	public ArrayList getStars() {
		return tabBoule;
	}

	/**
	 * Retourne le nombre d'étoiles.
	 * @return Nombre d'étoiles
	 */
	public int getNbStars() {
		return nbEtoiles;
	}

	/**
	 * Retourne le temps de recherche en ms pour l'ordinateur.
	 * @return Temps en millisecondes
	 */
	public String getSearchTime() {
		return new Long(SearchTime).toString();
	}

	/**
	 * Retourne le type d'IA.
	 * @return le type d'algorithme utilisé pour l'IA
	 */
	public int getModeIA() {
		return modeIA;
	}
	
	/**
	 * Retourne le nombre de noeuds parcourus.
	 * @return Nombre de noeuds
	 */
	public int getNbNoeuds() {
		return numeroConf;
	}

}

