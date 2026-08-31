import java.util.Scanner;

public class McdMcm {
    static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Ingrese el primer número: ");
        int numero1 = scanner.nextInt();

        System.out.println("Ingrese el segundo número: ");
        int numero2 = scanner.nextInt();

        int mcd = 1;

        for(int i = 1; i<=numero1 && i <= numero2; i++){
            if (numero1 % i == 0 && numero2 % i == 0) {
                mcd = i;
            }
        }

        int mcm = (numero1 * numero2) / mcd;

        System.out.println("MCD de " + numero1 + " y " + numero2 + " = " + mcd);
        System.out.println("MCM de " + numero1 + " y " + numero2 + " = " + mcm);

        scanner.close();
    }
}
