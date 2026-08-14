package Clase2;
import java.util.Scanner;

public class Ejercicio3 {
    public static boolean esBisiesto(int anio) {
        return (anio % 4 == 0 && (anio % 100 != 0 || anio % 400 == 0));
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese un año: ");
        int anio = sc.nextInt();

        if (esBisiesto(anio)) {
            System.out.println("El año " + anio + " es bisiesto.");
        } else {
            System.out.println("El año " + anio + " no es bisiesto.");
        }

        sc.close();
    }
}
