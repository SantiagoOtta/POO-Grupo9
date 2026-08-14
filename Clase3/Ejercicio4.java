package Clase3;
import java.util.Scanner;

public class Ejercicio4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int contador = 0;
        int mayores = 0;
        double nota;

        do {
            System.out.print("Ingrese nota " + (contador + 1) + ": ");
            nota = scanner.nextDouble();
            if (nota >= 3.0) {
                mayores++;
            }
            contador++;
        } while (contador < 5);

        System.out.println("Notas mayores o iguales a 3.0: " + mayores);
    }
}
