package teamprj;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;

public class ScoreManager {
    //성적 등록, 수정, 조건별 성적 조회(전체, 학년, 학기)
    private LinkedHashMap<Course, Score> scores = new LinkedHashMap<>();
    //scores는 LinkedHaskMap을 사용하여 Course객체를 키로, Score객체를 값으로 저장한다.
    //이를 통해 각 강의에 해당하는 성적을 연결하여 관리할 수 있으며, 동일한 강의의 성적이 중복 등록되는 걸 방지
    private ExamManager examManager;
    private final Scanner scanner;

    public ScoreManager(ExamManager examManager, Scanner scanner) {
        this.examManager = examManager;
        this.scanner = scanner;
    }

    public void addGrades(int todayMonth, int todayDay) {
        // 이미 시험을 본 강의만, 중복 없이 모으기
        LinkedHashSet<Course> courseSet = new LinkedHashSet<>();
        //중간, 기말 등 시험은 한 번 이상 봄
        //근데 성적은 강의별로 하나씩 등록하고 관리하기 떄문에 강의 중복되면 안돼
        //그래서 링크드해쉬셋으로 중복 제거
        for (Exam e : examManager.getPastExams(todayMonth, todayDay)) {
            courseSet.add(e.getCourse());
        }
        if (courseSet.isEmpty()) {
            System.out.println("성적을 입력할 수 있는(이미 시험을 치른) 강의가 없습니다.");
            return;
        }
        List<Course> list = new ArrayList<>(courseSet);
        System.out.println("------ 성적 입력 가능한 강의 목록 ------");
        int num = 1;
        for (Course c : list) {
            System.out.println(num + "." + c);
            num++;
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
    }//등록된 전체 성적을 출력하고 총 학점과 전체 평점 평균을 보여준다.
     //먼저 성적 등록되어 있는지 확인하고 등록된 성적이 없다면 안내 메시지 출력 후 종료
     //존재하면 포이치 구문으로 scores에 저장된 성적들 모두 출력하고 printGpa메서드를 호출하여 총 학점과 평점평균 출력

    public void showGradeByGrade(){//학년 별 보여주기
        int grade = Input.readInt(scanner,"조회할 성적의 학년을 입력해주세요 : ");

        List<Score> list = new ArrayList<>();
        for (Score score : scores.values()) {
            if (score.getGrade() == grade) {
                list.add(score);
            }
        }
        //scores에 저장된 성적들을 포이치구문으로 확인하면서 Score객체의 학년이
        //입력받은 학년과 일치하는 경우 list에 추가한다.

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
        //해당 학년의 성적 목록과 총 학점, 평점 평균 출력
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

    private int calcTotalCredit(List<Score> list){
        int total =0;
        for(Score s : list){
            total += s.getCredit();
        }
        return total;
    }//리스트에 있는 과목들의 총 학점 계산

    private double calcGpa(List<Score> list){
        int totalCredit = calcTotalCredit(list);
        if (totalCredit == 0) return 0;

        double sum = 0;
        for (Score s : list) {
            sum += s.getCredit() * s.getScore();
        }
        return sum / totalCredit;
    }//과목별 학점이 반영된 평점 평균을 계산하는 역할
     //각 과목의 학점과 성적을 곱하여 다 더하고 전체 학점으로 나누어 평점 평균을 계산한다.
     //전체 학점이 0이면 0반환 아니면 위


    private void printGpa(List<Score> list){
        System.out.printf("총 %d학점 | 평점평균 : %.2f%n",calcTotalCredit(list), calcGpa(list));
    }

    public void removeScore(Course course) {
        scores.remove(course);
    }//특정 강의의 성적을 삭제하는 메서드로, 해당 Corse 객체를 키로 사용하여 scores에서 성적을 제거한다.

    public LinkedHashMap<Course, Score> getScores() {
        return scores;
    }//현재 저장된 성적 정보를 반환하여 다른 클래스에서도 성적 데이터에 접근할 수 있도록 한다.
}