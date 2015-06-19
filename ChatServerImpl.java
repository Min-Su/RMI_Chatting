
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.*;
import java.util.*;
public class ChatServerImpl extends UnicastRemoteObject implements ChatServer
{ 
	private static final long serialVersionUID=198023L;
	Vector<ChatClient> vc = new Vector<ChatClient>();
	Vector<String> chatter = new Vector<String>();
	String m=null;
	ChatClient client=null;
	
	public ChatServerImpl() throws RemoteException
	{}
	
	public void connect(ChatClient c, String name)throws RemoteException
	{
		vc.addElement(c);
		chatter.add(name);
		System.out.println(name);
		for(int i = 0; i < vc.size(); i++) {
			vc.elementAt(i).update(chatter);
		}
	}
	
	public void sendMsg(String msg)throws RemoteException {
		for(int i=0; i<vc.size();i++){
			ChatClient cc=(ChatClient) vc.elementAt(i);
			try {
				cc.send_msg(msg);
			}catch(Exception e){
				System.out.println("Error in"+cc);  
				vc.removeElement(cc);
			}
		}
	}
	
	public void hidechat(String s_name, String r_name, String msg) throws RemoteException {
		String tmp = "(¹Ð´ã)" + s_name + ": " + msg + "\n";
		for(int i=0; i<vc.size(); i++) {
			if(chatter.elementAt(i).equals(r_name)) {
				vc.elementAt(i).hide_chat(tmp);
			}
			if(chatter.elementAt(i).equals(s_name)) {
				vc.elementAt(i).hide_chat(tmp);
			}
		}
	}
	
	public void disconnect(String name) throws RemoteException {
		for(int i = 0; i < chatter.size(); i++) {
			if(chatter.elementAt(i).equals(name)) {
				vc.remove(i);
				chatter.remove(i);
			}
		}
		for(int j = 0; j < chatter.size(); j++) {
			vc.elementAt(j).update(chatter);
			vc.elementAt(j).send_msg(name+"´ÔÀÌ ÅðÀåÇÏ¼Ì½À´Ï´Ù."+"\n");
		}
	}
	
	public void compulsory_cs(String name) throws RemoteException {
		for(int i = 0; i < vc.size(); i++) {
			if(chatter.elementAt(i).equals(name)) {
				vc.elementAt(i).compulsory_cc(name);
			}
		}
	}
	
	public static void main(String[] args)throws Exception{
		try{
		  LocateRegistry.createRegistry(1099);
		}catch(Exception ee){}
		ChatServerImpl sv=new ChatServerImpl();
		Naming.rebind("rmi://127.0.0.1/chat",sv);
		System.out.println("RMI Chat Server Ready");
	}

	
	
}