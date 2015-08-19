package context.arch.widget;

import java.io.IOException;

import context.arch.comm.DataObject;
import context.arch.comm.DataObjects;
import context.arch.comm.RequestObject;
import context.arch.comm.language.DecodeException;
import context.arch.comm.language.EncodeException;
import context.arch.comm.language.InvalidDecoderException;
import context.arch.comm.language.InvalidEncoderException;
import context.arch.comm.protocol.InvalidProtocolException;
import context.arch.comm.protocol.ProtocolException;
import context.arch.storage.Attributes;
import context.arch.util.Error;


public abstract class ESP8266Widget extends Widget implements IESP8266Widget {

	public static final String UPDATE_ESP8266 = "updateEPS8266";
	public static final Integer DEFAULT_ESP8266_PORT = 80;
	public static final String DEFAULT_ESP8266_ID = "ESP8266Widget";
	
	protected String esp8266Host;
	protected int esp8266Port; 
	protected String esp8266Id;
	
	public ESP8266Widget(String id, String esp8266Host, int esp8266Port, String esp8266Id) {
		super(id,id);
		this.esp8266Host = esp8266Host;
		this.esp8266Port = esp8266Port;
		this.esp8266Id = esp8266Id;
	}
	
	public ESP8266Widget(String id, String esp8266Host, int esp8266Port) {
		this(id, esp8266Host, esp8266Port, DEFAULT_ESP8266_ID);
	}
	
	public ESP8266Widget(String id, String esp8266Host) {
		this(id, esp8266Host, DEFAULT_ESP8266_PORT);
	}
	
	public ESP8266Widget(String id) {
		this(id,null,-1,null);
	}
	
	@Override
	public DataObject updateESP8266(Attributes attributes) {
		DataObjects v = new DataObjects();
		v.addElement(new DataObject(ID, esp8266Id));
		v.addElement(attributes.toDataObject());
		DataObject data = new DataObject(UPDATE_ESP8266, v); 

		try {

			DataObject dataObj = userRequest(new RequestObject(data, UPDATE_ESP8266, esp8266Host, esp8266Port));
			return dataObj;

		} catch (EncodeException ee) {
			System.out.println("BaseObject pollWidget EncodeException: "+ee);
		} catch (DecodeException de) {
			System.out.println("BaseObject pollWidget DecodeException: "+de);
		} catch (InvalidEncoderException iee) {
			System.out.println("BaseObject pollWidget InvalidEncoderException: "+iee);
		} catch (InvalidDecoderException ide) {
			System.out.println("BaseObject pollWidget InvalidDecoderException: "+ide);
		} catch (InvalidProtocolException ipe) {
			System.out.println("BaseObject pollWidget InvalidProtocolException: "+ipe);
		} catch (ProtocolException pe) {
			System.out.println("BaseObject pollWidget ProtocolException: "+pe);
		} catch (IOException ioe) {
			System.out.println("BaseObject pollWidget IOException: "+ioe);
			return (new Error(Error.IO_ERROR)).toDataObject();
		}

		return null;

	}

}
