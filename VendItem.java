import javax.swing.event.ChangeListener;

/** An item for selling.
	@author Jeremy Hilliker - jhilliker at langara
	@version 2018-03-17a
*/
public interface VendItem {

	/** @return the name of the item */
	String getName();

	/** @return the price of the item */
	double getPrice();

	/** @return the quantity of the items available for sale */
	int getQuantity();

	/** @return true if any of these items are in stock (available for sale),
		false otherwise */
	boolean hasStock();

	/** Reduces the stock of this item by one.
		Notifies the registered listener of the state change. */
	void decrementQuantity();

	/** Sets a listener to be notified of a change in this item's stock level
		(the quantity available).
		@param cl the listener to notify
		@throws IllegalStateException if there is already a listener
	*/
	void setListener(ChangeListener cl);
}
