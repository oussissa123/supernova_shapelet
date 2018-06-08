package oussissa.limos.supernova;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {
//	public static Collection<TimeSerie> data = new ArrayList<>();
//	public static Collection<TimeSerie> test = new ArrayList<>();
	public static int maxLen = 7;
	public static int minLen = 7;
	
	public static void main(String[] args) {
		
		List<DecisionTree> trees = new ArrayList<>();
		Collection<List<TimeSerie>> tests = new ArrayList<>();

		System.out.println("Starting ...");
		
		for(String band:new String []{"g","r","i","z"}) {
			Collection<TimeSerie> data = readToArray("data/des_train_mini.json", band);
			trees.add(AllFunctions.getTree(data));
			tests.add(readToArray("data/des_test_mini.json", band));
		}

		System.out.println("Training ok ...");
		List<Boolean> value = AllFunctions.doTest(trees, tests);
		System.out.println("Testing ok ...");
		save("result.text", value.toString() + "\n Accuracy: "+ AllFunctions.computeAccuracy(value)+ "\n Error: "+ AllFunctions.computeError(value));

		
		/*
		maxLen = AllFunctions.minlengh(data);
		minLen = maxLen;
		System.out.println(minLen);
		
		System.out.println("Starting ...");
		//long time = System.currentTimeMillis();
		TimeSerie result = AllFunctions.findShapeletBF(data, maxLen, minLen);
		//time = System.currentTimeMillis() - time;
		if (result != null) {
			String value = result.toString();//+"\ntime: "+time;
			readToArray("data/des_test_mini.json", test);
			List<Boolean> testing = AllFunctions.doTest(result,test);
			value += "\n"+testing.toString();
			value +="\n Accuracy: "+AllFunctions.computeAccuracy(testing);
			value +="\n Error: "+AllFunctions.computeError(testing);
			save("result.text", value);
			System.out.println(value);
		}
		//else
			///System.out.println(time);
		System.out.println("End ...");*/
	}
	
	public static String readData(String path){
		File file = new File(path);
		String result = "";
		try {
			Scanner scanner = new Scanner(file);
			StringBuffer value = new StringBuffer();
			while (scanner.hasNext()) 
				value.append(scanner.nextLine());
			scanner.close();
			result = value.toString();
		}catch(Exception e) {e.printStackTrace();}
		return result;
	}
	
	public static List<TimeSerie> readToArray(String path, String band) {
		List<TimeSerie> d = new ArrayList<>();
		String result = readData(path);
		JSONObject jo = new JSONObject(result);
		Iterator<String> keys = jo.keySet().iterator();
		while (keys.hasNext()) {
			String ke = keys.next();
			JSONObject j1 = jo.getJSONObject(ke);
			
			TimeSerie ts = new TimeSerie();
			ts.setId(ke);
			JSONArray gValues = j1.getJSONObject(band).getJSONArray("fluxcal");
			for (int i=0;i<gValues.length();i++) 
				ts.add(gValues.getDouble(i));
			int type = (j1.getJSONObject("header").getInt("type")==0)?0:1;
			ts.setObject_class(type);
			d.add(ts);
		}
		
		return d;
	}
	
	public static void save(String path, String value) {
		File file = new File(path);
		try {
			if (!file.exists())
				file.createNewFile();
			PrintWriter pw = new PrintWriter(file);
			pw.println(value);
			pw.flush();
			pw.close();
		}catch(Exception e) {}
	}
}
