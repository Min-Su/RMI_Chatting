
import java.rmi.*;
public interface ChatServer extends Remote {
	public void sendMsg(String m)throws RemoteException;
	public void hidechat(String s_name, String r_name, String msg)throws RemoteException; 
	public void connect(ChatClient c, String name)throws RemoteException;
	public void disconnect(String name)throws RemoteException;
	public void compulsory_cs(String name)throws RemoteException;
}