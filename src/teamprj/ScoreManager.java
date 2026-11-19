package teamprj;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;

public class ScoreManager {
    private LinkedHashMap<Course, Score> scores = new LinkedHashMap<>();
    private ExamManager examManager;
    private final Scanner scanner;

    public ScoreManager(ExamManager examManager, Scanner scanner) {
        this.examManager = examManager;
        this.scanner = scanner;
    }

    public void addGrades(int todayMonth, int todayDay) {
        // 이미 시험을 본 강의만, 중복 없이 모으기
        LinkedHashSet<Course> courseSet = new LinkedHashSet<>();
        for (Exam e : examManager.getPastExams(todayMonth, todayDay)) {
            courseSet.add(e.getCourse());
        }
        if (courseSet.isEmpty()) {
            System.out.println("성적을 입력할 수 있는(이미 시험을 치른) 강의가 없습니다.");
            return;
        }
        List<Course> list = new ArrayList<>(courseSet);
        System.out.println("------ 성적 입력 가능한 강의 목록 ------");
        for (int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + "." + list.get(i));
        }
        int choice = Input.readInt(scanner, "성적을 입력/수정 할 강의 번호 : ", 1, list.size());
        Course course = list.get(choice - 1);

        float value = Input.readFloat(scanner, "성적을 입력하세요 (0.0 ~ 4.5) : ", 0f, 4.5f);
        Score old = scores.get(course);
        if (old != null) {
            old.setScore(value);
            System.out.println("성적이 수정되었습니다.");
        } else {
            scores.put(course, new Score(course, value));
            System.out.println("성적이 등록되었습니다.");
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
        System.out.print("[총 학점] : ");
        printGpa(list);

    }

    public void showGradeByGrade(){//학년 별 보여주기
        int grade = Input.readInt(scanner,"조회할 성적의 학년을 입력해주세요 : ");

        List<Score> list = new ArrayList<>();
        for (Score score : scores.values()) {
            if (score.getGrade() == grade) {
                list.add(score);
            }
        }
        if(list.isEmpty()){
            System.out.println("학년에 등록된 성적이 없습니다.");
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
        int grade = Input.readInt(scanner,"조회할 성적의 학년을 입력해주세요 : ");
        int semester = Input.readInt(scanner,"조회할 성적의 학기를 입력해주세요 : ",1,2);

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
            int credit = s.getCredit();
            sum += credit*s.getScore();
            totalCredit += credit;
        }
        return totalCredit ==0 ? 0 : sum/totalCredit;
    }

    private int calcTotalCredit(List<Score> list){
        int total =0;
        for(Score s : list){
            total += s.getCredit();
        }
        return total;
    }

    private void printGpa(List<Score> list){
        System.out.printf("총 %d학점 | 평점평균 : %.2f%n",calcTotalCredit(list), calcGpa(list));
    }

public void removeScore(Course course) {
    scores.remove(course);
}

public LinkedHashMap<Course, Score> getScores() {
        return scores;
    }
}