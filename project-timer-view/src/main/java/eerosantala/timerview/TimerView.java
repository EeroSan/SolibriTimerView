package eerosantala.timerview;

import java.io.File;
import java.util.Collection;
import java.util.Properties;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.solibri.smc.api.model.Component;
import com.solibri.smc.api.ui.View;

public final class TimerView implements View {

    private TimerPanel timerPanel;
    private JLabel lastReset;

    private String HOME_DIR = System.getProperty("user.home");
    private String CONFIG_FILE_PATH = HOME_DIR + "\\Solibri\\timer_view.config";

    @Override
    public String getUniqueId() {
        return "TimerView";
    }

    @Override
    public String getName() {
        return "TimerView";
    }

    @Override
    public void initializePanel(JPanel panel) {
        this.lastReset = new JLabel("last reset");
        this.lastReset.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        Properties properties = SettingsView.loadPropertiesFile(new File(CONFIG_FILE_PATH));
        int time = 60;
        if (properties != null) {
            try {
                time = Integer.parseInt(properties.getProperty("timeInSeccondsField"));
            } catch (NumberFormatException e) {
                time = 60;
            }
        }

        timerPanel = new TimerPanel(time, properties);
        JButton settingsButton = new JButton("Settings");
        settingsButton.addActionListener(e -> {
            SettingsView settingsView = new SettingsView(CONFIG_FILE_PATH);
            settingsView.setVisible(true);
        });

        panel.add(timerPanel.getPanel());
        panel.add(settingsButton);
        panel.add(this.lastReset);

    }

    @Override
    public void onBasketSelectionChanged(Set<Component> oldSelection, Set<Component> newSelection) {
        lastReset.setText("last reset: basket selection changed");
        this.timerPanel.resetTimer();

    }

    @Override
    public void onComponentChosen(Component component) {
        lastReset.setText("last reset: component chosen");
        this.timerPanel.resetTimer();
    }

    @Override
    public void onComponentsHidden(Collection<Component> components) {
        lastReset.setText("last reset: components hidden");
        this.timerPanel.resetTimer();
    }

    @Override
    public void onComponentsZoomedTo(Collection<Component> components) {
        lastReset.setText("last reset: components zoomed to");
        this.timerPanel.resetTimer();
    }

    @Override
    public void onSettingsChanged() {
        lastReset.setText("last reset: settings changed");
        this.timerPanel.resetTimer();
    }

    @Override
    public void onCheckingStarted() {
        timerPanel.stopTimer();
        lastReset.setText("stopped timer due to checking");
    }

    @Override
    public void onCheckingEnded() {
        this.reStartTimer();
    }

    @Override
    public void onItoStarted() {
        timerPanel.stopTimer();
        lastReset.setText("stopped timer due to ITO");
    }

    @Override
    public void onItoFinished() {
        this.reStartTimer();
    }

    private void reStartTimer() {
        timerPanel.startTimer();
        lastReset.setText("restarted timer, checking ended");
    }

}
