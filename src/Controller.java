import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Timer;
import java.util.TimerTask;

public class Controller extends Thread {
    // private ThreadUI myOutput = new ThreadUI("Controller");
    private Event evShabbat, evButton;
    // private Light lights[];
    // private int carLightNumber = 4;
    // private int pedestrianLightNumber = 12;
    // private int flashingLightNumber = 1;
    private int eventsNumber = 16;
    private Event evReds[] = new Event[eventsNumber];
    private Event evGreens[] = new Event[eventsNumber];
    private Event evAcks[] = new Event[eventsNumber];
    private Event evShabbats[] = new Event[eventsNumber];

    enum OutState {
        WEEKDAY, SHABBAT
    };

    OutState outState;

    enum InWeekdayState {
        ALL_RED, GREEN_0, GREEN_1_2, GREEN_2_3
    };

    InWeekdayState inWeekdayState;

    Ramzor ramzorim[];
    TrafficLightFrame tlf;

    public Controller(TrafficLightFrame tlf, Ramzor ramzorim[], Event button, Event evShabbat) {
        this.tlf = tlf;
        this.ramzorim = ramzorim;

        this.evButton = button;
        this.evShabbat = evShabbat;

        createLights();

        setDaemon(true);
        // start();
    }

    private void createLights() {
        ramzorim[0] = new Ramzor(3, 40, 430, 110, 472, 110, 514, 110);
        ramzorim[1] = new Ramzor(3, 40, 450, 310, 450, 352, 450, 394);
        ramzorim[2] = new Ramzor(3, 40, 310, 630, 280, 605, 250, 580);
        ramzorim[3] = new Ramzor(3, 40, 350, 350, 308, 350, 266, 350);

        ramzorim[4] = new Ramzor(2, 20, 600, 18, 600, 40);
        ramzorim[5] = new Ramzor(2, 20, 600, 227, 600, 205);
        ramzorim[6] = new Ramzor(2, 20, 600, 255, 600, 277);
        ramzorim[7] = new Ramzor(2, 20, 600, 455, 600, 433);
        ramzorim[8] = new Ramzor(2, 20, 575, 475, 553, 475);
        ramzorim[9] = new Ramzor(2, 20, 140, 608, 150, 590);
        ramzorim[10] = new Ramzor(2, 20, 205, 475, 193, 490);
        ramzorim[11] = new Ramzor(2, 20, 230, 475, 250, 475);
        ramzorim[12] = new Ramzor(2, 20, 200, 453, 200, 433);
        ramzorim[13] = new Ramzor(2, 20, 200, 255, 200, 277);
        ramzorim[14] = new Ramzor(2, 20, 200, 227, 200, 205);
        ramzorim[15] = new Ramzor(2, 20, 200, 18, 200, 40);

        ramzorim[16] = new Ramzor(1, 30, 555, 645);

        initArrays();

        int i;
        for (i = 0; i < 4; i++) {// 0-3
            new CarLight(ramzorim[i], tlf.myPanel, evReds[i], evGreens[i], evShabbats[i], evAcks[i], i + 1);
        }
        for (; i < 16; i++) {// 4-15
            new PedestrianLight(ramzorim[i], tlf.myPanel, evReds[i], evGreens[i], evShabbats[i], evAcks[i]);
        }
        new FlashingLight(ramzorim[i], tlf.myPanel);// 16
    }

    private void initArrays() {
        for (int i = 0; i < eventsNumber; i++) {
            evReds[i] = new Event();
            evGreens[i] = new Event();
            evAcks[i] = new Event();
            evShabbats[i] = new Event();
        }
    }

    @Override
    public void run() {
        int sender = -1;
        long greenTime = 3000;
        boolean out = false;
        List<Integer> lightsNumbers = null;
        outState = OutState.WEEKDAY;
        inWeekdayState = InWeekdayState.ALL_RED;
        sendEvRedToAll();

        // Timer timer;
        // Event evTimer = new Event();

        while (true) {
            switch (outState) {
            case WEEKDAY:
                while (!out) {
                    switch (inWeekdayState) {
                    case ALL_RED:
                        while (true) {
                            if (evShabbat.arrivedEvent()) {
                                evShabbat.waitEvent();
                                sendEvRedToAll();
                                waitAckFromAll();
                                sendEvShabbatToAll();
                                out = true;
                                outState = OutState.SHABBAT;
                                break;
                            }
                            System.out.println("Im here! ALL_RED");
                            waitAckFromAll();
                            lightsNumbers = Arrays.asList(0, 6, 7, 9, 10, 12, 13);
                            sendEvGreenTo(lightsNumbers);
                            inWeekdayState = InWeekdayState.GREEN_0;
                            break;
                        }
                        break;

                    case GREEN_0:
                        while (true) {
                            // timer = new Timer(greenTime, evTimer);
                            System.out.println("Im here! GREEN_0");
                            if (evShabbat.arrivedEvent()) {
                                evShabbat.waitEvent();
                                // IS GREEN : 0, 6, 7, 9, 10, 12, 13
                                lightsNumbers = Arrays.asList(0, 6, 7, 9, 10, 12, 13);
                                sendEvRedTo(lightsNumbers);
                                waitAckFrom(lightsNumbers);
                                // sendEvRedToAll();
                                // waitAckFromAll();
                                sendEvShabbatToAll();
                                out = true;
                                outState = OutState.SHABBAT;
                                break;
                            } else if (evButton.arrivedEvent()) {
                                int s = (int) evButton.waitEvent();
                                if (sender == -1) {
                                    sender = s;
                                }
                            }
                            try {
                                sleep(greenTime);
                            } catch (InterruptedException exce) {
                            }
                            if (sender == 8 || sender == 11 || sender == 14 || sender == 15) {
                                // lightsNumbers = Arrays.asList(0, 1, 6, 7, 9, 10, 12, 13);
                                lightsNumbers = Arrays.asList(0, 6, 7, 9, 10, 12, 13);// IS GREEN : 0, 6, 7, 9, 10,
                                                                                      // 12,
                                                                                      // 13
                                sendEvRedTo(lightsNumbers);
                                waitAckFrom(lightsNumbers);
                                // lightsNumbers = Arrays.asList(2, 3, 4, 5, 8, 11, 14, 15);
                                lightsNumbers = Arrays.asList(2, 3, 4, 5, 8, 11, 14, 15);// IS GREEN : -
                                sendEvGreenTo(lightsNumbers);
                                inWeekdayState = InWeekdayState.GREEN_2_3;
                                sender = -1;
                                break;
                            } else {
                                // lightsNumbers = Arrays.asList(0, 8, 9, 10, 11, 14, 15);
                                lightsNumbers = Arrays.asList(0, 9, 10);// IS GREEN : 0, 6, 7, 9, 10, 12, 13
                                sendEvRedTo(lightsNumbers);
                                waitAckFrom(lightsNumbers);
                                // lightsNumbers = Arrays.asList(1, 2, 4, 5, 6, 7, 12, 13);
                                lightsNumbers = Arrays.asList(1, 2, 4, 5);// IS GREEN : 6, 7, 12, 13
                                sendEvGreenTo(lightsNumbers);
                                inWeekdayState = InWeekdayState.GREEN_1_2;
                                sender = -1;
                                break;
                            }
                        }
                        break;

                    case GREEN_1_2:
                        while (true) {
                            System.out.println("Im here! GREEN_1_2");
                            if (evShabbat.arrivedEvent()) {
                                evShabbat.waitEvent();
                                // IS GREEN : 1, 2, 4, 5, 6, 7, 12, 13
                                lightsNumbers = Arrays.asList(1, 2, 4, 5, 6, 7, 12, 13);
                                sendEvRedTo(lightsNumbers);
                                waitAckFrom(lightsNumbers);
                                // sendEvRedToAll();
                                // waitAckFromAll();
                                sendEvShabbatToAll();
                                out = true;
                                outState = OutState.SHABBAT;
                                break;
                            } else if (evButton.arrivedEvent()) {
                                int s = (int) evButton.waitEvent();
                                if (sender == -1) {
                                    sender = s;
                                }
                            }
                            try {
                                sleep(greenTime);
                            } catch (InterruptedException exce) {
                            }
                            if (sender == 9 || sender == 10) {
                                // lightsNumbers = Arrays.asList(1, 2, 3, 4, 5, 8, 11, 14, 15);
                                lightsNumbers = Arrays.asList(1, 2, 4, 5);// IS GREEN : 1, 2, 4, 5, 6, 7, 12, 13
                                sendEvRedTo(lightsNumbers);
                                waitAckFrom(lightsNumbers);
                                // lightsNumbers = Arrays.asList(0, 6, 7, 9, 10, 12, 13);
                                lightsNumbers = Arrays.asList(0, 9, 10);// IS GREEN : 6, 7, 12, 13
                                sendEvGreenTo(lightsNumbers);
                                inWeekdayState = InWeekdayState.GREEN_0;
                                sender = -1;
                                break;
                            } else {
                                // lightsNumbers = Arrays.asList(0, 1, 6, 7, 9, 10, 12, 13);
                                lightsNumbers = Arrays.asList(1, 6, 7, 12, 13);// IS GREEN : 1, 2, 4, 5, 6, 7, 12, 13
                                sendEvRedTo(lightsNumbers);
                                waitAckFrom(lightsNumbers);
                                // lightsNumbers = Arrays.asList(2, 3, 4, 5, 8, 11, 14, 15);
                                lightsNumbers = Arrays.asList(3, 8, 11, 14, 15);// IS GREEN : 2, 4, 5
                                sendEvGreenTo(lightsNumbers);
                                inWeekdayState = InWeekdayState.GREEN_2_3;
                                sender = -1;
                                break;
                            }
                        }
                        break;

                    case GREEN_2_3:
                        while (true) {
                            System.out.println("Im here! GREEN_2_3");
                            if (evShabbat.arrivedEvent()) {
                                evShabbat.waitEvent();
                                // IS GREEN : 2, 3, 4, 5, 8, 11, 14, 15
                                lightsNumbers = Arrays.asList(2, 3, 4, 5, 8, 11, 14, 15);
                                sendEvRedTo(lightsNumbers);
                                waitAckFrom(lightsNumbers);
                                // sendEvRedToAll();
                                // waitAckFromAll();
                                sendEvShabbatToAll();
                                out = true;
                                outState = OutState.SHABBAT;
                                break;
                            }
                            try {
                                sleep(greenTime);
                            } catch (InterruptedException exce) {
                            }
                            // evRed(1, 2, 3, 4, 5, 8, 11, 14, 15);
                            // lightsNumbers = Arrays.asList(1, 2, 3, 4, 5, 8, 11, 14, 15);
                            lightsNumbers = Arrays.asList(2, 3, 4, 5, 8, 11, 14, 15);// IS GREEN : 2, 3, 4, 5, 8, 11,
                                                                                     // 14, 15
                            sendEvRedTo(lightsNumbers);
                            waitAckFrom(lightsNumbers);
                            // lightsNumbers = Arrays.asList(0, 6, 7, 9, 10, 12, 13);
                            lightsNumbers = Arrays.asList(0, 6, 7, 9, 10, 12, 13);// IS GREEN : -
                            sendEvGreenTo(lightsNumbers);
                            // evGreen(0, 6, 7, 9, 10, 12, 13);
                            inWeekdayState = InWeekdayState.GREEN_0;
                            break;

                        }
                        break;
                    }
                }

                break;

            case SHABBAT:
                evShabbat.waitEvent();
                // sendEvRedToAll();
                out = false;
                sendEvShabbatToAll();
                outState = OutState.WEEKDAY;
                inWeekdayState = InWeekdayState.ALL_RED;
                break;
            }
        }

    }

    private void sendEvRedToAll() {
        List<Integer> lightsNumbers = new ArrayList<Integer>(eventsNumber);

        for (int i = 0; i < eventsNumber; i++) {
            lightsNumbers.add(i);
        }

        sendEvRedTo(lightsNumbers);
    }

    private void sendEvRedTo(List<Integer> lightsNumbers) {
        System.out.println(lightsNumbers.toString());

        for (int i : lightsNumbers) {
            System.out.println("Sending red " + i);
            evReds[i].sendEvent();
        }
    }

    private void sendEvGreenTo(List<Integer> lightsNumbers) {
        for (int i : lightsNumbers) {
            System.out.println("Sending green " + i);
            evGreens[i].sendEvent();
        }
    }

    private void waitAckFromAll() {
        List<Integer> lightsNumbers = new ArrayList<Integer>(eventsNumber);

        for (int i = 0; i < eventsNumber; i++) {
            lightsNumbers.add(i);
        }

        waitAckFrom(lightsNumbers);
    }

    private void waitAckFrom(List<Integer> lightsNumbers) {
        for (int i : lightsNumbers) {
            System.out.println("Waiting ack " + i);
            evAcks[i].waitEvent();
        }
    }

    private void sendEvShabbatToAll() {
        for (int i = 0; i < eventsNumber; i++) {
            evShabbats[i].sendEvent();
        }
    }

}