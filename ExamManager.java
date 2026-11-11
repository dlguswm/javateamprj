package teamprj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class ExamManager {
    private ArrayList<Exam> exams = new ArrayList<>();
    private CourseManager courseManager;
    private final Scanner scanner;

    public ExamManager(CourseManager courseManager, Scanner scanner) {
        this.courseManager = courseManager;
        this.scanner =scanner;
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
            int num=1;
            for(Exam exam : exams){
                System.out.println(num+"."+exam+Ddaycalculate.Dday(exam.getDday(todayMonth, todayDay)));
                num++;
            }
        }
    }

    public void deleteExam(int todayMonth, int todayDay){
        if(exams.isEmpty()){
            System.out.println("등록된 시험이 없습니다.");
            return;
        }else{
            showExams(todayMonth, todayDay);
            System.out.print("삭제할 시험 번호: ");
            int index = Input.readInt(scanner)-1;

            if(index<0 || index>= exams.size()){
                System.out.println("잘못된 번호입니다.");
                return;
            }
            Exam exam = exams.remove(index);
        }
    }

    public void removeExpiredExam(int todayMonth, int todayDay) {
        Iterator<Exam> it = exams.iterator();
        while (it.hasNext()) {
            Exam e = it.next();
            if (e.getDday(todayMonth, todayDay) <= -7) {
                //    System.out.println("[자동삭제] " + a);
                it.remove();
            }
        }
    }

    public ArrayList<Exam> getExams() {
        return exams;
    }

// ///////////////////////////////////////

    public Exam getExamByIndex(int index) {
        if (index < 0 || index >= exams.size()) {
            return null;
        }
        return exams.get(index);
    }

//    public Exam getExamByExamName(String examName){
//        for(Exam exam : exams){
//            if(exam.getCourseName().equals(examName)){
//                return exam;
//            }
//        }
//        return null;
//    }


}

