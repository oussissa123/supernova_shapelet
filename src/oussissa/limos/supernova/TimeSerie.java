package oussissa.limos.supernova;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TimeSerie extends ArrayList<Double>{
	/**
	 * 
	 */
	
	private String id;
	private int object_class;
	
	private static final long serialVersionUID = 1L;

	public TimeSerie() {
		super();
	}
	
	public TimeSerie(Collection<Double> values) {
		super(values);
	}
	
	public TimeSerie(List<Double> values) {
		super(values);
	}
	
	public TimeSerie(Collection<Double> values, String id) {
		super(values);
		this.id = id;
	}
	
	public TimeSerie(List<Double> values, String id) {
		super(values);
		this.id = id;
	}
	
	public TimeSerie(Collection<Double> values, String id, int clas) {
		super(values);
		this.id = id;
		object_class = clas;
	}
	
	public TimeSerie(List<Double> values, String id, int clas) {
		super(values);
		this.id = id;
		object_class = clas;
	}
	
	public TimeSerie(Collection<Double> values, int clas) {
		super(values);
		object_class = clas;
	}
	
	public TimeSerie(List<Double> values, int clas) {
		super(values);
		object_class = clas;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public int getObject_class() {
		return object_class;
	}
	
	public void setObject_class(int object_class) {
		this.object_class = object_class;
	}
	
}
