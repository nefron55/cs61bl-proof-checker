import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.LASTORE;

public class LineNumber {
	private List<Integer> ln;
	
	public LineNumber() {
		ln = new ArrayList<Integer>();
		ln.add(1);
	}
	
	public LineNumber(String str) {
		String[] strpts = str.split(".");
		for (String part : strpts) {
			ln.add(Integer.parseInt(part));
		}
	}
	
	public void addPoint() {
		ln.add(1);
	}
	
	// in the course of extending a proof, if an inference is valid
	// we drop off the last point and increment the preceding value by 1  
	public void resetPoint() {
		ln.remove(ln.size()-1);
		int last = ln.get(ln.size()-1);
		ln.set(ln.size()-1, last+1);
	}
	
	public void increment() {
		if (ln.size()==1 && ln.get(0)==1) {
			ln.set(0, 2);
		} else {
			int last = ln.get(ln.size()-1);
			ln.set(ln.size()-1, last+1);
		}
	}
	
	public static boolean isLegal(String str) {
	// TODO 
		return true;
	}
	
	public String toString() {
		String rtn = "";
		for (int i : ln) {
			rtn += i + ".";
		}
		return rtn.substring(0, rtn.length()-1);
	}
	
}