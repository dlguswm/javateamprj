package teamprj;

import java.util.ArrayList;
import java.util.Scanner;

public class CourseManager {
    private ArrayList<Course> courses = new ArrayList<>();
    private final Scanner scanner;

    public CourseManager(Scanner scanner) {
        this.scanner = scanner;
    }

    public void addCourse() {
        String courseName = Input.readString(scanner, "강의명 : ");
        int credit = Input.readInt(scanner,"학점 : ");
        int grade = Input.readInt(scanner,"학년 : ");
        int semester = Input.readInt(scanner,"학기 : ", 1,2);

        for(Course c : courses){
            if(c.getCourseName().equals(courseName) && c.getGrade()== grade && c.getSemester() == semester){
                System.out.println("이미 같은 강의가 등록되어 있습니다.");
                return;
            }
        }
        Course course = new Course(courseName, credit, grade, semester);
        courses.add(course);
    }

    public void showCourses(){
        if(courses.isEmpty()){
            System.out.println("등록된 강의가 없습니다.");
            return;
        }else{
            System.out.println("------- 강의 목록 -------");
            int count = 1;
            for(Course c : courses){
                System.out.println(count + "." + c);
                count++;
            }
        }
    }

    public Course deleteCourse() {
        if (courses.isEmpty()) {
            System.out.println("등록된 강의가 없습니다.");
            return null;
        }
        showCourses();
        int choice = Input.readInt(scanner, "삭제할 강의 번호 : ", 1, courses.size());
        return courses.remove(choice - 1);
    }


    public void updateCourse() {
        if (courses.isEmpty()) {
            System.out.println("등록된 강의가 없습니다.");
            return;
        }
        showCourses();
        int choice = Input.readInt(scanner, "수정할 강의 번호 : ", 1, courses.size());
        Course course = courses.get(choice - 1);

        System.out.println("현재 정보 : " + course);
        String courseName = Input.readString(scanner, "새 강의명 : ");
        int credit = Input.readInt(scanner, "새 학점 : ");
        int grade = Input.readInt(scanner, "새 학년 : ");
        int semester = Input.readInt(scanner, "새 학기 : ", 1, 2);

        for (Course c : courses) {
            if (c != course && c.getCourseName().equals(courseName) && c.getGrade() == grade && c.getSemester() == semester) {
                System.out.println("이미 같은 강의가 등록되어 있습니다.");
                return;
            }
        }
        course.setCourseName(courseName);
        course.setCredit(credit);
        course.setGrade(grade);
        course.setSemester(semester);
        System.out.println("강의 정보가 수정되었습니다.");
    }

    public Course getCourseByIndex(int index) {
        if (index < 0 || index >= courses.size()) {
            return null;
        }
        return courses.get(index);
    }

    public ArrayList<Course> getCourses(){
        return courses;
    }


}