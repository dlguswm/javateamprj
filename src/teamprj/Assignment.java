package teamprj;

public class Assignment {
    private Course course;
    private String title;
    private int dueMonth;
    private int dueDay;
    private boolean done;

    public Assignment(Course course, String title, int dueMonth, int dueDay,boolean done) {
        this.course = course;
        this.title = title;
        this.dueMonth = dueMonth;
        this.dueDay = dueDay;
        this.done = done;
    }

    public int getDday(int todayMonth, int todayDay) {
        return Ddaycalculate.calcDday(dueMonth, dueDay,todayMonth, todayDay);
    }//Ddaycalculate 클래스의 calcDday 메서드를 사용해서 디데이계산. 현재 날짜는 입력받아야 함

    public Course getCourse() {
        return course;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public int getDueMonth() {
        return dueMonth;
    }
    public void setDueMonth(int dueMonth) {
        this.dueMonth = dueMonth;
    }
    public int getDueDay() {
        return dueDay;
    }
    public void setDueDay(int dueDay) {
        this.dueDay = dueDay;
    }
    public boolean isDone() {
        return done;
    }

    @Override
    public String toString() {
        if (done == true) {
            return "[" + course.getCourseName() + "] " + title + " | 제출완료 | ";
        }else{
            return "[" + course.getCourseName() + "] " + title + " | 제출미완료 | ";
            // 나중에 매니저에서 뒤에 디데이 붙여서 출력하기
        }
    }

}
