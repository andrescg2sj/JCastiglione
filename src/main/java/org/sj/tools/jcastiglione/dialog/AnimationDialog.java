package org.sj.tools.jcastiglione.dialog;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AnimationDialog extends Dialog {
	
	TextField tfFrames;
	Button btnOk;
	Button btnCancel;
	int frames;

	public AnimationDialog(Frame f, int frames)
	{
		super(f);
		this.setModal(true);
		this.setTitle("Animation settings");
		this.setSize(300, 200);
		setLayout(new GridLayout(2,2));
		add(new Label("Frames"));
		tfFrames = new TextField("" + frames);
		add(tfFrames);
		btnOk = new Button("Ok");
		btnCancel = new Button("Canel");
		add(btnOk);
		add(btnCancel);
		
		 addWindowListener(new WindowAdapter() {
	            public void windowClosing(WindowEvent windowEvent){
	               dispose();
	            }
	         });
		 
		 btnOk.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
		    	 
		            setFrames(Integer.parseInt(tfFrames.getText())); 
		            setVisible(false);		
		         }

		 });
	}
	
	public int  getFrames()
	{
		return frames;
	}
	
	void setFrames(int frames)
	{
		this.frames = frames;
	}
	
	
}
