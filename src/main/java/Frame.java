import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;

public class Frame {
    private static final JFrame jFrame = new JFrame();
    private final JFileChooser fc = new JFileChooser();
    private JPanel panel;
    private JComboBox<String> comboBox;
    private JButton convert;
    private JButton chooser;
    private JLabel label;
    private File file;

    public Frame() {
        comboBox.addItem("txt");
        comboBox.addItem("html");
        comboBox.addItem("Docx");
        chooser.addActionListener(e -> {
            FileFilter filter = new FileFilter() {
                @Override
                public boolean accept(File f) {
                    String name = f.getName().toLowerCase();
                    return name.endsWith(".pdf") || f.isDirectory();
                }

                @Override
                public String getDescription() {
                    return "PDF Files";
                }
            };
            fc.setFileFilter(filter);
            int returnVal = fc.showOpenDialog(panel);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                String name = file.getName();
                if (file.getName().length() > 20)
                    name = file.getName().substring(0, 20) + "...";
                chooser.setText(name);
            }
        });

        convert.addActionListener(e -> {
            if (file == null) {
                JOptionPane.showMessageDialog(jFrame,
                    "Please Choose a PDF file first.",
                    "Alert",
                    JOptionPane.WARNING_MESSAGE);
            } else {
                String format = String.valueOf(comboBox.getSelectedItem());
                String filePath = file.getPath();
                filePath = file.getPath().substring(0, filePath.indexOf("."));
                Util util = new Util();
                String title = "The operation was successful";
                String msg = "The new file is saved next to the original file.";
                switch (format) {
                    case "txt" -> {
                        util.toTEXT(file, filePath + ".txt");
                        JOptionPane.showMessageDialog(jFrame,
                            msg,
                            title,
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                    case "html" -> {
                        util.toHTML(file, filePath + ".html");
                        JOptionPane.showMessageDialog(jFrame,
                            msg,
                            title,
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                    case "Docx" -> {
                        util.toDOCX(file, filePath + ".docx");
                        JOptionPane.showMessageDialog(jFrame,
                            msg,
                            title,
                            JOptionPane.INFORMATION_MESSAGE);
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + format);
                }
            }
        });
    }

    public static void main(String[] args) {
        new Frame().createAndShowGUI();
    }

    private void createAndShowGUI() {
        try {
            new Font("Monospaced", Font.PLAIN, 14);
            UIManager.setLookAndFeel(new FlatLightLaf());
            FlatDarkPurpleIJTheme.setup();

        } catch (Exception ex) {
            System.err.println("Failed to initialize LaF");
        }
        jFrame.setContentPane(new Frame().panel);
        jFrame.setTitle("PDF Converter");
        jFrame.setLocationRelativeTo(null);
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        jFrame.setSize(450, 300);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
    }
}
