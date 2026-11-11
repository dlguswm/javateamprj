package teamprj;

import java.util.Scanner;

public class Input {
    public static int readInt(Scanner scanner){
        while(true){
            try{
                return Integer.parseInt(scanner.nextLine().trim());
            }catch (NumberFormatException e){
                System.out.println("잘못된 입력입니다.");
            }
        }
    }

    public static int readInt(Scanner scanner, int min, int max){
        while(true){
            int value = readInt(scanner);
            if(value>= min && value<=max){
                return value;
            }
            System.out.println(min+"~"+max+"사이의 수를 입력해주세요. ");
        }
    }

    public static float readFloat(Scanner scanner){
        while(true){
            try {
                return Float.parseFloat(scanner.nextLine().trim());
            }catch (NumberFormatException e){
                System.out.println("잘못된 입력입니다.");
            }
        }
    }

    public static float readFloat(Scanner scanner, float min, float max){
        while(true){
            float value = readFloat(scanner);
            if(value >= min && value <= max){
                return value;
            }
            System.out.println(min+"~"+max+"사이의 수를 입력해주세요.");
        }
    }

    public static String readString(Scanner scanner){
        while(true){
            String s = scanner.nextLine().trim();
            if(!s.isEmpty()){
                return s;
            }
            System.out.println("빈 값은 입력할 수 없습니다. 다시 입력해 주세요. ");
        }
    }

    public static boolean readYesNo(Scanner scanner){
        while(true){
            String s = scanner.nextLine().trim();
            if(s.equalsIgnoreCase("y")){
                return true;
            }
            if(s.equalsIgnoreCase("n")){
                return false;
            }
            System.out.println("y 또는 n로 입력해주세요");
        }
    }

}
