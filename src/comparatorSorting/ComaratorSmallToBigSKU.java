package comparatorSorting;

import java.util.Comparator;

public class ComaratorSmallToBigSKU implements Comparator<String> {

	@Override
	public int compare(String sku1, String sku2) {

		return sku1.compareTo(sku2);
	}

}
