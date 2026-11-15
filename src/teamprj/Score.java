package teamprj;

import java.util.ArrayList;

public class Score {
    private String courseName;
    private Exam exam;
    private float score;

    public Score(String courseName, Exam exam, float score) {
        this.courseName = courseName;
        this.exam = exam;
        this.score = score;
    }

    public String getCourseName() {
        return courseName;
    }
    public Exam getExam() {
        return exam;
    }
    public float getScore() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    public int getGrade(){
        return exam.getGrade();
    }

    public int getSemester(){
        return exam.getSemester();
    }


    @Override
    public String toString() {
        return "[" +exam.getCourseName()+"] | "+exam.getGrade()+"학년 "+exam.getSemester()+"학기 |"
                +exam.getCredit()+"학점 | 받은 성적 : "+score;
    }

}
