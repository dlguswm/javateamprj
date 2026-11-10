package teamprj;

import java.util.ArrayList;
import java.util.Scanner;

public class ScoreManager {
    private ArrayList<Score> scores = new ArrayList<>();
    private CourseManager courseManager;
    private ExamManager examManager;
    Scanner scanner = new Scanner(System.in);

    public ScoreManager(ExamManager examManager) {
        this.examManager = examManager;
    }

    public void addGrades(int todayMonth, int todayDay){
        showGrade();
        examManager.showExams(todayMonth, todayDay);
        System.out.print("성적을 입력/수정 할 시험 번호 : ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        Exam exam = examManager.getExamByIndex(choice-1);

        if(exam == null){
            System.out.println("시험 번호를 다시 확인해 주세요.");
            return;
        }else {
            System.out.print("성적을 입력하세요 : ");
            float value = Float.parseFloat(scanner.nextLine());
            Score score = new Score(exam.getCourseName(),exam,value);
            scores.add(score);
        }
    }

    public void showGrade(){ //전체 보여주기
        if(scores.isEmpty()){
            System.out.println("등록된 시험 성적이 없습니다.");
            return;
        }else{
            for (int i = 0; i < scores.size(); i++) {
                Score g = scores.get(i);
                System.out.println((i + 1)+"."+g);
            }
        }
    }

    public void showGradeByGrade(){ //학년 별 보여주기

    }

    public void showGradeBySemester(){ //학기 별 보여주기

    }

}
