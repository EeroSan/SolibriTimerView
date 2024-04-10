package eerosantala.timerview;

import java.awt.GridLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

public class SettingsView extends JFrame {
    private String filePath;
    private JTextField storePathField;
    private JButton storePathButton;
    private JTextField timeInSeccondsField;
    private JCheckBox saveOnCloseField;

    private Properties properties;

    public SettingsView(String configPath) {
        this.filePath = configPath;
        this.properties = new Properties();
        loadProperties();

        this.setTitle("Settings");
        this.setSize(400, 300);

        JPanel mainPanel = new JPanel(new GridLayout(0, 3, 10, 10));
        this.storePathField = new JTextField(this.properties.getProperty("storePathField"));
        this.timeInSeccondsField = new JTextField(this.properties.getProperty("timeInSeccondsField"));

        this.saveOnCloseField = new JCheckBox("Save on close",
                Boolean.parseBoolean(this.properties.getProperty("saveOnCloseField")));

        JButton setFilePathButton = new JButton("Set file path");
        setFilePathButton.addActionListener(e -> {
            String path = openFile();
            if (path != null) {
                storePathField.setText(path);
                properties.setProperty("storePathField", path);
            }
        });

        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(e -> {
            properties.setProperty("storePathField", storePathField.getText());
            properties.setProperty("timeInSeccondsField", timeInSeccondsField.getText());
            properties.setProperty("saveOnCloseField", String.valueOf(saveOnCloseField.isSelected()));

            File configFile = new File(this.filePath);
            try {
                properties.store(new java.io.FileOutputStream(configFile), null);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        mainPanel.add(new JLabel("Time in seconds:"));
        mainPanel.add(timeInSeccondsField);
        mainPanel.add(new JLabel(""));

        mainPanel.add(new JLabel(""));
        mainPanel.add(saveOnCloseField);
        mainPanel.add(new JLabel(""));

        mainPanel.add(new JLabel("Save files to:"));
        mainPanel.add(storePathField);
        mainPanel.add(setFilePathButton);

        mainPanel.add(new JLabel(""));
        mainPanel.add(saveButton);
        this.add(mainPanel);

    }

    private String openFile() {
        JFileChooser fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        fileChooser.setDialogTitle("Select a folder to store the file");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int returnValue = fileChooser.showOpenDialog(null);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile().getAbsolutePath();
        } else {
            return null; // User canceled the operation
        }
    }

    private void loadProperties() {
        File configFile = new File(this.filePath);
        this.properties = SettingsView.loadPropertiesFile(configFile);
    }

    public static Properties loadPropertiesFile(File configFile) {
        Properties properties = new Properties();
        try {
            if (configFile.exists()) {
                FileInputStream inputStream = new FileInputStream(configFile);
                properties.load(inputStream);
                inputStream.close();
            } else {
                // Set default values if the file does not exist
                String userHome = System.getProperty("user.home");
                File defDir = new File(userHome, "Solibri");
                properties.setProperty("storePathField", defDir.getAbsolutePath());
                properties.setProperty("timeInSeccondsField", "3600");
                properties.setProperty("saveOnCloseField", "true");

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return properties;
    }

}
