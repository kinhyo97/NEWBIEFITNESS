package org.example.uiutils;

import javax.swing.*;
import java.awt.*;

public class UIUtils {
    // JLabel 생성하고 패널에 바로 추가하는 함수
    public static void addTitleLabel(JPanel panel, String text, int fontSize, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Malgun Gothic", Font.BOLD, fontSize));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(label);
    }

    // 만약 label만 반환받고 직접 add하고 싶을 경우에는 이 함수 사용
    public static JLabel createTitleLabel(String text, int fontSize, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Malgun Gothic", Font.BOLD, fontSize));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }
}
