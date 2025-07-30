//package org.example;
//
//import org.example.component.PrettyButton;
//
//import javax.swing.*;
//import java.awt.*;
//import java.awt.event.MouseAdapter;
//import java.awt.event.MouseEvent;
//
//
//public class RoutinePanelControllerbackup {
//    public static void routine_show(JPanel panel, App app) {
//        panel.removeAll();
//        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
//        panel.setBackground(Color.BLACK);
//
//        panel.add(Box.createVerticalStrut(30));
//
//        // 제목 추가 (패널에 직접 추가)
//        UIUtils.addTitleLabel(panel, "TODAY'S ROUTINE", 30, Color.PINK);
//        UIUtils.addTitleLabel(panel, "5 X 5 SUPER SET", 20, Color.WHITE);
//        UIUtils.addTitleLabel(panel, "2025-07-29 (MON)", 15, Color.WHITE);
//        UIUtils.addTitleLabel(panel, "EXERCISE      COUNT      WEIGHT      SET", 20, Color.WHITE);
//
//        panel.add(Box.createVerticalGlue());
//
//        PrettyButton prettyButton = new PrettyButton("Exercise Done !");
//        prettyButton.setBounds(30, 100, 200, 50);
//        prettyButton.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                System.out.println("운동 시작 버튼 클릭됨!");
//                app.switchCard("ROUTINE");     // 💡 카드 전환!
//                app.routine_show();             // 💡 내용 갱신
//            }
//        });
//        panel.add(prettyButton);
//        panel.add(Box.createVerticalStrut(30));
//
//        panel.revalidate();
//        panel.repaint();
//    }
//}
