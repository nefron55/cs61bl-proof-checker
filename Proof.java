import java.util.*;

public class Proof {
	private LineNumber ln;
	private Stack<Expression> showing;
	private HashMap<String,Expression> facts; // changed key to String (allows for names) LineNumber.toString() should be called when adding to HashMaps
	private LinkedList<String> printQueue;
	
	public Proof (TheoremSet theorems) {
		ln = new LineNumber();
		showing = new Stack<Expression>();
		facts = new HashMap<String,Expression>();
		printQueue = new LinkedList<String>();
	}
	
	public LineNumber nextLineNumber(){
		return ln;
	}

	public void extendProof (String x) throws IllegalLineException, IllegalInferenceException {
		// System.out.println("Entered: " + x);
		//printAllFacts();
		x = x.trim();
		// is print?
		if (x.equals("print")) {
			for (int i = 0; i < printQueue.size(); i++){
				System.out.print(ln.lines.get(i) + "\t");
				System.out.println(printQueue.get(i));
			}
			ln.increment();
		} else {
			// try to split into reason and expression
			String[] parts = x.split(" ");
			// LIST OF ALL LEGAL COMBOS:
			// "show" exp
			if (parts[0].equals("show") 
					&& Expression.isLegal(parts[1]) && parts.length == 2) {
				//		add exp to "showing" stack
				//		add point to line number
				showing.push(new Expression(parts[1]));
				if (ln.toString().equals("1")) {
					ln.increment();
				} else {
					ln.addPoint();
				}
			// "assume" exp
			} else if (parts[0].equals("assume") 
					&& Expression.isLegal(parts[1]) && parts.length == 2) {
				//		add exp to "facts" array (facts are assumptions, inferred results, theorems)
				//		increment line number	
				facts.put(ln.toString(), new Expression(parts[1]));
				ln.increment();

			// "mp" ln1 ln2 exp
			} else if (parts[0].equals("mp")
					&& LineNumber.isLegal(parts[1], ln) && LineNumber.isLegal(parts[2], ln)
					&& Expression.isLegal(parts[3]) && parts.length == 4) {
			//		check that ln1 is the left branch of ln2
			//		check that exp is the right branch of ln2
			//		if both true, 
			//			add exp to "facts"
			//			pop exp off stack if (exp2 == stack.peek())
			//			remove point from line number	
			//		else 
			//			throw IllegalInferenceException
			//			
				// TODO need to check these are legal line numbers to use in inference
				// i.e. we're not outside their "domain" of validity
				// TODO also need to handle failure case where fact with such number doesn't exist
				Expression e1 = getFactByLineNumber(new LineNumber(parts[1]));
				Expression e1then2 = getFactByLineNumber(new LineNumber(parts[2]));
				Expression e2 = new Expression(parts[3]);
				if (e1.isLeftBranchOf(e1then2) && e2.isRightBranchOf(e1then2)) {
					facts.put(ln.toString(), e2);
					if (e2.equals(showing.peek())) showing.pop();
					ln.resetPoint();
				} else {
					throw new IllegalInferenceException("Illegal Modus Ponens");
				}								
				
			// "mt" ln1 ln2 exp
			} else if (parts[0].equals("mt")
					&& LineNumber.isLegal(parts[1], ln) && LineNumber.isLegal(parts[2], ln)
					&& Expression.isLegal(parts[3]) && parts.length == 4) {
				
				// TODO
				
			// "co" ln1 ln2 exp
			} else if (parts[0].equals("co")
					&& LineNumber.isLegal(parts[1], ln) && LineNumber.isLegal(parts[2], ln)
					&& Expression.isLegal(parts[3]) && parts.length == 4) {
					
				// TODO
				
			// "ic" ln1 exp
			} else if (parts[0].equals("ic") && parts.length == 3 
					&& LineNumber.isLegal(parts[1], ln) 
					&& Expression.isLegal(parts[2])) {
				// 		check that ln1 is the right branch of exp
				//		if true, 
				//			add exp to "facts"
				//			pop exp off stack if (exp2 == stack.peek())
				//			remove point from line number	
				//		else 
				//			throw IllegalInferenceException
				// 
				LineNumber refln = new LineNumber(parts[1]);
				Expression e = new Expression(parts[2]);
				if (getFactByLineNumber(refln).isRightBranchOf(e)) {
					facts.put(ln.toString(), e);
					if (e.equals(showing.peek())) showing.pop();
					ln.resetPoint();
				} else {
					throw new IllegalInferenceException("Illegal Modus Ponens");
				}


			// "repeat" ln1 exp
			} else if (parts[0].equals("repeat") 
					&& LineNumber.isLegal(parts[1], ln) 
					&& Expression.isLegal(parts[2]) && parts.length == 3) {
				
				// TODO
				// what does "repeat" do?				
			
				
			} else {
				throw new IllegalLineException("Wrong number of things");
			}
			
			printQueue.add(x);
			
			// Now that we've done input exception checking, 
			// it's safe to add the line to print queue 
			// printQueue.add(x);
			
			// DONE PROCESSING USER INPUT, REST HAPPENS IN ProofChecker.main[]			
		}
	}
	
	public String toString ( ) {
		String str = "";
		for (int i = 0; i < printQueue.size(); i++){
			str += printQueue.get(i) + "\n";
		}
		return str;
	}

	public boolean isComplete ( ) {
		// TODO
		return false;
	}
	
	private Expression getFactByLineNumber(LineNumber ln) {
		return facts.get(ln.toString());
	}
	
	// for debugging, remove later
	private void printAllFacts() {
		Iterator<String> it = facts.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			System.out.println(key + " - " + facts.get(key).toInorderString());
		}
	}
}
