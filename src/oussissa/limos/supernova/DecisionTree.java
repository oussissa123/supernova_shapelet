package oussissa.limos.supernova;

public class DecisionTree {
	
	private int object_class;
	private TimeSerie timeSerie;
	private double splitPoint;
	private DecisionTree left;
	private DecisionTree right;
	
	
	public DecisionTree() {
	}
	
	public DecisionTree(int object_class) {
		this.object_class = object_class;
	}
	
	public DecisionTree(TimeSerie timeSerie, double splitPoint) {
		//this.timeSerie = timeSerie;
		setTimeSerie(timeSerie);
		this.splitPoint = splitPoint;
	}
	
	public TimeSerie getTimeSerie() {
		return timeSerie;
	}
	public void setTimeSerie(TimeSerie timeSerie) {
		this.timeSerie = new TimeSerie(timeSerie);
		this.timeSerie.setObject_class(timeSerie.getObject_class());
		this.timeSerie.setId(timeSerie.getId());
	}
	public double getSplitPoint() {
		return splitPoint;
	}
	public void setSplitPoint(double splitPoint) {
		this.splitPoint = splitPoint;
	}
	public DecisionTree getLeft() {
		return left;
	}
	public void setLeft(DecisionTree left) {
		this.left = left;
	}
	public DecisionTree getRight() {
		return right;
	}
	public void setRight(DecisionTree right) {
		this.right = right;
	}
	public void setObject_class(int object_class) {
		this.object_class = object_class;
	}
	public int getObject_class() {
		return object_class;
	}
	
	@Override
	public String toString() {
		String value = "{ "+timeSerie + " : " + splitPoint + " } [ "+ ((left!=null)?left.toString():"")+ " | "+((right!=null)?right.toString():"")+"]";
		return value;
	}
}
