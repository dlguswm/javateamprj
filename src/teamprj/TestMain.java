package teamprj;

import java.util.Scanner;

public class TestMain {
    public static void main(String[] args){
        TestMain test = new TestMain();
        test.menu();
    }

    public void menu(){
        Scanner scanner = new Scanner(System.in);

        CourseManager courseManager = new CourseManager(scanner);
        AssignmentManager assignmentManager = new AssignmentManager(courseManager, scanner);
        ExamManager examManager = new ExamManager(courseManager, scanner);
        ScoreManager scoreManager = new ScoreManager(examManager, scanner);

        int[] today = Ddaycalculate.readDate(scanner, "오늘 날짜를 입력해주세요 : (ex.11/01)   ");
        int todayMonth = today[0];
        int todayDay = today[1];

        assignmentManager.removeExpiredAssignment(todayMonth, todayDay);
        examManager.removeExpiredExam(todayMonth, todayDay);

        int choice =0;

        while(choice !=5){
            System.out.println("=========== 7조 메뉴 ===========");

            System.out.println("1)강의관리 2)과제관리 3)시험관리 4)학점관리 5)종료");
            System.out.print("메뉴 선택 : ");
            choice = Input.readInt(scanner,1,5);

            switch(choice){
                case 1 -> {
                    while (true) {
                        System.out.println("======= 강의 관리 =======");
                        courseManager.showCourses();
                        System.out.println("1)강의추가 2)강의삭제 3)메인메뉴로돌아가기");
                        System.out.print("메뉴 선택 : ");

                        int choice2 = Input.readInt(scanner,1,3);
                        if(choice2 == 3){
                            break;
                        }
                        switch (choice2) {
                            case 1 -> courseManager.addCourse();
                            case 2 -> courseManager.deleteCourse();
                            default -> System.out.println("잘못된 입력입니다.");
                        }
                    }
                }

                case 2 ->{
                    while(true){
                        System.out.println("======= 과제 관리 =======");
                        assignmentManager.showAssignment(todayMonth,todayDay);
                        System.out.println("1)과제추가 2)과제삭제 3)제출완료 4)강의별과제보기 5)메인메뉴로돌아가기");
                        System.out.print("메뉴 선택 : ");

                        int choice2 = Input.readInt(scanner,1,5);
                        if(choice2 ==5){
                            break;
                        }
                        switch (choice2){
                            case 1 -> assignmentManager.addAssignment();
                            case 2 -> assignmentManager.deleteAssignment(todayMonth,todayDay);
                            case 3 -> assignmentManager.updateDone(todayMonth,todayDay);
                            case 4 -> assignmentManager.showAssignmentByCourse(todayMonth,todayDay);
                            default -> System.out.println("잘못된 입력입니다.");
                        }
                    }
                }

                case 3 ->{
                    while(true){
                        System.out.println("======= 시험 관리 =======");
                        examManager.showExams(todayMonth, todayDay);
                        System.out.println("1)시험추가 2)시험삭제 3)메인메뉴로돌아가기");
                        System.out.print("메뉴 선택 : ");
                        int choice2 = Input.readInt(scanner,1,3);

                        if(choice2 ==3 ){
                            break;
                        }
                        switch (choice2){
                            case 1 -> examManager.addExam();
                            case 2 -> examManager.deleteExam(todayMonth,todayDay);
                            default -> System.out.println("잘못된 입력입니다.");
                        }
                    }
                }

                case 4 -> {
                    while (true) {
                        System.out.println("======학점 관리======");
                        scoreManager.showGrade();
                        System.out.println("1)성적 입력/수정 2)학년별학점보기 3)학기별학점보기 4)메인메뉴로돌아가기");
                        System.out.print("메뉴 선택 : ");
                        int choice2 = Input.readInt(scanner,1,4);

                        if (choice2 == 4) {
                            break;
                        }
                        switch (choice2) {
                            case 1 -> scoreManager.addGrades(todayMonth, todayDay);
                            case 2 -> scoreManager.showGradeByGrade();
                            case 3 -> scoreManager.showGradeBySemester();
                        }
                    }
                }

                case 5 ->{
                    break;
                }


            }
        }
    }
}