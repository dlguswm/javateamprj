package teamprj;

public class Score {
    private Course course;
    private float score;

    public Score(Course course, float score) {
        this.course = course;
        this.score = score;
    }

    public Course getCourse() { return course; }
    public int getCredit() { return course.getCredit(); }
    public int getGrade() { return course.getGrade(); }
    public int getSemester() { return course.getSemester(); }

    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "[" + course.getCourseName() + "] | " + course.getGrade() + "학년 " + course.getSemester() + "학기 | "
                + course.getCredit() + "학점 | 받은 성적 : " + score;
    }

}
