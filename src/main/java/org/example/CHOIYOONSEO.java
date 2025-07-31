package org.example;

import javax.swing.*;
import java.awt.*;

public class CHOIYOONSEO {
    public static void main(String args[]){
        JFrame frame = new JFrame("ㅇㅇ");


        JPanel panel = new JPanel();

        panel.setLayout(new FlowLayout());

        JButton button = new JButton("안녕");
        panel.add(button);
        frame.add(panel);

        frame.setVisible(true);

    }
}