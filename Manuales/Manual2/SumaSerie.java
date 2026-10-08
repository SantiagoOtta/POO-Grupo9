import java.util.Scanner;

public class SumaSerie {
    public static double CalcularSuma(int x){
        int i=1;
        double suma=0.0;
        while(i <= x){
            suma += 1.0 /i;
            i++;
        }
        return suma;
    }
    public static void main(String[] args) {
        Scanner ingreso = new Scanner(System.in);
        System.out.println("Ingrese un numero entero positivo:");
        int x = ingreso.nextInt();

        if (x>0){
            double resultado = CalcularSuma(x);
            System.out.println("El resultado es: "+resultado);
        }else {
            System.out.println("El numero ingresado no es un numero entero positivo");
        }
        ingreso.close();
    }
}