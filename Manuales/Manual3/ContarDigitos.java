public class ContarDigitos {
    public static void main(String[] args) {
        int numero = 12345;
        int contador = 0;

        do {
            numero = numero / 10;
            contador++;
        } while (numero > 0);

        System.out.println("La cantidad de digitos es: " + contador);
    }
}
