package fr.vitec.fmk.binding;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.WorkbenchPart;

import fr.vitec.fmk.reflect.ReflectUtil;

public class BindingManager {

	private static final String TEXT_SEP = ", ";
	private int controlPrefixLength;
	List<Control> controls;
	private Object model;
	private Object view;

	public class PairControlName{
		private Control control;
		private String name;
		public PairControlName(Control control, String name) {
			this.control = control;
			this.name = name;
		}
		public Control getControl() {
			return control;
		}
		public void setControl(Control control) {
			this.control = control;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}

	public BindingManager() {
		controlPrefixLength = 3;
		controls = new ArrayList<Control>();
	}

	public void addControls(WorkbenchPart view, Control ... controls) {
		this.view = view;
		this.controls.addAll(Arrays.asList(controls));
	}

	public void setModel(Object object) {
		model = object;
		List<Method> methods = ReflectUtil.findAllGettersOfClass(model);
		for(Method method : methods){
			updateControl(method.getName().substring(ReflectUtil.GETTER_PREFIX.length()));
		}
	}

	private void updateControl(String attributeName) {
		Control control = findControl(attributeName);
		if(control != null && controls.contains(control)){
			String text = getText(ReflectUtil.invokeGetterByFieldName(model, attributeName));
			//if(!control.isDisposed()){
				if(control instanceof Label){
					((Label)control).setText(text);
				} else if(control instanceof Text){
					((Text)control).setText(text);
				} 
//				System.out.println("non disposed: "+attributeName);
//			}else{
//				System.out.println("disposed: "+attributeName);
//			}
		}
	}

	private Control findControl(String attributeName) {
		Field[] fields = view.getClass().getDeclaredFields();
		Control control = null;
		for(Field field : fields){
			String fieldName = field.getName();
			if(fieldName.length()>controlPrefixLength && fieldName.substring(controlPrefixLength).equalsIgnoreCase(attributeName)){
				field.setAccessible(true);
				try {
					control = (Control) field.get(view);
					break;
				} catch (IllegalArgumentException e) {
					break;
				} catch (IllegalAccessException e) {
					break;
				}
			}
		}
		return control;
	}

	@SuppressWarnings("rawtypes")
	private String getText(Object object) {
		String str = "";
		if(object instanceof String){
			str = (String) object;
		}else if(object instanceof List){
			StringBuffer sb = new StringBuffer();
			String sep = "";
			for(Object element : (List)object){
				sb.append(sep);
				sb.append(element.toString());
				sep = TEXT_SEP;
			}
			str = sb.toString();
		}else if(object instanceof Label){
			str = ((Label)object).getText();
		} else if(object instanceof Text){
			str = ((Text)object).getText();
		} 
		return str;
	}

	public void updateModel() {
		Field[] fields = model.getClass().getDeclaredFields();
		for(Field field : fields){
			Control control = findControl(field.getName());
			if(control != null && controls.contains(control)){
				String controlText = getText(control);
				if(field.getType() == List.class){
					List<String> list = new ArrayList<String>();
					list.addAll(Arrays.asList(controlText.split(TEXT_SEP)));
					ReflectUtil.invokeSetterByFieldName(model, field.getName(), list, field.getType());
				}else{
					ReflectUtil.invokeSetterByFieldName(model, field.getName(), controlText, field.getType());
				}
			}
		}
	}

}
