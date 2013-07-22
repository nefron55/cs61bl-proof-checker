import java.util.*;

public class Proof {
	private LineNumber ln;
	private String reason;
	private Expression exp;

	public Proof (TheoremSet theorems) {
		ln = new LineNumber();
	}

	public LineNumber nextLineNumber ( ) {
		return ln;
	}

	public void extendProof (String x) throws IllegalLineException, IllegalInferenceException {
		System.out.println("Entered: " + x);
		x = x.trim();
		// is print?
		if (x.equals("print")) {
			// TODO: print proof so far
		} else {
			// try to split into reason and expression
			if(x.startsWith("show")){
				reason = "show";
				exp = new Expression(x.substring(5));
			}
			if(x.startsWith("assume")) {
				reason = "assume";
				exp = new Expression(x.substring(7));
			}
			if(x.startsWith("mp")) {
				reason = "mp";
				exp = new Expression(x.substring(3));
			}
			if(x.startsWith("mt")) {
				reason = "mt";
				exp = new Expression(x.substring(3));
			}
			if(x.startsWith("co")) {
				reason = "co";
				exp = new Expression(x.substring(3));
			}
			if(x.contains("ic")) {
				reason = "ic";
				exp = new Expression(x.substring(3));
			}
			if(x.contains("repeat")) {
				reason = "repeat";
				exp = new Expression(x.substring(7));
			} 
			System.out.println("reason: " + reason + "\nexpression: ");
			exp.print();
		}
	}
	
	public String toString ( ) {
		return "";
	}

	public boolean isComplete ( ) {
		return true;
	}
}