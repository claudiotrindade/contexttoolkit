package context.arch.widget;

import context.arch.comm.CommunicationsHandler;
import context.arch.comm.DataObject;
import context.arch.storage.Attributes;

public interface IArduinoWidget extends CommunicationsHandler {

	public DataObject updateArduino(Attributes attributes);
	
}
