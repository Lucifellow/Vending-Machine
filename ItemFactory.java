import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import javax.swing.event.*;

/** A factory that creates {@link VendItem}s.
	@author Jeremy Hilliker - jhilliker at langara
	@version 2018-03-17a
*/
public class ItemFactory {

	private final static String ITEMS_FILENAME = "items.csv";
	private final static Pattern delimCSV = Pattern.compile(",");

	/** Load a stream of items from a default file.
		Items are listed one per line where each item is formatted as:
			name,price,quantity
		@return a {@link Stream} of {@link VendItem} loaded from a data file
		@throws IOException when there is an error reading from the file
		@throws NumberFormatException when a number field is not a number
		@throws ArrayIndexOutOfBoundsException when an input line does not have enough elements
	*/
	public static Stream<VendItem> getItemsAsStream() throws IOException {
		URL itemsCSV = ItemFactory.class.getResource(ITEMS_FILENAME);
		if(itemsCSV == null) {
			throw new FileNotFoundException(ITEMS_FILENAME);
		}
		try {
			Path p = Paths.get(itemsCSV.toURI());
			return getItemsAsStream(p);
		} catch(URISyntaxException e) {
			throw new IOException(e);
		}
	}

	/** See {@link #getItemsAsStream()}.
		@see #getItemsAsStream()
		@param itemsCSV the file from which to load the items
		@return a {@link Stream} of {@link VendItem} loaded from the data file
		@throws IOException when there is an error reading from the file
		@throws NumberFormatException when a number field is not a number
		@throws ArrayIndexOutOfBoundsException when an input line does not have enough elements
	*/
	public static Stream<VendItem> getItemsAsStream(Path itemsCSV) throws IOException {
		return Files.lines(itemsCSV)
			.filter(l -> !l.startsWith("#"))
			.map(delimCSV::split)
			.map(VendItemImpl::new);
	}

	private static class VendItemImpl implements VendItem {

		private final String name;
		private final double price;
		private int qty;

		private ChangeListener cl;
		private ChangeEvent changeEvent;

		private VendItemImpl(String[] fields) {
			this(fields[0].trim(),
				Double.parseDouble(fields[1]),
				Integer.parseInt(fields[2]));
		}

		public VendItemImpl(String n, double p, int q) {
			this.name = n;
			this.price = p;
			this.qty = q;
			this.cl = null;
			this.changeEvent = null;
		}

		@Override public String getName() { return name; }
		@Override public double getPrice() { return price; }
		@Override public int getQuantity() { return qty; }
		@Override public boolean hasStock() { return getQuantity() > 0; }
		@Override public void decrementQuantity() {
			qty--;
			if(cl != null) { cl.stateChanged(changeEvent); }
		}
		@Override public void setListener(ChangeListener listener) {
			if(cl == null) {
				cl = Objects.requireNonNull(listener);
				changeEvent = new ChangeEvent(VendItemImpl.this);
			} else {
				throw new IllegalStateException("Already has listener: " + cl);
			}
		}
	}
}
