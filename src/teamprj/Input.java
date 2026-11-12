package teamprj;

import java.util.Scanner;

public class Input {

    public static int readInt(Scanner scanner, String prompt){
        while(true){
            System.out.print(prompt);
            try{
                return Integer.parseInt(scanner.nextLine().trim());
            }catch(NumberFormatException e){
                System.out.println("잘못된 입력입니다.");
            }
        }
    } //정수 받기


    public static int readInt(Scanner scanner, String prompt, int min, int max){
        while(true){
            int value = readInt(scanner, prompt);
            if(value >= min && value <= max){
                return value;
            }
            System.out.println(min+"~"+max+" 사이의 수를 입력해주세요.");
        }
    } //범위 있는 정수 받기

    public static float readFloat(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Float.parseFloat(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("잘못된 입력입니다.");
            }
        }
    }//실수 받기

    public static float readFloat(Scanner scanner, String prompt, float min, float max){
        while(true){
            float value = readFloat(scanner, prompt);
            if(value >= min && value <= max){
                return value;
            }
            System.out.println(min+"~"+max+" 사이의 수를 입력해주세요.");
        }
    }//범위 있는 실수 받기

    public static String readString(Scanner scanner, String prompt){
        while(true){
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            if(!s.isEmpty()){
                return s;
            }
            System.out.println("빈 값은 입력할 수 없습니다.");
        }
    }//문자열 받기

    public static boolean readYesNo(Scanner scanner, String prompt){
        while(true){
            System.out.print(prompt);
            String s = scanner.nextLine().trim();
            if(s.equalsIgnoreCase("y"))
                return true;
            if(s.equalsIgnoreCase("n"))
                return false;
            System.out.println("y 또는 n으로 입력해주세요.");
        }
    }

    public static int[] readDate(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String[] parts = scanner.nextLine().trim().split("/");
            if (parts.length != 2) {
                System.out.println("날짜 입력 형식이 잘못됐습니다. (ex.11/01)");
                continue;
            }
            try {
                int month = Integer.parseInt(parts[0].trim());
                int day = Integer.parseInt(parts[1].trim());
                if (Ddaycalculate.ValidDate(month, day)) {
                    return new int[]{month, day};
                }
                System.out.println("존재하지 않는 날짜입니다.");
            } catch (NumberFormatException e) {
                System.out.println("날짜 입력 형식이 잘못됐습니다. (ex.11/01)");
            }
        }
    }//날짜 입력받기


}