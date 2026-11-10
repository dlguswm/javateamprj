package teamprj;

import java.util.ArrayList;
import java.util.Scanner;

public class ScoreManager {
    private ArrayList<Score> scores = new ArrayList<>();
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
            int num=1;
            for(Score score : scores){
                System.out.println(num+"."+score);
                num++;
            }
        }
    }//출력되는 거 수정 해 <1학년2>학기이게 ㄱㅖ소ㅗㄱ 출력됨 일단 Score 오버라이딩 바꾸고 ...여기서 뭐 잘 해봐>

    public void showGradeByGrade(){//학년 별 보여주기
        System.out.print("조회할 성의 학년을 입력해주세요 : ");
        int grade = scanner.nextInt();
        scanner.nextLine();

        boolean found = false;
        int num=1;
        for (Score score : scores) {
            if (score.getGrade() == grade) {
                System.out.println(num + ". " + score);
                num++;
                found = true;
            }
        }
        if(!found){
            System.out.println("학년에 둥록된 성적이 없습니다.");
        }
    }

    public void showGradeBySemester(){//학기 별 보여주기
        System.out.print("조회할 성적의 학기를 입력해주세요 : ");
        int semester = scanner.nextInt();
        scanner.nextLine();

        boolean found = false;
        int num=1;
        for (Score score : scores) {
            if (score.getSemester() == semester) {
                System.out.println(num + ". " + score);
                num++;
                found = true;
            }
        }
        if(!found){
            System.out.println("학기에 둥록된 성적이 없습니다.");
        }
    }

}
