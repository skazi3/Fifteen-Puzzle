import javax.swing.JButton;

public class MyButton extends JButton{
	private JButton button;
	private String id;
	public MyButton() {
		button = new JButton();
	}
	public MyButton(String s) {
		super (s);
		id = s;
	}
	private String getID() {
		return id;
	}

	
}
