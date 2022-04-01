package comparatorSorting;

import java.util.Map;
import java.util.TreeMap;

public class MapUtil {
	/**
	 * Sort unSortedMap in generic way
	 * cotract:
	 * @param m - unSortedMap, comparator<String>(all my comparator are String)
	 * @throws nothing
	 */
	public static <K, V> Map<K, V> sortByKeys(Map<K, V> unsortedMap) {
		return new TreeMap<>(unsortedMap);
	}
}
