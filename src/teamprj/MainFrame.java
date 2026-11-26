package teamprj;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Scanner;

public class MainFrame extends JFrame {
    private CourseManager courseManager;
    private AssignmentManager assignmentManager;
    private ExamManager examManager;
    private ScoreManager scoreManager;
    private DataManager dataManager;

    private int todayMonth;
    private int todayDay;

    private JTabbedPane tabbedPane;
    //private JTextField dateField;
    private JLabel dateStatusLabel;
    private JLabel alertLabel;

    private CoursePanel coursePanel;
    private AssignmentPanel assignmentPanel;
    private ExamPanel examPanel;
    private ScorePanel scorePanel;
    private StatsPanel statsPanel;


    public MainFrame(String title) {
        super(title);
        initData();

        this.setSize(850, 600);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dataManager.saveData(courseManager, assignmentManager, examManager, scoreManager);
                JOptionPane.showMessageDialog(MainFrame.this,
                        "모든 데이터(강의/과제/시험/성적)가 파일에 저장되었습니다.",
                        "저장 완료", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        });

        createMenu();
        initLayout();
        updateAlertLabel();

        this.setVisible(true);
        showUpcomingAlertPopup();
    }

    private void initData() {
        Scanner dummyScanner = new Scanner(System.in);
        courseManager = new CourseManager(dummyScanner);
        assignmentManager = new AssignmentManager(courseManager, dummyScanner);
        examManager = new ExamManager(courseManager, dummyScanner);
        scoreManager = new ScoreManager(examManager, dummyScanner);
        dataManager = new DataManager();

        dataManager.loadData(courseManager, assignmentManager, examManager, scoreManager);
        initTodayDate();
        assignmentManager.removeExpiredAssignment(todayMonth, todayDay);
    }

    private void initTodayDate() {
        while (true) {
            String input = JOptionPane.showInputDialog(this,
                    "오늘 날짜를 입력하세요 (ex. 11/26)",
                    "기준 날짜 입력",
                    JOptionPane.QUESTION_MESSAGE);

            if (input == null) {
                System.exit(0);
            }

            try {
                String[] parts = input.trim().split("/");
                int m = Integer.parseInt(parts[0]);
                int d = Integer.parseInt(parts[1]);
                if (DdayCalculate.ValidDate(m, d)) {
                    todayMonth = m;
                    todayDay = d;
                    break;
                }
            } catch (Exception ignored) {
            }

            JOptionPane.showMessageDialog(this,
                    "올바른 날짜 형식(월/일)으로 다시 입력해주세요.",
                    "입력 오류",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createMenu() {
        JMenuBar mb = new JMenuBar();

        JMenu fileMenu = new JMenu("파일(File)");
        JMenuItem saveItem = new JMenuItem("저장하기");
        JMenuItem exitItem = new JMenuItem("저장 후 종료");

        saveItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dataManager.saveData(courseManager, assignmentManager, examManager, scoreManager);
                JOptionPane.showMessageDialog(MainFrame.this,
                        "모든 데이터가 파일에 저장되었습니다.",
                        "저장 완료", JOptionPane.INFORMATION_MESSAGE);
            }
        });

        exitItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dataManager.saveData(courseManager, assignmentManager, examManager, scoreManager);
                JOptionPane.showMessageDialog(MainFrame.this,
                        "모든 데이터가 저장되었습니다. 프로그램을 종료합니다.",
                        "종료", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        });

        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu viewMenu = new JMenu("탭 이동(Menu)");
        String[] menuTitles = {"1. 강의 관리", "2. 과제 관리", "3. 시험 관리", "4. 학점 관리", "5. 통계"};

        for (int i = 0; i < menuTitles.length; i++) {
            JMenuItem item = new JMenuItem(menuTitles[i]);
            final int tabIndex = i;
            item.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    tabbedPane.setSelectedIndex(tabIndex);
                }
            });
            viewMenu.add(item);
        }

        mb.add(fileMenu);
        mb.add(viewMenu);
        this.setJMenuBar(mb);
    }

    private void initLayout() {
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout(10, 10));

        // [상단 NORTH] 타이틀 및 기준 날짜 수정 바
        JPanel northPanel = new JPanel(new BorderLayout());
        northPanel.setBackground(new Color(240, 244, 248));

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        titlePanel.setBackground(new Color(240, 244, 248));
        JLabel titleLabel = new JLabel("대학생 학점 · 과제 트래커");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 16));
        titlePanel.add(titleLabel);

        JPanel datePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        datePanel.setBackground(new Color(240, 244, 248));

        dateStatusLabel = new JLabel("현재 기준일: " + todayMonth + "월 " + todayDay + "일");

        datePanel.add(dateStatusLabel);

        northPanel.add(titlePanel, BorderLayout.WEST);
        northPanel.add(datePanel, BorderLayout.EAST);
        contentPane.add(northPanel, BorderLayout.NORTH);

        // [중앙 CENTER] JTabbedPane으로 4개 탭 구성
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("맑은 고딕", Font.BOLD, 13));

        //coursePanel = new CoursePanel()

//-==============
        coursePanel = new CoursePanel(courseManager, assignmentManager, examManager, scoreManager);
        assignmentPanel = new AssignmentPanel(courseManager, assignmentManager, todayMonth, todayDay);
        examPanel = new ExamPanel(courseManager, examManager, todayMonth, todayDay, this::onExamChanged);
        scorePanel = new ScorePanel(scoreManager, todayMonth, todayDay, () -> statsPanel.refresh());
        statsPanel = new StatsPanel(scoreManager);

        // 팀원 A, B 패널 완성 시 아래 createDummyPanel(...) 대신 실제 패널 객체를 넣으세요.
        // 예: tabbedPane.addTab("1. 강의 관리", new CoursePanel(...));
        tabbedPane.addTab("1. 강의 관리", coursePanel);
        tabbedPane.addTab("2. 과제 관리", assignmentPanel);
        tabbedPane.addTab("3. 시험 관리", examPanel);
        tabbedPane.addTab("4. 학점 관리", scorePanel);
        tabbedPane.addTab("5. 통계", statsPanel);

        //=======
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                coursePanel.refresh();
                assignmentPanel.refresh();
                examPanel.refresh();
                scorePanel.refresh();
                statsPanel.refresh();
                updateAlertLabel();
            }
        });

        contentPane.add(tabbedPane, BorderLayout.CENTER);

        // [하단 SOUTH] 마감 임박 알림 상태바
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        alertLabel = new JLabel(" ");
        alertLabel.setFont(new Font("맑은 고딕", Font.BOLD, 13));
        southPanel.add(alertLabel);
        contentPane.add(southPanel, BorderLayout.SOUTH);

    }

    private void showUpcomingAlertPopup() {
        String msg = getUpcomingMessage("\n");
        if (!msg.equals("")) {
            JOptionPane.showMessageDialog(this,
                    "3일 이내 마감 임박 일정이 있습니다!\n\n" + msg,
                    "마감 임박 알림",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateAlertLabel() {
        String msg = getUpcomingMessage("  ");
        if (msg.equals("")) {
            alertLabel.setText("3일 이내 마감 임박 일정이 없습니다.");
            alertLabel.setForeground(Color.DARK_GRAY);
        } else {
            alertLabel.setText("※ 마감 임박 알림: " + msg);
            alertLabel.setForeground(Color.RED);
        }
    }

    private String getUpcomingMessage(String separator) {
        String msg = "";
        for (Assignment a : assignmentManager.getAssignments()) {
            int dday = a.getDday(todayMonth, todayDay);
            if (!a.isDone() && dday >= 0 && dday <= 3) {
                msg += "• [과제] " + a.getCourse().getCourseName() + " - " + a.getTitle()
                        + " (" + DdayCalculate.Dday(dday) + ")" + separator;
            }
        }
        for (Exam e : examManager.getExams()) {
            int dday = e.getDday(todayMonth, todayDay);
            if (dday >= 0 && dday <= 3) {
                msg += "• [시험] " + e.getCourseName()
                        + " (" + DdayCalculate.Dday(dday) + ")" + separator;
            }
        }
        return msg;
    }

    //==============
    private void onExamChanged(){
        scorePanel.refresh();
        statsPanel.refresh();
        updateAlertLabel();
    }

    //private JPanel createDummyPanel(String text) {
    //    JPanel p = new JPanel(new FlowLayout());
    //    JLabel label = new JLabel(text);
    //    label.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
    //    p.add(label);
    //    return p;
    //}

/*
    private class DateActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                String[] parts = dateField.getText().trim().split("/");
                int m = Integer.parseInt(parts[0]);
                int d = Integer.parseInt(parts[1]);
                if (DdayCalculate.ValidDate(m, d)) {
                    todayMonth = m;
                    todayDay = d;
                    dateStatusLabel.setText("현재 기준일: " + todayMonth + "월 " + todayDay + "일");
                    assignmentManager.removeExpiredAssignment(todayMonth, todayDay);
                    updateAlertLabel();

                    //==========
                    assignmentPanel.refresh();
                    examPanel.refresh();
                    scorePanel.refresh();
                    statsPanel.refresh();
                    //=============

                    showUpcomingAlertPopup();
                } else {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "존재하지 않는 날짜입니다. 다시 입력해주세요.",
                            "입력 오류", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(MainFrame.this,
                        "날짜 형식이 올바르지 않습니다 (ex. 11/26).",
                        "형식 오류", JOptionPane.ERROR_MESSAGE);
            }
        }

    } */



    public static void main(String[] args) {
        new MainFrame("7조 대학생 학점 · 과제 트래커");
    }
}