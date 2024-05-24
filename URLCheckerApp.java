import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;

public class URLCheckerApp extends JFrame {
    private JTextField hostField;
    private JButton fileButton;
    private JTextArea resultArea;
    private File selectedFile;
    private boolean isDarkMode = true; 

    private Color lightBackground = Color.WHITE;
    private Color lightForeground = Color.BLACK;
    private Color darkBackground = Color.BLACK;
    private Color darkForeground = Color.GREEN;
    private Color buttonBackground = new Color(30, 30, 30);
    private Color buttonForeground = Color.GREEN;

    public URLCheckerApp() {
        setTitle("FUFF");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        hostField = new JTextField();
        fileButton = new JButton("Select File");
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        fileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectFile();
            }
        });

        JButton checkButton = new JButton("Check URLs");
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkUrls();
            }
        });

        JCheckBox darkModeToggle = new JCheckBox("Dark Mode");
        darkModeToggle.setSelected(isDarkMode);
        darkModeToggle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isDarkMode = darkModeToggle.isSelected();
                updateColors();
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("Enter Host:"), BorderLayout.NORTH);
        panel.add(hostField, BorderLayout.CENTER);
        panel.add(fileButton, BorderLayout.EAST);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(panel, BorderLayout.CENTER);
        topPanel.add(darkModeToggle, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
        add(checkButton, BorderLayout.SOUTH);

        updateColors(); 
    }

    private void updateColors() {
        Color background = isDarkMode ? darkBackground : lightBackground;
        Color foreground = isDarkMode ? darkForeground : lightForeground;
        Color buttonBg = isDarkMode ? buttonBackground : lightBackground;
        Color buttonFg = isDarkMode ? buttonForeground : lightForeground;

        hostField.setBackground(background);
        hostField.setForeground(foreground);
        fileButton.setBackground(buttonBg);
        fileButton.setForeground(buttonFg);
        resultArea.setBackground(background);
        resultArea.setForeground(foreground);
        getContentPane().setBackground(background);
    }

    private void selectFile() {
        JFileChooser fileChooser = new JFileChooser();
        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            fileButton.setText(selectedFile.getName());
        }
    }

    private void checkUrls() {
        String host = hostField.getText();
        if (host.isEmpty() || selectedFile == null) {
            JOptionPane.showMessageDialog(this, "Please enter a host and select a file.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        URLChecker checker = new URLChecker(host, selectedFile, resultArea);
        new Thread(checker).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            URLCheckerApp app = new URLCheckerApp();
            app.setVisible(true);
        });
    }
}
