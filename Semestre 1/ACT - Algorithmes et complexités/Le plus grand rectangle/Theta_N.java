import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Theta_N {
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
     * Calcule en O(n) l'aire du plus grand rectangle parmi les points donnés
     * 
     * @param longueurAbscisse la longueur de l'axe des abscisses
     * @param hauteurOrdonnee la hauteur de l'axe des ordonnées
     * @param points la liste des points
     * @param nombreDePoints le nombre de points dans la liste
     * @return l'aire du plus grand rectangle vide et ayant sa base sur l'axe des abscisses
     */
    public static int calculDuPlusGrandRectangle(int maxHauteur, ArrayList<Point> points, int nombreDePoints) {
        int maxAire = 0;
        Stack<Integer> pile = new Stack<Integer>(); /* la pile des indices des points dans la liste */

        /* pour chaque point sauf (0,0) */
        for(int i = 1; i < nombreDePoints; i++) {
                /* si la pile est vide ou que l'ordonnée du point actuel est supérieure ou égale à celle du point en tête de pile */
                /* on calcule l'aire de hauteur de l'axe des ordonnées entre le point actuel et le point précédent */
                /* on met à jour l'aire maximale */
                /* on empile le point */
                if(pile.isEmpty() || points.get(i).getY() >= points.get(pile.peek()).getY()) {
                    int newAire = (points.get(i).getX() - points.get(i-1).getX()) * maxHauteur;
                    maxAire = Math.max(maxAire, newAire);
                    if(!pile.isEmpty() && points.get(pile.peek()).getY() == points.get(i).getY()) pile.pop(); /* si l'ordonnée de la tête de la pile = l'ordonnée du point actuel, on enlève la tête de pile */
                    pile.push(i);
                }
                
                /* sinon si la pile n'est pas vide et que l'ordonnée du point actuel est inférieure à celui de la tête de liste */
                else {
                    /* on dépile les points de la pile jusqu'à obtenir un point dont l'ordonnée est inférieure */
                    /* on calcule l'aire de hauteur du point dépilé entre le point actuel et soit la tête de pile si la pile n'est pas vide, soit 0 si la pile est vide */
                    /* on met à jour l'aire maximale */
                    while(!pile.isEmpty() && points.get(i).getY() <= points.get(pile.peek()).getY()) {
                        int iPointDepile = pile.pop();
                        int newAire = 0;
                        if(!pile.isEmpty()) newAire = points.get(iPointDepile).getY() * (points.get(i).getX() - points.get(pile.peek()).getX());
                        else                newAire = points.get(iPointDepile).getY() *  points.get(i).getX();
                        maxAire = Math.max(maxAire, newAire);
                    }
                    
                    /* on ajoute le point actuel en tête de pile */
                    pile.push(i);
                }
        }

        /* on dépile le dernier point de la liste */
        /* on calcule l'aire de hauteur de l'axe des ordonnées entre la tête de la pile et le point qui le précède dans la liste des points */
        /* on met à jour l'aire maximale */
        int iPointDepile = pile.pop();
        int newAire = (points.get(iPointDepile).getX() - points.get(iPointDepile-1).getX()) * maxHauteur;
        maxAire = Math.max(maxAire, newAire);

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
