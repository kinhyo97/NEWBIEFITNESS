package org.example.component;


import org.example.service.StatisticsService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WeeklyWorkoutDialog extends JDialog {

    public WeeklyWorkoutDialog(String userKey, Map<String, ArrayList<Object[]>> dateGroupedData) {
        setTitle("주간 운동 기록");
        setModal(true);
        setSize(700, 700);
        setLocation(0, 0);
        setLayout(new BorderLayout());

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(Color.LIGHT_GRAY);

        String[] columnNames = {"운동명", "세트", "반복", "중량"};

        for (Map.Entry<String, ArrayList<Object[]>> entry : dateGroupedData.entrySet()) {
            String dateTitle = entry.getKey(); // 예: "7월 29일 (월)"
            ArrayList<Object[]> rows = entry.getValue();

            // 날짜 라벨
            JLabel dateLabel = new JLabel(dateTitle);
            dateLabel.setFont(new Font("Malgun Gothic", Font.BOLD, 20));
            dateLabel.setBorder(new EmptyBorder(10, 10, 5, 0));
            dateLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // 날짜 라벨 (예: 2025-08-04)
            dateLabel.setForeground(Color.WHITE);  // 날짜 글자 색상
            dateLabel.setBackground(Color.BLACK);  // 라벨 배경 일치

            // 표 데이터
            Object[][] rowDataArray = new Object[rows.size()][4];  // 날짜 제외
            for (int i = 0; i < rows.size(); i++) {
                Object[] original = rows.get(i);
                rowDataArray[i] = new Object[]{ original[1], original[2], original[3], original[4] };  // index 1~4
            }

            JTable table = new JTable(new DefaultTableModel(rowDataArray, columnNames));
            table.setRowHeight(25);
            table.setFont(new Font("Malgun Gothic", Font.PLAIN, 17));
            table.getTableHeader().setFont(new Font("Malgun Gothic", Font.BOLD, 17));
            table.setFillsViewportHeight(true);
            table.setEnabled(false);


            table.setBackground(new Color(20, 20, 20));
            table.setForeground(Color.WHITE);
            table.setGridColor(Color.DARK_GRAY);

            // 헤더 배경 & 글자 색
            JTableHeader header = table.getTableHeader();
            header.setOpaque(true);
            header.setBackground(new Color(40, 40, 40));
            header.setForeground(Color.WHITE);
            header.setFont(new Font("Malgun Gothic", Font.BOLD, 14));
            header.setBorder(BorderFactory.createEmptyBorder());


            // 행 간격 및 간격 강조
            table.setRowHeight(28);

            // 가운데 정렬 적용
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }

            // 표 테이블 창 원활한 스크롤을 위해서
            table.addMouseWheelListener(e -> {
                JScrollPane outerScroll = (JScrollPane) SwingUtilities.getAncestorOfClass(JScrollPane.class, table);
                if (outerScroll != null) {
                    outerScroll.dispatchEvent(SwingUtilities.convertMouseEvent(table, e, outerScroll));
                }
            });


            JScrollPane tableScroll = new JScrollPane(table);
            tableScroll.setPreferredSize(new Dimension(550, table.getRowHeight() * rows.size() + 30));
            tableScroll.setBorder(BorderFactory.createEmptyBorder());  // ✅ 테두리 제거

            tableScroll.setBackground(Color.BLACK);
            tableScroll.getViewport().setBackground(Color.BLACK);

            // 날짜별로 추가
            contentPanel.add(dateLabel);
            contentPanel.add(header);
            contentPanel.add(table);
        }



        UIManager.put("TableHeader.cellBorder", BorderFactory.createEmptyBorder());  //

        JScrollPane scrollPane = new JScrollPane(contentPanel);

        contentPanel.setBackground(Color.BLACK);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(new Color(20, 20, 20));


        add(scrollPane, BorderLayout.CENTER);
    }
}
