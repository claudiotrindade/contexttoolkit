package context.arch.widget;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import context.arch.comm.DataObject;
import context.arch.comm.DataObjects;
import context.arch.comm.language.DecodeException;
import context.arch.comm.language.EncodeException;
import context.arch.comm.language.InvalidDecoderException;
import context.arch.comm.language.InvalidEncoderException;
import context.arch.connection.FactoryConnection;
import context.arch.connection.SerialConnection;
import context.arch.storage.AttributeNameValue;
import context.arch.storage.Attributes;

public abstract class ArduinoWidget extends Widget implements IArduinoWidget, SerialPortEventListener {

	public static final String UPDATE_ARDUINO = "updateArduino";
	public static final String DEFAULT_ARDUINO_PORT = "COM3";
	public static final String DEFAULT_ARDUINO_ID = "ArduinoWidget";
	public static final int TYPE_SENSOR = 0;
	public static final int TYPE_ACTUATOR = 1;
	public static final int TYPE_SENSOR_AND_ACTUATOR = 2;
	
	protected SerialConnection connection;
	protected String arduinoId;
	protected int type;
	
	public ArduinoWidget(String id, int type, String serialPort, String arduinoId) {
		super(id,id);
		this.type = type;
		this.arduinoId = arduinoId;
		this.connection = (SerialConnection) FactoryConnection.getConnetion(FactoryConnection.SERIAL, serialPort);
		if (connection != null) { 			
			if(!connection.isOpen()) {
				connection.open();
			}
			if (type == TYPE_SENSOR || type == TYPE_SENSOR_AND_ACTUATOR) {
				connection.addEventListener(this); 
			}
		}
	}
	
	public ArduinoWidget(String id, int type, String serialPort) {
		this(id, type, serialPort,DEFAULT_ARDUINO_ID);
	}
	
	public ArduinoWidget(String id, int type) {
		this(id, type, DEFAULT_ARDUINO_PORT);
	}

	@Override
	public void serialEvent(SerialPortEvent arg0) {
		try {
			Attributes atts = Attributes.fromDataObject(decodeData(connection.read()));
			atts.put(TIMESTAMP, new AttributeNameValue<Long>(TIMESTAMP, System.currentTimeMillis()));
			updateData(this.nonConstantAttributes.getSubset(atts));
		} catch (DecodeException e) {
			e.printStackTrace();
		} catch (InvalidDecoderException e) {
			e.printStackTrace();
		}
	}

	@Override
	public DataObject updateArduino(Attributes attributes) {
		if (connection != null) {
			if (!connection.isOpen()) {
				connection.open();
			}
			try {
				DataObjects v = new DataObjects();
				v.addElement(new DataObject(ID, arduinoId));
				v.addElement(attributes.toDataObject());
				DataObject data = new DataObject(UPDATE_ARDUINO, v);
				connection.write(encodeData(data));
			} catch (EncodeException e) {
				e.printStackTrace();
			} catch (InvalidEncoderException e) {
				e.printStackTrace();
			}
		}
		return new DataObject();
	}

}
