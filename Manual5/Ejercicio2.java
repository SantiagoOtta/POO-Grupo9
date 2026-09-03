package Manual5;
import java.util.Scanner;
public class Ejercicio2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese el valor del numero: ");
        int num = sc.nextInt();
        sc.close();
        int[] lista= {23,5,76,42,12,6,2,29};
        if (buscarLista(num,lista)){
            System.out.print("El numero se encuentra en la lista");
        }else{
            System.out.print("El numero no se encuentra en el lista");
        }

    }
    public static boolean buscarLista(int num,int[] lista){
        for(int i = 0; i<lista.length; i++){
            if(lista[i]==num){
                return true;
            }
            }
        return false;
        }
    }