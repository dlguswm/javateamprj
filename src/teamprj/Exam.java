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
    public int getDday(int todayMonth, int todayDay) {
        return Ddaycalculate.calcDday(examMonth, examDay,todayMonth, todayDay);
    }



    @Override
    public String toString() {
        return "["+course.getCourseName()+"] | "+course.getGrade()+"학년 "+course.getSemester()+
                "학기 | "+examMonth+"/"+examDay+" | ";

    }

}
