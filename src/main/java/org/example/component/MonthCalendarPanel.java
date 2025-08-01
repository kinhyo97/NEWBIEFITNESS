package org.example.component;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.function.Consumer;

public class MonthCalendarPanel extends JPanel {
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

    private void updateFromCombo() {
        int selectedYear = (int) yearCombo.getSelectedItem();
        int selectedMonth = (int) monthCombo.getSelectedItem();
        currentYearMonth = YearMonth.of(selectedYear, selectedMonth);
        updateCalendar();
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
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
