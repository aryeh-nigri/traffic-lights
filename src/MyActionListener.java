import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JRadioButton;

/*
 * Created on Tevet 5770 
 */

/**
 * @author �����
 */

public class MyActionListener implements ActionListener {

	private Event button, evShabbat;

	public MyActionListener(Event button, Event evShabbat) {
		this.button = button;
		this.evShabbat = evShabbat;
	}

	public void actionPerformed(ActionEvent e) {

		JRadioButton butt = (JRadioButton) e.getSource();
		int buttonNumber = Integer.parseInt(butt.getName());

		System.out.println("Button pressed: " + buttonNumber);

		// butt.setEnabled(false);
		// butt.setSelected(false);
		if (buttonNumber == 16) {
			evShabbat.sendEvent();
		} else {
			button.sendEvent(buttonNumber);
		}
	}

}
