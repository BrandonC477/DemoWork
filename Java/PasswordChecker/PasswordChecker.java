//Brandon Chesley 300262641
import java.util.Scanner;
public class PasswordChecker{
    public static void main(String[] args){
        Scanner input = new Scanner(System.in);
        String password = "";
        while(!(password.equals("endofinput"))){
            boolean isEight = true;
            boolean isAlpha = true;
            boolean isUpper = false;
            boolean isLower = false;
            boolean isDigit = false;
            boolean notPassword = false;
            char check = ' ';
            System.out.print("Password:");
            password = input.nextLine();
            int count;

            if(password.length() < 8){
                isEight = false;
            }
            if(!(password.toLowerCase().contains("password"))){
                notPassword = true;
            }

            for(count = 0; count < password.length(); count++){
                check = password.charAt(count);
                if(!(Character.isLetterOrDigit(check))){
                    isAlpha = false;
                }
                if(Character.isUpperCase(check)){
                    isUpper = true;
                }
                if(Character.isLowerCase(check)){
                    isLower = true;
                }
                if(Character.isDigit(check)){
                    isDigit = true;
                }
            }
            if(!(password.equals("endofinput"))){
                if(isEight == false){
                    System.out.println("Password must be 8 characters long");
                }else if(notPassword == false){
                    System.out.println("Password cannot contain the word password");
                }else if(isAlpha == false){
                    System.out.println("Password can only contain alphanumeric values");
                }else if(isUpper == false){
                    System.out.println("Password must contain an uppercase letter");
                }else if(isLower == false){
                    System.out.println("Password must contain a lowercase letter");
                }else if(isDigit == false){
                    System.out.println("Password must contain a number");
                }else{
                    System.out.println("Password is valid");
                }
            }
        }
    }
}