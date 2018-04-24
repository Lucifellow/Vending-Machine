import java.util.function.Consumer;
import java.awt.*;
import javax.swing.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

/** Displays the elements to sell a selected item. <ul>
		<li>Displays the item info: name, price, stock (quantity available). (JLabel)</li>
		<li>Has a space for the user to enter payment, and a vend button. (JTextField, JButton)</li>
		<li>Has a button to return coins, and a coin return. (JButton, JTextField)</li>
		<li>Has a vending area to provide output. (JTextArea: 5x40)</li>
	</ul>

	Behaviour of "return coins" button: <ul>
		<li>Clears the payment field and adds that value to the return coins field.</li>
		<li>If the payment field is invalid, display a message in the vend area.</li>
	</ul>

	Behaviour of coin return area: <ul>
		<li>When coins are returned, adds them to the coin return area's current amount.</li>
		<li>Existing invalid values in the coin return area are overwritten as if it held nothing.</li>
		<li>Existing negative values in the coin return area are overwritten as if it held nothing.</li>
	</ul>

	Behaviour of "VEND" button: <ul>
		<li>Enabled/disabled depending on if there is a
			selection, and if that selection has stock.</li>
		<li>Displays an error message when an invalid payment amount is entered.
			Does not clear payment area.
			Does not vend product.</li>
		<li>Displays an error message when insufficient payment is entered.
			Does not clear payment area.
			Does not vend product.</li>
		<li>Vends product when sufficient payment is entered.
			Clears payment area.
			Decrements stock of item.
			Adds overpayment (change) to the coin return area.
			Sets no item as selected.</li>
	</ul>
	
	@author Hrishikesh Vyas
	@author Jeremy Hilliker - jhilliker at langara
	@version 2018-03-30
*/
@SuppressWarnings("serial")
public class SalesComp extends JComponent {

	private JLabel txtOutItem;
	private JTextField txtInPayment;
	private JButton btnInVend;
	private JButton btnInReturnCoins;
	private JTextField txtOutCoinReturn;
	private JTextArea txtOutVendProduct;
	private VendItem item;

	public SalesComp() {
		setLayout(new GridLayout(3,1));			//main panel layout 
		JPanel top = new JPanel();					//for JLabel
		JPanel middle = new JPanel();				//For txtInPayment and btnInVend
		JPanel bottom = new JPanel();				//for txtOutVendProduct, txtOutCoinReturn, btnInReturnCoins
		JPanel coinsPanel = new JPanel();		// for txtOutCoinReturn, btnInReturnCoins
		
		add(top);
		top.setLayout(new GridLayout(1,1));
		top.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),"Selection"));		//Reference: https://docs.oracle.com/javase/7/docs/api/javax/swing/BorderFactory.html
		txtOutItem= new JLabel("Select Product to Vend.");
		top.add(txtOutItem);
		
		add(middle);
		middle.setLayout(new BorderLayout());
		txtInPayment = new JTextField();
		middle.add(txtInPayment,BorderLayout.NORTH);
		btnInVend = new JButton("Vend");
		btnInVend.setEnabled(false);
		btnInVend.addActionListener(ae -> vend());
		middle.add(btnInVend,BorderLayout.CENTER);
		middle.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder(),"Insert Coins:"));			//Reference: //Reference: https://docs.oracle.com/javase/7/docs/api/javax/swing/BorderFactory.html
		
		add(bottom);
		bottom.setLayout(new BorderLayout());
		btnInReturnCoins = new JButton("Return Coins");
		btnInReturnCoins.addActionListener(ae -> returnCoins());
		
		coinsPanel.setLayout(new GridLayout(1,2));
		coinsPanel.add(btnInReturnCoins);
		txtOutCoinReturn = new JTextField();
		coinsPanel.add(txtOutCoinReturn);
		bottom.add(coinsPanel,BorderLayout.NORTH);
		txtOutVendProduct = new JTextArea(5, 40);
		bottom.add(txtOutVendProduct,BorderLayout.CENTER);
	}

	/** Sets the selected item. <ul>
			<li>Updates the selection display.</li>
			<li>Clears the output area.</li>
			<li>enables/disables the vend button as appropriate</li>
		</ul>
		@param item the selected item. null indicates no selection.
	*/
	public void setSelected(VendItem item) {
		this.item = item;
		
		txtOutItem.setFont(new Font(Font.MONOSPACED,Font.PLAIN,16));		//Reference: http://www.java2s.com/Tutorial/Java/0240__Swing/SetFontandforegroundcolorforaJLabel.htm 
		txtOutItem.setText("<html>" + "Item: " + item.getName()
								+"<br />" +"Price: " + " $ " + item.getPrice() 
								+"<br />"+"Stock: " +  item.getQuantity() + "</html>");
		btnInVend.setEnabled(true);			//Enable vend button when an item is selected
	}

	private void returnCoins() {
		try {
			if(parseDouble(txtInPayment.getText()) < 0) {			//If the value in txtInPayment is negative
				throw new NumberFormatException();
			}
			addToCoinReturn(parseDouble(txtInPayment.getText()));			//Add coins in txtInPayment to txtOutCoinReturn and throws NumberFormatException if it is not a double number
		} catch (NumberFormatException e) {
			txtOutVendProduct.setFont(new Font(Font.MONOSPACED,Font.PLAIN,16));		//Reference: http://www.java2s.com/Tutorial/Java/0240__Swing/SetFontandforegroundcolorforaJLabel.htm 
			txtOutVendProduct.setText("Invalid Payment amount:"
											+ "\n \n \t" + txtInPayment.getText()
											+ "\n \n" + "Only feed me money please." );
		} finally {
			txtInPayment.setText("");		//Clear text from txtInPayment after adding it to txtOutCoinReturn
		}
	}

	private void addToCoinReturn(double d) {
		txtOutCoinReturn.setText("" + d);			//adds txtInPayment to txtOutCoinReturn
	}

	private void vend() {
		if(txtInPayment.getText().equals("")) {		//if txtInPayment is empty based on answer provided by user3148337 on https://stackoverflow.com/questions/17132452/java-check-if-jtextfield-is-empty-or-not
			txtOutVendProduct.setFont(new Font(Font.MONOSPACED,Font.PLAIN,16));		//Reference: http://www.java2s.com/Tutorial/Java/0240__Swing/SetFontandforegroundcolorforaJLabel.htm 
			txtOutVendProduct.setText("Please Enter Payment.");
			return;
		}
		try{
			if(parseDouble(txtInPayment.getText()) < item.getPrice()){		//If entered amount is less than price of item
				throw new IllegalStateException();
			}
			if(parseDouble(txtInPayment.getText()) >= item.getPrice()) {			//If entered amount is more than or equal to the price of item
				txtOutCoinReturn.setText("" + (parseDouble(txtInPayment.getText()) - item.getPrice()));		//Return remaining coins
				txtOutVendProduct.setFont(new Font(Font.MONOSPACED,Font.PLAIN,16));		//Reference: http://www.java2s.com/Tutorial/Java/0240__Swing/SetFontandforegroundcolorforaJLabel.htm 
				txtOutVendProduct.setText("****** VEND *****" + "\n \n"
												+ item.getName() + "\n \n"
												+ "***** **** *****"
												);
				txtInPayment.setText("");
				item.decrementQuantity();		//Decrease stock by 1
				txtOutItem.setText("Select Product to Vend.");	//reset label
				btnInVend.setEnabled(false);		//disable vend button
			}  	//catches an exception when the entered amount is not a number
		} catch(NumberFormatException e) {
			txtOutVendProduct.setFont(new Font(Font.MONOSPACED,Font.PLAIN,16));		//Reference: http://www.java2s.com/Tutorial/Java/0240__Swing/SetFontandforegroundcolorforaJLabel.htm 
			txtOutVendProduct.setText("Invalid Payment amount:"
											+ "\n \n \t" + txtInPayment.getText()
											+ "\n \n" + "Only feed me money please." );
															//When amount entered is less than price of item
		} catch(IllegalStateException e) {
			txtOutVendProduct.setFont(new Font(Font.MONOSPACED,Font.PLAIN,16));		//Reference: http://www.java2s.com/Tutorial/Java/0240__Swing/SetFontandforegroundcolorforaJLabel.htm
			txtOutVendProduct.setText(String.format("You have not enough money \n \n Gave: %.2f \n Need: %.2f ", parseDouble(txtInPayment.getText()) , item.getPrice()));	//Based on provided notes
		}
	}

	private static double parseDouble(String s) throws NumberFormatException {
		return Double.parseDouble(s.trim().replace("$", "").replace(",", "."));
	}
}
