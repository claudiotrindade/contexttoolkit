package context.arch.service;

import context.arch.comm.DataObject;
import context.arch.service.helper.FunctionDescriptions;
import context.arch.service.helper.ServiceInput;
import context.arch.widget.IESP8266Widget;

public abstract class ESP8266Service extends Service {

	protected IESP8266Widget widget;
	
	public ESP8266Service(IESP8266Widget widget) {
		super(widget);
		this.widget = widget;
	}
	
	public ESP8266Service(IESP8266Widget widget, String name, FunctionDescriptions functions) {
		super(widget, name, functions);
		this.widget = widget;
	}

	@Override
	public DataObject execute(ServiceInput serviceInput) {
		widget.updateESP8266(serviceInput.getInput());
		return new DataObject();
	}
	

}
