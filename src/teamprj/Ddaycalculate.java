package teamprj;

import java.util.Scanner;

public class Ddaycalculate{
    private static final int[] Days ={31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    public static boolean ValidDate(int month, int day){
        return month >=1 && month <=12 && day >=1 && day<= Days[month-1];
    } // 날짜 잘 입력했는지 확인

    private static int DayofYear(int month, int day){
        int total =0;
        for(int i=0; i<month-1; i++){
            total += Days[i];
        }
        return total+day;
    }

    public static int calcDday(int targetMonth, int targetDay, int todayMonth, int todayDay ){
        return DayofYear(targetMonth, targetDay)- DayofYear(todayMonth,todayDay);
    }// 디데이 계산해주는 메서드

    public static String Dday(int dday) {
        if (dday == 0) return "D-Day";
        if (dday < 0) return "D+" + (-dday);
        if (dday == 1 ) return "D-1 **";
        return "D-" + dday;
    } // 디데이 출력하는 메서드
}


