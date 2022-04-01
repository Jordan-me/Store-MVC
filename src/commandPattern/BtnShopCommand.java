package commandPattern;

import java.util.Vector;

import listeners.StoreUIeventListener;

public class BtnShopCommand implements ICommand {
	Vector<StoreUIeventListener> allListeners = new Vector<StoreUIeventListener>();

	public BtnShopCommand(Vector<StoreUIeventListener> allListeners) {
		this.allListeners = allListeners;
	}

	@Override
	public void execute() {
		for (StoreUIeventListener l : allListeners) {
			l.show(3);
		}

	}

	@Override
	public void undo() {
		for (StoreUIeventListener l : allListeners) {
			l.show(1);
		}

	}

}
