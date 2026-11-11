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
        System.out.print("시험을 등록할 강의 번호 : ");
        int choice = Input.readInt(scanner);
        Course course = courseManager.getCourseByIndex(choice - 1);

        if (course == null) {
            System.out.println("강의 번호를 다시 확인해 주세요.");
            return;
        } else {
            int[] date = Ddaycalculate.readDate(scanner, "시험 날짜를 입력하세요 : (ex.11/01)   ");
            int examMonth = date[0];
            int examDay = date[1];

            Exam exam = new Exam(course, examMonth, examDay);
            exams.add(exam);
        }
    }

    public void showExams(int todayMonth, int todayDay) {
        if (exams.isEmpty()) {
            System.out.println("등록된 시험이 없습니다.");
            return;
        } else {
            System.out.println("------ 시험 목록 ------");
            int num = 1;
            for (Exam exam : exams) {
                System.out.println(num + "." + exam + Ddaycalculate.Dday(exam.getDday(todayMonth, todayDay)));
                num++;
            }
        }
    }

    // [6번] 삭제한 시험을 반환 (취소/실패 시 null)
    public Exam deleteExam(int todayMonth, int todayDay) {
        if (exams.isEmpty()) {
            System.out.println("등록된 시험이 없습니다.");
            return null;
        }
        showExams(todayMonth, todayDay);
        System.out.print("삭제할 시험 번호: ");
        int index = Input.readInt(scanner) - 1;

        if (index < 0 || index >= exams.size()) {
            System.out.println("잘못된 번호입니다.");
            return null;
        }
        return exams.remove(index);
    }

    // [6번] 만료되어 지워진 시험 목록을 반환
    public List<Exam> removeExpiredExam(int todayMonth, int todayDay) {
        List<Exam> removed = new ArrayList<>();
        Iterator<Exam> it = exams.iterator();
        while (it.hasNext()) {
            Exam e = it.next();
            if (e.getDday(todayMonth, todayDay) <= -7) {
                removed.add(e);
                it.remove();
            }
        }
        return removed;
    }

    // [5번] 강의 삭제 시 해당 강의의 시험 삭제 + 지워진 시험 반환
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

    public ArrayList<Exam> getExams() {
        return exams;
    }

    public Exam getExamByIndex(int index) {
        if (index < 0 || index >= exams.size()) {
            return null;
        }
        return exams.get(index);
    }
}