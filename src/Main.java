import java.util.InputMismatchException;
import java.util.Scanner;

public class Main{
    private static int modulo(int num, int mod){
        if(num<0){
            return(num%mod+mod);
        }else{
            return(num%mod);
        }
    }

    public static void main(String[] args){
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter prime number: ");
            int inputPrime = scanner.nextInt();
            if (inputPrime>2 && inputPrime % 2 == 0) {
                throw new Exception("Given number is even");
            }
            if(inputPrime>Math.pow(2,30)-1 || inputPrime < 2){
                throw new InputMismatchException();
            }

            System.out.print("Enter squared number: ");
            int inputNumber = modulo(scanner.nextInt(),inputPrime);
            if(inputNumber>Math.pow(2,30)-1 || inputNumber < 2){
                throw new InputMismatchException();
            }

            FiniteField finiteField = new FiniteField(inputPrime);
            if(finiteField.legendreSymbol(inputNumber)!=1){
                throw new Exception("This number is not quadratic residue.");
            }

            Backend b = new Backend(inputPrime,inputNumber);
            b.run();

        }catch (InputMismatchException e){
            System.out.println("Given number is greater than 2^30-1 or not a number.");
        }catch (Exception e) {
            System.out.println("Error! " + e);
        }
    }
}
