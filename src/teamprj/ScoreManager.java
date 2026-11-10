package teamprj;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

public class ScoreManager {
    private LinkedHashMap<Exam, Score> scores = new LinkedHashMap<>();
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

            Score old = scores.get(exam);
            if(old != null){
                old.setScore(value);
            //    System.out.println("성적이 수정되었습니다.");
            }else{
                scores.put(exam, new Score(exam.getCourseName(), exam, value));
            //    System.out.println("성적이 등록되었습니다.");
            }
        }
    }

    public void showGrade() { //전체 보여주기
        if (scores.isEmpty()) {
            System.out.println("등록된 시험 성적이 없습니다.");
            return;
        }
        List<Score> list = new ArrayList<>(scores.values());

        int num = 1;
        for (Score score : scores.values()) {
            System.out.println(num + "." + score);
            num++;
        }
        System.out.print("[총 학점} : ");
        printGpa(list);

    }//출력되는 거 수정 해 <1학년2>학기이게 ㄱㅖ소ㅗㄱ 출력됨 일단 Score 오버라이딩 바꾸고 ...여기서 뭐 잘 해봐>

    public void showGradeByGrade(){//학년 별 보여주기
        System.out.print("조회할 성의 학년을 입력해주세요 : ");
        int grade = scanner.nextInt();
        scanner.nextLine();

        List<Score> list = new ArrayList<>(scores.values());
        for (Score score : scores.values()) {
            if (score.getGrade() == grade) {
                list.add(score);
            }
        }
        if(list.isEmpty()){
            System.out.println("학년에 둥록된 성적이 없습니다.");
            return;
        }

        int num=1;
        for(Score score : list){
            System.out.println(num+"."+score);
            num++;
        }
        System.out.print("["+grade+"학년]의 학점 : ");
        printGpa(list);
    }

    public void showGradeBySemester() { // 학년+학기 별 보여주기 + 해당 학기 평점평균
        System.out.print("조회할 성적의 학년을 입력해주세요 : ");
        int grade = scanner.nextInt();
        System.out.print("조회할 성적의 학기를 입력해주세요 : ");
        int semester = scanner.nextInt();
        scanner.nextLine();

        List<Score> list = new ArrayList<>();
        for (Score score : scores.values()) {
            if (score.getGrade() == grade && score.getSemester() == semester) {
                list.add(score);
            }
        }

        if (list.isEmpty()) {
            System.out.println("해당 학기에 등록된 성적이 없습니다.");
            return;
        }
        int num = 1;
        for (Score score : list) {
            System.out.println(num + ". " + score);
            num++;
        }
        System.out.print("[" + grade + "학년 " + semester + "학기] ");
        printGpa(list);
    }

    private double calcGpa(List<Score> list){
        double sum =0;
        int totalCredit =0;
        for(Score s : list){
            int credit = s.getExam().getCredit();
            sum += credit*s.getScore();
        }
        if(totalCredit ==0){
            return 0;
        }
        return sum/totalCredit;
    }

    private int calcTotalCredit(List<Score> list){
        int total =0;
        for(Score s : list){
            total += s.getExam().getCredit();
        }
        return total;
    }

    private void printGpa(List<Score> list){
        System.out.printf("총 %d학점 | 평점평균 : %.2f%n",calcTotalCredit(list), calcGpa(list));
    }

}
