package teamprj;

import java.io.*;
import java.util.*;

public class DataManager {
    private final String COURSE_FILE = "courses.txt";
    private final String ASSIGNMENT_FILE = "assignments.txt";
    private final String EXAM_FILE = "exams.txt";
    private final String SCORE_FILE = "scores.txt";

    // ==========================================
    // 1. 전체 데이터 불러오기 (Load)
    // ==========================================
    public void loadData(CourseManager cm, AssignmentManager am, ExamManager em, ScoreManager sm) {
        loadCourses(cm);
        loadAssignments(am, cm);
        loadExams(em, cm);
        loadScores(sm, em);
        System.out.println("[System] 이전 저장된 모든 데이터(강의/과제/시험/성적)를 불러왔습니다.");
    }

    private void loadCourses(CourseManager cm) {
        File file = new File(COURSE_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    Course course = new Course(parts[0], Float.parseFloat(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
                    cm.getCourses().add(course);
                }
            }
        } catch (Exception e) {
            System.out.println("[오류] 강의 데이터 불러오기 실패: " + e.getMessage());
        }
    }

    private void loadAssignments(AssignmentManager am, CourseManager cm) {
        File file = new File(ASSIGNMENT_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String courseName = parts[0];
                    String title = parts[1];
                    int month = Integer.parseInt(parts[2]);
                    int day = Integer.parseInt(parts[3]);
                    boolean done = Boolean.parseBoolean(parts[4]);

                    Course course = findCourseByName(cm, courseName);
                    if (course != null) {
                        Assignment assignment = new Assignment(course, title, month, day, done);
                        am.getAssignments().add(assignment);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[오류] 과제 데이터 불러오기 실패: " + e.getMessage());
        }
    }

    private void loadExams(ExamManager em, CourseManager cm) {
        File file = new File(EXAM_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String courseName = parts[0];
                    int month = Integer.parseInt(parts[1]);
                    int day = Integer.parseInt(parts[2]);

                    Course course = findCourseByName(cm, courseName);
                    if (course != null) {
                        Exam exam = new Exam(course, month, day);
                        em.getExams().add(exam);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[오류] 시험 데이터 불러오기 실패: " + e.getMessage());
        }
    }

    private void loadScores(ScoreManager sm, ExamManager em) {
        File file = new File(SCORE_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String courseName = parts[0];
                    int examMonth = Integer.parseInt(parts[1]);
                    int examDay = Integer.parseInt(parts[2]);
                    float scoreValue = Float.parseFloat(parts[3]);

                    Exam exam = findExam(em, courseName, examMonth, examDay);
                    if (exam != null) {
                        Score score = new Score(courseName, exam, scoreValue);
                        sm.getScores().put(exam, score);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("[오류] 성적 데이터 불러오기 실패: " + e.getMessage());
        }
    }

    // ==========================================
    // 2. 전체 데이터 저장하기 (Save)
    // ==========================================
    public void saveData(CourseManager cm, AssignmentManager am, ExamManager em, ScoreManager sm) {
        saveCourses(cm);
        saveAssignments(am);
        saveExams(em);
        saveScores(sm);
        System.out.println("\n[System] 모든 데이터(강의/과제/시험/성적)가 파일로 자동 저장되었습니다.");
    }

    private void saveCourses(CourseManager cm) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(COURSE_FILE))) {
            for (Course c : cm.getCourses()) {
                writer.println(c.getCourseName() + "," + c.getCredit() + "," + c.getGrade() + "," + c.getSemester());
            }
        } catch (IOException e) {
            System.out.println("[오류] 강의 저장 실패: " + e.getMessage());
        }
    }

    private void saveAssignments(AssignmentManager am) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ASSIGNMENT_FILE))) {
            for (Assignment a : am.getAssignments()) {
                writer.println(a.getCourse().getCourseName() + "," + getTitle(a) + "," + getDueMonth(a) + "," + getDueDay(a) + "," + isDone(a));
            }
        } catch (IOException e) {
            System.out.println("[오류] 과제 저장 실패: " + e.getMessage());
        }
    }

    private void saveExams(ExamManager em) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(EXAM_FILE))) {
            for (Exam e : em.getExams()) {
                writer.println(e.getCourse().getCourseName() + "," + e.getExamMonth() + "," + e.getExamDay());
            }
        } catch (IOException e) {
            System.out.println("[오류] 시험 저장 실패: " + e.getMessage());
        }
    }

    private void saveScores(ScoreManager sm) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SCORE_FILE))) {
            for (Map.Entry<Exam, Score> entry : sm.getScores().entrySet()) {
                Exam exam = entry.getKey();
                Score score = entry.getValue();
                writer.println(exam.getCourse().getCourseName() + "," + exam.getExamMonth() + "," + exam.getExamDay() + "," + score.getScore());
            }
        } catch (IOException e) {
            System.out.println("[오류] 성적 저장 실패: " + e.getMessage());
        }
    }

    // 헬퍼 메서드: 강의 이름으로 찾기
    private Course findCourseByName(CourseManager cm, String name) {
        for (Course c : cm.getCourses()) {
            if (c.getCourseName().equals(name)) return c;
        }
        return null;
    }

    // 헬퍼 메서드: 시험 찾기
    private Exam findExam(ExamManager em, String courseName, int month, int day) {
        for (Exam e : em.getExams()) {
            if (e.getCourse().getCourseName().equals(courseName) && e.getExamMonth() == month && e.getExamDay() == day) {
                return e;
            }
        }
        return null;
    }

    // Reflection을 활용해 Assignment의 private 필드 가져오기 보조
    private String getTitle(Assignment a) {
        try {
            var field = Assignment.class.getDeclaredField("title");
            field.setAccessible(true);
            return (String) field.get(a);
        } catch (Exception e) { return ""; }
    }

    private int getDueMonth(Assignment a) {
        try {
            var field = Assignment.class.getDeclaredField("dueMonth");
            field.setAccessible(true);
            return (int) field.get(a);
        } catch (Exception e) { return 0; }
    }

    private int getDueDay(Assignment a) {
        try {
            var field = Assignment.class.getDeclaredField("dueDay");
            field.setAccessible(true);
            return (int) field.get(a);
        } catch (Exception e) { return 0; }
    }

    private boolean isDone(Assignment a) {
        try {
            var field = Assignment.class.getDeclaredField("done");
            field.setAccessible(true);
            return (boolean) field.get(a);
        } catch (Exception e) { return false; }
    }
}
