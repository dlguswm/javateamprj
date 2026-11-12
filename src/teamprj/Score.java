package teamprj;

import java.util.ArrayList;

public class Score {
    private String courseName;
    //얘는 나중에 ScoreManager에서 ExamManager에 있는 getCourseNamge으로 받아올 거적
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

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    public void setExam(Exam exam) {
        this.exam = exam;
    }

    @Override
    public String toString() {
        return "<"+exam.getGrade()+"-"+exam.getSemester()+"> ["
                +exam.getCourseName()+"] | "+exam.getCredit()+"학점 | 받은 성적 : "+score;
    }

}
