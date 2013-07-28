import java.util.*;

public class TheoremSet {
	
	private HashMap<String,Expression> facts;

	public TheoremSet ( ) {
		facts = new HashMap<String,Expression>();
	}
	
	public boolean contains(String s){
		return facts.containsKey(s);
	}

	public void put (String s, Expression e) {
		facts.put(s, e);
	}
	
	public HashMap<String,Expression> transfer(){
		return facts;
	}
}
