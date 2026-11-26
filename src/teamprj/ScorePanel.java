package teamprj;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ScorePanel extends JPanel {
    private final ScoreManager scoreManager;
    private final Supplier<int[]> today;
    private final Runnable onChanged;

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"번호", "강의명", "학년/학기", "학점", "받은 성적"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };
    private final JTable table = new JTable(model);
    private final List<Score> shown = new ArrayList<>(); //지금 표에 보이는 성적들의 목록!! 조건별검색그거때메

    private final JComboBox<String> gradeFilter = new JComboBox<>(new String[]{"전체 학년", "1학년", "2학년", "3학년", "4학년"});
    private final JComboBox<String> semesterFilter = new JComboBox<>(new String[]{"전체 학기", "1학기", "2학기"});
    private final JLabel summary = new JLabel(" ");

    private final JComboBox<Course> courseBox = new JComboBox<>();
    private final JSpinner scoreSpinner = new JSpinner(new SpinnerNumberModel(4.0, 0.0, 4.5, 0.5));

    public ScorePanel(ScoreManager sm, Supplier<int[]> today, Runnable onChanged) {
        this.scoreManager = sm;
        this.today = today;
        this.onChanged = onChanged;

        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        top.add(new JLabel("조회"));
        top.add(gradeFilter);
        top.add(semesterFilter);
        gradeFilter.addActionListener(e -> reloadTable());
        semesterFilter.addActionListener(e -> reloadTable());
        add(top, BorderLayout.NORTH);

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);
        // 표에서 줄을 클릭하면 아래 입력 폼이 그 성적의 강의/점수로 바뀜
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int row = table.getSelectedRow();
            if (row < 0 || row >= shown.size()) return;
            Score sc = shown.get(row);
            courseBox.setSelectedItem(sc.getCourse());
            scoreSpinner.setValue((double) sc.getScore());
        });
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new BorderLayout());
        summary.setFont(summary.getFont().deriveFont(Font.BOLD, 14f));
        summary.setBorder(BorderFactory.createEmptyBorder(0, 4, 6, 0));
        bottom.add(summary, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(2, 1));
        JPanel row1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        JPanel row2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        row1.add(new JLabel("성적 입력 가능 강의"));
        courseBox.setPrototypeDisplayValue(new Course("가나다라마바사아자차카타파하", 3, 1, 1));
        row1.add(courseBox);
        row2.add(new JLabel("성적(0.0~4.5)"));
        row2.add(scoreSpinner);
        JButton saveBtn = new JButton("입력/수정");
        JButton deleteBtn = new JButton("성적 삭제");
        saveBtn.addActionListener(e -> onSave());
        deleteBtn.addActionListener(e -> onDelete());
        row2.add(saveBtn);
        row2.add(deleteBtn);
        form.add(row1);
        form.add(row2);
        bottom.add(form, BorderLayout.SOUTH);
        add(bottom, BorderLayout.SOUTH);

        refresh();
    }

    private void onSave() { //성적 입력,수정 버튼
        Course course = (Course) courseBox.getSelectedItem();
        if (course == null) {
            JOptionPane.showMessageDialog(this, "성적을 입력할 수 있는(이미 시험을 치른) 강의가 없습니다.",
                    "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }
        float value = ((Number) scoreSpinner.getValue()).floatValue();
        boolean created = scoreManager.setScore(course, value);
        JOptionPane.showMessageDialog(this, created ? "성적이 등록되었습니다." : "성적이 수정되었습니다.");
        changed();
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (row < 0 || row >= shown.size()) {
            JOptionPane.showMessageDialog(this, "삭제할 성적을 표에서 선택해 주세요.", "알림", JOptionPane.WARNING_MESSAGE);
            return;
        }
        scoreManager.removeScore(shown.get(row).getCourse());
        changed();
    }

    private void changed() {
        refresh();
        if (onChanged != null) onChanged.run();
    }

    private void reloadTable() {
        int grade = gradeFilter.getSelectedIndex();     // 0 = 전체
        int semester = semesterFilter.getSelectedIndex();  // 0 = 전체
        shown.clear();
        model.setRowCount(0);
        int num = 1;
        for (Score sc : scoreManager.getScores().values()) {
            if (grade != 0 && sc.getGrade() != grade) continue;
            if (semester != 0 && sc.getSemester() != semester) continue;
            shown.add(sc);
            model.addRow(new Object[]{num++, sc.getCourse().getCourseName(),
                    sc.getGrade() + "학년 " + sc.getSemester() + "학기", sc.getCredit(), sc.getScore()});
        }
        summary.setText(
                shown.isEmpty()
                ? "표시할 성적이 없습니다."
                : String.format("총 %d학점 | 평점평균 : %.2f",
                scoreManager.totalCreditOf(shown), scoreManager.gpaOf(shown)));
    }

    public void refresh() {
        int[] t = today.get();
        Object selected = courseBox.getSelectedItem();
        courseBox.removeAllItems();
        for (Course c : scoreManager.getGradableCourses(t[0], t[1])) courseBox.addItem(c);
        if (selected != null) courseBox.setSelectedItem(selected);
        reloadTable();
    }
}
