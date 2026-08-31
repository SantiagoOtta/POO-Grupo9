import java.util.Scanner;

public class Factorial {
    static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese un número positivo: ");
        int numero = scanner.nextInt();
        int factorial = 1;

        for (int i = 1; i <= numero; i++) {
            factorial = factorial * i;
        }

        System.out.println("El factorial de " + numero + " es: " + factorial);

        scanner.close();
    }
}
