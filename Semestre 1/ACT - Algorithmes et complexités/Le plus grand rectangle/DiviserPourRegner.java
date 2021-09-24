import java.util.ArrayList;
import java.util.Scanner;

public class DiviserPourRegner {
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
     * Calcule en O(n.log(n)) l'aire du plus grand rectangle parmi les points donnés 
     * 
     * @param maxHauteur la hauteur de l'axe des ordonnées
     * @param points la liste des points
     * @param nombreDePoints le nombre de points dans la liste
     * @return l'aire du plus grand rectangle vide et ayant sa base sur l'axe des abscisses
     */
    public static int calculDuPlusGrandRectangle(int maxHauteur, ArrayList<Point> points, int nombreDePoints) {

             /* s'il n'y a pas assez de points pour former un rectangle */
             if(nombreDePoints  < 2) return 0; /* retourne une valeur qui sera ignorée */
        /* s'il y a tout juste assez de points pour former un rectangle */
        else if(nombreDePoints == 2) return (points.get(1).getX() - points.get(0).getX()) * maxHauteur; /* retourne l'aire du seul rectangle construisable */
        /* sinon s'il y a plus deux points */
        else {
            /* on cherche l'indice de la hauteur minimale parmi les points autres que le premier et le dernier */
            int minHauteurIndex = 1;
            for(int i = 2; i < nombreDePoints-1; i++) {
                if(points.get(i).getY() < points.get(minHauteurIndex).getY()) minHauteurIndex = i;
            }

            /* on calcule l'aire du rectangle aux arêtes gauche et droite les plus éloignés */
            int maxAire = (points.get(nombreDePoints-1).getX() - points.get(0).getX()) * points.get(minHauteurIndex).getY();

            /* on divise la liste des points en deux, le point à la hauteur minimale étant le point d'intersection */
            ArrayList<Point> pointsAGauche = new ArrayList<Point>(points.subList(0, minHauteurIndex+1));
            ArrayList<Point> pointsADroite = new ArrayList<Point>(points.subList(minHauteurIndex, nombreDePoints));

            /* on met à jour l'aire maximale */
            maxAire = Math.max(maxAire, calculDuPlusGrandRectangle(maxHauteur, pointsAGauche, minHauteurIndex+1));
            maxAire = Math.max(maxAire, calculDuPlusGrandRectangle(maxHauteur, pointsADroite, nombreDePoints-minHauteurIndex));
            
            return maxAire;
        }
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
