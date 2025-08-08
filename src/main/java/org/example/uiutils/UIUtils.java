package org.example;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

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

    public static ImageIcon createRoundedIcon(String path, int size) {
        try {
            BufferedImage original = ImageIO.read(UIUtils.class.getResource(path));
            Image scaled = original.getScaledInstance(size, size, Image.SCALE_SMOOTH);

            BufferedImage rounded = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = rounded.createGraphics();

            g2.setClip(new Ellipse2D.Float(0, 0, size, size));
            g2.drawImage(scaled, 0, 0, null);
            g2.dispose();

            return new ImageIcon(rounded);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // UIUtils.java 내부에 추가
    public static class UnitHintTextField extends JTextField {
        private final String hint;
        private final Font hintFont;
        private final Color hintColor;

        public UnitHintTextField(String hint) {
            this.hint = hint;
            this.hintFont = new Font("맑은 고딕", Font.PLAIN, 14);
            this.hintColor = Color.GRAY;
            setHorizontalAlignment(JTextField.RIGHT); // 오른쪽 정렬
            setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 30)); // 우측 여백 확보
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setFont(hintFont);
                g2.setColor(hintColor);

                FontMetrics fm = g2.getFontMetrics();
                int x = getWidth() - fm.stringWidth(hint) - 10;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;

                g2.drawString(hint, x, y);
                g2.dispose();
            }
        }
    }


    public static JPanel createLabeledRow(String labelText, JComponent inputComponent) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false); // 배경 투명하게
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // 여백

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 14));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputComponent.setAlignmentX(Component.LEFT_ALIGNMENT);

        wrapper.add(label);
        wrapper.add(Box.createVerticalStrut(5));
        wrapper.add(inputComponent);

        return wrapper;
    }

    // 📌 좌우 정렬용 (예: 라벨 왼쪽, 입력 오른쪽)
    public static JPanel createHorizontalLabeledRow(String labelText, JComponent inputComponent) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new FlowLayout(FlowLayout.LEFT));
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 14));

        wrapper.add(label);
        wrapper.add(inputComponent);

        return wrapper;
    }

    public static JPanel createLabelOnlyRow(String text) {
        JPanel wrapper = new JPanel();
        wrapper.setLayout(new BoxLayout(wrapper, BoxLayout.Y_AXIS));
        wrapper.setOpaque(false);
        wrapper.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));  // 위아래 여백

        JLabel label = new JLabel(text);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);  // 가운데 정렬
        label.setHorizontalAlignment(SwingConstants.CENTER);

        wrapper.add(label);
        return wrapper;
    }

}
