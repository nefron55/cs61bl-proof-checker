import java.util.*;

public class Proof {
	private LineNumber ln;
	private Stack<Expression> showing;
	private HashMap<String,Expression> facts; // changed key to String (allows for names) LineNumber.toString() should be called when adding to HashMaps
	private LinkedList<String> printQueue;
	private boolean isDone = false;
	private boolean isDebugging = false;

	public Proof() {
		ln = new LineNumber();
		showing = new Stack<Expression>();
		facts = new HashMap<String,Expression>();
		printQueue = new LinkedList<String>();		
	}
	
	public Proof (TheoremSet theorems) {
		ln = new LineNumber();
		showing = new Stack<Expression>();
		facts = theorems.transfer();
		printQueue = new LinkedList<String>();
	}
	
	public void startDebugging(){
		isDebugging = true;
	}

	public LineNumber nextLineNumber(){
		return ln;
	}

	public void extendProof (String x) throws IllegalLineException, IllegalInferenceException {
		// System.out.println("Entered: " + x);
		//printAllFacts();
		x = x.trim();
		// is print?
		if (x.equals("print") && x.length() == 5) {
			for (int i = 0; i < printQueue.size(); i++){
				System.out.print(ln.lines.get(i) + "\t");
				System.out.println(printQueue.get(i));
			}
			ln.increment();
			//System.out.print(toString());
		} else {
			// try to split into reason and expression
			String[] parts = x.split(" ");
			// LIST OF ALL LEGAL COMBOS:
			// "show" exp
			if (parts[0].equals("show")  && parts.length == 2) {
				//		add exp to "showing" stack
				//		add point to line number
				showing.push(new Expression(parts[1]));
				if (ln.toString().equals("1")) {
					ln.increment();
				} else {
					ln.addPoint();
				}
			// "assume" exp
			} else if (parts[0].equals("assume") && parts.length == 2
					&& (printQueue.getLast().trim().split(" ")[0].equals("show") ||
							(printQueue.getLast().trim().split(" ")[0].equals("assume") && isDebugging))){
				//		add exp to "facts" array (facts are assumptions, inferred results, theorems)
				//		increment line number
				doAssume(parts);

			// "mp" ln1 ln2 exp
			} else if (parts[0].equals("mp")  && parts.length == 4) {
			//		check that ln1 is the left branch of ln2
			//		check that exp is the right branch of ln2
			//		if both true, 
			//			add exp to "facts"
			//			pop exp off stack if (exp2 == stack.peek())
			//			remove point from line number	
			//		else 
			//			throw IllegalInferenceException
			//			
				if  (!ln.isLegalReference(parts[1])){
					throw new IllegalLineException("Cannot refer to line " + parts[1]);
				} else if (!ln.isLegalReference(parts[2])){
					throw new IllegalLineException("Cannot refer to line " + parts[2]);
				}
				
				doMP(parts);

			// "mt" ln1 ln2 exp
			} else if (parts[0].equals("mt") && parts.length == 4) {
				if (!ln.isLegalReference(parts[1])){
					throw new IllegalLineException("Connot refer to line " + parts[1]);
				} else if (!ln.isLegalReference(parts[2])){
					throw new IllegalLineException("Cannot refer to line " + parts[2]);
				}
				
				doMT(parts);

			// "co" ln1 ln2 exp
			} else if (parts[0].equals("co") && parts.length == 4){
					if (!ln.isLegalReference(parts[1])){
						throw new IllegalLineException("Unable to refer to line " + parts[1]);
					} else if (!ln.isLegalReference(parts[2])){
						throw new IllegalLineException("Unable to refer to line " + parts[2]);
					}
					
					doCO(parts);
				

			// "ic" ln1 exp
			} else if (parts[0].equals("ic") && parts.length == 3) {
				// 		check that ln1 is the right branch of exp
				//		if true, 
				//			add exp to "facts"
				//			pop exp off stack if (exp2 == stack.peek())
				//			remove point from line number	
				//		else 
				//			throw IllegalInferenceException
				// 
				if (!ln.isLegalReference(parts[1])){
					throw new IllegalLineException("Unable to refer to line " + parts[1]);
				}
				doIC(parts);

			// "repeat" ln1 exp
			} else if (parts[0].equals("repeat")
					&& parts.length == 3 
					//&& getFactByLineNumber(parts[1]).myRoot.equals("1")
					) {
				//cant access a line that begins with 'show' that hasn't been completed yet
				// if expression given by line number in repeat statement is in facts, then show.pop
				//and put it in facts again
				//else illegal repeat statement
				if (!ln.isLegalReference(parts[1])){
					throw new IllegalLineException("Unable to refer to line " + parts[1]);
				}
				doRepeat(parts);
	
			} else if (facts.containsKey(parts[0])){
				Expression exp = facts.get(parts[0]);
				Expression e = new Expression(parts[1]);
				if (exp.isApplicable(e)){
					completed(e);					
				} else {
					throw new IllegalLineException("Line not compatible with theorem.");
				}
			} else {
				throw new IllegalLineException("Wrong number of things");
			}

			// Now that we've done input exception checking, 
			// it's safe to add the line to print queue 			
			printQueue.add(x);

			// Now check if there's anything left on showing stack
			// If not and we're not on line 1, then the proof is complete
			if (showing.isEmpty() && !ln.toString().equals("1")) {
				isDone = true;
			}
			
			if (isDebugging){
				System.out.println("isComplete? " + isDone + ". Things on showing stack:");
				Stack<Expression> showCopy = new Stack<Expression>();
				showCopy.addAll(showing);
				while (!showCopy.isEmpty()) {
					System.out.println(showCopy.pop().toInorderString());
				}
			}
			// DONE PROCESSING USER INPUT, REST HAPPENS IN ProofChecker.main[]	
			
		}
	}
	
	private void doMP(String [] parts) throws IllegalLineException, IllegalInferenceException{
		Expression e1, e1then2;
		if(getFactByLineNumber(parts[1]).toInorderString().length()  > getFactByLineNumber(parts[2]).toInorderString().length()){
			e1then2 = getFactByLineNumber(parts[1]);
			e1 = getFactByLineNumber(parts[2]);
		} else {
			e1then2 = getFactByLineNumber(parts[2]);
			e1 = getFactByLineNumber(parts[1]);
		}
		Expression e2 = new Expression(parts[3]);
		if (e1.isLeftBranchOf(e1then2) && e2.isRightBranchOf(e1then2)) {
			this.completed(e2);
		} else {
			throw new IllegalInferenceException("Illegal Modus Ponens");
		}
	}
	
	private void doMT(String[] parts) throws IllegalLineException, IllegalInferenceException {
		Expression part1 = getFactByLineNumber(parts[1]);
		Expression part2 = getFactByLineNumber(parts[2]); 
		Expression note2, e1then2;
		if (part1.isNegation() && part2.isFollows()) {
			note2 = part1;
			e1then2 = part2;
		} else if (part1.isFollows() && part2.isNegation()) {
			note2 = part2;
			e1then2 = part1;
		} else {
			throw new IllegalInferenceException("Illegal Modus Tollens");
		}
		Expression note1 = new Expression(parts[3]);
		if(note1.isNegation() 
			&& note1.getMyRight().isEqual(e1then2.getMyLeft()) 
			&& note2.getMyRight().isEqual(e1then2.getMyRight())) {
				this.completed(note1);
		} else {
			throw new IllegalInferenceException("Illegal Modus Tollens");
		}
	}
	
	private void doIC(String[] parts) throws IllegalLineException, IllegalInferenceException{
		Expression e = new Expression(parts[2]);
		Expression factoid = getFactByLineNumber(parts[1]);
		if (factoid.isRightBranchOf(e)) {
			this.completed(e);
		} else {
			throw new IllegalInferenceException("Illegal Implication");
		}
	}
	
	private void doCO(String[] parts) throws IllegalLineException, IllegalInferenceException{
		Expression e1 = getFactByLineNumber(parts[1]);
		Expression e2 = getFactByLineNumber(parts[2]);
		if ((e1.isNegation() && e2.isRightBranchOf(e1))
				|| (e2.isNegation() && e1.isRightBranchOf(e2))) {
			Expression e = new Expression(parts[3]);
			this.completed(e);
		} else {
			throw new IllegalInferenceException("Illegal Contradiction");
		}
	}
	private void doAssume(String[] parts) throws IllegalLineException, IllegalInferenceException{
		Expression e = new Expression(parts[1]);
		if (isDebugging || (e.isNegation() && e.getMyRight().isEqual(showing.peek().getMyRoot())) ||
				showing.peek().isFollows() && e.isLeftBranchOf(showing.peek())){
			facts.put(ln.toString(), e);
			ln.increment();
		} else {
			throw new IllegalLineException("Con only assume ~E or the left side of implication E.");
		}
	}
	
	private void doRepeat(String[] parts) throws IllegalLineException, IllegalInferenceException{
		if(!parts[1].equals("1")
				&& getFactByLineNumber(parts[1]).equals(new Expression(parts[2]))){
			Expression e = new Expression(parts[2]);
			facts.put(ln.toString(), e);
			showing.pop();
		} else {
			throw new IllegalInferenceException("Illegal Repeat Statement");
		}
	}

	public String toString () {
		String str = "";
		for (int i = 0; i < printQueue.size(); i++){
			str += printQueue.get(i) + "/n"; //need to account for /n
		}
		return str;
	}

	public boolean isComplete ( ) {
		return isDone;
	}

	private Expression getFactByLineNumber(String ln) {
		return facts.get(ln);
	}
	
	private void completed(Expression e) throws IllegalLineException {
		facts.put(ln.toString(), e);
		if (e.equals(showing.peek())){
			showing.pop();
			if (LineNumber.lineLevel(ln.toString()) != 0){
				ln.removePeriod();
				facts.put(ln.toString(), e);
			}
			ln.increment();
		} else {
			ln.increment();
		}
	}
	

	// for debugging, remove later
	public void printAllFacts() {
		System.out.println("-----facts-----");
		Iterator<String> it = facts.keySet().iterator();
		while (it.hasNext()) {
			String key = it.next();
			System.out.println(key + " - " + facts.get(key).toInorderString());
		}
		System.out.println("--end-facts---\n");
	}
	
	public LineNumber getLine() {
		return ln;
	}
}
