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

    @Override
    public String toString() {
        return "["+courseName+"] | "+grade+"학년-"+semester+"학기 | "+credit+"학점";
    }

}
