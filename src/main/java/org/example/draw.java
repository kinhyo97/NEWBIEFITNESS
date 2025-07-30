import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class draw extends JPanel {

    // 패널에 그림을 그리는 함수
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawElephant((Graphics2D) g);
    }

    // 코끼리 그리는 함수
    private void drawElephant(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(160, 160, 160)); // 코끼리 회색

        // 몸통
        g2.fillOval(100, 100, 200, 100);

        // 귀
        g2.fillOval(80, 110, 60, 60);

        // 다리 (두 개만 간단하게)
        g2.fillRect(120, 180, 20, 40);
        g2.fillRect(220, 180, 20, 40);

        // 코 (트렁크) - 곡선
        QuadCurve2D trunk = new QuadCurve2D.Float();
        trunk.setCurve(280, 130, 340, 160, 280, 190);
        g2.setStroke(new BasicStroke(10));
        g2.draw(trunk);

        // 눈
        g2.setColor(Color.BLACK);
        g2.fillOval(250, 125, 8, 8);
    }

    // main 함수로 실행
    public static void main(String[] args) {
        JFrame frame = new JFrame("🐘 코끼리 그리기 예제");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setContentPane(new draw()); // 패널 붙이기
        frame.setVisible(true);
    }
}
