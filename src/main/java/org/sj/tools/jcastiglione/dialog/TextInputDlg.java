package org.sj.tools.jcastiglione.dialog;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TextInputDlg extends Dialog {

	//TextField textFld;
	TextArea textArea;
	Button btnOk;
	Button btnCancel;
	boolean status = false;
	String text;
	

	public TextInputDlg(Frame f, String defText)
	{
		super(f);
		this.setModal(true);
		this.setTitle("Text input");
		this.setSize(300, 200);
		setLayout(new GridLayout(2,2));
		add(new Label("Text:"));
		//textFld = new TextField(defText);
		//add(textFld);
		textArea = new TextArea(defText);
		add(textArea);	
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
		    	 	status = true;
		            setVisible(false);
		            //text = textFld.getText();
		            text = textArea.getText();
		         }

		 });
	}
	
	public boolean getStatus() {
		return status;
	}
	
	public String getText() {
		return text;
	}
	
}
