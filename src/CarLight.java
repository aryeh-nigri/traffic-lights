import java.awt.Color;
import javax.swing.JPanel;

public class CarLight extends Thread {
    private Event evRed, evGreen, evShabbat, evAck;
    private int counter;

    enum OutState {
        WEEKDAY, SHABBAT;
    };

    OutState outState;

    enum InWeekdayState {
        GREEN, GREEN_OFF, GREEN_ON, YELLOW_TO_RED, YELLOW_TO_GREEN, RED;
    };

    InWeekdayState inWeekdayState;

    enum InShabbatState {
        ON, OFF;
    }

    InShabbatState inShabbatState;
    Ramzor ramzor;
    JPanel panel;
    private boolean stop = true;

    public CarLight(Ramzor ramzor, JPanel panel, Event evRed, Event evGreen, Event evShabbat, Event evAck, int key) {
        this.ramzor = ramzor;
        this.panel = panel;

        this.evRed = evRed;
        this.evGreen = evGreen;
        this.evShabbat = evShabbat;
        this.evAck = evAck;

        new CarsMaker(panel, this, key);
        // new CarMoovingWithNum(panel, this, key, 7);

        setDaemon(true);
        start();
    }

    public boolean isStop() {
        return stop;
    }

    @Override
    public void run() {

        boolean outWeek = false, outShabbat = false;
        outState = OutState.WEEKDAY;
        inWeekdayState = InWeekdayState.RED;
        inShabbatState = InShabbatState.ON;
        turnRedAndSendAck();

        while (true) {
            switch (outState) {
            case WEEKDAY:
                while (!outWeek) {
                    switch (inWeekdayState) {
                    case GREEN:
                        while (true) {
                            if (evShabbat.arrivedEvent()) {
                                evShabbat.waitEvent();
                                outShabbat = false;
                                turnYellow();
                                outWeek = true;
                                outState = OutState.SHABBAT;
                                inShabbatState = InShabbatState.ON;
                                break;
                            } else if (evRed.arrivedEvent()) {
                                evRed.waitEvent();
                                turnGray();
                                counter = 0;
                                inWeekdayState = InWeekdayState.GREEN_OFF;
                                break;
                            } else
                                yield();
                        }
                        break;

                    case GREEN_OFF:
                        while (true) {
                            if (evShabbat.arrivedEvent()) {
                                evShabbat.waitEvent();
                                outShabbat = false;
                                turnYellow();
                                outWeek = true;
                                outState = OutState.SHABBAT;
                                inShabbatState = InShabbatState.ON;
                                break;
                            } else {
                                try {
                                    sleep(400);
                                } catch (InterruptedException exce) {
                                }
                                turnGreen();
                                counter++;
                                inWeekdayState = InWeekdayState.GREEN_ON;
                                break;
                            }

                        }
                        break;

                    case GREEN_ON:
                        while (true) {
                            if (evShabbat.arrivedEvent()) {
                                evShabbat.waitEvent();
                                outShabbat = false;
                                turnYellow();
                                outWeek = true;
                                outState = OutState.SHABBAT;
                                inShabbatState = InShabbatState.ON;
                                break;
                            } else if (counter >= 3) {
                                turnYellow();
                                inWeekdayState = InWeekdayState.YELLOW_TO_RED;
                                break;
                            } else {
                                try {
                                    sleep(400);
                                } catch (InterruptedException exce) {
                                }
                                turnGray();
                                inWeekdayState = InWeekdayState.GREEN_OFF;
                                break;
                            }
                        }
                        break;

                    case YELLOW_TO_RED:
                        while (true) {
                            if (evShabbat.arrivedEvent()) {
                                evShabbat.waitEvent();
                                outShabbat = false;
                                turnYellow();
                                outWeek = true;
                                outState = OutState.SHABBAT;
                                inShabbatState = InShabbatState.ON;
                                break;
                            } else {
                                try {
                                    sleep(1500);
                                } catch (InterruptedException exce) {
                                }
                                turnRedAndSendAck();
                                inWeekdayState = InWeekdayState.RED;
                                break;
                            }
                        }
                        break;

                    case YELLOW_TO_GREEN:
                        while (true) {
                            if (evShabbat.arrivedEvent()) {
                                evShabbat.waitEvent();
                                outShabbat = false;
                                turnYellow();
                                outWeek = true;
                                outState = OutState.SHABBAT;
                                inShabbatState = InShabbatState.ON;
                                break;
                            } else {
                                // try {
                                // sleep(1500);
                                // } catch (InterruptedException exce) {}
                                turnGreen();
                                try {
                                    sleep(5000);
                                } catch (InterruptedException exce) {
                                }
                                inWeekdayState = InWeekdayState.GREEN;
                                break;
                            }
                        }
                        break;

                    case RED:
                        while (true) {
                            if (evShabbat.arrivedEvent()) {
                                evShabbat.waitEvent();
                                outShabbat = false;
                                turnYellow();
                                outWeek = true;
                                outState = OutState.SHABBAT;
                                inShabbatState = InShabbatState.ON;
                                break;
                            } else if (evGreen.arrivedEvent()) {
                                evGreen.waitEvent();
                                try {
                                    sleep(1000);
                                } catch (InterruptedException exce) {
                                }
                                turnYellowFromRed();
                                try {
                                    sleep(1500);
                                } catch (InterruptedException exce) {
                                }
                                inWeekdayState = InWeekdayState.YELLOW_TO_GREEN;
                                break;
                            } else
                                yield();
                        }
                        break;
                    }
                }
                break;

            case SHABBAT:
                while (!outShabbat) {
                    switch (inShabbatState) {
                    case ON:
                        while (true) {
                            if (evShabbat.arrivedEvent()) {
                                evShabbat.waitEvent();
                                outWeek = false;
                                turnRedAndSendAck();
                                outShabbat = true;
                                outState = OutState.WEEKDAY;
                                inWeekdayState = InWeekdayState.RED;
                                break;
                            } else {
                                try {
                                    sleep(500);
                                } catch (InterruptedException exce) {
                                }
                                turnGray();
                                inShabbatState = InShabbatState.OFF;
                                break;
                            }
                        }
                        break;

                    case OFF:
                        while (true) {
                            if (evShabbat.arrivedEvent()) {
                                evShabbat.waitEvent();
                                outWeek = false;
                                turnRedAndSendAck();
                                outShabbat = true;
                                outState = OutState.WEEKDAY;
                                inWeekdayState = InWeekdayState.RED;
                                break;
                            } else {
                                try {
                                    sleep(500);
                                } catch (InterruptedException exce) {
                                }
                                turnYellow();
                                inShabbatState = InShabbatState.ON;
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }

    }

    public void setLight(int place, Color color) {
        ramzor.colorLight[place - 1] = color;
        panel.repaint();
    }

    private void turnYellow() {
        setLight(1, Color.LIGHT_GRAY);
        setLight(2, Color.YELLOW);
        setLight(3, Color.LIGHT_GRAY);
    }

    private void turnGreen() {
        stop = false;
        setLight(1, Color.LIGHT_GRAY);
        setLight(2, Color.LIGHT_GRAY);
        setLight(3, Color.GREEN);
    }

    private void turnGray() {
        setLight(1, Color.LIGHT_GRAY);
        setLight(2, Color.LIGHT_GRAY);
        setLight(3, Color.LIGHT_GRAY);
    }

    private void turnRedAndSendAck() {
        stop = true;
        setLight(1, Color.RED);
        setLight(2, Color.LIGHT_GRAY);
        setLight(3, Color.LIGHT_GRAY);
        evAck.sendEvent();
    }

    private void turnYellowFromRed() {// 1 is already RED, and 3 LIGHT_GRAY
        setLight(2, Color.YELLOW);
    }

}