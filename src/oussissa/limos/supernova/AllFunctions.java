package oussissa.limos.supernova;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
//import java.util.Random;

public class AllFunctions {

	private static double split_dist = 0f;
	public static double finalValue = 0f;
	
	public static List<TimeSerie> dt1 ;
	public static List<TimeSerie> dt2 ;
	
	
	public static Collection<TimeSerie> d1 ;
	public static Collection<TimeSerie> d2 ;
	
	
	
	
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
		 TimeSerie result = null;
		 Collection<TimeSerie> candidates = generateCandidates(data, maxLen, minLen);
		 double bsf_gain = 0;
		 Iterator<TimeSerie> d = candidates.iterator();
		 //System.out.println(candidates.size());
		 while (d.hasNext()) {
			 TimeSerie val = d.next();
			double gain = checkCandidate(data, val);
			if (gain>bsf_gain) {
				bsf_gain = gain;
				result = val;
				finalValue = split_dist;
				d1 = new ArrayList<>(dt1);
				d2 = new ArrayList<>(dt2);
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
		dt1 = new ArrayList<>();
		dt2 = new ArrayList<>();
		List<TimeSerie> all = new ArrayList<>();
		double result = Double.MIN_VALUE; 
		
		List<Double> possibleValue = getPossibleValues(objectHistograms);
		for (double d:possibleValue) {
			dt1.clear();
			dt2.clear();
			all.clear();
			for (int i=0;i<objectHistograms.size();i++) {
				ObjectHistogram elt = objectHistograms.get(i);
				if (elt.getDistance()<d)
					dt1.add(elt.getTimeSerie());
				else
					dt2.add(elt.getTimeSerie());
				all.add(elt.getTimeSerie());
			}
			double iid = ((dt1.size()/all.size())*entropyBin(dt1))+((dt2.size()/all.size())*entropyBin(dt2));
			double id = entropyBin(all);
			double a = result;
			result = Double.max(result, id-iid);
			if (a<result) 
				split_dist = d;
			else
				break;
		}
		return result;
	}

	public static double computeObtimalSplitPoint(List<ObjectHistogram> objectHistograms) {
		double result = 0f;
		
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
	
	
	public static boolean doTest(DecisionTree dt, TimeSerie test){
		if (dt.getRight()!=null&&dt.getLeft()!=null) { 
			double distance = subsequenceDistEuclidien(test, dt.getTimeSerie());
			if (distance<dt.getSplitPoint())
				return doTest(dt.getLeft(), test);
			else
				return doTest(dt.getRight(), test);
		}
		else 
			return test.getObject_class() == dt.getObject_class();
	}
	
	
	public static List<Boolean> doTest(DecisionTree dt, Collection<TimeSerie> dataTest){
		List<Boolean> result = new ArrayList<>();
		Iterator<TimeSerie> data = dataTest.iterator();
		while (data.hasNext()) 
			result.add(doTest(dt, data.next()));
		return result;
	}
	
	public static Predict doTesting(DecisionTree dt, TimeSerie test){
		if (dt.getRight()!=null&&dt.getLeft()!=null) { 
			double distance = subsequenceDistEuclidien(test, dt.getTimeSerie());
			if (distance<dt.getSplitPoint())
				return doTesting(dt.getLeft(), test);
			else
				return doTesting(dt.getRight(), test);
		}
		else {
			Predict ts = new Predict();
			ts.setObjectID(test.getId());
			ts.setPredictValue(dt.getObject_class());
			ts.setTrueValue(test.getObject_class());
			return ts;
		}
	}

	
	public static Predict doVote(List<DecisionTree> listDT, List<TimeSerie> next) {
		Predict predict = new Predict();
		int compFor0 = 0;
		for(int i=0;i<listDT.size();i++) {
			predict = doTesting(listDT.get(0), next.get(0));
			if (predict.getPredictValue()==0)
				compFor0++;
		}
		double p = Double.valueOf(compFor0)/Double.valueOf(listDT.size());
		if (p>0.5)
			predict.setPredictValue(0);
		else
			predict.setPredictValue(1);
		return predict;
	}

	
	public static List<Boolean> doTest(List<DecisionTree> listDT, Collection<List<TimeSerie>> dataTest){
		List<Boolean> result = new ArrayList<>();
		List<Predict> data = doTesting(listDT, dataTest);
		for (Predict next:data) 
			result.add(next.getTrueValue()==next.getPredictValue());
		return result;
	}
	
	public static List<Predict> doTesting(List<DecisionTree> listDT, Collection<List<TimeSerie>> dataTest){
		List<Predict> result = new ArrayList<>();
		Iterator<List<TimeSerie>> data = dataTest.iterator();
		while (data.hasNext()) {
			result.add(doVote(listDT, data.next()));
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
	
	public static int minlengh(Collection<TimeSerie> data) {
		Iterator<TimeSerie> times = data.iterator();
		int min = Integer.MAX_VALUE;//, max = 0;
		while (times.hasNext()) {
			TimeSerie ts = times.next();
			min = Integer.min(min, ts.size());
			//max = Integer.max(max, ts.size());
		}
		return min;
	}
	public static DecisionTree getTree(Collection<TimeSerie> data) {
		DecisionTree result = null;
		double d = entropyBin(data);
		if (d == 0) 
			return new DecisionTree((new ArrayList<TimeSerie>(data)).get(0).getObject_class());
		int maxLen = minlengh(data);
		int minLen = maxLen;
		TimeSerie r = findShapeletBF(data, maxLen, minLen);
		result = new DecisionTree(r, finalValue);
		result.setLeft(getTree(dt1));
		result.setRight(getTree(dt2));
		return result;
	}
	
}