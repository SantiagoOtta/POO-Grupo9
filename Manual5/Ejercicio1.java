package Manual5;

public class Ejercicio1 {
    public static void main(String[] args) {
       int[] numeros = {12,36,24,8,10,67};

       int mayor= numeros[0];
       int menor= numeros[0];

       for (int i =1; i<numeros.length; i++) {
           if (numeros[i]>mayor) {
               mayor = numeros[i];
           }

           if (numeros[i]<menor) {
               menor = numeros[i];
           }
       }
       System.out.println("El elemento menor de la lista es:" + menor);
       System.out.println("El elemento mayor de la lista es:" + mayor);
    }

}
