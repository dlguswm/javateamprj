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


    public String getCourseName() {
        return this.course.getCourseName();
    }
    public float getCredit(){
        return this.course.getCredit();
    }
    public int getGrade() {
        return this.course.getGrade();
    }
    public int getSemester() {
        return this.course.getSemester();
    }


    @Override
    public String toString() {
        return "["+course.getCourseName()+"] | "+examMonth+"/"+examDay+" | ";

    }

}
