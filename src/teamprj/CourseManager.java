package teamprj;

import java.util.ArrayList;
import java.util.Scanner;

public class CourseManager {
    private ArrayList<Course> courses = new ArrayList<>();
    private final Scanner scanner;
    //강의들을 담는 ArrayList. 등록 순서 대로 보여주고 번호로 고를 수 있게함

    public CourseManager(Scanner scanner) {
        this.scanner = scanner;
    }


    //강의추가
    public void addCourse() {
        String courseName = Input.readString(scanner, "강의명 : ");
        int credit = Input.readInt(scanner,"학점 : ");
        int grade = Input.readInt(scanner,"학년 : ");
        int semester = Input.readInt(scanner,"학기 : ", 1,2);
        //강의명, 학점, 학년, 학기를 입력받음 학기만 범위 1, 2를 지정

        //중복검사
        //이미 등록된 강의를 하나씩 보면서 강의명, 학년, 학기가 모두 같으면 막음
        for(Course c : courses){
            if(c.getCourseName().equals(courseName) && c.getGrade()== grade && c.getSemester() == semester){
                System.out.println("이미 같은 강의가 등록되어 있습니다.");
                return;
            }
        }
        Course course = new Course(courseName, credit, grade, semester);
        courses.add(course);
    }


    //강의 목록 보가
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


    //강의삭제
    public Course deleteCourse() {
        if (courses.isEmpty()) {
            System.out.println("등록된 강의가 없습니다.");
            return null;
        }
        showCourses();
        int choice = Input.readInt(scanner, "삭제할 강의 번호 : ", 1, courses.size());
        return courses.remove(choice - 1);
    }


    //강의수정
    public void updateCourse() {
        if (courses.isEmpty()) {
            System.out.println("등록된 강의가 없습니다.");
            return;
        }
        showCourses();
        int choice = Input.readInt(scanner, "수정할 강의 번호 : ", 1, courses.size());
        Course course = courses.get(choice - 1);//강의고르고 겟으로 꺼내기

        System.out.println("현재 정보 : " + course);
        String courseName = Input.readString(scanner, "새 강의명 : ");
        int credit = Input.readInt(scanner, "새 학점 : ");
        int grade = Input.readInt(scanner, "새 학년 : ");
        int semester = Input.readInt(scanner, "새 학기 : ", 1, 2);

        //중복검사
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


    //번호로 강의 꺼내기
    public Course getCourseByIndex(int index) {
        if (index < 0 || index >= courses.size()) {
            return null;
        }
        return courses.get(index);
    }

    public ArrayList<Course> getCourses(){
        return courses;
    }


    // ================= GUI용 메서드 (2단계) =================
    // Scanner로 입력받지 않고, 화면에서 받은 값을 매개변수로 받아 처리함
    // 결과는 출력하지 않고 true/false로 돌려주면 화면 쪽에서 알림창을 띄움

    // 중복 검사: except는 비교에서 뺄 강의 (수정할 때 자기 자신, 추가할 때는 null)
    private boolean isDuplicate(String name, int grade, int semester, Course except) {
        for (Course c : courses) {
            if (c != except && c.getCourseName().equals(name)
                    && c.getGrade() == grade && c.getSemester() == semester) {
                return true;
            }
        }
        return false;
    }

    // 강의 추가: 중복이면 false
    public boolean addCourse(String name, int credit, int grade, int semester) {
        if (isDuplicate(name, grade, semester, null)) {
            return false;
        }
        courses.add(new Course(name, credit, grade, semester));
        return true;
    }

    // 강의 수정: index번째 강의를 새 값으로 바꿈, 중복이면 false
    public boolean updateCourse(int index, String name, int credit, int grade, int semester) {
        Course course = courses.get(index);
        if (isDuplicate(name, grade, semester, course)) {
            return false;
        }
        course.setCourseName(name);
        course.setCredit(credit);
        course.setGrade(grade);
        course.setSemester(semester);
        return true;
    }

    // 강의 삭제: 지운 강의를 돌려줘서 과제·시험·성적도 함께 지울 수 있게 함
    public Course deleteCourse(int index) {
        return courses.remove(index);
    }
}
