package files_Iterator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import model.Client;
import model.Product;
import model.Store;
import model.sortType;

public class ProductsFile implements Iterable<Map.Entry<String, Product>> {
	final String FILE_NAME = "products.txt";
	private RandomAccessFile rafWrite;
	private RandomAccessFile rafRead;
	private File myFile;
	private long startPos;
	private long currPosRead;
	private long currPosWrite;
	private long sizeRecordWrited;
	private long endOfFile;
	private long prevPosReaded;
	private Entry<String, Product> myEntryTemp;

	public ProductsFile() throws FileNotFoundException, IOException {
		myFile = new File(FILE_NAME);
		rafWrite = new RandomAccessFile(myFile, "rw");
		rafRead = new RandomAccessFile(myFile, "r");
		endOfFile = myFile.length();
		myEntryTemp = new MyEntry<String, Product>();

	}

	public MyEntry readEntryFromFile() {

		MyEntry<String, Product> entry;
		String code = null;
		Product p = null;
		try {
			prevPosReaded = rafRead.getFilePointer();
			code = rafRead.readUTF();
			p = new Product(rafRead.readUTF(), rafRead.readInt(), rafRead.readInt(), null);
			if (rafRead.readBoolean()) {
				p.setBuyer(new Client(rafRead.readUTF(), rafRead.readUTF(), rafRead.readBoolean()));
			}
			currPosRead = rafRead.getFilePointer();
			sizeRecordWrited = currPosRead - prevPosReaded + 1;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		entry = new MyEntry(code, p);
		return entry;
	}

	public void writeToFileProduct(String sku, Product p) {
		try {
			rafWrite.seek(currPosWrite);
			rafWrite.writeUTF(sku);
			rafWrite.writeUTF(p.getNameP());
			rafWrite.writeInt(p.getCostPrice());
			rafWrite.writeInt(p.getSellPrice());
			if (p.getBuyer() != null) {
				rafWrite.writeBoolean(true);
				rafWrite.writeUTF(p.getBuyer().getName());
				rafWrite.writeUTF(p.getBuyer().getPhone());
				rafWrite.writeBoolean(p.getBuyer().isGetUpdate());
			} else {
				rafWrite.writeBoolean(false);
			}
			currPosWrite = rafWrite.getFilePointer();
		} catch (IOException e) {

			System.out.println(e.getMessage());
		}
	}

	public void writeToFileSortType(sortType st) {
		try {
			rafWrite.writeUTF(st.name());
			startPos = rafWrite.getFilePointer();
			currPosRead = startPos;
			currPosWrite = startPos;
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}

	}

	public sortType readFromFileSortType() {

		try {
			String sort = rafRead.readUTF();
			startPos = rafRead.getFilePointer();
			return sortType.getSortType(sort);
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;

	}

	public void addProductToFile(String prevCode, String currCode, Product currPro) {
		Iterator<Entry<String, Product>> iterator = iterator();

		if (!iterator.hasNext()) {
			writeToFileProduct(currCode, currPro);
			return;
		} else if (prevCode == null && iterator.hasNext()) {
			myEntryTemp = iterator.next();
			writeToFileProduct(currCode, currPro);
			replaceRecordTilEnd(iterator);
			return;
		}
		boolean flagToContinue = false;
		while (iterator.hasNext()) {
			myEntryTemp = iterator.next();
			if (myEntryTemp.getKey().equals(prevCode)) {
				currPosWrite = currPosRead;
				if (iterator.hasNext()) {
					myEntryTemp = iterator.next();
					flagToContinue = true;
				}
				writeToFileProduct(currCode, currPro);
				break;
			}
		}
		if (flagToContinue) {
			replaceRecordTilEnd(iterator);

		}
	}

	public void replaceProductInFile(String currCode, Product newPro) {
		Iterator<Entry<String, Product>> iterator = iterator();
		boolean flagToContinue = false;
		while (iterator.hasNext()) {
			currPosWrite = currPosRead;
			myEntryTemp = iterator.next();
			if (myEntryTemp.getKey().equals(currCode)) {
				if (iterator.hasNext()) {
					myEntryTemp = iterator.next();
					flagToContinue = true;
				}
				writeToFileProduct(currCode, newPro);
				break;
			}
		}
		if (flagToContinue) {
			replaceRecordTilEnd(iterator);

		} else {
			if (currPosWrite < endOfFile) {
				try {
					rafWrite.setLength(endOfFile - (endOfFile - currPosWrite));
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			}

		}

	}

	public void removeProsuctFromFile(String currCode) {
		Iterator<Entry<String, Product>> iterator = iterator();
		while (iterator.hasNext()) {
			currPosWrite = currPosRead;
			myEntryTemp = iterator.next();
			if (myEntryTemp.getKey().equals(currCode)) {
				iterator.remove();
			}
		}
	}

	public void replaceRecordTilEnd(Iterator<Entry<String, Product>> iterator) {
		Map.Entry<String, Product> myEntryCurr = new MyEntry();
		while (iterator.hasNext()) {
			myEntryCurr = myEntryTemp;
			myEntryTemp = iterator.next();
			writeToFileProduct((String) myEntryCurr.getKey(), (Product) myEntryCurr.getValue());
		}
		if (myEntryTemp != null) {
			writeToFileProduct(myEntryTemp.getKey(), myEntryTemp.getValue());
		}
		if (currPosWrite < endOfFile) {
			try {
				rafWrite.setLength(endOfFile - (endOfFile - currPosWrite));
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

	}

	@Override
	public Iterator<Map.Entry<String, Product>> iterator() {

		try {
			return new IteratorFile();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}

	class IteratorFile implements Iterator<Entry<String, Product>> {

		public IteratorFile() throws FileNotFoundException, IOException {
			rafRead.seek(startPos);
			currPosWrite = startPos;
			currPosRead = startPos;
			endOfFile = myFile.length();
		}

		@Override
		public boolean hasNext() {
			return currPosRead < endOfFile ? true : false;
		}

		@Override
		public MyEntry next() {
			return readEntryFromFile();
		}

		public void remove() {
			try {
				if (hasNext()) {
					myEntryTemp = next();
					replaceRecordTilEnd(this);
				} else {
					rafWrite.setLength(endOfFile - (currPosRead - currPosWrite));
				}
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}

	}

	public void loadDataFromFile(Store store) {
		sortType type;
		try {
			if (rafRead.length() == 0) {
				return;
			} else {
				type = readFromFileSortType();
				if (myFile.length() == startPos) {
					rafRead.setLength(0);
				} else {
					store.setSortType(type);
					readMapFromFile(store);
				}
			}
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	public void readMapFromFile(Store store) {
		Iterator<Entry<String, Product>> itFile = iterator();
		int totalCostMoney = 0, totalNumProducts = 0, totalNumSells = 0, totalSellMoney = 0;
		while (itFile.hasNext()) {
			myEntryTemp = itFile.next();
			store.getProductMap().put(myEntryTemp.getKey(), myEntryTemp.getValue());
			totalCostMoney += myEntryTemp.getValue().getCostPrice();
			totalNumProducts++;
			if (myEntryTemp.getValue().getBuyer() != null) {
				store.addClientToDB(myEntryTemp.getValue().getBuyer().getPhone(), myEntryTemp.getValue().getBuyer(),
						false);
				totalSellMoney += myEntryTemp.getValue().getSellPrice();
				totalNumSells++;
			}
		}
		store.setTotalCostMoney(totalCostMoney);
		store.setTotalNumProducts(totalNumProducts);
		store.setTotalNumSells(totalNumSells);
		store.setTotalSellMoney(totalSellMoney);

	}
}
