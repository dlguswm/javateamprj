package teamprj;

public class Exam {
    private Course course;
    private int examMonth;
    private int examDay;

    public Exam(Course course, int examMonth, int examDay) {
        this.course = course;
        this.examMonth = examMonth;
        this.examDay = examDay;
    }

    public Course getCourse() {
        return course;
    }
    public int getExamMonth() {
        return examMonth;
    }
    public int getExamDay() {
        return examDay;
    }

    public int getDday(int todayMonth, int todayDay) {
        return Ddaycalculate.calcDday(examMonth, examDay,todayMonth, todayDay);
    }//Ddaycalculate 클래스의 calcDday 메서드를 사용해서 디데이계산. 현재 날짜는 나중에 메인에서 입력받을거임

    /////////////////////////////////////
    public String getCourseName() {
        return this.course.getCourseName();
    }

    public int getCredit(){
        return this.course.getCredit();
    }

    public int getGrade() {
        return this.course.getGrade();
    }
    public int getSemester() {
        return this.course.getSemester();
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    public void setExamMonth(int examMonth) {
        this.examMonth = examMonth;
    }
    public void setExamDay(int examDay) {
        this.examDay = examDay;
    }


    /////////////////////////////////// 이건 다 Grade에 쓸려고
    /// 왜 Course거 안 쓰냐면 강의는 듣지만 시험은 안 보는 과목 있을 수 있으니 ??
    /// 시험을 봐야 -> 성적이 나오기 ??
    /// 아니면 걍 Courseㄱ거 써도 되나/...??


    @Override
    public String toString() {
        return "["+course.getCourseName()+"] | "+examMonth+"/"+examDay+" | ";

    }

}
