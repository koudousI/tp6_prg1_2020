package fr.istic.prg1.tree;

import java.util.Scanner;

import fr.istic.prg1.tree_util.AbstractImage;
import fr.istic.prg1.tree_util.Iterator;
import fr.istic.prg1.tree_util.Node;
import fr.istic.prg1.tree_util.NodeType;

/**
 * @author Mickaël Foursov <foursov@univ-rennes1.fr>
 * @author koudous ibouraima
 * @author kadidiatou Sanogo
 * @version 5.0
 * @since 2016-04-20
 * 
 *        Classe décrivant les images en noir et blanc de 256 sur 256 pixels
 *        sous forme d'arbres binaires.
 * 
 */

public class Image extends AbstractImage {
	private static final Scanner standardInput = new Scanner(System.in);

	public Image() {
		super();
	}

	public static void closeAll() {
		standardInput.close();
	}

	/**
	 * @param x
	 *            abscisse du point
	 * @param y
	 *            ordonnée du point
	 * @pre !this.isEmpty()
	 * @return true, si le point (x, y) est allumé dans this, false sinon
	 */
	@Override
	public boolean isPixelOn(int x, int y ) { //50
		
		Iterator<Node>it = this.iterator();
        boolean paire = true; // true = 0 / 2 / 4 .. false = 1 / 3 / 5 ...
        int minimumY1 = 0, minimumX=0;
        int maximumY = 256, maximumX = 256;
        int milieuY, milieuX;
        
        while(it.nodeType()==NodeType.DOUBLE){
            if(paire){
                milieuY = (minimumY1 + maximumY)/2; //y=128
                if(y < milieuY){
                    maximumY = milieuY;
                    it.goLeft();
                }
                else{
                    minimumY1 = milieuY;
                    it.goRight();
                }
                paire = false;
            }
            else{
                milieuX = (minimumX + maximumX)/2;
                if(x < milieuX){
                    maximumX = milieuX;
                    it.goLeft();
                }
                else{
                    minimumX = milieuX;
                    it.goRight();
                }
                paire = true;
            }
        }
    return it.getValue().state == 1;
	}

	/**
	 * On efface this. puis on fait un parcours infixe de image2 et de this,
	 * en copiant a chaque étape (noeud) la valeur du noeud de image2 dans this. 
	 * 
	 * this devient identique à image2.
	 *
	 * @param image2
	 *            image à copier
	 *
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void affect(AbstractImage image2) {
		Iterator<Node>it = this.iterator();
		Iterator<Node>it1 = image2.iterator();
		it.clear();
		affectAux(it,it1);
	}

	/*
	 * fonction auxiliare
	 * parcourt infixe
	 */
	private static void affectAux(Iterator<Node>it, Iterator<Node>it1) {
		
        if (it1.nodeType()!=NodeType.SENTINEL){
        	
            it.addValue(Node.valueOf(it1.getValue().state));
        	it1.goLeft();
        	it.goLeft();
        	affectAux(it, it1);
        	it1.goUp();
        	it.goUp();
        	
        	it1.goRight();
        	it.goRight();
        	affectAux(it, it1);
        	it1.goUp();
        	it.goUp();
        }

	}
	
	
	/**
	 * ce qui est en haut vient en bas et ce qui est en bas va en haut.
	 * ce qui est à gauche va à droite et ce qui est à droite va à gauche.
	 * 
	 * this devient rotation de image2 à 180 degrés.
	 *
	 * @param image2
	 *            image pour rotation
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void rotate180(AbstractImage image2) {
		Iterator<Node> it = this.iterator();
        Iterator<Node> it1 = image2.iterator();

        it.clear();
        rotate180Aux(it, it1);
	}
	
	/*
	 * fonction auxiliaire
	 * parcourt préfixe
	 */
	private static void rotate180Aux(Iterator<Node> it, Iterator<Node> it1) {
		
        if (!it1.isEmpty()) {
            
            it.addValue(Node.valueOf(it1.getValue().state));
            it.goLeft();
            it1.goRight();
            rotate180Aux(it, it1);
            it.goUp();
            it1.goUp();
            
            it.goRight();
            it1.goLeft();
            rotate180Aux(it, it1);
            it.goUp();
            it1.goUp();
        }
	}
	
	
	/**
	 * this devient rotation de image2 à 90 degrés dans le sens des aiguilles
	 * d'une montre.
	 *
	 * @param image2
	 *            image pour rotation
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void rotate90(AbstractImage image2) {
		System.out.println();
		System.out.println("-------------------------------------------------");
		System.out.println("Fonction non demeandee");
		System.out.println("-------------------------------------------------");
		System.out.println();	    
	}
	
	
	
	

	/**
	 * On fait un parcourt infixe de this jusqu'au feuilles (tant que state est égale à 2) et on fait une condition pour inverser 
	 * la valeur des states quand on arrive sur les feuilles.(on passe les states 1 à 0 et inversement).
	 * 
	 * this devient inverse vidéo de this, pixel par pixel.
	 *
	 * @pre !image.isEmpty()
	 */
	@Override
	public void videoInverse() {
		 Iterator<Node> it = this.iterator();
         VideoInverseAux(it);
	}
	
	/*
	 * fonction auxiliaire
	 * parcour infixe
	 */
	private static void VideoInverseAux(Iterator<Node> it) {
		
		if (it.getValue().state == 2)  // it.nodeType()==NodeType.DOUBLE
		{
            it.goLeft();
            VideoInverseAux(it);
            it.goUp();
            it.goRight();
            VideoInverseAux(it);
            it.goUp();
        } 
		else if (it.getValue().state == 1) {
            it.setValue(Node.valueOf(0));
        } 
		else {
            it.setValue(Node.valueOf(1));
        }
	}

	
	
	
	/**
	 * principe: 
	 * -On inverse uniquement les noeud après avoir fait une division verticale.
	 * Autrement dit, quand le niveau de it1 est pair, c'est en ce moment que lorsqu'on va à 
	 * gauche dans image2, il faut aller à droite dans this et inversement.
	 * - quand on se déplace il faut mettre à jour la valeur du niveau pour savoir à quelle 
	 * niveau on se trouve dans l'arbre afin de pouvoir determiner si on est sur un niveau pair ou impair
	 * this devient image miroir verticale de image2.
	 *
	 * @param image2
	 *            image à agrandir
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void mirrorV(AbstractImage image2) {
		Iterator<Node> it = this.iterator();
        Iterator<Node> it1 = image2.iterator();
        it.clear();
        int levesIsPair = 0;	//Pour savoir si c'est node set pair ou impair. Initialiser à pair
        mirrorAux(it, it1, levesIsPair);
	}

	private static void mirrorAux(Iterator<Node> it, Iterator<Node> it1, int levesIsPair) {
		if (it1.nodeType() != NodeType.SENTINEL) {
            it.addValue(it1.getValue());
            if (levesIsPair % 2 == 0) {
                it.goLeft();
                it1.goRight();
                levesIsPair++;
                mirrorAux(it, it1, levesIsPair);
                it.goUp();
                it1.goUp();
                it.goRight();
                it1.goLeft();
                mirrorAux(it, it1, levesIsPair);
                it.goUp();
                it1.goUp();
                levesIsPair--;
            } 
            else {
                it.goLeft();
                it1.goLeft();
                levesIsPair++;
                mirrorAux(it, it1, levesIsPair);
                it.goUp();
                it1.goUp();
                it.goRight();
                it1.goRight();
                mirrorAux(it, it1, levesIsPair);
                it.goUp();
                it1.goUp();
                levesIsPair--;
            }
		}
	}
	
	
	
	/**
	 * principe: 
	 * -On inverse uniquement les noeud après avoir fait une division Horizontale.
	 * Autrement dit, quand le niveau de it1 est impair, c'est en ce moment que lorsqu'on va à 
	 * gauche dans image2, il faut aller à droite dans this et inversement.
	 * - quand on se déplace il faut mettre à jour la valeur du niveau pour savoir à quelle 
	 * niveau on se trouve dans l'arbre afin de pouvoir determiner si on est sur un niveau pair ou impair
	 * this devient image miroir verticale de image2.
	 * this devient image miroir horizontale de image2.
	 *
	 * @param image2
	 *            image à agrandir
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void mirrorH(AbstractImage image2) {
		Iterator<Node> it = this.iterator();
        Iterator<Node> it1 = image2.iterator();
        it.clear();
        int levesIsPair = 1;	//Pour savoir si c'est node set pair ou impair. Initialiser à impair
        mirrorAux(it, it1, levesIsPair);
	}

	
	
	
	/**
	 * Principe: this devient quart supérieur gauche de image2. donc on divise image2 horizontalement,
	 * 			 puis on va à gauche (en haut) et ensuite on divise image2 verticalement puis on va à gauche
	 * 			 pour atteindre le quart gauce (division de l'image en 4), puis on fait une copie de image2 dans this (appel de affectAux).
	 * 			 Si it2.nodeType() != NodeType.DOUBLE , alors on copie le node de image2 dans this.
	 * 
	 * this devient quart supérieur gauche de image2.
	 *
	 * @param image2
	 *            image à agrandir
	 * 
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void zoomIn(AbstractImage image2) {
		
		Iterator<Node> it2 = image2.iterator();
		Iterator<Node> it = this.iterator();
		it.clear();
		if (it2.nodeType() == NodeType.DOUBLE) {
                    it2.goLeft();
                    if (it2.nodeType() == NodeType.DOUBLE) {
                        it2.goLeft();
                        affectAux(it, it2);
                    } else {
                        it.addValue(Node.valueOf(it2.getValue().state));
                    }
		} else {
                    it.addValue(Node.valueOf(it2.getValue().state));
		}
		
	}
	
	
	

	/**
	 * Le quart supérieur gauche de this devient image2, le reste de this
	 * devient éteint.
	 * 
	 * @param image2
	 *            image à réduire
	 * @pre !image2.isEmpty()
	 */
	@Override
	public void zoomOut(AbstractImage image2) {
		
		Iterator<Node> it = this.iterator();
		Iterator<Node> it2 = image2.iterator();
		int rang = 2;
		it.clear();
		it.addValue(Node.valueOf(2));
		it.goRight();
		it.addValue(Node.valueOf(0));
		it.goUp();
		it.goLeft();
		it.addValue(Node.valueOf(2));
		it.goRight();
		it.addValue(Node.valueOf(0));
		it.goUp();
		it.goLeft();
		auxZoomOut(it, it2, rang);
		if (it.getValue().state == 0) {
                    it.goRoot();
                    it.clear();
                    it.addValue(Node.valueOf(0));
		}
		
		
	}

	/*
	 * 
	 * @param it
	 * @param it2
	 * @param rang
	 */
	 private void auxZoomOut(Iterator<Node> it, Iterator<Node> it2, int rang) {
         if (it2.nodeType() == NodeType.DOUBLE) {
             if (rang < 16) {
                 it.addValue(it2.getValue());
                 it.goLeft();
                 it2.goLeft();
                 auxZoomOut(it, it2, rang + 1);
                 int gauche = it.getValue().state;
                 it.goUp();
                 it2.goUp();
                 it.goRight();
                 it2.goRight();
                 auxZoomOut(it, it2, rang + 1);
                 int droit = it.getValue().state;
                 it.goUp();
                 it2.goUp();

                 if (gauche == droit && gauche != 2) {
                     it.clear();
                     it.addValue(Node.valueOf(droit));
                 }
             } else {
                 int c = auxCompteur(it2);
                 if (c < 0) {
                     it.addValue(Node.valueOf(0));
                 } else {
                     it.addValue(Node.valueOf(1));
                 }
             }
         } else {
             it.addValue(Node.valueOf(it2.getValue().state));
         }
	}

	 
	 /*
	  * 
	  * @param it
	  * @return
	  */
     private int auxCompteur(Iterator<Node> it) {

     if (it.nodeType() == NodeType.DOUBLE) {
         it.goLeft();
         int g = auxCompteur(it);
         it.goUp();
         it.goRight();
         int d = auxCompteur(it);
         it.goUp();
         return g + d;
     }

     if (it.getValue().state == 1) {
             return 1;
     } else {
             return -1;
     }

	}
	
	
	
     
	/**
	 * Principe:-étant sur un meme niveau dans les deux arbres (images1 et image2),
	 * 			 quand un des arbres a tous ses pixels éteint( et donc 0), on ne parcours
	 * 			 plus le second arbre jusqua la fin (meme si  ce derrnier a un sous arbre bien long),
	 * 			 On met tous les pixels de this à éteint (0) à partir du meme niveau que celui des 2 arbres 
	 * 
	 *			-étant sur un meme niveau dans les deux arbres (images1 et image2),
	 * 			 quand un des arbres a tous ses pixels allumé ( et donc 1), on parcours
	 * 			 le second arbre jusqua la fin (meme si  ce dernier a un sous arbre bien long),
	 * 			 jusqu'a atteindre niveau plus petit ou le sous arbre à des pixels éteint.
	 * 			 Puis on copie uniquement les pixels allumé de ce dernier dans this.
	 * 
	 * 			 - Cas ou on est sur un noeud double et que les fils on meme valeur( c-a-d tous allumé
	 * 				ou tous eteint), il faud alors remplacé le noeud double en un seul noeud et lui affecte
	 * 				la seule valeur qu'il a en vrai. (union des pixels ayant le meme etat). 
	 * 
	 * 
	 * this devient l'intersection de image1 et image2 au sens des pixels
	 * allumés.
	 * 
	 * @pre !image1.isEmpty() && !image2.isEmpty()
	 * 
	 * @param image1 premiere image
	 * @param image2 seconde image
	 */
	@Override
	public void intersection(AbstractImage image1, AbstractImage image2) {

		Iterator<Node> it1 = this.iterator();
        Iterator<Node> it2 = image1.iterator();
        Iterator<Node> it3 = image2.iterator();
        it1.clear();
        intersectionAux(it1, it2, it3);
	}
	
	
	private void intersectionAux(Iterator<Node> it1, Iterator<Node> it2, Iterator<Node> it3) {

        if (it2.getValue().state == 2 && it3.getValue().state == 2) {
	            it1.addValue(Node.valueOf(2));
	            it1.goLeft(); 
	            it2.goLeft();
	            it3.goLeft();
	            intersectionAux(it1, it2, it3);
	            int neudGauche = it1.getValue().state;
	            it1.goUp();
	            it2.goUp();
	            it3.goUp();
	            it1.goRight();
	            it2.goRight(); 
	            it3.goRight();
	            intersectionAux(it1, it2, it3);
	            int neudDroit = it1.getValue().state;
	            it1.goUp();
	            it2.goUp(); 
	            it3.goUp(); 	
	            if (neudGauche == neudDroit && neudGauche != 2) {	
	           	 	it1.clear();
	                it1.addValue(Node.valueOf(neudGauche));
	            }

        } else if (it2.getValue().state == 0 || it3.getValue().state == 0)
            it1.addValue(Node.valueOf(0));
        else if (it2.getValue().state == 1 && it3.getValue().state == 1)
            it1.addValue(Node.valueOf(1));
        else if (it2.getValue().state == 1 && it3.getValue().state == 2)
            affectAux(it1, it3);
        else if (it2.getValue().state == 2 && it3.getValue().state == 1)
            affectAux(it1, it2);
	}
	

	/**
	 * Principe:-étant sur un meme niveau dans les deux arbres (images1 et image2),
	 * 			 quand un des arbres a tous ses pixels allumé( et donc 1), on ne parcours
	 * 			 plus le second arbre jusqua la fin (meme si  ce derrnier a un sous arbre bien long),
	 * 			 On met tous les pixels de this à allumé (1) à partir du meme niveau que celui des 2 arbres 
	 * 
	 *			-étant sur un meme niveau dans les deux arbres (images1 et image2),
	 * 			 quand un des arbres a tous ses pixels éteint ( et donc 0), on parcours
	 * 			 le second arbre jusqua la fin (meme si  ce dernier a un sous arbre bien long),
	 * 			 jusqu'a atteindre niveau plus petit ou le sous arbre à des pixels allumés.
	 * 			 Puis on copie uniquement les pixels allumé de ce dernier dans this.
	 * 
	 * 			 - Cas ou on est sur un noeud double et que les fils on meme valeur( c-a-d tous allumé
	 * 				ou tous etein), il faut alors remplacé le noeud double en un seul noeud et lui affecte
	 * 				la seule valeur qu'il a en vrai. (union des pixels ayant le meme etat). 
	 * 
	 * this devient l'union de image1 et image2 au sens des pixels allumés.
	 * 
	 * @pre !image1.isEmpty() && !image2.isEmpty()
	 * 
	 * @param image1 premiere image
	 * @param image2 seconde image
	 */
	@Override
	public void union(AbstractImage image1, AbstractImage image2) {
		Iterator<Node> it1 = this.iterator();
        Iterator<Node> it2 = image1.iterator();
        Iterator<Node> it3 = image2.iterator();
        it1.clear(); 
        unionAux(it1, it2, it3);
	}
	
	private void unionAux(Iterator<Node> it1, Iterator<Node> it2, Iterator<Node> it3) {

        if (it2.getValue().state == 2 && it3.getValue().state == 2) {
	            it1.addValue(Node.valueOf(2)); // créer un node de val 2
	            it1.goLeft();
	            it2.goLeft();
	            it3.goLeft();
	            unionAux(it1, it2, it3);
	            int neudGauche = it1.getValue().state;
	            it1.goUp();
	            it2.goUp();
	            it3.goUp();
	            it1.goRight();
	            it2.goRight();
	            it3.goRight();
	            unionAux(it1, it2, it3);
	            int neudDroit = it1.getValue().state;
	            it2.goUp();
	            it3.goUp();
	            it1.goUp();
	            if (neudGauche == neudDroit && neudGauche != 2) {
	                it1.clear();
	                it1.addValue(Node.valueOf(neudGauche));
	            } 

        } else if (it2.getValue().state == 0 && it3.getValue().state == 0)
            it1.addValue(Node.valueOf(0));
        else if (it2.getValue().state == 1 || it3.getValue().state == 1)
            it1.addValue(Node.valueOf(1));
        else if (it2.getValue().state == 0 && it3.getValue().state == 2)
            affectAux(it1, it3);
        else if (it2.getValue().state == 2 && it3.getValue().state == 0)
            affectAux(it1, it2);
    }

	
	
	
	/**
	 * Attention : cette fonction ne doit pas utiliser la commande isPixelOn
	 * 
	 * @return true si tous les points de la forme (x, x) (avec 0 <= x <= 255)
	 *         sont allumés dans this, false sinon
	 */
	@Override
	public boolean testDiagonal() {
		Iterator<Node> it = this.iterator();
        return testDiagonalAux(it, 0);
	}
	
	private static boolean testDiagonalAux(Iterator<Node> it, int profondeur) {
		
		boolean resultat = true;
        if (it.getValue().state == 2) {
            it.goLeft();
            profondeur++;
            if (it.getValue().state == 2) {
                it.goLeft();
                profondeur++;
            }
        }
        if (it.getValue().state == 0) {
            return false;
        }
        if (it.getValue().state == 2) {
            resultat = testDiagonalAux(it, 0);
        }
        if (it.getValue().state == 1 && profondeur == 1) {
            resultat = true;
            it.goUp();
        }
        if (it.getValue().state == 1 && profondeur == 2) {
            resultat = true;
            it.goUp();
            it.goUp();
        }
        if (it.getValue().state == 2) {
            it.goRight();
            profondeur++;
            if (it.getValue().state == 2) {
                it.goRight();
                profondeur++;
            }
        }
        if (it.getValue().state == 0) {
            return false;
        }
        if (it.getValue().state == 2) {
            resultat = testDiagonalAux(it, 0);
        }
        if (it.getValue().state == 1 && profondeur == 1) {
            resultat = true;
            it.goUp();
        }
        if (it.getValue().state == 1 && profondeur == 2) {
            resultat = true;
            it.goUp();
            it.goUp();
        }
        return resultat;
    }
	
	
	

	/**
	 * principe: meme logique que isPixelOn sauf qu'on traite deux points
	 * 
	 * @param x1
	 *            abscisse du premier point
	 * @param y1
	 *            ordonnée du premier point
	 * @param x2
	 *            abscisse du deuxième point
	 * @param y2
	 *            ordonnée du deuxième point
	 * @pre !this.isEmpty()
	 * @return true si les deux points (x1, y1) et (x2, y2) sont représentés par
	 *         la même feuille de this, false sinon
	 */
	@Override
	public boolean sameLeaf(int x1, int y1, int x2, int y2) {
		
		Iterator<Node>it = this.iterator();
        boolean paire = true; // true = 0 / 2 / 4 .. false = 1 / 3 / 5 ...
        int minimumY1 =0, minimumX1=0;
        int maximumY1 = 256, maximumX1 = 256;
        int milieuY1 = (minimumY1 + maximumY1)/2;
        int milieuX1 = (minimumX1 + maximumX1)/2;
        //
        int minimumY2 =0, minimumX2=0;
        int maximumY2 = 256, maximumX2 = 256;
        int milieuY2 = (minimumY2 + maximumY2)/2;
        int milieuX2 = (minimumX2 + maximumX2)/2;
        
        
        while(it.nodeType()==NodeType.DOUBLE){
            if(paire){
                if(y1 < milieuY1 && y2 < milieuY2){
                    maximumY1 = milieuY1;
                    maximumY2 = milieuY2;
                    milieuY1 = (minimumY1 + milieuY1)/2;
                    milieuY2 = (minimumY2 + milieuY2)/2;
                    
                    it.goLeft();
                }
                else if(y1 >= milieuY1 && y2 >= milieuY2){
                    minimumY1 = milieuY1;
                    minimumY2 = milieuY2;
                    milieuY1 = (maximumY1 + milieuY1)/2;
                    milieuY2 = (maximumY2 + milieuY2)/2;
                    
                    it.goRight();
                }
                
                else{
                	return false;
                }
                
                paire = false;

            }
            else{
                if(x1<milieuX1 && x2<milieuX2){
                    maximumX1= milieuX1;
                    maximumX2= milieuX2;
                    milieuX1 = (minimumX1 + milieuX1)/2;
                    milieuX2 = (minimumX2 + milieuX2)/2;
                    it.goLeft();
                }
                else if (x1>=milieuX1 && x2 >= milieuX2){
                    minimumX1 = milieuX1;
                    minimumX2 = milieuX2;
                    milieuX1 = (maximumX1 + milieuX1)/2;
                    milieuX2 = (maximumX2 + milieuX2)/2;
                    it.goRight();
                }
                else {
                	return false;
                }
                
                paire = true;

            }
        }
        return true;
	}

	

	/**
	 * @param image2
	 *            autre image
	 * @pre !this.isEmpty() && !image2.isEmpty()
	 * @return true si this est incluse dans image2 au sens des pixels allumés
	 *         false sinon
	 */
	@Override
	public boolean isIncludedIn(AbstractImage image2) {
		
		return isIncludedIn(this.iterator(), image2.iterator());
		
	}
	
	/*
	 * 
	 * @param it1
	 * @param it2
	 * @return
	 */
	private boolean isIncludedIn(Iterator<Node> it1, Iterator<Node> it2) {
         if (it1.isEmpty())
             return true;
         if (it2.isEmpty())
             return false;
         if (it1.getValue().state == 0 || it2.getValue().state == 1)
             return true;
         boolean included = false;
         if (it1.getValue().state == it2.getValue().state) {
             if (it1.getValue().state == 1) {
                 included = true;
             } else {
                 it1.goLeft();
                 it2.goLeft();
                 included = isIncludedIn(it1, it2);
                 it1.goUp();
                 it2.goUp();
                 if (included) {
                     it1.goRight();
                     it2.goRight();
                     included &= isIncludedIn(it1, it2);
                     it1.goUp();
                     it2.goUp();
                 }
             }
         } else {
             it2.goLeft();
             included = isIncludedIn(it1, it2);
             it2.goUp();
             if (!included) {
                     it2.goRight();
                 included = isIncludedIn(it1, it2);
                 it2.goUp();
             }
         }
         return included;
	}
	
	

}
