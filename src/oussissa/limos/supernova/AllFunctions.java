package oussissa.limos.supernova;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
//import java.util.Random;

public class AllFunctions {

	private static double split_dist = 0f;
	public static double finalValue = 0f;
	public static Collection<TimeSerie> generateCandidates(Collection<TimeSerie> data, int maxLen, int minLen){
		Collection<TimeSerie> result = new ArrayList<>();
		int l = maxLen;
		
		while (l>=minLen){
			Iterator<TimeSerie> values = data.iterator();
			while(values.hasNext()) 
				result.addAll(getSubsequences(values.next(), l));
			l--;
		}
		return result;
	}

	private static Collection<TimeSerie> getSubsequences(TimeSerie next, int l) {
		 Collection<TimeSerie> result = new ArrayList<>();
		 for(int i=0;i<next.size()-l+1;i++) 
			 result.add(new TimeSerie(next.subList(i, i+l)));
		return result;
	}
	
	public static void display(Collection<? extends Object> c) {
		Iterator<? extends Object> a = c.iterator();
		while (a.hasNext())
			System.out.println(a.next());
	}
	
	public static  TimeSerie findShapeletBF(Collection<TimeSerie> data, int maxLen, int minLen){
//		 Random r = new Random();
//		 int index = r.nextInt(data.size());
		 TimeSerie result = null;//(new ArrayList<TimeSerie>(data)).get(index);
		 Collection<TimeSerie> candidates = generateCandidates(data, maxLen, minLen);
		 
		 //display(candidates);
		 
		 
		 double bsf_gain = 0;
		 Iterator<TimeSerie> d = candidates.iterator();
		 System.out.println(candidates.size());
		 while (d.hasNext()) {
			 TimeSerie val = d.next();
			double gain = checkCandidate(data, val);
			//System.out.println(gain);
			if (gain>bsf_gain) {
				bsf_gain = gain;
				result = val;
				finalValue = split_dist;
			}
		 }
		 return result;
	}

	private static double checkCandidate(Collection<TimeSerie> data, TimeSerie val) {
		double result = 0f;
		List<ObjectHistogram> objectHistograms = new ArrayList<>();
		Iterator<TimeSerie> d = data.iterator();
		while (d.hasNext()) {
			TimeSerie current = d.next();
			double distance = subsequenceDistEuclidien(current, val);
			objectHistograms.add(new ObjectHistogram(current, distance));
		}
		result = calculateInformationGain(objectHistograms);
		return result;
	}

	private static double calculateInformationGain(List<ObjectHistogram> objectHistograms) {
		List<TimeSerie> d1 = new ArrayList<>();
		List<TimeSerie> d2 = new ArrayList<>();
		List<TimeSerie> all = new ArrayList<>();
		double result = Double.MIN_VALUE; 
		
		List<Double> possibleValue = getPossibleValues(objectHistograms);
		for (double d:possibleValue) {
			split_dist = d;//meanDistance(objectHistograms);//d;
			for (int i=0;i<objectHistograms.size();i++) {
				ObjectHistogram elt = objectHistograms.get(i);
				if (elt.getDistance()<split_dist)
					d1.add(elt.getTimeSerie());
				else
					d2.add(elt.getTimeSerie());
				all.add(elt.getTimeSerie());
			}
			double iid = ((d1.size()/all.size())*entropyBin(d1))+((d2.size()/all.size())*entropyBin(d2));
			double id = entropyBin(all);
			result = Double.min(result, id-iid);
		}
		return result;
	}

	public static double meanDistance(List<ObjectHistogram> obj) {
		double d = 0f;
		for (int i=0;i<obj.size();i++) {
			d+=obj.get(i).getDistance();
		}
	    return d/obj.size();
	}
	public static List<Double> getPossibleValues(List<ObjectHistogram> obj) {
		List<Double> value = new ArrayList<>();
		for (int i=0;i<obj.size()-1;i++) {
			for(int j=i+1;j<obj.size();j++) {
				if (obj.get(i).getDistance()>obj.get(j).getDistance()) {
					ObjectHistogram e = obj.get(i);
					obj.set(i, obj.get(j));
					obj.set(j, e);
				}
			}
		}
		
		for (int i=0;i<obj.size()-1;i++) {
			for(int j=i+1;j<obj.size();j++) {
		      double d = (obj.get(j).getDistance() + obj.get(i).getDistance())/2.0f;
		      value.add(d);
			}
		}
		return value;
	}

	public static double subsequenceDistEuclidien(TimeSerie current, TimeSerie val) {
		
		if (val.size()>current.size()) {
			TimeSerie temp = val;
			val = current;
			current = temp;
		}
			
		double result = Float.MAX_VALUE;
		int l = val.size();
		for (int i=0;i<current.size()+1-l;i++) {
			List<Double> temp = current.subList(i, i+l);
			double other = 0;
			for(int j=0;j<l;j++)
				other = other + (val.get(j)-temp.get(j))*(val.get(j)-temp.get(j));
			other = Math.sqrt(other);
			result = Math.min(result, other);
		}	
		return result;
	}
	
	public static double entropyBin(Collection<TimeSerie> data) {
		double result = 0;
		int cla = 0 ;
		int size = data.size();
		Iterator<TimeSerie> timeseries = data.iterator();
		List<TimeSerie> classA = new ArrayList<>(), classB = new ArrayList<>();
		while (timeseries.hasNext()) {
			TimeSerie ts = timeseries.next();
			if (ts.getObject_class() == cla)
				classA.add(ts);
			else
				classB.add(ts);
		}
		double a = Double.valueOf(classA.size())/Double.valueOf(size);
		double b = Double.valueOf(classB.size())/Double.valueOf(size);
		
		double a1 = (a==0)?0:a*Math.log(a);
		double b1 = (b==0)?0:b*Math.log(b);
		
		result = (-1)*(a1+b1);
		
		//System.out.println(result);
		
		return result;
	}
	
	public static double entropyBin(Collection<TimeSerie> data, int cla) {
		double result = 0;
		int size = data.size();
		Iterator<TimeSerie> timeseries = data.iterator();
		List<TimeSerie> classA = new ArrayList<>(), classB = new ArrayList<>();
		while (timeseries.hasNext()) {
			TimeSerie ts = timeseries.next();
			if (ts.getObject_class() == cla)
				classA.add(ts);
			else
				classB.add(ts);
		}
		double a = classA.size()/size;
		double b = classB.size()/size;
		result = -(a*Math.log(a)+b*Math.log(b));
		return result;
	}
	
	public static List<Boolean> doTest(TimeSerie shap, Collection<TimeSerie> dataTest){
		List<Boolean> result = new ArrayList<>();
		Iterator<TimeSerie> data = dataTest.iterator();
		
		System.out.println(finalValue);
		
		while (data.hasNext()) {
			TimeSerie test = data.next();
			int clas = test.getObject_class();
			double distance = subsequenceDistEuclidien(test, shap);
			if ((finalValue>distance && clas == 0)||(finalValue<=distance && clas == 1)) 
				result.add(true);
			else
				result.add(false);
		}
		
		return result;
	}
	
	public static double computeAccuracy(List<Boolean> result) {
		double accuracy = 0f;
		int i = 0;
		for(boolean b:result)
			if (b)
				i++;
		accuracy = Double.valueOf(i)/Double.valueOf(result.size());
		return accuracy;
	}
	
	public static double computeError(List<Boolean> result) {
		double error = 0f;
		int i = 0;
		for(boolean b:result)
			if (!b)
				i++;
		error = Double.valueOf(i)/Double.valueOf(result.size());
		return error;
	}	
	
}