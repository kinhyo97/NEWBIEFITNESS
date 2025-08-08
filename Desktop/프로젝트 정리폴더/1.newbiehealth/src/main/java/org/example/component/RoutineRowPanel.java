package org.example.component;

import javax.swing.*;
import java.awt.*;

public class RoutineRowPanel extends JPanel {
    private JButton selectButton;
    private String selectedExerciseName = "";
    private JTextField count, weightField, setsField;
    private JCheckBox doneCheckBox;

    public RoutineRowPanel(JPanel parentListPanel, JPanel wrapperToRemove) {
        this(parentListPanel, wrapperToRemove, null, "5", "50", "3");
    }

    public RoutineRowPanel(JPanel parentListPanel, JPanel wrapperToRemove, String name, String reps, String weight, String sets) {
        setLayout(new BorderLayout(10, 0));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        setBackground(Color.BLACK);

        // ✅ 삭제 버튼
        JButton deleteButton = new JButton("X");
        deleteButton.setForeground(Color.RED);
        deleteButton.setPreferredSize(new Dimension(30, 30));
        deleteButton.setMargin(new Insets(0, 0, 0, 0));
        deleteButton.addActionListener(e -> {
            parentListPanel.remove(wrapperToRemove);
            parentListPanel.revalidate();
            parentListPanel.repaint();
        });

        JPanel deleteWrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        deleteWrapper.setBackground(Color.BLACK);
        deleteWrapper.add(Box.createHorizontalStrut(5));
        deleteWrapper.add(deleteButton);
        add(deleteWrapper, BorderLayout.WEST);

        // ✅ 운동 선택 버튼
        selectButton = new JButton(name != null ? name : "운동 선택");
        selectedExerciseName = name != null ? name : "";
        selectButton.setBackground(Color.DARK_GRAY);
        selectButton.setForeground(Color.WHITE);
        selectButton.setPreferredSize(new Dimension(120, 30));
        selectButton.addActionListener(e -> new ExerciseSelectorPopup(this));

        // ✅ 운동 상세 입력창 (reps, weight, sets)
        JPanel infoPanel = new JPanel(new GridLayout(1, 4, 10, 0));
        infoPanel.setBackground(Color.BLACK);
        infoPanel.add(selectButton);

        count = new JTextField(reps);
        weightField = new JTextField(weight + "kg");
        setsField = new JTextField(sets);
        for (JTextField field : new JTextField[]{count, weightField, setsField}) {
            field.setHorizontalAlignment(JTextField.CENTER);
            field.setFont(new Font("Arial", Font.PLAIN, 9));
            infoPanel.add(field);
        }

        add(infoPanel, BorderLayout.CENTER);

        doneCheckBox = new JCheckBox();
        doneCheckBox.setBackground(Color.BLACK);
        add(doneCheckBox, BorderLayout.EAST);
    }

    public void setExerciseName(String name) {
        selectedExerciseName = name;
        if (selectButton != null) {
            selectButton.setText(name);
        }
    }

    //RoutineRowPanel의 Getter 메서드들
    public String getExerciseName() {
        return selectedExerciseName;
    } // 운동선택 버튼에서 선택된 운동 이름을 return

    public boolean isChecked() {
        return doneCheckBox.isSelected();
    } // 체크박스 체크여부

    public String getReps() {
        return count.getText().trim();
    } // reps필드 입력

    public String getWeight() {
        return weightField.getText().replace("kg", "").trim();
    } //weight 필드입력

    public String getSets() {
        return setsField.getText().trim();
    } //sets필드 입력 리턴
}