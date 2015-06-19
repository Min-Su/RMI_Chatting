import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class ChatClientImpl extends JFrame implements ChatClient, ActionListener {
	private static final long serialVersionUID = 929395L;
	JTextField tf = new JTextField();
	JTextArea ta = new JTextArea();
	String name = null;
	ChatServer cs = null;
	JPanel p1 = new JPanel();
	JPanel p2 = new JPanel(new GridLayout(4, 1));
	Label inwonlb = new Label("인원 : ", Label.RIGHT);
	Label inwonlb1 = new Label("0", Label.CENTER);
	Label inwonlb2 = new Label("명", Label.LEFT);
	List inwonli = new List(5, false);
	CheckboxGroup cg = new CheckboxGroup();
	Checkbox hidecb = new Checkbox("밀담 전송", cg, false);
	Checkbox voidcb = new Checkbox("밀담 해제", cg, true);
	Button disconnbt = new Button("끝내기");
	Button compulsory = new Button("강제퇴장");
	JTextField ipFld = new JTextField("127.0.0.1");
	JTextField nick = new JTextField();
	Button connBtn = new Button("Connect");

	public ChatClientImpl() {
		super("RMI Chatting Client");
		tf.setBorder(new TitledBorder("Enter Message"));
		tf.addActionListener(this);
		add(tf, BorderLayout.SOUTH);
		add(new JScrollPane(ta), BorderLayout.CENTER);
		p1.setLayout(new GridLayout(1, 5));
		p1.add(new JLabel("Server IP: ", JLabel.RIGHT));
		p1.add(ipFld);
		p1.add(new JLabel("Nick Name: ", JLabel.RIGHT));
		p1.add(nick);
		p1.add(connBtn);
		add(p1, "North");

		Panel p1_1 = new Panel(new FlowLayout());
		p1_1.add(inwonlb);
		p1_1.add(inwonlb1);
		p1_1.add(inwonlb2);
		p2.add(p1_1);
		p2.add(inwonli);
		Panel p1_2 = new Panel(new BorderLayout());
		Panel p1_2_1 = new Panel(new GridLayout(2, 1));
		p1_2_1.add(hidecb);
		p1_2_1.add(voidcb);
		p1_2.add("Center", p1_2_1);
		Panel p1_2_2 = new Panel(new GridLayout(2, 1));
		p1_2_2.add(compulsory);
		p1_2_2.add(disconnbt);
		p2.add(p1_2);
		p2.add(p1_2_2);

		add(p2, BorderLayout.EAST);

		connBtn.addActionListener(this);
		disconnbt.addActionListener(this);
		compulsory.addActionListener(this);
		setLocation(200, 200);
		setSize(600, 410);
		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		try {
			
			if (e.getSource() == connBtn) {
				if (nick.getText() == null
						|| nick.getText().trim().length() < 1)
					return;

				String ip = "rmi://" + ipFld.getText().trim();
				cs = (ChatServer) Naming.lookup(ip + "/chat");
				name = nick.getText().trim();
				cs.connect(this, this.name);
				cs.sendMsg(name + " 님이 입장하셨습니다.\n");
				connBtn.setEnabled(false);
				return;
			}
			
			if (e.getSource() == disconnbt) {
				if(cs == null) {
					System.exit(0);
				}
				else {
					cs.disconnect(name);
					System.exit(0);
				}
			}
			
			if (e.getSource() == compulsory) {
				if(cs == null) {
					return;
				}
				else {
					String str = null;
					if((str = inwonli.getSelectedItem()).equals(this.name)) {
						ta.append("본인은 강제 퇴장 시킬 수 없습니다.\n");
						ta.setCaretPosition(ta.getText().length());
						return;
					}
					else if((str = inwonli.getSelectedItem()) != null) {
						cs.compulsory_cs(str);
					}
				}
			}
			
			if (cg.getSelectedCheckbox() == hidecb) {
				String str = null;
				String msg = null;
				if ((str = inwonli.getSelectedItem()) != null) {
					msg = tf.getText().trim();
					cs.hidechat(name, str, msg);
					tf.setText("");
					return;
				} else
					tf.setText("");
					return;
			}
			
			String m = tf.getText().trim();
			cs.sendMsg(name + ": " + m + "\n");
			tf.setText("");
			
		} catch (Exception ee) {
			System.out.println("error:" + ee);
		}
	}

	public void send_msg(String m) throws RemoteException {
		ta.append(m);
		ta.setCaretPosition(ta.getText().length());
	}

	public void hide_chat(String m) throws RemoteException {
		String str = null;
		str = m;
		ta.append(str);
		ta.setCaretPosition(ta.getText().length());
	}

	public void update(Vector<String> chatter) throws RemoteException {
		inwonli.removeAll();
		for (int i = 0; i < chatter.size(); i++) {
			inwonli.add(chatter.elementAt(i));
		}
		inwonlb1.setText(Integer.toString(chatter.size()));
	}
	
	public void compulsory_cc(String name) throws RemoteException {
		cs.disconnect(name);
		System.exit(0);
	}

	public static void main(String[] args) throws Exception {
		ChatClientImpl cc = new ChatClientImpl();
		UnicastRemoteObject.exportObject(cc);
	}
	
}