package context.arch.connection;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.TooManyListenersException;

public class SerialConnection implements Connection {

	protected SerialPort serialPort;
	protected String port;
	
	private static final String PORT_NAMES[] = {
			"/dev/cu.usbmodem411", // Mac OS X
			"/dev/tty.usbmodem411", // Mac OS X
			"/dev/tty.usbserial", // Mac OS X
			"/dev/ttyUSB0", // Linux
			"/dev/usbdev", // Linux
			"/dev/tty", // Linux
	        "/dev/serial", // Linux
			"COM3", // Windows
	};
	
	protected InputStream input;
	protected OutputStream output;
	
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;
	
	public SerialConnection() {}
	
	public SerialConnection(String port) {
		this.port = port;
	}

	public boolean addEventListener(SerialPortEventListener serialPortEventListener){
		if (serialPort != null) {
			try {
				serialPort.addEventListener(serialPortEventListener);
				serialPort.notifyOnDataAvailable(true);
				return true;
			} catch (TooManyListenersException e) {
				System.err.println(e.toString());
			}
		}
		return false;
	}
	
	@Override
	public boolean open() {
		
		if(serialPort != null) { return true; };
		
		CommPortIdentifier portId = getSerialPort();
		
		if (portId != null) {
			try {
				serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);

				serialPort.setSerialPortParams(DATA_RATE,
						SerialPort.DATABITS_8,
						SerialPort.STOPBITS_1,
						SerialPort.PARITY_NONE);

				input = serialPort.getInputStream();
				output = serialPort.getOutputStream();

				return true;
			} catch (PortInUseException p) {
				System.err.println(p.toString());
			} catch (UnsupportedCommOperationException u) {
				System.err.println(u.toString());
			} catch (IOException io) {
				System.err.println(io.toString());
			}
		}
		return false;
	}

	@Override
	public boolean isOpen() {
		return (serialPort != null);
	}

	@Override
	public boolean close() {
		if (serialPort != null) {
			try {
				input.close();
				output.close();
				serialPort.removeEventListener();
				serialPort.close();
				serialPort = null;
				return true;
			} catch (IOException e) {
				System.err.println(e.toString());
			}
		}
		return false;
	}

	@Override
	public boolean write(String data) {		
        try {
			output.write( data.getBytes() );
			return true;
		} catch (IOException e) {
			System.err.println(e.toString());
		}
		return false;
	}

	@Override
	public InputStreamReader read() {
		return new InputStreamReader(input);
	}
	
	private CommPortIdentifier getSerialPort() {
		
		if (port != null) {
			try {
				return CommPortIdentifier.getPortIdentifier(port);
			} catch (NoSuchPortException e) {
				e.printStackTrace();
			}
			return null;
		}
		
		Enumeration<?> portEnum = CommPortIdentifier.getPortIdentifiers();
		while (portEnum.hasMoreElements()) {
			CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
			for (String portName : PORT_NAMES) {
				if (currPortId.getName().equals(portName)) {
					return currPortId;
				}
			}
		}
		return null;
	}

}
