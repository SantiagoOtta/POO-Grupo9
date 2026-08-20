import java.util.Scanner;

public class Ejercicio2{
    public static void CalcularFibonacci(int x){
        int fibonacciA=0;
        int fibonacciB=1;
        int i  = 1;
        while(i < x){
            System.out.println(fibonacciA);
            int suma=fibonacciA+fibonacciB;
            fibonacciA=fibonacciB;
            fibonacciB=suma;
            i++;
        }
    }
    public static void main(String[] args) {
        Scanner ingreso = new Scanner(System.in);
        System.out.print("Ingresa la cantidad  de terminos a mostrar: ");
        int x=ingreso.nextInt();

        if (x>0){
            CalcularFibonacci(x);
        }else {
            System.out.println("El numero tiene que ser entero y positivo");
        }
        ingreso.close();
    }
}