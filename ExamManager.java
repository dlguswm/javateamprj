package teamprj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class ExamManager {
    private ArrayList<Exam> exams = new ArrayList<>();
    private CourseManager courseManager;
    private final Scanner scanner;

    public ExamManager(CourseManager courseManager, Scanner scanner) {
        this.courseManager = courseManager;
        this.scanner = scanner;
    }

    public void addExam() {
        courseManager.showCourses();
        int choice = Input.readInt(scanner,"시험을 등록할 강의 번호 : ");
        Course course = courseManager.getCourseByIndex(choice - 1);

        if (course == null) {
            System.out.println("강의 번호를 다시 확인해 주세요.");
            return;
        }
        int[] date = Input.readDate(scanner, "시험 날짜를 입력하세요 : (ex.11/01)   ");
        int examMonth = date[0];
        int examDay = date[1];

        for(Exam e : exams){
            if(e.getCourse() == course && e.getExamMonth()== examMonth && e.getExamDay() == examDay){
                System.out.println("이미 같은 날짜의 시험이 등록되어 있습니다.");
                return;
            }
        }
        Exam exam = new Exam(course, examMonth, examDay);
        exams.add(exam);
    }

    public void showExams(int todayMonth, int todayDay) {
        if (exams.isEmpty()) {
            System.out.println("등록된 시험이 없습니다.");
            return;
        }
        System.out.println("------ 시험 목록 ------");
        int num = 1;
        for (Exam exam : exams) {
            int dday = exam.getDday(todayMonth, todayDay);
            String status = (dday <= 0) ? " [응시완료]" : " [예정]";
            System.out.println(num + "." + exam + Ddaycalculate.Dday(dday) + status);
            num++;
        }
    }

    public Exam deleteExam(int todayMonth, int todayDay) {
        if (exams.isEmpty()) {
            System.out.println("등록된 시험이 없습니다.");
            return null;
        }
        showExams(todayMonth, todayDay);
        int choice = Input.readInt(scanner,"삭제할 시험 번호 : ",1,exams.size());
        return exams.remove(choice-1);
    }

//    // 만료되어 지워진 시험 목록을 반환
//    public List<Exam> removeExpiredExam(int todayMonth, int todayDay) {
//        List<Exam> removed = new ArrayList<>();
//        Iterator<Exam> it = exams.iterator();
//        while (it.hasNext()) {
//            Exam e = it.next();
//            if (e.getDday(todayMonth, todayDay) <= -1) {
//                removed.add(e);
//                it.remove();
//            }
//        }
//        return removed;
//    }

    // 강의 삭제 시 해당 강의의 시험 삭제 + 지워진 시험 반환
    public List<Exam> removeByCourse(Course course) {
        List<Exam> removed = new ArrayList<>();
        for (Exam e : exams) {
            if (e.getCourse() == course) {
                removed.add(e);
            }
        }
        exams.removeAll(removed);
        return removed;
    }

    public Exam getExamByIndex(int index) {
        if (index < 0 || index >= exams.size()) {
            return null;
        }
        return exams.get(index);
    }

    public List<Exam> getPastExams(int todayMonth, int todayDay) {
        List<Exam> pastExams = new ArrayList<>();
        for (Exam e : exams) {
            // D-Day가 0(오늘)이거나 음수(D+1, D+2... 이미 지난 시험)인 경우만 포함
            if (e.getDday(todayMonth, todayDay) <= 0) {
                pastExams.add(e);
            }
        }
        return pastExams;
    }

    public ArrayList<Exam> getExams() {
        return exams;
    }
}