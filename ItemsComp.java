import java.io.IOException;
import java.util.stream.Stream;
import java.util.function.Consumer;
import java.awt.*;
import javax.swing.*;

/** Displays a bunch of items for selection.<ul>
		<li>At least 16 items must be displayed.</li>
		<li>All 16 items must be displayed at all times.</li>
		<li>Items must display their name and price.</li>
		<li>Items must somehow indicate that are out of stock,
			and must not be selectable.</li>
		<li>A dialog box shows an error message if the items cannot be loaded.
			{@link JOptionPane#showMessageDialog(Component, Object, String, int)}</li>
	</ul>

	@author Hrishikesh Vyas
	@author Jeremy Hilliker - jhilliker at langara
	@version 2018-03-30
*/
@SuppressWarnings("serial")
public class ItemsComp extends JComponent {

	private final static int NUM_ROWS = 4;
	private final static int NUM_COLS = 4;

	private final Consumer<VendItem> selectionNotice;

	/**
		@param selectionNotice notify this callback when an item becomes selected
	*/
	public ItemsComp(Consumer<VendItem> selectionNotice) {
		this.selectionNotice = selectionNotice;
		// TODO
		setLayout(new GridLayout(NUM_ROWS,NUM_COLS));
		setBorder(BorderFactory.createTitledBorder
			(BorderFactory.createRaisedBevelBorder(),"Select an Item:"));
		try{
			ItemFactory.getItemsAsStream()						//make buttons for every venditem in stream
			.forEach(a -> add(makeButton(a)));					//Throws IO exception when there is no .csv file
		}
		catch(IOException e){										//Display a dialog message
			JOptionPane.showMessageDialog(this,e,"Error opening file",0);		//0 indicates error message 		//Reference: https://docs.oracle.com/javase/8/docs/api/javax/swing/JOptionPane.html#showMessageDialog-java.awt.Component-java.lang.Object-java.lang.String-int-
		}
	}

	private JButton makeButton(VendItem vi) {
		
		JButton b = new JButton();	
		
		b.setText("<html>"+vi.getName()+"<br/>" + "<center>" + "$"+vi.getPrice()+"</html>");			//code for html based on provided notes
		vi.setListener(ae -> b.setEnabled(vi.hasStock()));			//Checks if the VendItem has positive amount of stock and enables the JButton if true or else disables the button
		b.addActionListener(ae -> selectionNotice.accept(vi));		//Based on explanation given by Jeremy in class for Consumer<T>	//Reference: https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html#accept-T-
		return b;
	}
}
