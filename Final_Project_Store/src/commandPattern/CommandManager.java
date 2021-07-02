package commandPattern;

import java.util.Stack;

import controller.StoreController;

public class CommandManager {
	private Stack<ICommand> undos = new Stack<ICommand>();
	private Stack<ICommand> undosClient = new Stack<ICommand>();

	private ICommand unPushedCommand;

	public CommandManager() {
	}

	public void executeCommand(ICommand c, boolean pushVal) {
		c.execute();

		if (pushVal) {
			undos.push(c);
		} else {
			unPushedCommand = c;
		}
	}

	public boolean isUndoAvailable() {
		return !undos.empty();
	}

	public void undo(boolean unPushedComm) {

		if (unPushedComm) {
			unPushedCommand.undo();
		} else {
			assert (!undos.empty());
			ICommand command = undos.pop();
			command.undo();
		}
	}

	public void undoClient(boolean b) {
		if (!b) {
			unPushedCommand.undo();
			return;
		}
		assert (!undosClient.empty());
		ICommand command = undosClient.pop();
		command.undo();

	}

	public boolean isUndoClientAvailable() {
		return !undosClient.empty();
	}

	public void executeCommandClient(ICommand c, boolean b) {
		c.execute();
		if (!b) {
			if (c instanceof BtnAddUndoClientCommand) {
				return;
			}
			unPushedCommand = c;
			return;
		}
		undosClient.push(c);
	}
}
