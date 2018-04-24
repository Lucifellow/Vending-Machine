import java.awt.*;
import javax.swing.*;

/** The main vending machine component;
	displays the item selection area, and the item sale area.
	@author Hrishikesh Vyas
	@author Jeremy Hilliker - jhilliker at langara
	@version 2018-03-30
*/
@SuppressWarnings("serial")
public class VendComp extends JComponent {

	public VendComp() {
		// TODO
		setLayout(new BorderLayout());
		SalesComp right = new SalesComp();
														//Below code for consumer is based on explanation given by Jeremy in class for the callback consumer method(setter methods)
		add(new ItemsComp(ae -> right.setSelected(ae)), BorderLayout.WEST);		
		add(right, BorderLayout.CENTER);
	}
}
