import java.util.ArrayList;
import java.util.List;

import com.sun.org.apache.bcel.internal.generic.LASTORE;

public class LineNumber {
	private List<Integer> ln;
	List<String> lines;
	
	public LineNumber() {
		ln = new ArrayList<Integer>();
		lines = new ArrayList<String>();
		ln.add(1);
		lines.add(toString());
	}
	
	public LineNumber(String str) {
		//assert isLegal(str, ln);
		String[] strpts = str.split(".");
		for (String part : strpts) {
			ln.add(Integer.parseInt(part));
		}
		lines.add(toString());
	}
	
	public void addPoint() {
		ln.add(1);
		lines.add(toString());
	}
	
	// in the course of extending a proof, if an inference is valid
	// we drop off the last point and increment the preceding value by 1  
	public void resetPoint() {
		ln.remove(ln.size()-1);
		int last = ln.get(ln.size()-1);
		ln.set(ln.size()-1, last+1);
		lines.add(toString());
	}
	
	public void increment() {
		if (ln.size()==1 && ln.get(0)==1) {
			ln.set(0, 2);
		} else {
			int last = ln.get(ln.size()-1);
			ln.set(ln.size()-1, last+1);
		}
		lines.add(toString());
;	}
	
	public static boolean isLegal(String str) throws IllegalLineException {
		int i = 0; String s;
		while (i < str.length()){
			s = str.substring(i,i+1);
			try {
				Integer.parseInt(s);
				i++;
			} catch (NumberFormatException e){
				if (s.equals(".")){
					i++;
					continue;
				} else {
					return false;
				}
			}
		}
		return true;
	}
	
	public static boolean isLegal(String str, LineNumber ln){
		if (lineLevel(str) >= lineLevel(ln.toString())){
			return false;
		}
		return true;
	}
	
	private static int lineLevel (String str){
		int count = 0;
		for (int i = 0; i < str.length(); i++){
			if (str.charAt(i) == '.'){
				count++;
			}
		}
		return count;
	}
	
	public String toString() {
		String rtn = "";
		for (int i : ln) {
			rtn += i + ".";
		}
		return rtn.substring(0, rtn.length()-1);
	}
	
}
