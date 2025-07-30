package org.example;

import javax.swing.*;
import java.awt.*;
import org.example.uiutils.UIUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.data.category.DefaultCategoryDataset;


public class StatisticsPanelController {
    public static void statistics_show(JPanel panel, App app) {
        panel.removeAll();
        panel.setLayout(new BorderLayout());  // flowLayout 에서 수정
        panel.setBackground(Color.BLACK);

        // BorderLayout.NORTH에 들어갈 전체 상단 박스
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        topPanel.setBackground(Color.BLACK);

        // UiUtil 변경할 것
        JLabel titleLabel = new JLabel("Statistics");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);  // 수직 중앙 정렬
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBackground(Color.BLACK);
        titleLabel.setOpaque(true);



        // 상단 제목 배치
        topPanel.add(Box.createVerticalStrut(20));
        topPanel.add(titleLabel);
        topPanel.add(Box.createVerticalStrut(30));
        // 신체 정보 패널
        topPanel.add(createBodyInfoPanel());
        topPanel.add(Box.createVerticalStrut(50));
        // 종합 분석 패널
        topPanel.add(createSummaryPanel());

        // 스크롤바
        JScrollPane pageScroll = new JScrollPane(topPanel);
        pageScroll.setBorder(null);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);

        panel.add(pageScroll, BorderLayout.CENTER); // 스크롤바

        // 마우스 이벤트 처리 시 재배치, 변화 알림 및 재 페인팅
        panel.revalidate();
        panel.repaint();
    }

    // 신체 정보 패널
    private static JPanel createBodyInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBackground(Color.BLACK);



        StatBox weightBox = new StatBox("체중(kg)", "63.4");
        StatBox fatBox = new StatBox("체지방률(%)", "18.2");
        StatBox muscleBox = new StatBox("근육량(kg)", "6.4");

        // 패널에 추가
        panel.add(Box.createHorizontalStrut(30));
        panel.add(weightBox);
        panel.add(Box.createHorizontalStrut(30));
        panel.add(fatBox);
        panel.add(Box.createHorizontalStrut(30));
        panel.add(muscleBox);
        panel.add(Box.createHorizontalStrut(30));

        return panel;
    }
    
    // 종합 분석 감싸는 박스 패널 추가
    
    private static JPanel createSummaryPanel() {
        JPanel wrapper = new JPanel();
        wrapper.setBackground(Color.BLACK);
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));

        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
        box.setBackground(Color.WHITE);
        box.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        box.setMaximumSize(new Dimension(350, 800));

        addLabeledText(box,"운동 습관 분석", 24, Color.BLACK, 10);
        addLabeledText(box,"총 운동 시간 : 8시간 36분", 20, Color.BLACK, 50);

        int[] sampleCounts = {2, 1, 3, 2, 3, 1, 2};  // 일 ~ 토 운동 횟수
        WeeklyChartPanel chartPanel = new WeeklyChartPanel(sampleCounts);

        box.add(Box.createVerticalStrut(20));
        box.add(chartPanel);

        // 코드 너무 길어지는 것 같아서 메소드 리펙토링함 (addLabeledText)
        box.add(new LineSetting(1));
        addLabeledText(box, "총 소모 칼로리", 20, Color.BLACK, 20);
        addLabeledText(box, "6,832 kcal", 40, Color.BLACK, 10);
        box.add(new LineSetting(1));
        addLabeledText(box, "체지방률, 근육량 변화 요약", 20, Color.BLACK, 20);
        addLabeledText(box, "(지난 주 대비)", 20, Color.DARK_GRAY, 10);
        addLabeledText(box, "체지방률 : 1.5 % 감소", 30, Color.BLACK, 10);
        addLabeledText(box, "골격근량 : 0.3 kg 증가", 30, Color.BLACK, 15);
        box.add(new LineSetting(1));
        addLabeledText(box, "이번 주 운동 목표 달성률", 20, Color.BLACK, 20);
        addLabeledText(box, "95%", 40, Color.BLACK, 10);

        // LineSetting 객체 : 구분선
        
        wrapper.add(box);
        return wrapper;
    }
    private static void addLabeledText(JPanel panel, String text, int fontSize, Color color, int spacingBefore) {
        panel.add(Box.createVerticalStrut(spacingBefore));
        panel.add(UIUtils.createTitleLabel(text, fontSize, color));
    }
}

// 위의 신체 정보 블록 클래스

class StatBox extends JPanel {
    private final JLabel valueLabel;

    public StatBox(String title, String value) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.GRAY);

        Dimension size = new Dimension(100, 80);
        setPreferredSize(size);
        setMaximumSize(size); // 반드시 함께 설정하기

        setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true));

        JLabel titleLabel = UIUtils.createTitleLabel(title, 12, Color.WHITE);
        valueLabel = UIUtils.createTitleLabel(value, 35, Color.WHITE);

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        valueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(titleLabel);
        add(Box.createVerticalStrut(5));
        add(valueLabel);
    }

    
    // 신체 정보 바꾸고 싶을 때 쓰는 메소드
    public void updateValue(String newValue) {
        valueLabel.setText(newValue);
    }
}

class WeeklyChartPanel extends JPanel {
    public WeeklyChartPanel(int[] counts) {
        setLayout(new BorderLayout());

        // 데이터셋 생성(요일 및 운동 횟수)
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        String[] days = {"일", "월", "화", "수", "목", "금", "토"};
        for (int i = 0; i < counts.length; i++) {
            dataset.addValue(counts[i], "운동 횟수", days[i]);
        }

        // 꺾은선 차트
        JFreeChart lineChart = ChartFactory.createLineChart(
                "요일별 운동 횟수",
                "요일",  // X축 레이블
                "횟수",  // Y축 레이블
                dataset,  // 그래프에 들어갈 데이터셋
                PlotOrientation.VERTICAL,  // 차트 방향 (세로 꺾은선)
                false, // 범례 표시 여부
                true,  // 툴팁 표시 여부(마우스 올릴 시 값 설명)
                false  // URL 링크 여부
        );

        ChartPanel chartPn = new ChartPanel(lineChart);
        chartPn.setPreferredSize(new Dimension(380, 200));
        chartPn.setBackground(Color.LIGHT_GRAY);
        chartPn.setMaximumSize(new Dimension(350, 350));

        Font font = new Font("Malgun Gothic", Font.PLAIN, 12);

        CategoryPlot plot = lineChart.getCategoryPlot();
        
        plot.setInsets(new RectangleInsets(10, 10, 10, 10));  // 안쪽 여백 확보
        
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
        setPreferredSize(new Dimension(300, thickness));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(getBackground());
        g.drawLine(0, 0 ,getWidth(), 0);  // 수평 직선
    }
}