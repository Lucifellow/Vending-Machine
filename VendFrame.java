import javax.swing.JFrame;

/** A vending machine.
	CPSC 1181 201810 assignment #08: GUIs

	To compile and run:
		{@code javac *.java && java VendFrame}

	@author Jeremy Hilliker - jhilliker at langara
	@version 2018-03-17a
*/

@SuppressWarnings("serial")
public class VendFrame extends JFrame {

	public VendFrame() {
		setContentPane(new VendComp());
	}

	public static void main(String[] args) {
		JFrame f = new VendFrame();
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setTitle("Vending Machine. CPSC 1181 201810 assignment #08: GUIs");
		f.pack(); // does layout of all components
		f.setVisible(true);
	}
}
