package teamprj;

import javax.imageio.stream.IIOByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class AssignmentManager {
    private ArrayList<Assignment> assignments = new ArrayList<>();
    private CourseManager courseManager;
    private final Scanner scanner;

    public AssignmentManager(CourseManager courseManager, Scanner scanner) {
        this.courseManager = courseManager;
        this.scanner = scanner;
    }

    public void addAssignment() {
        courseManager.showCourses();
        System.out.print("과제를 등록할 강의 번호 : ");
        int choice = Input.readInt(scanner);
        Course course = courseManager.getCourseByIndex(choice - 1);

        if (course == null) {
            System.out.println("강의 번호를 다시 확인해 주세요.");
            return;
        }
        System.out.print("등록할 과제명을 입력하세요 : ");
        String title = Input.readString(scanner);
        int[] due = Ddaycalculate.readDate(scanner, "과제 마감 날짜 : (ex.11/01)   ");
        int dueMonth = due[0];
        int dueDay = due[1];
        System.out.print("제출 여부 : (y or n)   ");
        String done1 = scanner.nextLine().trim();
        boolean done2 = done1.equalsIgnoreCase("y");

        Assignment assignment = new Assignment(course, title, dueMonth, dueDay, done2);
        assignments.add(assignment);
    }

    public void showAssignment(int todayMonth, int todayDay) { //기본은 디데이순으로 할거임
        if (assignments.isEmpty()) {
            System.out.println("등록된 과제가 없습니다.");
            return;
        }
        System.out.println("------ 과제 목록 ------");
        sortByDday(todayMonth, todayDay);

        int num = 1;
        for (Assignment assignment : assignments) {
            System.out.println(num + "." + assignment + Ddaycalculate.Dday(assignment.getDday(todayMonth, todayDay)));
            num++;
        }
    }

    private void sortByDday(int todayMonth, int todayDay) {
        for (int i = 0; i < assignments.size() - 1; i++) {
            int min = i;
            for (int j = i + 1; j < assignments.size(); j++) {
                if (assignments.get(j).getDday(todayMonth, todayDay) < assignments.get(min).getDday(todayMonth, todayDay)) {
                    min = j;
                }
            }
            Assignment temp = assignments.get(i);
            assignments.set(i, assignments.get(min));
            assignments.set(min, temp);
        }
    }

    public void showAssignmentByCourse(int todayMonth, int todayDay) {
        if (assignments.isEmpty()) {
            System.out.println("등록된 과제가 없습니다.");
            return;
        }
        courseManager.showCourses();
        System.out.print("조회할 강의의 과제 번호 : ");
        int choice = Input.readInt(scanner);
        Course course = courseManager.getCourseByIndex(choice - 1);

        if (course == null) {
            System.out.println("강의 번호를 다시 확인해 주세요");
            return;
        }

        boolean found = false;
        for (Assignment assignment : assignments) {
            if (assignment.getCourse() == course) {
                System.out.println(assignment + Ddaycalculate.Dday(assignment.getDday(todayMonth, todayDay)));
                found = true;
            }
        }
        if (!found) {
            System.out.println("등록된 과제 없음");
        }
    }

    public void deleteAssignment(int todayMonth, int todayDay) {
        if (assignments.isEmpty()) {
            System.out.println("등록된 과제가 없습니다.");
            return;
        }
        showAssignment(todayMonth, todayDay);
        System.out.println("삭제할 과제 번호 : ");
        int index = Input.readInt(scanner) - 1;
        if ((index) < 0 || index >= assignments.size()) {
            System.out.println("잘못된 번호입니다.");
            return;
        }
        Assignment assignment = assignments.remove(index);
    }

    public void updateDone(int todayMonth, int todayDay) {  //과제 제출여부 수정할 때
        if (assignments.isEmpty()) {
            System.out.println("등록된 과제가 없습니다.");
            return;
        }
        showAssignment(todayMonth, todayDay);
        System.out.print("제출 여부를 변경할 과제 번호 : ");
        int choice = Input.readInt(scanner,1,assignments.size());
        //if (choice < 1 || choice > assignments.size()) {
        //    System.out.println("잘못된 번호입니다.");
        //    return;
        //}

        Assignment assignment = assignments.get(choice - 1);
        System.out.print("제출 완료했나요? (y/n) :  ");
        String input = scanner.nextLine();

        if (input.equalsIgnoreCase("y")) {
            assignment.setDone(true);
        } else if (input.equalsIgnoreCase("n")) {
            assignment.setDone(false);
        } else {
            System.out.println("잘못된 입력입니다.");
            return;
        }
    }

    public void removeByCourse(Course course){
        assignments.removeIf(a -> a.getCourse() == course);
    }

    public void removeExpiredAssignment(int todayMonth, int todayDay) { //과제 D+7 되면 자동삭제되게 하기
        Iterator<Assignment> it = assignments.iterator();
        while (it.hasNext()) {
            Assignment a = it.next();
            if (a.getDday(todayMonth, todayDay) <= -7) {
                //    System.out.println("[자동삭제] " + a);
                it.remove();
            }
        }
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }
}
