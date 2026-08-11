package Ejercicio1;
import java.util.Scanner;

public class Ejercicio1 {
    public static void resolverCuadratica(double a, double b, double c) {
        if (a == 0) {
            System.out.println("No es una ecuación cuadrática (a debe ser distinto de 0).");
            return;
        }

        double discriminante = b * b - 4 * a * c;

        if (discriminante > 0) {
            double x1 = (-b + Math.sqrt(discriminante)) / (2 * a);
            double x2 = (-b - Math.sqrt(discriminante)) / (2 * a);
            System.out.println("Discriminante positivo: dos raíces reales distintas");
            System.out.println("x1 = " + x1);
            System.out.println("x2 = " + x2);

        } else if (discriminante == 0) {
            double x = -b / (2 * a);
            System.out.println("Discriminante igual a 0: raíz real doble");
            System.out.println("x = " + x);

        } else {
            double parteReal = -b / (2 * a);
            double parteImaginaria = Math.sqrt(-discriminante) / (2 * a);
            System.out.println("Discriminante negativo: dos raíces complejas conjugadas");
            System.out.println("x1 = " + parteReal + " + " + parteImaginaria + "i");
            System.out.println("x2 = " + parteReal + " - " + parteImaginaria + "i");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese el coeficiente a: ");
        double a = sc.nextDouble();

        System.out.print("Ingrese el coeficiente b: ");
        double b = sc.nextDouble();

        System.out.print("Ingrese el coeficiente c: ");
        double c = sc.nextDouble();

        resolverCuadratica(a, b, c);

        sc.close();
    }
}
