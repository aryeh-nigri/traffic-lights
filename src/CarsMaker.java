import javax.swing.JPanel;

public class CarsMaker extends Thread {
	JPanel myPanel;
	private CarLight myRamzor;
	int key;

	public CarsMaker(JPanel myPanel, CarLight myRamzor, int key) {
		this.myPanel = myPanel;
		this.myRamzor = myRamzor;
		this.key = key;
		setDaemon(true);
		start();
	}

	public void run() {
		try {
			while (true) {
				sleep(300);
				if (!myRamzor.isStop()) {
					new CarMooving(myPanel, myRamzor, key);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
