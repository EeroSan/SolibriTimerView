package eerosantala.timerview;

import java.util.Collection;
import java.util.Set;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import com.solibri.smc.api.model.Component;
import com.solibri.smc.api.ui.View;

public final class TimerView implements View {

    private TimerPanel timerPanel;
    private JLabel lastReset;

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
        timerPanel = new TimerPanel(1 * 60);
        panel.add(timerPanel.getPanel());
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
