package context.arch.service;

import context.arch.comm.DataObject;
import context.arch.service.helper.FunctionDescriptions;
import context.arch.service.helper.ServiceInput;
import context.arch.widget.IArduinoWidget;

public abstract class ArduinoService extends Service {

	protected IArduinoWidget widget;
	
	public ArduinoService(IArduinoWidget widget) {
		super(widget);
		this.widget = widget;
	}
	
	public ArduinoService(IArduinoWidget widget, String name, FunctionDescriptions functions) {
		super(widget, name, functions);
		this.widget = widget;
	}

	@Override
	public DataObject execute(ServiceInput serviceInput) {
		widget.updateArduino(serviceInput.getInput());	
		return new DataObject();
	}
	

}
