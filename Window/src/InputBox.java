import java.io.BufferedWriter;
import java.io.IOException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

class InputBox {

	GridLayout boxLayout;
	Composite boxContent;
	Group box;
	public String inputEingabe = "";
	public boolean newInput;
	private BufferedWriter bw;

	InputBox(Composite parent) 
	{
		boxContent = parent;
		createBox();
	}
	
	 void createBox()
	 {
		 box = new Group(boxContent, SWT.None);
		 boxLayout = new GridLayout();
		 boxLayout.numColumns=3;
		 box.setLayout(boxLayout);
		 createContents();
	 }
	
	 void createContents()
	 {
		new Label(box, SWT.HORIZONTAL).setText("Eingabe:");
		final Text text = new Text(box, SWT.BORDER);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 1;
		text.setLayoutData(data);
		 
		Button script = new Button(box, SWT.PUSH);
		script.setText("R-Script");
		
		Button button = new Button(box, SWT.PUSH);
		button.setText("Enter");
		
		GridData gridData = new GridData();
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		box.setLayoutData(gridData);
		
		/*
		text.addKeyListener(new KeyListener()
		{
			public void keyPressed(KeyEvent e)
			{
			}

			@Override
			public void keyReleased(KeyEvent arg0) 
			{
				if(arg0.keyCode == SWT.ESC)
				{
					inputEingabe = text.getText() + "\n";
					pasteToBufferedWriter(inputEingabe);
					text.setText("");
				}
			}
				
		});*/
		
		button.addMouseListener(new MouseAdapter()
		{
			//Writes the text 
			public void mouseUp(MouseEvent e)
			{
				inputEingabe = text.getText() + "\n";
				pasteToBufferedWriter(inputEingabe);
				text.setText("");
			}

		});
		
		script.addMouseListener(new MouseAdapter()
		{
			//Writes the text 
			public void mouseUp(MouseEvent e)
			{
				new SrciptFinder((Shell)boxContent, bw);
			}
		});
	 }
	 	 
	 public void setVisible(boolean visible)
	 {
		 this.box.setVisible(visible);
	 }
	 
	 public void setBufferedWriter(BufferedWriter bw)
	 {
		 this.bw = bw;
	 }
	 
	 public BufferedWriter getBufferedWriter()
	 {
		 return this.bw;
	 }
	 
	 private void pasteToBufferedWriter(String input)
	 {
		 try {
				bw.write(input);
				bw.flush();

			} catch (IOException e1) {
				System.out.println("BufferedWriter is null");
				e1.printStackTrace();
			}
	 }
	 
}