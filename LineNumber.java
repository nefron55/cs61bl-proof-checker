import java.util.ArrayList;
import java.util.List;

public class LineNumber {
	private List<Integer> nums;
	List<String> lines;
	
	public LineNumber() {
		nums = new ArrayList<Integer>();
		lines = new ArrayList<String>();
		nums.add(1);
		lines.add(toString());
	}
	
	public LineNumber(String str) throws IllegalLineException {
		//assert isLegal(str, ln);
		nums = new ArrayList<Integer>();
		lines = new ArrayList<String>();
		if (!isLegal(str)){
			throw new IllegalLineException("Line not valid.");
		}
		String[] strpts = str.split(".");
		for (String part : strpts) {
			nums.add(Integer.parseInt(part));
		}
		lines.add(toString());
	}
	
	public void addPoint() {
		nums.add(1);
		lines.add(toString());
	}
	
	// in the course of extending a proof, if an inference is valid
	// we drop off the last point and increment the preceding value by 1  
//	public void resetPoint() {
//		removePeriod();
//		increment();
//		}
	
	public void removePeriod() throws IllegalLineException{
		if(nums.size() > 1){
			nums.remove(nums.size()-1);
		} else {
			throw new IllegalLineException("No period to remove");
		}
	}
	
	public void increment() {
		if (nums.size()==1 && nums.get(0)==1) {
			nums.set(0, 2);
		} else {
			int last = nums.get(nums.size()-1);
			nums.set(nums.size()-1, last+1);
		}
		lines.add(toString());
;	}
	
	public static boolean isLegal(String str) throws IllegalLineException{
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
	
	public boolean isLegalReference(String str) throws IllegalLineException {
		String current = this.toString();
		int i = 0;
		try {
			while (i < str.length()-1){
				if (str.charAt(i) != current.charAt(i)){
					return false;
				}
				i++;
			}
			if (Integer.parseInt(str.substring(i)) > Integer.parseInt(current.substring(i,i+1))){
				return false;
			}
			return true;
		} catch (NumberFormatException e){
			throw new IllegalLineException("Line format not valid.");
		}
	}
	
	public static int lineLevel (String str){
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
		for (int i : nums) {
			rtn += i + ".";
		}
		return rtn.substring(0, rtn.length()-1);
	}
	
}
