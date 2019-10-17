import javafx.scene.control.ComboBox;

import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import javax.swing.*;

public class SimpleGUI extends JFrame {
    private Main ctx;
    public String selectedTree = "";

    SimpleGUI(Main main) {
        this.ctx = main;
        initComponents();
        this.setVisible(true);
    }

    private void buttonStartActionPerformed(ActionEvent e) {
        ctx.setPaused(false);
        this.setVisible(false);
        Main.settingsSet = true;
        Main.treeType = selectedTree;
    }

    private void initComponents() {
        //GEN-BEGIN:initComponents
        JLabel label1 = new JLabel();
        JButton buttonStart = new JButton();
        JComboBox<String> ComboBox1 = new JComboBox<>();

        //======== this ========
        setBackground(new Color(51, 51, 51));
        setTitle("Super Simple Power Chopper");
        setResizable(false);
        Container contentPane = getContentPane();
        contentPane.setLayout(null);

        ComboBox1.addItem("Tree");
        ComboBox1.addItem("Oak");
        ComboBox1.addItem("Willow");
        ComboBox1.addItem("Maple");
        ComboBox1.addItem("Yew");
        ComboBox1.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    selectedTree = Objects.requireNonNull(ComboBox1.getSelectedItem()).toString();
                }
            }
        });
        ComboBox1.setFont(new Font(".SF NS Text", Font.PLAIN, 12));
        ComboBox1.setForeground(new Color(255, 255, 255));
        ComboBox1.setBounds(8, 30, 130, 30);
        contentPane.add(ComboBox1);

        label1.setText("Powerchops trees all over the world.");
        label1.setFont(new Font(".SF NS Text", Font.PLAIN, 12));
        label1.setForeground(new Color(255, 255, 255));
        label1.setBounds(8, 2, 300, 30);
        contentPane.add(label1);


        //---- buttonStart ----
        buttonStart.setText("START");
        buttonStart.setFocusCycleRoot(true);
        buttonStart.setFont(new Font(".SF NS Text", Font.PLAIN, 12));
        buttonStart.setBackground(new Color(35, 35, 35));
        buttonStart.addActionListener(this::buttonStartActionPerformed);
        contentPane.add(buttonStart);
        buttonStart.setBounds(8, 92, 70, 30);

        contentPane.setPreferredSize(new Dimension(365, 140));
        pack();
        setLocationRelativeTo(getOwner());
    }
}
