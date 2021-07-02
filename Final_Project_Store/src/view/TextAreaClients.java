package view;

import java.io.OutputStream;
import java.util.ArrayList;

import client_observer.Sender;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class TextAreaClients extends OutputStream   implements Runnable{
	 private final  TextArea destination;
	 private Stage theStage;
	 private Scene theScene;
	private ArrayList<String> myPrint = new ArrayList<String>();


	    public TextAreaClients (String msg, TextArea destination)
	    {
	    	theStage = new Stage();
	        if (destination == null) {
	            throw new IllegalArgumentException ("Destination is null");
	        }
	       myPrint.addAll(Sender.getInstance().getAnswer());
	       Sender.getInstance().removeAnswer();
	        this.destination = destination;
	        destination.setEditable(false);
	        destination.appendText("Message sent: " + msg + "\n\nSubscribed Client:\n");
	        theScene = new Scene(destination);
	        theStage.setScene(theScene);
	        
	        theStage.show();
	        
	    }

		@Override
		public void write(int militime) {
		 if(myPrint.isEmpty()) {
			 destination.appendText("\nWe are Sorry But you dont have any clients\n");
		 }
			for(String s: myPrint) {
				try {
					Thread.sleep(militime*1000);
				} catch (InterruptedException e) {
					System.out.println(e.getMessage());
				}
				destination.appendText("\n" + s);
			}
			
		}

		@Override
		public void run() {
			write(2);			
		}
 
}
