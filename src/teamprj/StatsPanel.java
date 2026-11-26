package teamprj;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** 통계 탭: 학기별 평점평균 막대 차트 */
public class StatsPanel extends JPanel {
    private final ScoreManager scoreManager;
    private final JLabel summary = new JLabel(" ");
    private final Chart chart = new Chart();
    private final JComboBox<String> modeBox = new JComboBox<>(new String[]{"학기별", "학년별"});

    public StatsPanel(ScoreManager sm) {
        this.scoreManager = sm;
        setLayout(new BorderLayout(8, 8));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        summary.setFont(summary.getFont().deriveFont(Font.BOLD, 15f));
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        top.add(new JLabel("보기"));
        top.add(modeBox);
        top.add(summary);
        modeBox.addActionListener(e -> refresh());   // 선택이 바뀌면 차트 다시 계산
        add(top, BorderLayout.NORTH);
        add(chart, BorderLayout.CENTER);
        refresh();
    }

    public void refresh() {
        boolean byGrade = modeBox.getSelectedIndex() == 1;   // 0 = 학기별, 1 = 학년별
        TreeMap<Integer, List<Score>> bySemester = new TreeMap<>();
        for (Score s : scoreManager.getScores().values()) {
            int key = byGrade ? s.getGrade() : s.getGrade() * 100 + s.getSemester();
            bySemester.computeIfAbsent(key, k -> new ArrayList<>()).add(s);
        }
        List<String> labels = new ArrayList<>();
        List<Double> gpas = new ArrayList<>();
        List<Integer> credits = new ArrayList<>();
        for (Map.Entry<Integer, List<Score>> e : bySemester.entrySet()) {
            labels.add(byGrade
                    ? e.getKey() + "학년"
                    : (e.getKey() / 100) + "학년 " + (e.getKey() % 100) + "학기");
            gpas.add(scoreManager.gpaOf(e.getValue()));
            credits.add(scoreManager.totalCreditOf(e.getValue()));
        }
        List<Score> all = new ArrayList<>(scoreManager.getScores().values());
        summary.setText(all.isEmpty()
                ? "등록된 성적이 없습니다."
                : String.format("전체: 총 %d학점 | 평점평균 %.2f / 4.5",
                scoreManager.totalCreditOf(all), scoreManager.gpaOf(all)));
        chart.setData(labels, gpas, credits);
    }

    private static class Chart extends JPanel {
        private List<String> labels = new ArrayList<>();
        private List<Double> gpas = new ArrayList<>();
        private List<Integer> credits = new ArrayList<>();
        private static final double MAX = 4.5;

        Chart() { setBackground(Color.WHITE); setPreferredSize(new Dimension(600, 340)); }

        void setData(List<String> l, List<Double> g, List<Integer> c) {
            labels = l; gpas = g; credits = c;
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g0) {
            super.paintComponent(g0);
            Graphics2D g = (Graphics2D) g0.create();
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            if (gpas.isEmpty()) {
                String msg = "성적 데이터가 없습니다.";
                FontMetrics fm = g.getFontMetrics();
                g.setColor(Color.GRAY);
                g.drawString(msg, (getWidth() - fm.stringWidth(msg)) / 2, getHeight() / 2);
                g.dispose();
                return;
            }

            int left = 50, right = 20, top = 30, bottom = 55;
            int w = getWidth() - left - right;
            int h = getHeight() - top - bottom;
            FontMetrics fm = g.getFontMetrics();

            // 눈금선 (0.0 ~ 4.5, 0.5 간격)
            for (int i = 0; i <= 9; i++) {
                double v = i * 0.5;
                int y = top + h - (int) (h * v / MAX);
                g.setColor(new Color(225, 225, 225));
                g.drawLine(left, y, left + w, y);
                g.setColor(Color.DARK_GRAY);
                String t = String.format("%.1f", v);
                g.drawString(t, left - 8 - fm.stringWidth(t), y + fm.getAscent() / 2 - 1);
            }
            g.setColor(Color.DARK_GRAY);
            g.drawLine(left, top, left, top + h);
            g.drawLine(left, top + h, left + w, top + h);

            int n = gpas.size();
            int slot = w / n;
            int barW = Math.min(70, (int) (slot * 0.6));
            for (int i = 0; i < n; i++) {
                int bh = (int) (h * gpas.get(i) / MAX);
                int x = left + i * slot + (slot - barW) / 2;
                int y = top + h - bh;
                g.setColor(new Color(66, 133, 244));
                g.fillRoundRect(x, y, barW, bh, 6, 6);

                g.setColor(Color.BLACK);
                String val = String.format("%.2f", gpas.get(i));
                g.drawString(val, x + (barW - fm.stringWidth(val)) / 2, y - 5);

                String lab = labels.get(i);
                g.drawString(lab, x + (barW - fm.stringWidth(lab)) / 2, top + h + 18);
                String cr = credits.get(i) + "학점";
                g.setColor(Color.GRAY);
                g.drawString(cr, x + (barW - fm.stringWidth(cr)) / 2, top + h + 34);
            }
            g.dispose();
        }
    }
}
