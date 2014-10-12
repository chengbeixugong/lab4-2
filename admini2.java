import java.net.*;
import java.util.Calendar;
import java.awt.*;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class admini2 extends JFrame implements ActionListener {
	TextArea display = new TextArea("");
	TextField input = new TextField("");
	JButton sendJButton = new JButton("����");
	JButton exitJButton = new JButton("�˳�");
	int border = display.getWidth();
	public admini2() {
		super("admini1 Talking");
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
		int i=0;
		i=5;
		int j=i+3;
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("North", display);
		getContentPane().add("Center", panelInput);
		sendJButton.addActionListener(this);
		exitJButton.addActionListener(this);
		input.addActionListener(this);
		// ��ʼ������ 800*800
		setSize(250, 250);
		setVisible(true);
		// ��Ӧ�ض���С
		// pack();

//		try {
//
//			// this.add(comp)
//			sendJButton.addActionListener(this);
//		} catch (Exception e) {
//			System.out.println("Lovely Jenny");
//		}
	}
	// send data
	void sendData() {
		try {
			String msg = input.getText();
			if (msg.equals(""))
				return;
			//���ʱ��
			Calendar c = Calendar.getInstance();
			String blank = "";
			String name = ("From admini2: " +c.get(Calendar.HOUR)+":"
					+c.get(Calendar.MINUTE)+"\n");
			//����ո������Ҷ���
			for(int i =0;i<(36-name.length());i++){
				blank += " ";
			}
			//��ʾ	������Ϣ
			display.append(blank);display.append(name);
			display.append(blank+"    "+msg+ "\n");

			InetAddress address = InetAddress.getByName("localhost");
			int len = msg.length();
			byte[] message = new byte[len];
			msg.getBytes(0, len, message, 0);
			DatagramPacket packet = new DatagramPacket(message, len, address,
					8888);
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
			DatagramSocket socket = new DatagramSocket(9999);
			while (true) {
				socket.receive(packet);
				String s = new String(buffer, 0, 0, packet.getLength());

				Calendar c = Calendar.getInstance();
				display.append("From admini1: " +c.get(Calendar.HOUR)+":"
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
		admini2 t1=new admini2();
		t1.waitForData();
	}
}