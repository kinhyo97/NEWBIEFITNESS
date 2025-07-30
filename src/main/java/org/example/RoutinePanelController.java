package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.example.component.PrettyButton;
import org.example.component.SmallButton;

public class RoutinePanelController {
    public static void routine_show(JPanel panel, App app) {
        panel.removeAll();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.BLACK);

        // 루틴 불러오기
        SmallButton loadButton = new SmallButton("Load Routine");
        loadButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("운동 시작 버튼 클릭됨!");
                app.switchCard("ROUTINE");
                app.routine_show();
            }
        });

// 왼쪽 정렬을 위한 래퍼 패널
        JPanel leftWrap = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0)); // hgap, vgap 모두 0
        leftWrap.setOpaque(false); // 필요 시 setBackground(Color.BLACK)
        leftWrap.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // ← 여기서 왼쪽만 약간만 띄움
        leftWrap.add(loadButton);

// 💡 레이아웃 깨짐 방지
        leftWrap.setMaximumSize(new Dimension(Integer.MAX_VALUE, loadButton.getPreferredSize().height));

// 패널에 추가
        panel.add(leftWrap);




        panel.add(Box.createVerticalStrut(30));
        org.example.UIUtils.addTitleLabel(panel, "TODAY'S ROUTINE", 30, Color.PINK);
        org.example.UIUtils.addTitleLabel(panel, "5 X 5 SUPER SET", 20, Color.WHITE);
        org.example.UIUtils.addTitleLabel(panel, "2025-07-29 (MON)", 15, Color.WHITE);
        org.example.UIUtils.addTitleLabel(panel, "   EXERCISE     COUNT   WEIGHT      SET       ", 20, Color.WHITE);

        // 리스트 패널
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.BLACK);
        panel.add(listPanel);

        // + Add 버튼
        JButton addButton = new JButton("+ Add");
        addButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addButton.addActionListener(e -> {
            JPanel wrapper = new JPanel();
            wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
            wrapper.setOpaque(false);

            wrapper.add(Box.createVerticalStrut(10));
            routineRowPanel row = new routineRowPanel(listPanel, wrapper);
            wrapper.add(row);

            listPanel.add(wrapper);
            listPanel.revalidate();
            listPanel.repaint();
        });
        panel.add(Box.createVerticalStrut(10));
        panel.add(addButton);
        panel.add(Box.createVerticalGlue());

        PrettyButton prettyButton = new PrettyButton("Exercise Done !");
        prettyButton.setBounds(30, 100, 200, 50);
        prettyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("운동 시작 버튼 클릭됨!");
                app.switchCard("ROUTINE");
                app.routine_show();
            }
        });
        panel.add(prettyButton);
        panel.add(Box.createVerticalStrut(30));

        panel.revalidate();
        panel.repaint();
    }
}

// 운동 한 줄 컴포넌트
class routineRowPanel extends JPanel {
    public routineRowPanel(JPanel parentListPanel, JPanel wrapperToRemove) {
        setLayout(new BorderLayout(10, 0));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        setBackground(Color.BLACK);

        JButton deleteButton = new JButton("X");
        deleteButton.setForeground(Color.RED);
        deleteButton.setPreferredSize(new Dimension(30, 30));
        deleteButton.setMargin(new Insets(0, 0, 0, 0));

        JPanel deleteWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        deleteWrapper.setBackground(Color.BLACK);
        deleteWrapper.add(Box.createHorizontalStrut(5));
        deleteWrapper.add(deleteButton);
        add(deleteWrapper, BorderLayout.WEST);

        JPanel infoPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        infoPanel.setBackground(Color.BLACK);

        JTextField exName = new JTextField("Exercise");
        JTextField count = new JTextField("5");
        JTextField weight = new JTextField("50kg");
        JTextField sets = new JTextField("3");

        for (JTextField field : new JTextField[]{exName, count, weight, sets}) {
            field.setHorizontalAlignment(JTextField.CENTER);
            infoPanel.add(field);
        }

        add(infoPanel, BorderLayout.CENTER);

        JCheckBox doneCheckBox = new JCheckBox();
        doneCheckBox.setBackground(Color.BLACK);
        add(doneCheckBox, BorderLayout.EAST);

        deleteButton.addActionListener(e -> {
            parentListPanel.remove(wrapperToRemove);
            parentListPanel.revalidate();
            parentListPanel.repaint();
        });
    }
}
