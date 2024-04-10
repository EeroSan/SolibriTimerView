package eerosantala.timerview;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Timer;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.awt.BorderLayout;

public class TimerPanel {
    private JPanel panel;
    private JLabel timerLabel;
    private Timer timer;
    private int startTime; // = 5 * 60; // 5 minutes in seconds
    private int originalTime;
    private JButton startButton;

    private Properties properties;

    public TimerPanel(int setTime, Properties props) {
        properties = props;
        this.startTime = setTime;
        this.originalTime = setTime;
        this.panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.setLayout(new BorderLayout());

        timerLabel = new JLabel(formatTime(startTime));
        timerLabel.setFont(timerLabel.getFont().deriveFont(24f));
        timerLabel.setHorizontalAlignment(JLabel.CENTER);

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!com.solibri.smc.api.SMC.isBusy()) {
                    startTime--;
                    timerLabel.setText(formatTime(startTime));
                    if (startTime <= 0) {
                        timer.stop();
                        String fileName = com.solibri.smc.api.SMC.getModel().getName();
                        String folderPath = properties.getProperty("storePathField");
                        Path dirPath = Paths.get(folderPath);
                        if (java.nio.file.Files.exists(dirPath)) {
                            String pathToSave = folderPath + "\\autoclose_" + fileName + "_" + getCurrentDateTime()
                                    + ".smc";
                            Path path = Paths.get(pathToSave);
                            Boolean saveOnClose = Boolean.parseBoolean(properties.getProperty("saveOnCloseField"));
                            if (saveOnClose) {
                                com.solibri.smc.api.SMC.saveModel(path);
                            }

                        }
                        com.solibri.smc.api.SMC.shutdownNow();
                        resetTimer();
                    }
                } else {
                    timerLabel.setText(formatTime(startTime));
                }
            }
        });

        JButton resetButton = new JButton("Reset Timer");

        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetTimer();
            }
        });
        startButton = new JButton("Stop Timer");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (timer.isRunning()) {
                    timer.stop();
                    startButton.setText("Start Timer");
                } else {
                    timer.start();
                    startButton.setText("Stop Timer");
                }

            }
        });
        panel.add(resetButton, BorderLayout.NORTH);
        panel.add(timerLabel, BorderLayout.CENTER);
        // panel.add(startButton, BorderLayout.SOUTH);

        resetTimer();
    }

    public String getCurrentDateTime() {
        java.util.Date date = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        return sdf.format(date);
    }

    public void resetTimer() {
        startTime = this.originalTime;
        timerLabel.setText(formatTime(startTime));
        timer.start();
    }

    public void resetTimer(int newtime) {
        startTime = newtime;
        timerLabel.setText(formatTime(startTime));
        timer.start();
    }

    private String formatTime(int seconds) {
        int minutes = seconds / 60;
        int secs = seconds % 60;
        return String.format("%02d:%02d remaining", minutes, secs);
    }

    public JPanel getPanel() {
        return this.panel;
    }

    public void startTimer() {
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    public void stopTimer() {
        if (timer.isRunning()) {
            timer.stop();
        }
    }
}
