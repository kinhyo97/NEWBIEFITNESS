package org.example.pages;

import javax.swing.*;
import java.awt.*;
import org.example.UIUtils;
import org.example.component.*;

public class PageLastPage extends JPanel {
    public PageLastPage() {
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30)); // 여백 설정

        // 🔷 제목 라벨 (2줄)


        JPanel labelPanel = new JPanel();
        labelPanel.setLayout(new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        labelPanel.setBackground(Color.WHITE);
        labelPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        add(labelPanel);
        add(Box.createVerticalStrut(30)); // 제목 아래 간격

        // 🔶 여기에 본문 입력 영역 또는 선택지 컴포넌트 삽입
        // 예시로 빈 JLabel 추가

        JLabel lastLabel = new JLabel("고생하셨습니다.");
        lastLabel.setFont(new Font("맑은 고딕", Font.BOLD, 35));
        lastLabel.setForeground(Color.PINK);
        lastLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // ✅ 핵심 정렬
        lastLabel.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 정렬

        JLabel lastLabel2 = new JLabel("제출 버튼을 눌러주세요.");
        lastLabel2.setFont(new Font("맑은 고딕", Font.BOLD, 35));
        lastLabel2.setForeground(Color.PINK);
        lastLabel2.setAlignmentX(Component.CENTER_ALIGNMENT); // ✅ 핵심 정렬
        lastLabel2.setHorizontalAlignment(SwingConstants.CENTER); // 텍스트 정렬

        add(Box.createVerticalGlue());     // 🔼 위 공간
        add(lastLabel);
        add(lastLabel2);// 💬 가운데 텍스트
        add(Box.createVerticalGlue());
    }
}
