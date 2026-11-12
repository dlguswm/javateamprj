package teamprj;

public class Course {
    private String courseName;
    private int credit;
    private int grade;
    private int semester;

    public Course(String courseName, int credit, int grade, int semester) {
        this.courseName = courseName;
        this.credit = credit;
        this.grade = grade;
        this.semester = semester;
    }

    public String getCourseName() {
        return courseName;
    }
    public int getCredit() {
        return credit;
    }
    public int getGrade() {
        return grade;
    }
    public int getSemester() {
        return semester;
    }

    //Setter
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public void setCredit(int credit) {
        this.credit = credit;
    }
    public void setGrade(int grade) {
        this.grade = grade;
    }
    public void setSemester(int semester) {
        this.semester = semester;
    }

    @Override
    public String toString() {
        return "["+courseName+"] | "+grade+"학년-"+semester+"학기 | "+credit+"학점";
    }

}
