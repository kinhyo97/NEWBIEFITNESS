package org.example;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.WeekFields;
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
import org.example.component.WeeklyWorkoutDialog;

public class StatisticsPanelController {
    
    // 데이터 불러오는 static 변수들
    static StatisticsService statisticsServiceInfo = new StatisticsService();
    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd(E)", Locale.KOREAN);
    static String dayString = LocalDate.now().format(formatter);
//    static Map<String, Integer> weekly = statisticsServiceInfo.fetchWorkoutCountByDay(dayString);
    static String userKey = statisticsServiceInfo.getUserKey_id();
    static String selectedDateStr = null;

    // 전역 선언부
    static JToggleButton toggleMode = new JToggleButton("일일 평균 기준");


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

        // 프로그레스 바 생성
        final CircularProgressBar progressBar = new CircularProgressBar();
        progressBar.setPreferredSize(new Dimension(300, 300));
        progressBar.setProgress(statisticsServiceInfo.percentageAccomplishmentCalories(userKey, LocalDate.now().toString()));

        JPanel circleWrap = new JPanel(new BorderLayout());
        circleWrap.setOpaque(true);
        circleWrap.setBackground(Color.BLACK);
        circleWrap.setMaximumSize(new Dimension(300, 300));
        circleWrap.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        circleWrap.add(progressBar, BorderLayout.CENTER);

        JLabel dateLabel = new JLabel(LocalDate.now().format(formatter), SwingConstants.CENTER);

        String selectedDate = dateLabel.getText();

        String selectedDateProgress = LocalDate.now().format(formatter);


        // 신체 정보 컨테이너에 올림(또..?)
        localSummaryPanelContainer.setLayout(new BoxLayout(localSummaryPanelContainer, BoxLayout.Y_AXIS));
        localSummaryPanelContainer.add(createSummaryPanel(userKey, dayString));
        // 캘린터 표시를 위한 익명 클래스 & 날짜 선택 버튼
        ActionPrettyButton dateButton = new ActionPrettyButton("조회할 날짜 선택");
        dateButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 날짜 라벨
        // (dateLabel 생성 시부터 요일 포맷 포함시키기))
        ActionPrettyButton weekRecordButton = new ActionPrettyButton("운동 기록 조회");
        weekRecordButton.addActionListener(e -> {
            String queryDate;

            if (selectedDateStr == null || selectedDateStr.isEmpty()) {
                // 사용자가 선택하지 않았으면 → 오늘 날짜
                LocalDate today = LocalDate.now();
                queryDate = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")); // ✅ 요일 제외
            } else {
                // 예: "2025-08-04(월)" → "2025-08-04" 로 자르기
                queryDate = selectedDateStr.split("\\(")[0].trim(); // ✅ 요일 제거
            }

            Map<String, ArrayList<Object[]>> result = statisticsServiceInfo.fetchWeeklyWorkoutGrouped(userKey, queryDate);

            if (result.isEmpty()) {
                System.out.println("[디버깅] 조회된 운동 기록이 없습니다.");
            }

            new WeeklyWorkoutDialog(userKey, result).setVisible(true);
        });

        // JLabel dateLabel = new JLabel(LocalDate.now().format(formatter), SwingConstants.CENTER);
        dateLabel.setForeground(Color.WHITE);
        dateLabel.setFont(new Font("Malgun Gothic", Font.PLAIN, 20));

        // 신체 정보 같이 업데이트 하려고 익명 클래스에 쓰기 위해 넣음
        JPanel bodyInfoContainer = new JPanel();
        bodyInfoContainer.setBackground(Color.BLACK);
        // 패널 위에 패널 위에 패널... <- 리펙토링 나중에 할 필요 존재 ✅✅✅
        // 익명 클래스는 정의와 동시에 객체를 바로 생성해서 쓸 수 있음
        dateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame calendar = new JFrame("날짜를 선택해주세요");

                calendar.setSize(380, 350);
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

                        selectedDateStr = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd(E)", Locale.KOREAN)); //
                        dateLabel.setText("선택된 날짜: " + selectedDateStr);

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

                        int rate = statisticsServiceInfo.percentageAccomplishmentCalories(userKey, selectedDateStr);
                        progressBar.setProgress(rate);
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


        // 스크롤바
        JScrollPane pageScroll = new JScrollPane(middlePanel);
        pageScroll.setBorder(null);
        pageScroll.getVerticalScrollBar().setUnitIncrement(16);

        middlePanel.add(Box.createVerticalStrut(20));

        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        circleWrap.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel dynamicTitleLabel = new JLabel("일일 칼로리 대비 섭취 비율");
        dynamicTitleLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 30));
        dynamicTitleLabel.setForeground(Color.WHITE);
        dynamicTitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        /* !! 패널에 추가하는 부분 !! */

        // 상단 제목 배치
        middlePanel.add(Box.createVerticalStrut(20));
        middlePanel.add(titleLabel);
        middlePanel.add(Box.createVerticalStrut(30));

        // 신체 정보 패널
        bodyInfoContainer.add(createBodyInfoPanel(userKey, LocalDate.now().toString()));
        middlePanel.add(bodyInfoContainer);
        middlePanel.add(Box.createVerticalStrut(30));
        org.example.UIUtils.addTitleLabel(middlePanel, "운동 기록 조회", 30, Color.WHITE);
        middlePanel.add(Box.createVerticalStrut(3));

        // 날짜 라벨 / 선택 받는 파란색 라벨
        dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        middlePanel.add(dateLabel);
        middlePanel.add(Box.createVerticalStrut(20));
        middlePanel.add(dateButton);
        middlePanel.add(Box.createVerticalStrut(20));
        middlePanel.add(weekRecordButton);

        // 원형 목표 달성률 라벨

        middlePanel.add(Box.createVerticalStrut(20));
        middlePanel.add(dynamicTitleLabel);
        middlePanel.add(Box.createVerticalStrut(20));

        middlePanel.add(Box.createVerticalStrut(10));
        middlePanel.add(circleWrap);
        middlePanel.add(Box.createVerticalStrut(20));


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

    private static String getCurrentDateQuery() {
        return (selectedDateStr != null && !selectedDateStr.isEmpty())
                ? selectedDateStr.split("\\(")[0].trim()   // "2025-08-04(월)" → "2025-08-04"
                : LocalDate.now().toString();              // 오늘 날짜 "2025-08-04"
    }


    // 신체 정보 패널
    private static JPanel createBodyInfoPanel(String userKey, String dayString) {
        RoundedPanel1 roundedBodyInfoPanel = new RoundedPanel1(50);

        roundedBodyInfoPanel.setBackground(Color.BLACK);
        roundedBodyInfoPanel.setLayout(new GridBagLayout());  // flowLayout -> 수직 정렬 기준 무조건 상단.

        Map<String, Object> info = statisticsServiceInfo.fetchBodyInfo(userKey, dayString);

        StatBox weightBox = new StatBox("현재 체중(kg)", String.valueOf(info.get("present_weight")), 35);
        StatBox goalBox = new StatBox("목표 칼로리", String.valueOf(info.get("target_calories")), 32);
        StatBox dietBox = new StatBox("식단 타입", String.valueOf(info.get("diet_type")), 28);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        roundedBodyInfoPanel.add(weightBox, gbc);
        gbc.gridx = 1;
        roundedBodyInfoPanel.add(goalBox, gbc);
        gbc.gridx = 2;
        roundedBodyInfoPanel.add(dietBox, gbc);

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

        box.add(Box.createVerticalStrut(10));
        addLabeledText(box,"종합 분석", 24, Color.BLACK, 6);
        addLabeledText(box,"(지난 기록 기준)", 12, Color.BLACK, 10);
        box.add(Box.createVerticalStrut(10));
        /*
        String timeText = getTotalActivityTimeText(workoutByDay);

        JLabel totalLabel = fieldViewText(timeText, 27, Color.BLACK);
        box.add(totalLabel);

        box.add(Box.createVerticalStrut(10));
        */

        /*
        WeeklyChartPanel chartPanel = new WeeklyChartPanel(dayString);

        box.add(Box.createVerticalStrut(10));
        box.add(chartPanel);

        */
        // 코드 너무 길어지는 것 같아서 메소드 리펙토링함 (addLabeledText)
        box.add(new LineSetting(1));
        box.add(Box.createVerticalStrut(3));
        addLabeledText(box, "오늘 총 섭취 칼로리", 20, Color.BLACK, 20);
        
        // 총 칼로리 불러오기
        int cal = statisticsServiceInfo.totalCalories(userKey, dayString);
        JLabel totalCalories = fieldViewText( cal + " kcal", 40, Color.BLACK);
        box.add(totalCalories);

        //box.add(new LineSetting(1));
        box.add(Box.createVerticalStrut(5));
        //addLabeledText(box, "체지방률, 근육량 변화 요약", 22, Color.BLACK, 5);

        /*
        String previousDate = statisticsServiceInfo.getLastRecordedDateBefore(userKey, dayString);

        // 이전 기록 없을 때 조건 처리
        
        if (previousDate != null) {
            LocalDate lastDate = LocalDate.parse(previousDate);
            String formatted = lastDate.format(DateTimeFormatter.ofPattern("yyyy년 M월 d일"));
            JLabel label = fieldViewText("(" + formatted + " 기준)", 20, Color.DARK_GRAY);
            box.add(label);
        } else {
            JLabel label = fieldViewText("(이전 기록 없음)", 20, Color.DARK_GRAY);
            box.add(label);
        }

        box.add(Box.createVerticalStrut(20));

        double fatPer = statisticsServiceInfo.changeFatMass(userKey, dayString);
        JLabel fatMass = fieldViewText( "체지방률 " + fatPer + " % 감소", 30, Color.BLACK);
        box.add(fatMass);

        double musclePer = statisticsServiceInfo.changeMuscleMass(userKey, dayString);
        JLabel muscleMass = fieldViewText("근육량 " + musclePer + " kg 증가", 30, Color.BLACK);
        box.add(muscleMass);
*/
        box.add(Box.createVerticalStrut(20));
        box.add(new LineSetting(1));
        box.add(Box.createVerticalStrut(5));
        addLabeledText(box, "한 줄 코멘트", 22, Color.BLACK, 10);

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

    public StatBox(String title, String value, int fontSize) {
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
        valueLabel = org.example.UIUtils.createTitleLabel(value, fontSize, Color.BLACK);

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
