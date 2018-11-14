import java.awt.Color;

import javax.swing.JPanel;

public class FlashingLight extends Thread {
    Ramzor ramzor;
    JPanel panel;

    enum State {
        ON, OFF
    };

    State state;
    int flashingTime = 500;

    public FlashingLight(Ramzor ramzor, JPanel panel) {
        this.ramzor = ramzor;
        this.panel = panel;

        setDaemon(true);
        start();
    }

    @Override
    public void run() {
        state = State.ON;
        turnYellow();

        while (true) {
            switch (state) {
            case ON:
                try {
                    sleep(flashingTime);
                } catch (InterruptedException exce) {
                }
                turnGray();
                state = State.OFF;
                break;

            case OFF:
                try {
                    sleep(flashingTime);
                } catch (InterruptedException exce) {
                }
                turnYellow();
                state = State.ON;
                break;
            }
        }

    }

    public void setLight(int place, Color color) {
        ramzor.colorLight[place - 1] = color;
        panel.repaint();
    }

    private void turnGray() {
        setLight(1, Color.LIGHT_GRAY);
    }

    private void turnYellow() {
        setLight(1, Color.YELLOW);
    }

}
