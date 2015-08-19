package context.arch.connection;

import java.io.InputStreamReader;

public interface Connection {
	
	public boolean open();
	public boolean isOpen();
	public boolean close();
	
	public boolean write(String data);
	public InputStreamReader read();
}
