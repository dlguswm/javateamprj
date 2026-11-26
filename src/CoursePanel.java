package teamprj;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

// 강의 탭 화면: 강의 목록 표 + 입력칸 + 추가/수정/삭제 버튼
public class CoursePanel extends JPanel {
    private CourseManager courseManager;
    private AssignmentManager assignmentManager;
    private ExamManager examManager;
    private ScoreManager scoreManager;

    private DefaultTableModel model;   // 표에 들어갈 데이터
    private JTable table;              // 강의 목록 표

    private JTextField nameField = new JTextField(10);
    private JTextField creditField = new JTextField(3);
    private JTextField gradeField = new JTextField(3);
    private JComboBox<Integer> semesterBox = new JComboBox<>(new Integer[]{1, 2});

    public CoursePanel(CourseManager courseManager, AssignmentManager assignmentManager,
                       ExamManager examManager, ScoreManager scoreManager) {
        this.courseManager = courseManager;
        this.assignmentManager = assignmentManager;
        this.examManager = examManager;
        this.scoreManager = scoreManager;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. 가운데: 강의 목록 표
        String[] columns = {"강의명", "학점", "학년", "학기"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;   // 표에서 직접 고치지 못하게 막고, 수정은 아래 입력칸으로 함
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);   // 한 줄만 선택
        table.setRowHeight(26);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 2. 아래: 입력칸과 버튼
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("강의명"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("학점"));
        inputPanel.add(creditField);
        inputPanel.add(new JLabel("학년"));
        inputPanel.add(gradeField);
        inputPanel.add(new JLabel("학기"));
        inputPanel.add(semesterBox);

        JButton addButton = new JButton("추가");
        JButton updateButton = new JButton("수정");
        JButton deleteButton = new JButton("삭제");
        JButton clearButton = new JButton("새로 입력");
        inputPanel.add(addButton);
        inputPanel.add(updateButton);
        inputPanel.add(deleteButton);
        inputPanel.add(clearButton);
        add(inputPanel, BorderLayout.SOUTH);

        // 3. 이벤트 연결
        addButton.addActionListener(e -> addCourse());
        updateButton.addActionListener(e -> updateCourse());
        deleteButton.addActionListener(e -> deleteCourse());
        clearButton.addActionListener(e -> clearFields());
        nameField.addActionListener(e -> addCourse());   // 강의명 칸에서 Enter를 누르면 추가

        // 표에서 줄을 고르면 그 강의 정보가 입력칸에 채워짐
        table.getSelectionModel().addListSelectionListener(e -> fillFields());

        // 표를 선택한 상태에서 Delete(맥은 delete=Backspace) 키를 누르면 삭제
        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    deleteCourse();
                }
            }
        });

        refresh();
    }

    // 강의 목록을 표에 다시 그림
    public void refresh() {
        model.setRowCount(0);   // 표 비우기
        for (Course c : courseManager.getCourses()) {
            model.addRow(new Object[]{c.getCourseName(), c.getCredit() + "학점",
                    c.getGrade() + "학년", c.getSemester() + "학기"});
        }
    }

    // 입력칸의 값을 확인해서 Course로 묶어 돌려줌, 잘못 입력했으면 알림 후 null
    private Course readInput() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            showMessage("강의명을 입력하세요.");
            return null;
        }
        try {
            int credit = Integer.parseInt(creditField.getText().trim());
            int grade = Integer.parseInt(gradeField.getText().trim());
            if (credit <= 0 || grade <= 0) {
                showMessage("학점과 학년은 1 이상으로 입력하세요.");
                return null;
            }
            int semester = (Integer) semesterBox.getSelectedItem();
            return new Course(name, credit, grade, semester);
        } catch (NumberFormatException e) {   // 숫자가 아닌 값을 넣었을 때
            showMessage("학점과 학년은 숫자로 입력하세요.");
            return null;
        }
    }

    private void addCourse() {
        Course input = readInput();
        if (input == null) {
            return;
        }
        boolean ok = courseManager.addCourse(input.getCourseName(), input.getCredit(),
                input.getGrade(), input.getSemester());
        if (ok) {
            refresh();
            clearFields();
        } else {
            showMessage("이미 같은 강의가 등록되어 있습니다.");
        }
    }

    private void updateCourse() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showMessage("수정할 강의를 표에서 선택하세요.");
            return;
        }
        Course input = readInput();
        if (input == null) {
            return;
        }
        boolean ok = courseManager.updateCourse(row, input.getCourseName(), input.getCredit(),
                input.getGrade(), input.getSemester());
        if (ok) {
            refresh();
            table.setRowSelectionInterval(row, row);   // 수정한 줄을 계속 선택해 둠
        } else {
            showMessage("이미 같은 강의가 등록되어 있습니다.");
        }
    }

    private void deleteCourse() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showMessage("삭제할 강의를 표에서 선택하세요.");
            return;
        }
        Course course = courseManager.getCourses().get(row);
        int answer = JOptionPane.showConfirmDialog(this,
                course.getCourseName() + " 강의를 삭제하면 이 강의의 과제·시험·성적도 함께 삭제됩니다.\n삭제할까요?",
                "강의 삭제", JOptionPane.YES_NO_OPTION);
        if (answer != JOptionPane.YES_OPTION) {
            return;
        }
        Course removed = courseManager.deleteCourse(row);
        assignmentManager.removeByCourse(removed);
        examManager.removeByCourse(removed);
        scoreManager.removeScore(removed);
        refresh();
        clearFields();
    }

    // 표에서 고른 강의의 정보를 입력칸에 채움
    private void fillFields() {
        int row = table.getSelectedRow();
        ArrayList<Course> courses = courseManager.getCourses();
        if (row == -1 || row >= courses.size()) {
            return;
        }
        Course c = courses.get(row);
        nameField.setText(c.getCourseName());
        creditField.setText(String.valueOf(c.getCredit()));
        gradeField.setText(String.valueOf(c.getGrade()));
        semesterBox.setSelectedItem(c.getSemester());
    }

    private void clearFields() {
        table.clearSelection();
        nameField.setText("");
        creditField.setText("");
        gradeField.setText("");
        semesterBox.setSelectedIndex(0);
        nameField.requestFocus();
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
