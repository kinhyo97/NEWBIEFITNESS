package org.example;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.Map;

import org.example.component.*;
import org.example.db.JdbcStatistics;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.DefaultCategoryDataset;


public class StatisticsPanelController {
    
    // 데이터 불러오는 static 변수들
    static JdbcStatistics jdbc = new JdbcStatistics();
    static Map<String, Integer> weekly = jdbc.fetchWorkoutCountByDay();


    public static void statistics_show(JPanel panel, App app) {
        panel.removeAll();
        panel.setLayout(new BorderLayout());  // flowLayout 에서 수정

        String[] info = jdbc.fetchBodyInfo();

        // BorderLayout.NORTH에 들어갈 전체 상단 박스
        JPanel middlePanel = new JPanel();
        middlePanel.setLayout(new BoxLayout(middlePanel, BoxLayout.Y_AXIS));
        middlePanel.setBackground(Color.BLACK);

        JLabel titleLabel = new JLabel("Statistics");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 35));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // 수직 중앙 정렬
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBackground(Color.BLACK);
        titleLabel.setOpaque(true);

        // 스크롤바
        JScrollPane pageScroll = new JScrollPane(middlePanel);
        pageScroll.setBorder(null);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);

        middlePanel.add(Box.createVerticalStrut(20));

        // 상단 제목 배치
        middlePanel.add(Box.createVerticalStrut(20));
        middlePanel.add(titleLabel);
        middlePanel.add(Box.createVerticalStrut(30));

        // 신체 정보 패널
        middlePanel.add(createBodyInfoPanel());
        middlePanel.add(Box.createVerticalStrut(30));
        org.example.UIUtils.addTitleLabel(middlePanel, "운동 목표 달성률", 30, Color.WHITE);

        // 원형 프로그레스 박스
        JPanel circleWrap = new JPanel();
        circleWrap.setOpaque(true);
        circleWrap.setBackground(Color.BLACK);
        circleWrap.setLayout(new BorderLayout());
        circleWrap.setMaximumSize(new Dimension(300, 300));

        circleWrap.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 여유 여백

        CircularProgressBar progressBar = new CircularProgressBar();
        progressBar.setPreferredSize(new Dimension(300, 300));
        progressBar.setProgress(95);
        circleWrap.add(progressBar, BorderLayout.CENTER);

        middlePanel.add(Box.createVerticalStrut(20));
        middlePanel.add(circleWrap);
        middlePanel.add(Box.createVerticalStrut(20));
        circleWrap.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 종합 분석 패널
        middlePanel.add(createSummaryPanel());
        middlePanel.add(Box.createVerticalStrut(20));

        panel.add(pageScroll, BorderLayout.CENTER); // 스크롤바


        SwingUtilities.invokeLater(() -> pageScroll.getVerticalScrollBar().setValue(0));

        // 마우스 이벤트 처리 시 재배치, 변화 알림 및 재 페인팅
        panel.revalidate();
        panel.repaint();
    }

    // 신체 정보 패널
    private static JPanel createBodyInfoPanel() {
        RoundedPanel1 roundedBodyInfoPanel = new RoundedPanel1(50);
        roundedBodyInfoPanel.setBackground(Color.BLACK);
        roundedBodyInfoPanel.setLayout(new GridBagLayout());  // flowLayout -> 수직 정렬 기준 무조건 상단.

        JdbcStatistics jdbcStat = new JdbcStatistics();
        String[] info = jdbcStat.fetchBodyInfo();

        StatBox weightBox = new StatBox("체중(kg)", info[0]);
        StatBox fatBox = new StatBox("체지방률(%)", info[1]);
        StatBox muscleBox = new StatBox("근육량(kg)", info[2]);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        roundedBodyInfoPanel.add(weightBox, gbc);
        gbc.gridx = 1;
        roundedBodyInfoPanel.add(fatBox, gbc);
        gbc.gridx = 2;
        roundedBodyInfoPanel.add(muscleBox, gbc);

        return roundedBodyInfoPanel;
    }

    // 종합 분석 감싸는 박스 패널 추가

    private static JPanel createSummaryPanel() {

        JPanel wrapper = new JPanel();
        wrapper.setBackground(Color.BLACK);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        RoundedPanel1 box = new RoundedPanel1(30);

        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(new Color(141, 196, 244));
        box.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.setMaximumSize(new Dimension(350, 1500));

        addLabeledText(box,"운동 습관 분석", 24, Color.BLACK, 6);
        addLabeledText(box,"(한 주 기준)", 12, Color.BLACK, 10);
        box.add(Box.createVerticalStrut(10));

        Map<String, Integer> workoutByDay = weekly;  // static 변수
        String timeText = getTotalAcitivityTimeText(workoutByDay);

        JLabel totalLabel = fieldViewText(timeText, 27, Color.BLACK);
        box.add(totalLabel);

        WeeklyChartPanel chartPanel = new WeeklyChartPanel();

        box.add(Box.createVerticalStrut(10));
        box.add(chartPanel);

        // 코드 너무 길어지는 것 같아서 메소드 리펙토링함 (addLabeledText)
        box.add(Box.createVerticalStrut(10));
        addLabeledText(box, "총 소모 칼로리", 20, Color.BLACK, 20);
        
        // 총 칼로리 불러오기


        JLabel totalCalories = fieldViewText(String.valueOf(jdbc.totalCalories()) + " kcal", 40, Color.BLACK);
        box.add(totalCalories);

        box.add(Box.createVerticalStrut(20));
        box.add(new LineSetting(1));
        box.add(Box.createVerticalStrut(5));
        addLabeledText(box, "체지방률, 근육량 변화 요약", 20, Color.BLACK, 5);
        addLabeledText(box, "(지난 주 대비)", 20, Color.DARK_GRAY, 5);
        JLabel fatmass = fieldViewText( "체지방률 : " + String.valueOf(jdbc.changeFatMass()) + " % 감소", 30, Color.BLACK);
        box.add(fatmass);

        JLabel muscleMass = fieldViewText("근육량 : " + String.valueOf(jdbc.changeMuscleMass()) + " kg 증가", 30, Color.BLACK);
        box.add(muscleMass);

        box.add(Box.createVerticalStrut(20));
        box.add(new LineSetting(1));
        box.add(Box.createVerticalStrut(5));
        addLabeledText(box, "한 줄 코멘트", 20, Color.BLACK, 10);

        box.add(Box.createVerticalStrut(10));

        JTextArea oneCommentLine = CommentLine("너무너무 훌륭해요!\n지금까지 꾸준히 운동하셨네요!\n");  // JTextArea - \n 사용을 위해
        oneCommentLine.setOpaque(false);

        box.add(Box.createVerticalStrut(20));
        box.add(oneCommentLine);
        box.add(Box.createVerticalStrut(10));
        // LineSetting 객체 : 구분선

        wrapper.add(box);
        return wrapper;
    }
    private static void addLabeledText(JPanel panel, String text, int fontSize, Color color, int spacingBefore) {
        panel.add(Box.createVerticalStrut(spacingBefore));
        panel.add(org.example.UIUtils.createTitleLabel(text, fontSize, color));
    }

    public static JLabel fieldViewText(String text, int fontSize, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(new Font("Malgun Gothic", Font.BOLD, fontSize));

        return label;
    }

    public static String getTotalAcitivityTimeText(Map<String, Integer> mapDay) {
        int totalActivityTimes = 0;

        JdbcStatistics jdbcTime = new JdbcStatistics();
        Map<String, Integer> dataDayTimeCounts = jdbcTime.fetchWorkoutCountByDay();


        for (Map.Entry<String, Integer> entry : dataDayTimeCounts.entrySet()) {
            totalActivityTimes += entry.getValue();
        }

        int hours = totalActivityTimes / 60;
        int minutes = totalActivityTimes % 60;

        return String.format("총 운동 시간 : %d시간 %d분", hours, minutes);
    }

    // JTextArea -> 자동 줄바꿈 가능!!!
    public static JTextArea CommentLine(String comm) {
        JTextArea comment = new JTextArea(comm);
        comment.setFont(new Font("Malgun Gothic", Font.BOLD, 22));
        comment.setLineWrap(true);
        comment.setBackground(null);

        comment.setMargin(new Insets(0, 20, 0, 0));

        return comment;
    }
}

// 위의 신체 정보 블록 클래스

class StatBox extends JPanel {

    JLabel valueLabel;

    public StatBox(String title, String value) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setOpaque(false);

        RoundedPanel1 roundedStatBox = new RoundedPanel1(20);
        roundedStatBox.setLayout(new BoxLayout(roundedStatBox, BoxLayout.Y_AXIS));
        roundedStatBox.setBackground(new Color(141, 196, 244, 255));
        roundedStatBox.setOpaque(false);

        Dimension size = new Dimension(100, 100);
        roundedStatBox.setPreferredSize(size);
        roundedStatBox.setMaximumSize(size); // 반드시 함께 설정하기

        JLabel titleLabel = org.example.UIUtils.createTitleLabel(title, 14, Color.BLACK);
        valueLabel = org.example.UIUtils.createTitleLabel(value, 35, Color.BLACK);

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        roundedStatBox.add(Box.createVerticalStrut(10));
        roundedStatBox.add(titleLabel);
        roundedStatBox.add(Box.createVerticalStrut(5));
        roundedStatBox.add(valueLabel);

        add(roundedStatBox);
    }

    // 신체 정보 바꾸고 싶을 때 쓰는 메소드
    public void updateValue(String newValue) {
        valueLabel.setText(newValue);
    }
}

class WeeklyChartPanel extends JPanel {
    public WeeklyChartPanel() {
        setLayout(new BorderLayout());

        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        String[] daysKorean = {"일", "월", "화", "수", "목", "금", "토"};

        // 데이터셋 생성(요일 및 운동 횟수)
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        JdbcStatistics jdbcStatWeekDayCounts = new JdbcStatistics();

        Map<String, Integer> dataDayTimeCounts = jdbcStatWeekDayCounts.fetchWorkoutCountByDay();

        for (int i = 0; i < days.length; i++) {
            int count = dataDayTimeCounts.getOrDefault(days[i], 0);
            dataset.addValue(count, "운동 시간", daysKorean[i]);
        }

        // 꺾은선 차트
        JFreeChart lineChart = ChartFactory.createLineChart(
                "요일별 운동 시간",
                "요일",  // X축 레이블
                "활동 시간",  // Y축 레이블
                dataset,  // 그래프에 들어갈 데이터셋
                PlotOrientation.VERTICAL,  // 차트 방향 (세로 꺾은선)
                false, // 범례 표시 여부
                true,  // 툴팁 표시 여부(마우스 올릴 시 값 설명)
                false  // URL 링크 여부
        );

        lineChart.setBackgroundPaint(new Color(185, 228, 255));  // 차트 배경색

        ChartPanel chartPn = new ChartPanel(lineChart);
        chartPn.setPreferredSize(new Dimension(350, 200));
        chartPn.setBackground(Color.WHITE);  // 차트 패널 안쪽 색
        chartPn.setMaximumSize(new Dimension(350, 350));

        Font font = new Font("Malgun Gothic", Font.PLAIN, 12);

        CategoryPlot plot = lineChart.getCategoryPlot();

        plot.setInsets(new RectangleInsets(10, 10, 10, 10));  // 안쪽 여백 확보

        plot.setBackgroundPaint(Color.WHITE);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        plot.getDomainAxis().setLabelFont(font);  // x축 레이블
        plot.getDomainAxis().setTickLabelFont(font);  // x축 눈금
        plot.getRangeAxis().setLabelFont(font);  // y축 레이블
        plot.getRangeAxis().setTickLabelFont(font);  // y축 눈금

        lineChart.getTitle().setFont(new Font("Malgun Gothic", Font.BOLD, 14));

        add(chartPn, BorderLayout.CENTER);
    }
}


// 구분선 긋는 클래스

class LineSetting extends JPanel {
    public LineSetting(int thickness) {
        setBackground(Color.DARK_GRAY);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, thickness)); // 가로 전체로 확장
        setPreferredSize(new Dimension(400, thickness));
        setBorder(null);  // 여백 제거
    }
    
    @Override
    public Insets getInsets() {
        return new Insets(0, 0, 0, 0); // 내부 여백 제거
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.drawLine(0, 0 ,getWidth() - 1, 0);  // 수평 직선
    }
}