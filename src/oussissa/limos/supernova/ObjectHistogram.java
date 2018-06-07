package oussissa.limos.supernova;

public class ObjectHistogram {
	private TimeSerie timeSerie;
	private double distance;
	
	
	public ObjectHistogram() {
	}

	public ObjectHistogram(TimeSerie timeSerie, double distance) {
		this.timeSerie = timeSerie;
		this.distance = distance;
	}
	
	public TimeSerie getTimeSerie() {
		return timeSerie;
	}
	public void setTimeSerie(TimeSerie timeSerie) {
		this.timeSerie = timeSerie;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(float distance) {
		this.distance = distance;
	}
	
	
}
