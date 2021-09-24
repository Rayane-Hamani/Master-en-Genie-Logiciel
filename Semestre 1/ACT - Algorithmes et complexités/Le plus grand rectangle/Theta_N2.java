import java.util.ArrayList;
import java.util.Scanner;

public class Theta_N2 {
    public static class Point {
        private final int x;
        private final int y;
    
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    
        public int getX() {
            return x;
        }
    
        public int getY() {
            return y;
        }
    }

    /**
     * Calcule en O(n^2) l'aire du plus grand rectangle parmi les points donnés
     * 
     * @param maxHauteur la hauteur de l'axe des ordonnées
     * @param points la liste des points
     * @param nombreDePoints le nombre de points dans la liste
     * @return l'aire du plus grand rectangle vide et ayant sa base sur l'axe des abscisses
     */
    public static int calculDuPlusGrandRectangle(int maxHauteur, ArrayList<Point> points, int nombreDePoints) {
        int maxAire = 0;

        /* pour chaque bordure gauche */
        for(int i = 0; i < nombreDePoints-1; i++) {
            /* on initialise la hauteur minimale à la hauteur de l'axe des ordonnées */
            int minHauteur = maxHauteur;

            /* pour chaque bordure droite de la bordure gauche */
            for(int j = i+1; j < nombreDePoints; j++) {
                /* si l'arête droite est plus haute que la hauteur minimale */
                if(minHauteur < points.get(j).getY()) {
                    continue; /* on ignore le point et on passe au suivant */
                }
                /* sinon si l'arête droite est plus basse que la hauteur minimale */
                else {
                    /* on calcule l'aire du rectangle entre les points d'index i et j et de hauteur minimale */
                    int newAire = (points.get(j).getX() - points.get(i).getX()) * minHauteur;

                    /* on met à jour l'aire maximale */
                    maxAire = Math.max(maxAire, newAire);

                    /* on met à jour la nouvelle hauteur minimale pour la prochaine bordure droite */
                    minHauteur = Math.min(minHauteur, points.get(j).getY());
                }
            }
        }

        return maxAire;
    }

    public static void main(String[] args) {
        /* on lit les inputs pour le programme */
        Scanner sc = new Scanner(System.in);

        final int maxLongueur = sc.nextInt();
        final int maxHauteur = sc.nextInt();

        int nombreDePoints = sc.nextInt() + 2;
        ArrayList<Point> points = new ArrayList<Point>();

        /* on liste les points à prendre en compte */
        points.add(new Point(0, 0));
        for(int i = 0; i < nombreDePoints-2; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            points.add(new Point(x, y));
        }
        points.add(new Point(maxLongueur, 0));

        /* on n'oublie pas de fermer le scanner ! */
        sc.close();
        
        /* on affiche l'aire du plus grand rectangle après l'avoir calculé ! */
        System.out.println(calculDuPlusGrandRectangle(maxHauteur, points, nombreDePoints));
    }
}
