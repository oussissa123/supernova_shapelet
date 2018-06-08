package oussissa.limos.supernova;
public class Predict {
	private int predictValue;
	private int trueValue;
	private String objectID;
	
	public Predict() {
	}
	public int getPredictValue() {
		return predictValue;
	}
	public void setPredictValue(int predictValue) {
		this.predictValue = predictValue;
	}
	public int getTrueValue() {
		return trueValue;
	}
	public void setTrueValue(int trueValue) {
		this.trueValue = trueValue;
	}
	public String getObjectID() {
		return objectID;
	}
	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}	
}
