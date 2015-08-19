package context.arch.storage;

import context.arch.comm.DataObject;
import context.arch.comm.DataObjects;
import context.arch.discoverer.Discoverer;
import context.arch.discoverer.component.AbstractElement;
import context.arch.storage.AttributeNameValue;
import context.arch.storage.Attributes;

/**
 * This class is a container for an attribute name, value and type.
 * TODO: how to handle characteristics such as: possible nominal values, min/max range? 
 */
public class AttributeNameValuePin<T extends Comparable<? super T>> extends AttributeNameValue<T> {

	protected int pin;
	protected String pinType;
	
	public static final String ANALOG = "analog";
	public static final String DIGITAL = "digital";

	public static final String ATTRIBUTE_NAME_VALUE_PIN = "attributeNameValuePin";
	
	public static final String ATTRIBUTE_PIN = "attributePin";
	public static final String ATTRIBUTE_PIN_TYPE = "attributePinType";

	/**
	 * Constructor that takes only a name, pin and pinType
	 *
	 * @param name Name of attribute to store
	 */
	public AttributeNameValuePin(String name, Class<T> type, Integer pin, String pinType) {
		super(name, type);
		this.pin = pin;
		this.pinType = pinType;
	}

	@SuppressWarnings("unchecked")
	public AttributeNameValuePin(String name, T value, Integer pin, String pinType) {
		this(name, (Class<T>)value.getClass(), value, pin, pinType);
	}

	/**
	 * Constructor that takes a name, value, type, pin and pinType
	 *
	 * @param name Name of attribute to store
	 * @param value Value of attribute to store
	 * @param type Datatype of attribute to store
	 * @param pin Pin of attribute to arduino
	 * @param pinType PinType of attribute to arduino
	 */
	public AttributeNameValuePin(String name, Class<T> type, T value, Integer pin, String pinType) {
		super(name, type, value);
		this.pin = pin;
		this.pinType = pinType;
	}
	
	public AttributeNameValuePin(String name, Class<T> type, T value, Integer pin, String pinType, Attributes subAttributes) {
		this(name, type, value, pin, pinType);
		this.subAttributes = subAttributes;
	}
	
	/**
	 * Allows to dynamically (at runtime) instantiate a AttributeNameValue with the
	 * corresponding generic type.
	 * @param <T>
	 * @param name
	 * @param value
	 * @param pin
	 * @param pinType
	 * @return
	 */
	public static <T extends Comparable<? super T>> AttributeNameValuePin<T> instance(String name, T value, Integer pin, String pinType) {
		return new AttributeNameValuePin<T>(name, value, pin, pinType);
	}

	public static <T extends Comparable<? super T>> AttributeNameValuePin<T> instance(String name, Class<T> type, Integer pin, String pinType) {
		return new AttributeNameValuePin<T>(name, type, pin, pinType);
	}

	/**
	 * Use this to create a AttributeNameValue where the type of value is determined dynamically at runtime.
	 * @param <T>
	 * @param name
	 * @param type
	 * @param value
	 * @param pin
	 * @param pinType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<? super T>> AttributeNameValuePin<T> instance(String name, Class<T> type, Comparable<? super T> value, Integer pin, String pinType) {
		return new AttributeNameValuePin<T>(name, type, (T) value, pin, pinType);
	}

	@SuppressWarnings("unchecked")
	public static <T extends Comparable<? super T>> AttributeNameValuePin<T> instance(String name, Class<T> type, Comparable<? super T> value, Integer pin, String pinType, Attributes subAttrs) {
		return new AttributeNameValuePin<T>(name, type, (T) value, pin, pinType, subAttrs);
	}
	
	/**
	 * Constructor that takes a DataObject as input.  The DataObject
	 * must have <ATTRIBUTE_NAME_VALUE_PIN> as its top-level tag
	 *
	 * @param attribute DataObject containing the attribute info
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<? super T>> AttributeNameValuePin<T> fromDataObject(DataObject data) {
		String name = data.getDataObject(ATTRIBUTE_NAME_VALUE_PIN).getValue();
		String tClassName = data.getDataObject(ATTRIBUTE_TYPE).getValue();
		try {
			return fromDataObject(data, name, (Class<T>) Class.forName(tClassName));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}

	protected static <T extends Comparable<? super T>> AttributeNameValuePin<T> fromDataObject(DataObject data, String name, Class<T> type) {
		try {
			Attributes subAttrs = Attributes.fromDataObject(data);
			
			// invoke .valueOf(String) method to parse value from string
			String strValue = data.getDataObject(ATTRIBUTE_VALUE).getValue();
			T value = null;
			if (strValue != null && !strValue.equals("null")) { // e.g. because value not set
				value = valueOf(type, strValue);
			}
			
			String strPin = data.getDataObject(ATTRIBUTE_PIN).getValue();
			if (strPin == null || strPin.equals("null")) { // e.g. because pin not set
				return null;
			}
			Integer pin = Integer.valueOf(strPin);
			
			String pinType = data.getDataObject(ATTRIBUTE_PIN_TYPE).getValue();
			
			return AttributeNameValuePin.instance(name, type, value, pin, pinType, subAttrs);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	

	/**
	 * Converts this object to a DataObject.
	 *
	 * @return AttributeNameValuePin object converted to an <ATTRIBUTE_NAME_VALUE_PIN> DataObject
	 */
	@Override
	public DataObject toDataObject() {
		DataObject dobj = super.toDataObject();
		
		// change name value pin for data object
		dobj.setName(ATTRIBUTE_NAME_VALUE_PIN);
		
		/*
		 * Extend data object from Attribute
		 */
		DataObjects children = dobj.getChildren();
		
		children.add(new DataObject(ATTRIBUTE_PIN, String.valueOf(pin)));
		children.add(new DataObject(ATTRIBUTE_PIN_TYPE, pinType));
		
		return dobj;
	}  
	
	/**
	 * Copy value from source attribute if their types match.
	 * @param source
	 * @return true if the copy was successful.
	 */
	@SuppressWarnings("unchecked")
	public boolean copyValue(AttributeNameValuePin<?> source) {
		if (source.getType().equals(type)) {
			setValue((T)source.getValue());
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * A printable version of this class.
	 *
	 * @return String version of this class
	 */
	public String toString() {
		return super.toString() +
			   ",pin=" + getPin() +
			   ",pinType=" + getPinType();
	}
	
	public int getPin() {
		return pin;
	}
	
	public String getPinType() {
		return pinType;
	}
	
	
	@Override
	public AttributeNameValuePin<T> clone() {
		return new AttributeNameValuePin<T>(name, type, value, pin, pinType, subAttributes);
	}

	@Override
	public AttributeNameValuePin<T> cloneWithNewName(String name) {
		return new AttributeNameValuePin<T>(name, type, value, pin, pinType, subAttributes);		
	}
	
	public AttributeNameValuePin<T> cloneWithNewValue(Object value) {
		AttributeNameValuePin<T> att = new AttributeNameValuePin<T>(name, type, null, pin, pinType, subAttributes);
		att.setValue(value);
		return att;
	}

	/**
	 * Convert to value codex representation that is used by the Discoverer component model, for querying.
	 * Format: name+type+value+pin+pinType, where '+' would be the URL encoded form of space ' '.
	 * @see #fromValueCodex(String)
	 * @see AbstractElement#fromDataObject(DataObject)
	 * @return
	 */
	@Override
	public String toValueCodex() {
		return super.toValueCodex() + Discoverer.FIELD_SEPARATOR + pin + Discoverer.FIELD_SEPARATOR + pinType;
	}
	
	/**
	 * Create a AttributeNameValue (shallow with no sub-attributes) from the value codex representation
	 * that is used by the Discoverer component model, for querying.
	 * Format: name+type+value, where '+' would be the URL encoded form of space ' '.
	 * @param valueCodex
	 * @return
	 * @see #toValueCodex()
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Comparable<? super T>> AttributeNameValuePin<T> fromValueCodex(String valueCodex) {
		String[] args = valueCodex.split("\\+"); // "name+type+value+pin+pinType" -> {name, type, value, pin, pinType}
		String name = args[0];
		String typeClassname = args[1];
		String strValue = args[2];
		String strPin = args[3];
		String pinType = args[4];
		
		try {
			Class<T> type = (Class<T>) Class.forName(typeClassname);
			T value = valueOf(type, strValue);
			Integer pin = Integer.valueOf(strPin);
			return AttributeNameValuePin.instance(name, type, value, pin, pinType);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}	
}
