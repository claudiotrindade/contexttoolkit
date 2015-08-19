package context.arch.discoverer;

import java.util.Iterator;
import java.util.Vector;

import context.arch.storage.Attribute;

public class ComponentDescriptions extends Vector<ComponentDescription> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -852247212944841325L;

	public ComponentDescription mergeComponentDescriptions() {
		ComponentDescription result = new ComponentDescription();
		Iterator<ComponentDescription> itr = this.iterator();
		while (itr.hasNext()) {
			ComponentDescription cd = itr.next();
			for (Attribute att : cd.getConstantAttributesFull().values()) {
				result.getConstantAttributesFull().add(att);
			}
			for (Attribute att : cd.getNonConstantAttributes().values()) {
				result.getNonConstantAttributes().add(att);
			}
			// TODO unir os demais atributos. Poderam ser usados em algum momento. 
		}
		return result;
	}

}
