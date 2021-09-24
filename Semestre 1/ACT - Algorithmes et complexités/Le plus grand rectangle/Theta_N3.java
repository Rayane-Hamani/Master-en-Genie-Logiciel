import java.util.ArrayList;
import java.util.Scanner;

public class Theta_N3 {
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

    public static void main(String[] args) {
        /* on lit les inputs pour le programme */
        Scanner sc = new Scanner(System.in);

        int lengthGrid = sc.nextInt();
        int heightGrid = sc.nextInt();
        int numberOfPoints = sc.nextInt();
        ArrayList<Point> listOfPoints = new ArrayList<Point>();

        /* on ajoute les points à prendre en compte */
        listOfPoints.add(new Point(0, 0));
        for(int i = 0; i < numberOfPoints; i++) {
            int x = sc.nextInt();
            int y = sc.nextInt();
            Point point = new Point(x, y);
            listOfPoints.add(point);
        }
        listOfPoints.add(new Point(lengthGrid, 0));
        numberOfPoints += 2; /* on met à jour le nombre de point dans la liste */
        
        /* Calcule en O(n^3), pour calculer la surface de rectangle la plus grande possible */
        int surfaceMax = 0;
         /* on itére sur tout les points du graphe qu'on nommera i */
        for(int i = 0; i < numberOfPoints-1; i++) {
  	    /* Pour chacun de ces points i on va calculer la surface maximale possible avec tout les points j ce trouvant après le point j. */	
            for(int j = i+1; j < numberOfPoints; j++) {
                 int hauteurMinimale = heightGrid;
		 /* on va itérer sur tout les points nommés k, entre les points i et j afin de trouver la hauteur minimale parmi ces points k
		car normalement certains points intermédaire peuvent ce trouver a l'intérieur des rectangles, interdisant normalement le calcules de surface de ces
		rectangles, mais a la place on va minimiser au maximum leur surface pour eviter de trouver une surface maximale érronée. */
		 if ((j - i) > 1) {
		    for(int k = i+1; k < j; k++){
                        if (listOfPoints.get(k).getY() < hauteurMinimale) {
			     hauteurMinimale = listOfPoints.get(k).getY();
                        }
		    }
		 }
		/*On regarde si la surface calculée est plus grande que la surface maximale actuelle trouvée*/
		 int surface = (listOfPoints.get(j).getX() - listOfPoints.get(i).getX()) * hauteurMinimale;
		 if ( surfaceMax < surface ) {
	              surfaceMax = surface;
		 }
            }     
        }

        /* on affiche la surface */
        System.out.println(surfaceMax);

        sc.close();
    }
}
