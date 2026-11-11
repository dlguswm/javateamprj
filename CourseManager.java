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
        System.out.print("강의명: ");
        String courseName = scanner.nextLine();
        System.out.print("학점: ");
        float credit = Input.readFloat(scanner,0f,4.5f);
        System.out.print("학년: ");
        int grade = Input.readInt(scanner);
        System.out.print("학기: ");
        int semester = Input.readInt(scanner,1,2);
        //scanner.nextLine();

        Course course = new Course(courseName, credit, grade, semester);
        courses.add(course); //ArrayList에 추가하기
    }

    public void showCourses(){
        if(courses.isEmpty()){
            System.out.println("등록된 강의가 없습니다.");
            return;
        }else{
            System.out.println("------- 강의 목록 -------");
            for(int i =0; i< courses.size(); i++){
                System.out.println((i+1)+"."+courses.get(i)); //얘도 포이치로 바ㅜ꺼라
            }
        }
    }



    public void deleteCourse(){
        if(courses.isEmpty()){
            System.out.println("등록된 강의가 없습니다.");
            return;
        }else{
            showCourses();
            System.out.print("삭제할 강의 번호: ");
            int index = Input.readInt(scanner)-1;

            if(index<0 || index>= courses.size()){
                System.out.println("잘못된 번호입니다.");
                return;
            }
            Course course = courses.remove(index);
        }
    }

    public ArrayList<Course> getCourses(){
        return courses;
    }

    // 번호로 강의 가져오기
    public Course getCourseByIndex(int index) {
        if (index < 0 || index >= courses.size()) {
            return null;
        }
        return courses.get(index);
    }
    // 나중에 과제나 시험을 추가할 때 사용
    //사용자가 강의를 선택하면 해당 강의를 가져와서 과제나 시험 객체에 전닳하기ㅣ

}
