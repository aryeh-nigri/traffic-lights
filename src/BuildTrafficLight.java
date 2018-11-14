import java.util.HashMap;

import javax.swing.JRadioButton;

/*
 * Created on Mimuna 5767  upDate on Addar 5772 
 */

/**
 * @author �����
 */
public class BuildTrafficLight {

	public static void main(String[] args) {
		final int numOfLights = 4 + 12 + 1;
		Ramzor ramzorim[] = new Ramzor[numOfLights];
		TrafficLightFrame tlf = new TrafficLightFrame(" ���''� installation of traffic lights", ramzorim);

		Event button = new Event();
		Event evShabbat = new Event();

		MyActionListener myListener = new MyActionListener(button, evShabbat);

		Controller controller = new Controller(tlf, ramzorim, button, evShabbat);
		controller.start();


		JRadioButton butt[] = new JRadioButton[13];

		for (int i = 0; i < butt.length - 1; i++) {
			butt[i] = new JRadioButton();
			butt[i].setName(Integer.toString(i + 4));
			butt[i].setOpaque(false);
			butt[i].addActionListener(myListener);
			tlf.myPanel.add(butt[i]);
		}
		butt[0].setBounds(620, 30, 18, 18);
		butt[1].setBounds(620, 218, 18, 18);
		butt[2].setBounds(620, 267, 18, 18);
		butt[3].setBounds(620, 447, 18, 18);
		butt[4].setBounds(566, 495, 18, 18);
		butt[5].setBounds(162, 608, 18, 18);
		butt[6].setBounds(213, 495, 18, 18);
		butt[7].setBounds(240, 457, 18, 18);
		butt[8].setBounds(220, 443, 18, 18);
		butt[9].setBounds(220, 267, 18, 18);
		butt[10].setBounds(220, 218, 18, 18);
		butt[11].setBounds(220, 30, 18, 18);

		butt[12] = new JRadioButton();
		butt[12].setName(Integer.toString(16));
		butt[12].setBounds(50, 30, 55, 20);
		butt[12].setText("���");//shabbat
		butt[12].setOpaque(false);
		butt[12].addActionListener(myListener);
		tlf.myPanel.add(butt[12]);
	}
}
