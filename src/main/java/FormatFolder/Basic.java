package FormatFolder;

import javax.swing.*;
import java.awt.*;

class Basic extends JFrame {

    public Basic() {
        setTitle("SUGGEST FITNESS - SURVEY");
        setSize(500, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE); // 배경 흰색

        setVisible(true);
    }

    class SurveyPanel extends JPanel {
        public SurveyPanel() {
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            setBackground(Color.WHITE);

        }
    }

    public static void main(String[] args) {
        new Basic();
    }
}
