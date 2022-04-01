package memento;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class History {

	private List<ProductMemento> states = new ArrayList<>();
	private Map<ProductMemento, ProductMemento> replacedStates = new LinkedHashMap<>();

	public void push(ProductMemento proMem) {
		states.add(proMem);
	}

	public ProductMemento[] pop() {
		ProductMemento arr[] = new ProductMemento[2];
		int lastIndex = states.size() - 1;
		ProductMemento lastState = states.get(lastIndex);
		states.remove(lastState);
		arr[0] = lastState;
		if (replacedStates.containsKey(lastState)) {
			arr[1] = replacedStates.get(lastState);
			push(arr[1]);
			replacedStates.remove(lastState);
		}
		return arr;
	}

	public void replace(ProductMemento newMemento, ProductMemento oldMemento) {
		replacedStates.put(newMemento, oldMemento);
		states.remove(oldMemento);
		push(newMemento);
	}

	public void remove(String proCode) {
		for (ProductMemento p : states) {
			if (p.getCode().equals(proCode)) {
				if (replacedStates.get(p) != null) {
					replacedStates.remove(p);
				}
				states.remove(p);
				return;
			}
		}
	}
}
