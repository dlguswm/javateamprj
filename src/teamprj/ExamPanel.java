package teamprj;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.function.Supplier;

public class ExamPanel extends JPanel {
    private final CourseManager courseManager;
    private final ExamManager examManager;

    //private final Supplier<int[]> today;   // {월, 일}
    private final int todayMonth;
    private final int todayDay;
    private final Runnable onChanged;      // 시험이 바뀌면 다른 탭 갱신용

    private final DefaultTableModel model = new DefaultTableModel(
            new String[]{"번호", "강의명", "학년/학기", "시험일", "D-day", "상태"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
        //표의 데이터를 담는 통
    };
    private final JTable table = new JTable(model);
    private final JComboBox<Course> courseBox = new JComboBox<>();
    private final JSpinner monthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
    private final JSpinner daySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 31, 1));
    //daySpinner는 나중에 validDate로 한 번 더 검사.


    public ExamPanel(CourseManager cm, ExamManager em, int todayMonth, int todayDay, Runnable onChanged) {
        this.courseManager = cm;
        this.examManager = em;
        this.todayMonth = todayMonth;
        this.todayDay = todayDay;
        this.onChanged = onChanged;

        setLayout(new BorderLayout(8, 8));
        //보더레이아웃으로 가운데에 표, 아래쪽에 입력 폼
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(24);

        //표 행을 클릭하면 밑에 폼이 그 클릭된 애 정보로 자동 바뀌기
        table.getSelectionModel().addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;
            int row = table.getSelectedRow();
            Exam exam = examManager.getExamByIndex(row);
            if (exam != null) {
                courseBox.setSelectedItem(exam.getCourse());
                monthSpinner.setValue(exam.getExamMonth());
                daySpinner.setValue(exam.getExamDay());
            }
        });
        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel form = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        //폼은 플로우레이아웃으로 라벨, 콤보박스, 스피너, 버튼을 가로로 나열
        form.add(new JLabel("강의"));
        courseBox.setPrototypeDisplayValue(new Course("가나다라마바사아자차카타파하", 3, 1, 1));
        form.add(courseBox);
        form.add(new JLabel("시험일"));
        form.add(monthSpinner);
        form.add(new JLabel("월"));
        form.add(daySpinner);
        form.add(new JLabel("일"));

        JButton addBtn = new JButton("추가");
        JButton updateBtn = new JButton("날짜 수정");
        JButton deleteBtn = new JButton("삭제");
        addBtn.addActionListener(e -> onAdd());
        updateBtn.addActionListener(e -> onUpdate());
        deleteBtn.addActionListener(e -> onDelete());
        form.add(addBtn);
        form.add(updateBtn);
        form.add(deleteBtn);
        add(form, BorderLayout.SOUTH);

        refresh();
    }

    private void onAdd() { //시험 추가
        Course course = (Course) courseBox.getSelectedItem();
        if (course == null) {
            warn("먼저 강의 탭에서 강의를 등록해 주세요.");
            return;
        }
        int examMonth= (Integer) monthSpinner.getValue();
        int examDay = (Integer) daySpinner.getValue();
        if (!DdayCalculate.ValidDate(examMonth, examDay)) {
            warn("존재하지 않는 날짜입니다.");
            return;
        }
        if (!examManager.addExam(course, examMonth, examDay)) {
            warn("이미 같은 날짜의 시험이 등록되어 있습니다.");
            return;
        }
        changed();
    }

    private void onUpdate() { //시험 날짜 수정
        Exam exam = examManager.getExamByIndex(table.getSelectedRow());
        if (exam == null) {
            warn("수정할 시험을 표에서 선택해 주세요.");
            return;
        }
        int examMonth = (Integer) monthSpinner.getValue();
        int examDay = (Integer) daySpinner.getValue();
        if (!DdayCalculate.ValidDate(examMonth, examDay)) {
            warn("존재하지 않는 날짜입니다.");
            return;
        }
        for (Exam e : examManager.getExams()) {
            if (e != exam && e.getCourse() == exam.getCourse()
                    && e.getExamMonth() == examMonth && e.getExamDay() == examDay) {
                warn("이미 같은 날짜의 시험이 등록되어 있습니다.");
                return;
            }//중복검사 중복 조건은 ->강의명&&날짜 같. e!=exam은 자기 자신 제외하고 중복 검사
        }
        exam.setExamMonth(examMonth);
        exam.setExamDay(examDay);
        changed();
    }

    private void onDelete() {
        int row = table.getSelectedRow();
        if (examManager.getExamByIndex(row) == null) {
            warn("삭제할 시험을 표에서 선택해 주세요.");
            return;
        }
        if (JOptionPane.showConfirmDialog(this, "선택한 시험을 삭제할까요?", "삭제 확인",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            examManager.removeExam(row);
            changed();
        }
    }

    private void changed() {
        refresh(); //내 화면 갱신
        if (onChanged != null) onChanged.run(); //다른 탭 갱신
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "알림", JOptionPane.WARNING_MESSAGE);
    }

    // 강의,시험 목록을 다시 읽어 화면 갱신 (다른 탭에서도 호출 가능)
    public void refresh() {
        Object selected = courseBox.getSelectedItem();
        courseBox.removeAllItems();
        for (Course c : courseManager.getCourses()) courseBox.addItem(c);
        if (selected != null) courseBox.setSelectedItem(selected);

        model.setRowCount(0);
        List<Exam> exams = examManager.getExams();
        int num = 1;
        for (Exam e : exams) {
            int dday = e.getDday(todayMonth, todayDay);
            model.addRow(new Object[]{
                    num++,
                    e.getCourseName(),
                    e.getGrade() + "학년 " + e.getSemester() + "학기",
                    e.getExamMonth() + "/" + e.getExamDay(),
                    DdayCalculate.Dday(dday),
                    dday <= 0 ? "응시완료" : "예정"});
        }
    }
}
