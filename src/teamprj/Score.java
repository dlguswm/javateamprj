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
    public float getGrade() {
        return score;
    }

    public void setScore(float score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return "<"+exam.getGrade()+"-"+exam.getSemester()+"> \n ["
                +exam.getCourseName()+"] | "+exam.getCredit()+"학점 | 받은 성적 : "+score;
    }
}
