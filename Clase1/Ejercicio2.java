package Ejercicio2;
import java.util.Scanner;

public class Ejercicio2 {
    public static void mostrarMes(int numeroMes) {
        String[] nombres = {
                "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
                "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        };

        int[] dias = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

        if (numeroMes >= 1 && numeroMes <= 12) {
            System.out.println("El mes " + numeroMes + " es " + nombres[numeroMes - 1]
                    + " y tiene " + dias[numeroMes - 1] + " días.");
        } else {
            System.out.println("Número de mes inválido. Debe estar entre 1 y 12.");
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Ingrese el número de mes (1-12): ");
        int mes = sc.nextInt();

        mostrarMes(mes);

        sc.close();
    }
}
