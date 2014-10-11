import java.applet.Applet;
import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;



public class MainDashboard extends JApplet{
	 private String[] tabmenu = { "Meaning","Sysnonys","Wiki" };

		  private JTabbedPane tabs = new JTabbedPane();

		  private JTextField txt = new JTextField(20);
		  private JButton button = new JButton("Search");

		  public void init() {
		    for (int i = 0; i < tabmenu.length; i++)
		      tabs.addTab(tabmenu[i], new JTextArea());
		    tabs.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent e) {
//		        txt.setText("Tab selected: " + tabs.getSelectedIndex());
		      }
		    });
		    Container cp = getContentPane();
		    cp.setLayout(new GridBagLayout());
		    GridBagConstraints c = new GridBagConstraints();
		    
		                    //natural height, maximum width
//		                    c.fill = GridBagConstraints.HORIZONTAL;
		   

//		    button = new JButton("Button 1");
//		    if (shouldWeightX) {
//		                       c.weightx = 0.5;
//		    }
		                       
		    c.fill = GridBagConstraints.HORIZONTAL;
		    c.gridx = 0;
		    c.gridy = 0;
		    cp.add(txt, c);

//		    button = new JButton("Button 2");
		    c.fill = GridBagConstraints.HORIZONTAL;
//		    c.weightx = 0.5;
		    c.ipady = 4; 
		    c.gridx = 1;
		    c.gridy = 0;
		    button.setSize(40, 10);
		    cp.add(button, c);


		    c.fill = GridBagConstraints.HORIZONTAL;
//		    c.weightx = 0.5;
		    c.ipady = 60;      //make this component tall
		    c.weightx = 0.0;
		    c.gridwidth = 3;
		    c.gridx = 0;
		    c.gridy = 2;
		    cp.add(tabs, c);
//		    cp.setSize(800, 800);

//		    button = new JButton("Long-Named Button 4");
//		    c.fill = GridBagConstraints.HORIZONTAL;
//		    c.ipady = 40;      //make this component tall
//		    c.weightx = 0.0;
//		    c.gridwidth = 3;
//		    c.gridx = 0;
//		    c.gridy = 1;
//		    pane.add(button, c);
//
//		    cp.add(BorderLayout.NORTH, txt);
//		    cp.add(BorderLayout.NORTH, txt);
//		    cp.add(tabs);
		  }
		  public static void main(String[] args) {
			    run(new MainDashboard(), 350, 200);
			  }

		  public static void run(JApplet applet, int width, int height) {
			    JFrame frame = new JFrame();
			    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    frame.getContentPane().add(applet);
			    frame.setSize(800, 800);
			    applet.init();
			    applet.start();
			    frame.setVisible(true);
			  }
}
