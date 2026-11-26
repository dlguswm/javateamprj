package teamprj;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

// 과제 탭 화면: 강의 필터 + 과제 표(제출 체크박스) + 입력칸 + 버튼 + 제출 진행률
public class AssignmentPanel extends JPanel {
    private CourseManager courseManager;
    private AssignmentManager assignmentManager;
    private int todayMonth;
    private int todayDay;

    private DefaultTableModel model;
    private JTable table;
    private ArrayList<Assignment> shownList = new ArrayList<>();   // 지금 표에 보이는 과제들 (표의 줄 순서와 같음)
    private boolean refreshing = false;   // 표를 다시 그리는 중에는 체크박스 이벤트를 무시하기 위한 표시

    private JComboBox<Object> filterBox = new JComboBox<>();   // 위쪽: "전체" 또는 강의 선택
    private JComboBox<Course> courseBox = new JComboBox<>();   // 아래쪽: 과제를 등록할 강의
    private JTextField titleField = new JTextField(12);
    private JTextField dateField = new JTextField(5);
    private JProgressBar progressBar = new JProgressBar();

    public AssignmentPanel(CourseManager courseManager, AssignmentManager assignmentManager,
                           int todayMonth, int todayDay) {
        this.courseManager = courseManager;
        this.assignmentManager = assignmentManager;
        this.todayMonth = todayMonth;
        this.todayDay = todayDay;

        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 1. 위: 강의별 보기(조건 검색)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(new JLabel("강의별 보기"));
        topPanel.add(filterBox);
        topPanel.add(new JLabel("   오늘 " + todayMonth + "/" + todayDay + " · 마감이 가까운 순"));
        add(topPanel, BorderLayout.NORTH);

        // 2. 가운데: 과제 표 (첫 칸은 체크박스)
        String[] columns = {"제출", "강의", "과제명", "마감일", "D-day"};
        model = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                return column == 0 ? Boolean.class : String.class;   // 0번 칸을 Boolean으로 두면 체크박스로 보임
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;   // 체크박스만 누를 수 있게
            }
        };
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(26);
        table.getColumnModel().getColumn(0).setMaxWidth(50);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 3. 아래: 입력칸, 버튼, 진행률
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.add(new JLabel("강의"));
        inputPanel.add(courseBox);
        inputPanel.add(new JLabel("과제명"));
        inputPanel.add(titleField);
        inputPanel.add(new JLabel("마감일(11/20)"));
        inputPanel.add(dateField);
        JButton addButton = new JButton("추가");
        JButton updateButton = new JButton("수정");
        JButton deleteButton = new JButton("삭제");
        inputPanel.add(addButton);
        inputPanel.add(updateButton);
        inputPanel.add(deleteButton);

        progressBar.setStringPainted(true);   // 막대 위에 글자 표시
        JPanel progressPanel = new JPanel(new BorderLayout(10, 0));
        progressPanel.add(new JLabel("제출 진행률"), BorderLayout.WEST);
        progressPanel.add(progressBar, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        bottomPanel.add(inputPanel);
        bottomPanel.add(progressPanel);
        add(bottomPanel, BorderLayout.SOUTH);

        // 4. 이벤트 연결
        addButton.addActionListener(e -> addAssignment());
        updateButton.addActionListener(e -> updateAssignment());
        deleteButton.addActionListener(e -> deleteAssignment());
        dateField.addActionListener(e -> addAssignment());   // 마감일 칸에서 Enter를 누르면 추가
        filterBox.addActionListener(e -> refresh());         // 강의를 바꾸면 그 강의 과제만 보기
        table.getSelectionModel().addListSelectionListener(e -> fillFields());

        // 체크박스를 누르면 그 과제의 제출 여부를 바꿈
        model.addTableModelListener(e -> {
            if (refreshing || e.getColumn() != 0) {
                return;
            }
            int row = e.getFirstRow();
            boolean done = (Boolean) model.getValueAt(row, 0);
            shownList.get(row).setDone(done);
            updateProgress();
        });

        table.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                    deleteAssignment();
                }
            }
        });

        refreshCourses();
    }

    // 강의 목록이 바뀌었을 수 있으니 콤보박스를 다시 채우고 표도 다시 그림 (과제 탭을 열 때마다 호출)
    public void refreshCourses() {
        Object selected = filterBox.getSelectedItem();
        refreshing = true;
        filterBox.removeAllItems();
        courseBox.removeAllItems();
        filterBox.addItem("전체");
        for (Course c : courseManager.getCourses()) {
            filterBox.addItem(c);
            courseBox.addItem(c);
        }
        if (selected instanceof Course && courseManager.getCourses().contains(selected)) {
            filterBox.setSelectedItem(selected);   // 보고 있던 강의가 남아 있으면 그대로 유지
        }
        refreshing = false;
        refresh();
    }

    // 과제 표를 다시 그림
    public void refresh() {
        if (refreshing) {
            return;
        }
        refreshing = true;
        Object selected = filterBox.getSelectedItem();
        Course filter = (selected instanceof Course) ? (Course) selected : null;   // "전체"면 null
        shownList = assignmentManager.getAssignmentList(filter, todayMonth, todayDay);

        model.setRowCount(0);
        for (Assignment a : shownList) {
            int dday = a.getDday(todayMonth, todayDay);
            model.addRow(new Object[]{a.isDone(), a.getCourse().getCourseName(), a.getTitle(),
                    a.getDueMonth() + "/" + a.getDueDay(), DdayCalculate.Dday(dday)});
        }
        refreshing = false;
        updateProgress();
    }

    // 지금 보이는 과제 중 제출한 비율을 진행률 막대에 표시
    private void updateProgress() {
        int total = shownList.size();
        int doneCount = 0;
        for (Assignment a : shownList) {
            if (a.isDone()) {
                doneCount++;
            }
        }
        if (total == 0) {
            progressBar.setValue(0);
            progressBar.setString("과제 없음");
            return;
        }
        int percent = doneCount * 100 / total;
        progressBar.setValue(percent);
        progressBar.setString(doneCount + " / " + total + " 제출 (" + percent + "%)");
    }

    // "11/20" 형식의 날짜를 {11, 20}으로 바꿈, 잘못된 날짜면 알림 후 null
    private int[] readDate() {
        String[] parts = dateField.getText().trim().split("/");
        try {
            if (parts.length == 2) {
                int month = Integer.parseInt(parts[0].trim());
                int day = Integer.parseInt(parts[1].trim());
                if (DdayCalculate.ValidDate(month, day)) {
                    return new int[]{month, day};
                }
            }
        } catch (NumberFormatException e) {
            // 아래에서 알림
        }
        showMessage("마감일을 11/20 형식으로 올바르게 입력하세요.");
        return null;
    }

    private void addAssignment() {
        Course course = (Course) courseBox.getSelectedItem();
        if (course == null) {
            showMessage("먼저 강의 탭에서 강의를 등록하세요.");
            return;
        }
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            showMessage("과제명을 입력하세요.");
            return;
        }
        int[] due = readDate();
        if (due == null) {
            return;
        }
        if (assignmentManager.addAssignment(course, title, due[0], due[1])) {
            refresh();
            titleField.setText("");
            dateField.setText("");
            titleField.requestFocus();
        } else {
            showMessage("같은 강의에 같은 마감일 과제가 이미 있습니다.");
        }
    }

    private void updateAssignment() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showMessage("수정할 과제를 표에서 선택하세요.");
            return;
        }
        String title = titleField.getText().trim();
        if (title.isEmpty()) {
            showMessage("과제명을 입력하세요.");
            return;
        }
        int[] due = readDate();
        if (due == null) {
            return;
        }
        Assignment assignment = shownList.get(row);
        if (assignmentManager.updateAssignment(assignment, title, due[0], due[1])) {
            refresh();
        } else {
            showMessage("같은 강의에 같은 마감일 과제가 이미 있습니다.");
        }
    }

    private void deleteAssignment() {
        int row = table.getSelectedRow();
        if (row == -1) {
            showMessage("삭제할 과제를 표에서 선택하세요.");
            return;
        }
        Assignment assignment = shownList.get(row);
        int answer = JOptionPane.showConfirmDialog(this,
                assignment.getTitle() + " 과제를 삭제할까요?", "과제 삭제", JOptionPane.YES_NO_OPTION);
        if (answer == JOptionPane.YES_OPTION) {
            assignmentManager.deleteAssignment(assignment);
            refresh();
        }
    }

    // 표에서 고른 과제의 정보를 입력칸에 채움
    private void fillFields() {
        int row = table.getSelectedRow();
        if (refreshing || row == -1 || row >= shownList.size()) {
            return;
        }
        Assignment a = shownList.get(row);
        courseBox.setSelectedItem(a.getCourse());
        titleField.setText(a.getTitle());
        dateField.setText(a.getDueMonth() + "/" + a.getDueDay());
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
