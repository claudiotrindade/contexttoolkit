package context.arch.connection;

import java.util.HashMap;
import java.util.Map;

public abstract class FactoryConnection {

	public static int SERIAL = 0;
	
	private static Map<String, SerialConnection> serials = new HashMap<String, SerialConnection>();
	
	public static Connection getConnetion(int type) {
		return getConnetion(type, null);
	}
	
	public static Connection getConnetion(int type, String port) {
		switch(type) {
		case 0:
			if (!serials.containsKey(port)) {
				serials.put(port, new SerialConnection(port));
			}
			return serials.get(port);
		}
		return null;
	}
}
