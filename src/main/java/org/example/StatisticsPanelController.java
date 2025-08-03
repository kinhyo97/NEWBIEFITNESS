package org.example;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

import org.checkerframework.checker.units.qual.A;
import org.example.component.*;
import org.example.service.StatisticsService;
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
    static StatisticsService statisticsServiceInfo = new StatisticsService();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd(E)", Locale.KOREAN);
    static String dayString = LocalDate.now().format(formatter);
//    static Map<String, Integer> weekly = statisticsServiceInfo.fetchWorkoutCountByDay(dayString);
    static String userKey = statisticsServiceInfo.getUserKey_id();

    public static void statistics_show(JPanel panel, App app) {
        panel.removeAll();
        panel.setLayout(new BorderLayout());  // flowLayout 에서 수정

        JPanel localSummaryPanelContainer = new JPanel();

        JPanel summaryPanelContainer = new JPanel();
        summaryPanelContainer.setLayout(new BoxLayout(summaryPanelContainer, BoxLayout.Y_AXIS));
        summaryPanelContainer.add(createSummaryPanel(userKey, dayString));


        if (!(summaryPanelContainer.getLayout() instanceof BoxLayout)) {
            summaryPanelContainer.setLayout(new BoxLayout(summaryPanelContainer, BoxLayout.Y_AXIS));
        }


        // 상단에 들어갈 박스
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

        // 날짜 라벨
        // (dateLabel 생성 시부터 요일 포맷 포함시키기))
        

        JLabel dateLabel = new JLabel(LocalDate.now().format(formatter), SwingConstants.CENTER);
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));
        
        // 신체 정보 같이 업데이트 하려고 익명 클래스에 쓰기 위해 넣음
        JPanel bodyInfoContainer = new JPanel();
        bodyInfoContainer.setBackground(Color.BLACK);
        // 패널 위에 패널 위에 패널... <- 리펙토링 나중에 할 필요 존재 ✅✅✅

        // 원형 프로그레스 박스
        JPanel circleWrap = new JPanel();
        circleWrap.setOpaque(true);
        circleWrap.setBackground(Color.BLACK);
        circleWrap.setLayout(new BorderLayout());
        circleWrap.setMaximumSize(new Dimension(300, 300));

        circleWrap.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); // 여유 여백

        String selectedDate = dateLabel.getText();

        CircularProgressBar progressBar = new CircularProgressBar();
        progressBar.setPreferredSize(new Dimension(300, 300));
        progressBar.setProgress(statisticsServiceInfo.percentAccomplish(statisticsServiceInfo.getUserKey_id(), selectedDate));
        circleWrap.add(progressBar, BorderLayout.CENTER);

        // 신체 정보 컨테이너에 올림(또..?)
        localSummaryPanelContainer.setLayout(new BoxLayout(localSummaryPanelContainer, BoxLayout.Y_AXIS));
        localSummaryPanelContainer.add(createSummaryPanel(userKey, dayString));


        // 캘린터 표시를 위한 익명 클래스 & 날짜 선택 버튼
        ActionPrettyButton dateButton = new ActionPrettyButton("조회할 날짜 선택");
        dateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 익명 클래스는 정의와 동시에 객체를 바로 생성해서 쓸 수 있음
        dateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame calendar = new JFrame("날짜를 선택해주세요");

                calendar.setSize(350, 350);
                calendar.setLayout(new BorderLayout());
                calendar.setLocation(0, 0);

                String currentlySelectedStr = dateLabel.getText().replace("선택된 날짜: ", "").trim();
                LocalDate defaultDate;
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd(E)", Locale.KOREAN);
                try {
                    defaultDate = LocalDate.parse(currentlySelectedStr, formatter);
                } catch (Exception ex) {
                    defaultDate = LocalDate.now();
                    String todayStr = defaultDate.format(formatter);
                    dateLabel.setText("오늘 날짜: " + todayStr);
                }

                MonthCalendarPanel calendarPanel = new MonthCalendarPanel(defaultDate, new Consumer<LocalDate>() {
                    @Override
                    public void accept(LocalDate selectedDate) {
                        // 달력 닫기
                        calendar.dispose();
                        String selectedDateStr = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd(E)", Locale.KOREAN)); //
                        dateLabel.setText("선택된 날짜: " + selectedDateStr);
                        int rate = statisticsServiceInfo.percentAccomplish(statisticsServiceInfo.getUserKey_id(), selectedDateStr);
                        progressBar.setProgress(rate);

                        System.out.println(selectedDateStr);

                        // 신체 정보 패널 갱신
                        bodyInfoContainer.removeAll();  // 기존 컴포넌트 제거
                        JPanel updatedPanel = createBodyInfoPanel(userKey, selectedDateStr);
                        bodyInfoContainer.add(updatedPanel);  // 새로 만든 것 추가
                        bodyInfoContainer.revalidate();
                        bodyInfoContainer.repaint();

                        // 종합 분석 패널 갱신
                        localSummaryPanelContainer.removeAll();
                        localSummaryPanelContainer.add(createSummaryPanel(userKey, selectedDateStr));
                        localSummaryPanelContainer.revalidate();
                        localSummaryPanelContainer.repaint();

                        // 자동 이동 방지 -> 캘린더 선택 시 밑으로 내려가는 문제 해결을 위해 스크롤 최상단으로 이동
                        SwingUtilities.invokeLater(() -> {
                            middlePanel.scrollRectToVisible(new Rectangle(0, 0, 1, 1));
                        });
                    }
                });

                calendar.add(calendarPanel, BorderLayout.CENTER);
                calendar.setVisible(true);

            }
        });



        // middlePanel.add(calendar);

        // 스크롤바
        JScrollPane pageScroll = new JScrollPane(middlePanel);
        pageScroll.setBorder(null);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);

        middlePanel.add(Box.createVerticalStrut(20));

        /* !! 패널에 추가하는 부분 !! */

        // 상단 제목 배치
        middlePanel.add(Box.createVerticalStrut(20));
        middlePanel.add(titleLabel);
        middlePanel.add(Box.createVerticalStrut(30));


        // 신체 정보 패널
        bodyInfoContainer.add(createBodyInfoPanel(userKey, LocalDate.now().toString()));
        middlePanel.add(bodyInfoContainer);
        middlePanel.add(Box.createVerticalStrut(30));
        org.example.UIUtils.addTitleLabel(middlePanel, "운동 목표 달성률", 30, Color.WHITE);
        middlePanel.add(Box.createVerticalStrut(3));
        
        // 날짜 라벨
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        middlePanel.add(dateLabel);
        middlePanel.add(Box.createVerticalStrut(20));
        middlePanel.add(dateButton);

        // 원형 목표 달성률 라벨
        middlePanel.add(Box.createVerticalStrut(20));
        middlePanel.add(circleWrap);
        middlePanel.add(Box.createVerticalStrut(20));
        circleWrap.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 종합 분석 패널
        middlePanel.add(localSummaryPanelContainer);
        middlePanel.add(Box.createVerticalStrut(20));

        panel.add(pageScroll, BorderLayout.CENTER); // 스크롤바


        // 자동 이동 방지 -> 캘린더 선택 시 밑으로 내려가는 문제 해결을 위해 스크롤 최상단으로 이동
        SwingUtilities.invokeLater(() -> {
            middlePanel.scrollRectToVisible(new Rectangle(0, 0, 1, 1));
        });


        // 마우스 이벤트 처리 시 재배치, 변화 알림 및 재 페인팅
        panel.revalidate(); // 레이아웃 재계산
        panel.repaint();
    }

    // 신체 정보 패널
    private static JPanel createBodyInfoPanel(String userKey, String dayString) {
        RoundedPanel1 roundedBodyInfoPanel = new RoundedPanel1(50);

        roundedBodyInfoPanel.setBackground(Color.BLACK);
        roundedBodyInfoPanel.setLayout(new GridBagLayout());  // flowLayout -> 수직 정렬 기준 무조건 상단.

        Map<String, String> info = statisticsServiceInfo.fetchBodyInfo(userKey, dayString);

        StatBox weightBox = new StatBox("체중(kg)", info.get("weight_kg"));
        StatBox fatBox = new StatBox("체지방률(%)", info.get("body_fat_after"));
        StatBox muscleBox = new StatBox("근육량(kg)", info.get("muscle_mass_after"));

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

    private static JPanel createSummaryPanel(String userKey, String dayString) {
        Map<String, Integer> workoutByDay = statisticsServiceInfo.fetchWorkoutCountByDay(dayString);

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

        String timeText = getTotalActivityTimeText(workoutByDay);

        JLabel totalLabel = fieldViewText(timeText, 27, Color.BLACK);
        box.add(totalLabel);

        WeeklyChartPanel chartPanel = new WeeklyChartPanel(dayString);

        box.add(Box.createVerticalStrut(10));
        box.add(chartPanel);

        // 코드 너무 길어지는 것 같아서 메소드 리펙토링함 (addLabeledText)
        box.add(Box.createVerticalStrut(10));
        addLabeledText(box, "이번 주 총 소모 칼로리", 20, Color.BLACK, 20);
        
        // 총 칼로리 불러오기
        int cal = statisticsServiceInfo.totalCalories(userKey, dayString);
        JLabel totalCalories = fieldViewText( cal + " kcal", 40, Color.BLACK);
        box.add(totalCalories);

        box.add(Box.createVerticalStrut(20));
        box.add(new LineSetting(1));
        box.add(Box.createVerticalStrut(5));
        addLabeledText(box, "체지방률, 근육량 변화 요약", 20, Color.BLACK, 5);
        addLabeledText(box, "(지난 주 대비)", 20, Color.DARK_GRAY, 5);

        double fatPer = statisticsServiceInfo.changeFatMass(userKey, dayString);
        JLabel fatMass = fieldViewText( "체지방률 : " + fatPer + " % 감소", 30, Color.BLACK);
        box.add(fatMass);

        double musclePer = statisticsServiceInfo.changeMuscleMass(userKey, dayString);
        JLabel muscleMass = fieldViewText("근육량 : " + musclePer + " kg 증가", 30, Color.BLACK);
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

    public static String getTotalActivityTimeText(Map<String, Integer> mapDay) {
        int totalActivityTimes = 0;

        for (Map.Entry<String, Integer> entry : mapDay.entrySet()) {
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
    StatisticsService jdbcStatWeekDayCounts = new StatisticsService();

    public WeeklyChartPanel(String dateStr) {
        setLayout(new BorderLayout());


        // 데이터셋 생성(요일 및 운동 횟수)
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        Map<String, Integer> dataDayTimeCounts = jdbcStatWeekDayCounts.fetchWorkoutCountByDay(dateStr);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd(E)", Locale.KOREAN);
        DateTimeFormatter labelFormat = DateTimeFormatter.ofPattern("M/d");  // X축 레이블

        for (String d : getWeekDates(dateStr)) {
            LocalDate ld = LocalDate.parse(d, formatter);
            String label = ld.format(labelFormat);

            int count = dataDayTimeCounts.getOrDefault(d, 0);

            dataset.addValue(count, "운동 시간", label);
        }

        // 꺾은선 차트
        JFreeChart lineChart = ChartFactory.createLineChart(
                "날짜별 운동 시간",
                "날짜",  // X축 레이블
                "활동 시간(분)",  // Y축 레이블
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

    public static ArrayList<String> getWeekDates(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd(E)", Locale.KOREAN);
        LocalDate baseDate = LocalDate.parse(date, formatter);

        // 해당 주의 월요일 구하기
        LocalDate startOfWeek = baseDate.minusDays(baseDate.getDayOfWeek().getValue() % 7);

        ArrayList<String> weekDates = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            weekDates.add(startOfWeek.plusDays(i).format(formatter));
        }
        return weekDates;
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

class MonthCalendarPanel extends JPanel {
    private final Color sundayColor = new Color(255, 80, 80);
    private final Color saturdayColor = new Color(80, 80, 255);
    private final Color todayBg = new Color(255, 200, 200);
    private final Color selectedBg = new Color(255, 170, 0);

    private LocalDate selectedDate = null;
    private Consumer<LocalDate> onDateSelected;
    private LocalDate today = LocalDate.now();
    private YearMonth currentYearMonth = YearMonth.from(today);

    private JPanel calendarGrid;
    private JComboBox<Integer> yearCombo;
    private JComboBox<Integer> monthCombo;

    public MonthCalendarPanel(Consumer<LocalDate> onDateSelected) {
        this.onDateSelected = onDateSelected;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);


        // 상단: 연도 & 월 선택
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        header.setBackground(Color.WHITE);

        yearCombo = new JComboBox<>();
        for (int y = today.getYear() - 5; y <= today.getYear() + 5; y++) {
            yearCombo.addItem(y);
        }
        yearCombo.setSelectedItem(currentYearMonth.getYear());

        monthCombo = new JComboBox<>();
        for (int m = 1; m <= 12; m++) {
            monthCombo.addItem(m);
        }
        monthCombo.setSelectedItem(currentYearMonth.getMonthValue());

        yearCombo.addActionListener(e -> updateFromCombo());
        monthCombo.addActionListener(e -> updateFromCombo());

        header.add(new JLabel("YEAR"));
        header.add(yearCombo);
        header.add(Box.createHorizontalStrut(10));
        header.add(new JLabel("MONTH"));
        header.add(monthCombo);

        add(header, BorderLayout.NORTH);

        // 달력 그리드
        calendarGrid = new JPanel(new GridLayout(0, 7));
        calendarGrid.setBackground(Color.WHITE);
        add(calendarGrid, BorderLayout.CENTER);

        updateCalendar();
    }
    public MonthCalendarPanel(LocalDate defaultDate, Consumer<LocalDate> onDateSelected) {
        this.onDateSelected = onDateSelected;
        this.selectedDate = defaultDate;
        this.currentYearMonth = YearMonth.from(defaultDate);

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        initHeader();
        updateCalendar();
    }


    private void updateFromCombo() {
        int selectedYear = (int) yearCombo.getSelectedItem();
        int selectedMonth = (int) monthCombo.getSelectedItem();
        currentYearMonth = YearMonth.of(selectedYear, selectedMonth);
        updateCalendar();
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }

    private void initHeader() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER));
        header.setBackground(Color.WHITE);

        yearCombo = new JComboBox<>();
        for (int y = today.getYear() - 5; y <= today.getYear() + 5; y++) {
            yearCombo.addItem(y);
        }
        yearCombo.setSelectedItem(currentYearMonth.getYear());

        monthCombo = new JComboBox<>();
        for (int m = 1; m <= 12; m++) {
            monthCombo.addItem(m);
        }
        monthCombo.setSelectedItem(currentYearMonth.getMonthValue());

        yearCombo.addActionListener(e -> updateFromCombo());
        monthCombo.addActionListener(e -> updateFromCombo());

        header.add(new JLabel("YEAR"));
        header.add(yearCombo);
        header.add(Box.createHorizontalStrut(10));
        header.add(new JLabel("MONTH"));
        header.add(monthCombo);

        add(header, BorderLayout.NORTH);

        calendarGrid = new JPanel(new GridLayout(0, 7));
        calendarGrid.setBackground(Color.WHITE);
        add(calendarGrid, BorderLayout.CENTER);
    }


    private void updateCalendar() {
        calendarGrid.removeAll();

        // 요일 헤더
        String[] days = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        for (int i = 0; i < 7; i++) {
            JLabel dayLabel = new JLabel(days[i], SwingConstants.CENTER);
            dayLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
            dayLabel.setForeground(i == 0 ? sundayColor : (i == 6 ? saturdayColor : Color.BLACK));
            calendarGrid.add(dayLabel);
        }

        // 시작 요일 전 빈 칸
        int firstDayOfWeek = currentYearMonth.atDay(1).getDayOfWeek().getValue() % 7;
        for (int i = 0; i < firstDayOfWeek; i++) {
            calendarGrid.add(new JLabel(""));
        }

        // 날짜 버튼
        for (int day = 1; day <= currentYearMonth.lengthOfMonth(); day++) {
            LocalDate date = currentYearMonth.atDay(day);
            JButton dayBtn = new JButton(String.valueOf(day));
            dayBtn.setFocusPainted(false);
            dayBtn.setContentAreaFilled(false);
            dayBtn.setBorderPainted(false);
            dayBtn.setOpaque(true);
            dayBtn.setBackground(Color.WHITE);

            dayBtn.setForeground(date.getDayOfWeek().getValue() % 7 == 0 ? sundayColor :
                    date.getDayOfWeek().getValue() % 7 == 6 ? saturdayColor : Color.BLACK);

            if (date.equals(today)) dayBtn.setBackground(todayBg);
            if (date.equals(selectedDate)) dayBtn.setBackground(selectedBg);

            dayBtn.addActionListener(e -> {
                selectedDate = date;
                if (onDateSelected != null) onDateSelected.accept(selectedDate);
                updateCalendar(); // 선택된 날짜 강조 위해 다시 그림
            });

            calendarGrid.add(dayBtn);
        }

        revalidate();
        repaint();
    }
}
