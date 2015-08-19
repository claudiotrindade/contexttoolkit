package context.arch.widget;

import context.arch.comm.CommunicationsHandler;
import context.arch.comm.DataObject;
import context.arch.storage.Attributes;

public interface IESP8266Widget extends CommunicationsHandler {

	public DataObject updateESP8266(Attributes attributes);
	
}
