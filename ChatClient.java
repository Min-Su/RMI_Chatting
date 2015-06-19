
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;
public interface ChatClient extends Remote {
	public void send_msg(String m)throws RemoteException;
	public void hide_chat(String m)throws RemoteException;
	public void update(Vector<String> chatter)throws RemoteException;
	public void compulsory_cc(String name)throws RemoteException;
}