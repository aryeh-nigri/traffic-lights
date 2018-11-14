import java.awt.Color;
import javax.swing.JPanel;

public class PedestrianLight extends Thread {
    private Event evRed, evGreen, evShabbat, evAck;

    enum OutState {
        WEEKDAY, SHABBAT
    };

    OutState outState;

    enum InState {
        GREEN, RED
    };

    InState inState;
    Ramzor ramzor;
    JPanel panel;

    public PedestrianLight(Ramzor ramzor, JPanel panel, Event evRed, Event evGreen, Event evShabbat, Event evAck) {
        this.ramzor = ramzor;
        this.panel = panel;

        this.evRed = evRed;
        this.evGreen = evGreen;
        this.evShabbat = evShabbat;
        this.evAck = evAck;

        setDaemon(true);
        start();
    }

    @Override
    public void run() {

        boolean out = false;
        outState = OutState.WEEKDAY;
        inState = InState.RED;
        turnRedAndSendAck();

        while (true) {
            switch (outState) {
            case WEEKDAY:
                while (!out) {
                    switch (inState) {
                    case GREEN:
                        while (true) {
                            if (evShabbat.arrivedEvent()) {
                                evShabbat.waitEvent();
                                turnGray();
                                out = true;
                                outState = OutState.SHABBAT;
                                break;
                            } else if (evRed.arrivedEvent()) {
                                System.out.println("PedestrianLight evRed");
                                evRed.waitEvent();
                                turnRedAndSendAck();
                                inState = InState.RED;
                                break;
                            } else
                                yield();
                        }
                        break;

                    case RED:
                        while (true) {
                            if (evShabbat.arrivedEvent()) {
                                evShabbat.waitEvent();
                                turnGray();
                                out = true;
                                outState = OutState.SHABBAT;
                                break;
                            } else if (evGreen.arrivedEvent()) {
                                System.out.println("PedestrianLight evGreen");
                                evGreen.waitEvent();
                                try {
                                    sleep(1000);
                                } catch (InterruptedException exce) {
                                }
                                turnGreen();
                                try {
                                    sleep(5000);
                                } catch (InterruptedException exce) {
                                }
                                inState = InState.GREEN;
                                break;
                            } else
                                yield();
                        }
                        break;

                    }
                }
                break;

            case SHABBAT: {
                evShabbat.waitEvent();
                out = false;
                turnRedAndSendAck();
                outState = OutState.WEEKDAY;
                inState = InState.RED;
                break;
            }
            }
        }

    }

    public void setLight(int place, Color color) {
        ramzor.colorLight[place - 1] = color;
        panel.repaint();
    }

    private void turnGreen() {
        // System.out.println("PedestrianLight GREEN");
        setLight(1, Color.GRAY);
        setLight(2, Color.GREEN);
    }

    private void turnRedAndSendAck() {
        // System.out.println("PedestrianLight RED");
        setLight(1, Color.RED);
        setLight(2, Color.GRAY);
        evAck.sendEvent();
    }

    private void turnGray() {
        // System.out.println("PedestrianLight GRAY");
        setLight(1, Color.GRAY);
        setLight(2, Color.GRAY);
    }

}