//Brandon Chesley 300262641
import java.util.Scanner;
public class BubbleSort{
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        System.out.print("Please enter the # of numbers to be sorted:");
        int arrayLength = input.nextInt();
        System.out.print("Enter 1 for int's or 2 for doubles: ");
        int intOrDouble = input.nextInt();
        int[] intArray = new int[arrayLength];
        double[] doubleArray = new double[arrayLength];
        for(int i = 0; i < intArray.length; i++){
            System.out.print("Please enter a number:");
            if(intOrDouble == 1){                intArray[i] += input.nextInt();             
            }else if(intOrDouble == 2){
                doubleArray[i] += input.nextInt();
            }
        }
        if(intOrDouble == 1){
            bubbleSort(intArray);
        }else if(intOrDouble == 2){
            bubbleSort(doubleArray);
        }
    }

    public static void bubbleSort(int[] arrayInt){
        int temp;
        for (int x = 0; x < arrayInt.length - 1; x++){
            for(int  y = 0;y < arrayInt.length - 1; y++){
                if(arrayInt[y] > arrayInt[y + 1]){
                    temp = arrayInt[y];
                    arrayInt[y] = arrayInt[y+1];
                    arrayInt[y + 1] = temp;
                } 
            }
            System.out.print(arrayInt[0]);
            for(int i = 1; i < arrayInt.length;i++){
                System.out.print("," + arrayInt[i]);
            }
            System.out.println();
        }
    }

    public static void bubbleSort(double[] arrayDouble){
        double temp;
        for (int x = 0; x < arrayDouble.length - 1; x++){
            for(int  y = 0;y < arrayDouble.length - 1; y++){
                if(arrayDouble[y] > arrayDouble[y + 1]){
                    temp = arrayDouble[y];
                    arrayDouble[y] = arrayDouble[y+1];
                    arrayDouble[y + 1] = temp;
                } 
            }
            System.out.print(arrayDouble[0]);
            for(int i = 1; i < arrayDouble.length;i++){
                System.out.print("," + arrayDouble[i]);
            }
            System.out.println();
        }
    }
}

