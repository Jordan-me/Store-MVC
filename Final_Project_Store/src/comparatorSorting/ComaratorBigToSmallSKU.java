package comparatorSorting;

import java.util.Comparator;

public class ComaratorBigToSmallSKU implements Comparator<String> {

	@Override
	public int compare(String sku1, String sku2) {
		return sku2.compareTo(sku1);
	}

}
