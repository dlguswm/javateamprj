package teamprj;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

public class AssignmentManager {
    private ArrayList<Assignment> assignments = new ArrayList<>();
    private CourseManager courseManager;
    private final Scanner scanner;

    //생성자
    public AssignmentManager(CourseManager courseManager, Scanner scanner) {
        this.courseManager = courseManager;
        this.scanner = scanner;
    }


    public void addAssignment() {
        courseManager.showCourses();
        int choice = Input.readInt(scanner,"과제를 등록할 강의 번호 : ");
        Course course = courseManager.getCourseByIndex(choice - 1);
        if (course == null) {
            System.out.println("강의 번호를 다시 확인해 주세요.");
            return;
        }

        String title = Input.readString(scanner,"등록할 과제명을 입력하세요 : ");
        int[] due = Input.readDate(scanner, "과제 마감 날짜 : (ex.11/01)   ");
        int dueMonth = due[0];
        int dueDay = due[1];

        for(Assignment a : assignments){
            if(a.getCourse() == course && a.getDueMonth() == dueMonth && a.getDueDay() == dueDay){
                System.out.println("이미 같은 과제가 등록되어 있습니다.");
                return;
            }
        }

        boolean done2 = Input.readYesNo(scanner,"제출 여부 : (y/n)");
        Assignment assignment = new Assignment(course, title, dueMonth, dueDay, done2);
        assignments.add(assignment);
    }


    //디데이순으로 전체 과제 보기
    public void showAssignment(int todayMonth, int todayDay) {
        if (assignments.isEmpty()) {
            System.out.println("등록된 과제가 없습니다.");
            return;
        }
        System.out.println("------ 과제 목록 ------");
        sortByDday(todayMonth, todayDay);

        int num = 1;
        for (Assignment assignment : assignments) {
            System.out.println(num + "." + assignment + DdayCalculate.Dday(assignment.getDday(todayMonth, todayDay)));
            num++;
        }
    }


    //디데이 순으로 정렬
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


    //강의별 과제 보기 (조건검색)
    public void showAssignmentByCourse(int todayMonth, int todayDay) {
        if (assignments.isEmpty()) {
            System.out.println("등록된 과제가 없습니다.");
            return;
        }
        courseManager.showCourses();
        int choice = Input.readInt(scanner, "조회할 강의의 과제 번호 : ");
        Course course = courseManager.getCourseByIndex(choice - 1);
        if (course == null) {
            System.out.println("강의 번호를 다시 확인해 주세요");
            return;
        }
        boolean found = false;
        for (Assignment assignment : assignments) {
            if (assignment.getCourse() == course) {
                System.out.println(assignment + DdayCalculate.Dday(assignment.getDday(todayMonth, todayDay)));
                found = true;
            }
        }
        if (!found) {
            System.out.println("등록된 과제 없음");
        }
    }


    //과제 삭제
    //목록을 보여주고 번호를 받아 그 위치의 과제를 remove() 지우면 뒤의 과제들이 자동으로 앞으로 당겨짐
    public void deleteAssignment(int todayMonth, int todayDay) {
        if (assignments.isEmpty()) {
            System.out.println("등록된 과제가 없습니다.");
            return;
        }
        showAssignment(todayMonth, todayDay);
        int choice = Input.readInt(scanner,"삭제할 과제 번호 : ",1,assignments.size());
        assignments.remove(choice-1);
    }


    //제출 여부 수정
    public void updateDone(int todayMonth, int todayDay) {
        if (assignments.isEmpty()) {
            System.out.println("등록된 과제가 없습니다.");
            return;
        }
        showAssignment(todayMonth, todayDay);
        int choice = Input.readInt(scanner,"제출 여부를 변경할 과제 번호 : ",1,assignments.size());
        Assignment assignment = assignments.get(choice - 1);
        boolean done = Input.readYesNo(scanner,"제출 완료했나요? (y/n)");
        assignment.setDone(done);
    }


    //과제 수정
    //과제를 고르고 현재 정보를 보여준 다음 새 과제명과 새 마감일을 입력받아 setter로 바꿈
    public void updateAssignment(int todayMonth, int todayDay) {
        if (assignments.isEmpty()) {
            System.out.println("등록된 과제가 없습니다.");
            return;
        }
        showAssignment(todayMonth, todayDay);
        int choice = Input.readInt(scanner, "수정할 과제 번호 : ", 1, assignments.size());
        Assignment assignment = assignments.get(choice - 1);

        System.out.println("현재 정보 : " + assignment + assignment.getDueMonth() + "/" + assignment.getDueDay());
        String title = Input.readString(scanner, "새 과제명 : ");
        int[] due = Input.readDate(scanner, "새 마감 날짜 : (ex.11/01)   ");

        for (Assignment a : assignments) {
            if (a != assignment && a.getCourse() == assignment.getCourse()
                    && a.getDueMonth() == due[0] && a.getDueDay() == due[1]) {
                System.out.println("이미 같은 과제가 등록되어 있습니다.");
                return;
            }
        }
        assignment.setTitle(title);
        assignment.setDueMonth(due[0]);
        assignment.setDueDay(due[1]);
        System.out.println("과제 정보가 수정되었습니다.");
    }


    //강의 삭제 시 그 과제 삭제
    public void removeByCourse(Course course){
        assignments.removeIf(a -> a.getCourse() == course);
    }


    //과제 D+7 되면 자동 삭제
    public void removeExpiredAssignment(int todayMonth, int todayDay) {
        Iterator<Assignment> it = assignments.iterator();
        while (it.hasNext()) {
            Assignment a = it.next();
            if (a.getDday(todayMonth, todayDay) <= -7) {
                it.remove();
            }
        }
    }

    //과제 목록 전체
    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }


    // ================= GUI용 메서드 (2단계) =================
    // 화면에서 받은 값을 매개변수로 받아 처리하고, 결과는 true/false로 돌려줌

    // 중복 검사: 같은 강의 + 같은 마감일이면 중복 (except는 수정할 때 자기 자신)
    private boolean isDuplicate(Course course, int month, int day, Assignment except) {
        for (Assignment a : assignments) {
            if (a != except && a.getCourse() == course
                    && a.getDueMonth() == month && a.getDueDay() == day) {
                return true;
            }
        }
        return false;
    }

    // 과제 추가: 처음엔 제출 안 한 상태(false)로 추가, 중복이면 false
    public boolean addAssignment(Course course, String title, int month, int day) {
        if (isDuplicate(course, month, day, null)) {
            return false;
        }
        assignments.add(new Assignment(course, title, month, day, false));
        return true;
    }

    // 과제 수정: 과제명과 마감일을 바꿈, 중복이면 false
    public boolean updateAssignment(Assignment assignment, String title, int month, int day) {
        if (isDuplicate(assignment.getCourse(), month, day, assignment)) {
            return false;
        }
        assignment.setTitle(title);
        assignment.setDueMonth(month);
        assignment.setDueDay(day);
        return true;
    }

    // 과제 삭제: 표에서 고른 과제 객체를 그대로 지움
    public void deleteAssignment(Assignment assignment) {
        assignments.remove(assignment);
    }

    // 디데이 순으로 정렬한 뒤, course가 null이면 전체 과제, 아니면 그 강의 과제만 돌려줌
    public ArrayList<Assignment> getAssignmentList(Course course, int todayMonth, int todayDay) {
        sortByDday(todayMonth, todayDay);
        ArrayList<Assignment> result = new ArrayList<>();
        for (Assignment a : assignments) {
            if (course == null || a.getCourse() == course) {
                result.add(a);
            }
        }
        return result;
    }
}
