import java.net.*;
import java.util.Calendar;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class admini1 extends JFrame implements ActionListener {
	TextArea display = new TextArea("");
	TextField input = new TextField("");
	JButton sendJButton = new JButton("发送");
	JButton exitJButton = new JButton("退出");

	public admini1() {
		super("admini2 Talking");
		// set display
		display.setText("");
		display.setEditable(false);
		// Button Layout
		JPanel panelButton = new JPanel(new GridLayout(2, 1));
		panelButton.add("North", sendJButton);
		panelButton.add("South", exitJButton);
		// Button and input layout
		JPanel panelInput = new JPanel(new GridLayout(1, 2));
		panelInput.add(input);
		panelInput.add(panelButton);
		//
		int j=0;
		j=7;
		//int i=j-5;
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("North", display);
		getContentPane().add("Center", panelInput);
		sendJButton.addActionListener(this);
		exitJButton.addActionListener(this);
		input.addActionListener(this);
		// 初始化窗体 800*800
		setSize(250, 250);
		setVisible(true);
		// 适应特定大小
		// pack();
	}
	// send data
	void sendData() {
		try {
			String msg = input.getText();
			if (msg.equals(""))
				return;
			//获得时间
			Calendar c = Calendar.getInstance();
			String blank = "";
			String name = ("From admini1: " +c.get(Calendar.HOUR)+":"
					+c.get(Calendar.MINUTE)+"\n");
			//计算空格数，右对齐
			for(int i =0;i<(36-name.length());i++){
				blank += " ";
			}
			//显示	本人信息
			display.append(blank);display.append(name);
			display.append(blank+"    "+msg+ "\n");

			InetAddress address = InetAddress.getByName("localhost");
			int len = msg.length();
			byte[] message = new byte[len];
			msg.getBytes(0, len, message, 0);
			DatagramPacket packet = new DatagramPacket(message, len, address,
					9999);
			DatagramSocket socket = new DatagramSocket();
			socket.send(packet);
		}
		catch (Exception e) {
		}
	}

	void waitForData() {
		try {
			byte[] buffer = new byte[1024];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
			DatagramSocket socket = new DatagramSocket(8888);
			while (true) {
				socket.receive(packet);
				String s = new String(buffer, 0, 0, packet.getLength());

				Calendar c = Calendar.getInstance();
				display.append("From admini2: " +c.get(Calendar.HOUR)+":"
						+c.get(Calendar.MINUTE)+"\n");

				display.append("    "+s + "\n");
				packet = new DatagramPacket(buffer, buffer.length);
			}
		} catch (Exception e) {
		}
	}


	public void actionPerformed(ActionEvent e) {
		Component com = (Component) e.getSource();
		if (com.equals(exitJButton)) {
			System.exit(0);
		} else {
			sendData();
			this.input.setText("");
		}
	}

	public static void main(String args[]) {
		admini1 t1=new admini1();
		t1.waitForData();
	}
}
