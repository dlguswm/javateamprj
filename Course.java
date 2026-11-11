package teamprj;

public class Course {
    private String courseName;
    private float credit;
    private int grade;
    private int semester;

    public Course(String courseName, float credit, int grade, int semester) {
        this.courseName = courseName;
        this.credit = credit;
        this.grade = grade;
        this.semester = semester;
    }

    public String getCourseName() {
        return courseName;
    }
    public float getCredit() {
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
