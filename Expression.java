public class Expression {
	private static final String indent1 = "    ";

	private ExpNode myRoot;	

	public Expression (Object o) {
		if (o == null) {
			myRoot = null;
		}
	}
	
	public Expression (ExpNode t) {
		myRoot = t;
	}

	public Expression (String s) throws IllegalLineException {
		myRoot = expTreeHelper(s);
	}	

	private ExpNode expTreeHelper (String expr) throws IllegalLineException {
		expr = expr.trim();
		if (expr.isEmpty()) throw new IllegalLineException("expression is empty");
		if (expr.contains(" ")) throw new IllegalLineException("expression contains spaces: " + expr);
		if (expr.charAt (0) != '(') {
	    	// if not parenthesized, the only possible legal inputs are:
	    	// 1) lower-case letter
	    	// 2) "~" followed by legal expression
	    	if (expr.length() == 1 && Character.isLetter(expr.charAt(0))) {
	    		return new ExpNode(expr.substring(0,1));
	    	} else if (expr.charAt(0) == '~') {
	    		return new ExpNode(expr.substring(0,1), null, expTreeHelper(expr.substring(1, expr.length())));
	    	} else {
	    		throw new IllegalLineException("bad line: " + expr);
	    	}
		} else {
	        // expr is a parenthesized expression.

	        // find the main operator (an occurrence of &, |, or => 
	    	// not nested in parentheses), and construct the two subtrees.
	        int nesting = 0;
	        int opPos = 0; // operator position
	        boolean isFollows = false;
	        // TODO the loop below is missing error handling (like when there's no operator but there should be)
	        for (int k=1; k<expr.length()-1; k++) {
	            if (expr.charAt(k) == '(') { 
	            	nesting++;
	            } else if (expr.charAt(k) == ')') {
	            	nesting--;
	            } else if (nesting == 0 && (expr.charAt(k) == '&' || expr.charAt(k) == '|')) {
	            	opPos = k;
	            	break;
	            } else if (nesting == 0 && (expr.charAt(k) == '=' 
	            		&& expr.length() > k+1 && expr.charAt(k+1) == '>')) {
	            	opPos = k;
	            	isFollows = true;
	            	break;
	            }
	        }
	        if (opPos == 0){
	        	throw new IllegalLineException("proper operator not found in " + expr);
	        }
	        String opnd1 = expr.substring (1, opPos);
	        String opnd2, op;
	        if (isFollows) {
	        	opnd2 = expr.substring (opPos+2, expr.length()-1);
		        op = expr.substring (opPos, opPos+2);	        	
	        } else {
		        opnd2 = expr.substring (opPos+1, expr.length()-1);
		        op = expr.substring (opPos, opPos+1);	        	
	        }
	        return new ExpNode (op, expTreeHelper(opnd1), expTreeHelper(opnd2)); 
	    }
	}	

	// Print the values in the tree in inorder: values in the left
	// subtree first (in inorder), then the root value, then values
	// in the right subtree (in inorder).
	public String toInorderString( ) {
		return toInorderString(myRoot);
	}

	private static String toInorderString (ExpNode t) {
		if (t == null) {
			return "";
		} else if (t.isLeaf()) {
			return t.myItem;
		} else if (t.myItem.equals("~")) {
			return "~" + toInorderString(t.myRight);
		} else {
			return "(" + toInorderString(t.myLeft) + t.myItem + toInorderString(t.myRight) + ")"; 
		}
	}

	public boolean equals(Expression e) {
		return this.toInorderString().equals(e.toInorderString());
	}

	public void print ( ) {
	    if (myRoot != null) {
	        printHelper (myRoot, 0);
	    }
	}

	private static void printHelper (ExpNode root, int indent) {
	    if (root.myRight != null) printHelper (root.myRight, indent+1) ;
	    println (root.myItem, indent);
		if (root.myLeft != null) printHelper(root.myLeft, indent+1);
	}

	private static void println (Object obj, int indent) {
	    for (int k=0; k<indent; k++) {
	        System.out.print (indent1);
	    }
	    System.out.println (obj);
	}
	
	public Expression replaceLeaves(Expression input) throws IllegalLineException{
		Expression copy = new Expression(this.toInorderString());
		if (input != null){
			copy.myRoot = replaceHelper(copy.myRoot, input.myRoot);
		}
		return copy;
	}
	
	private ExpNode replaceHelper(ExpNode source, ExpNode input) throws IllegalLineException{
		if (source.isLeaf()){
			return input;
		}
		if (input.isLeaf() || !source.myItem.equals(input.myItem)){
			throw new IllegalLineException("Line not compatible with theorem.");
		}
		if (source.myLeft != null && input.myLeft != null){
			source.myLeft = replaceHelper(source.myLeft, input.myLeft);
		}
		source.myRight = replaceHelper(source.myRight, input.myRight);
		return source;
	}
	
	public boolean isApplicable(Expression input) throws IllegalLineException{
		Expression copy = this.replaceLeaves(input);
		if (copy.toInorderString().equals(input.toInorderString())){
			return true;
		} else {
			return false;
		}
	}

	public boolean isLeftBranchOf(Expression e) {
		return (this.myRoot.isEqual(e.myRoot.myLeft));
	}

	public boolean isRightBranchOf(Expression e) {
		return (this.myRoot.isEqual(e.myRoot.myRight));
	}
	
	public boolean isNegation() {
		return this.myRoot.myItem.equals("~");
	}
	
	public boolean isFollows() {
		return this.myRoot.myItem.equals("=>");
	}
	
	public ExpNode getMyRight() {
		return this.myRoot.myRight;
	}
		
	public ExpNode getMyLeft() {
		return this.myRoot.myLeft;
	}
	
	public String getMyItem() {
		return this.myRoot.myItem;
	}
	
	public ExpNode getMyRoot() {
		return myRoot;
	}

	static class ExpNode {
		public String myItem;
		public ExpNode myLeft;
		public ExpNode myRight;

		public ExpNode (String str) {
			myItem = str;
			myLeft = myRight = null;
		}

		public ExpNode (String str, ExpNode left, ExpNode right) {
			myItem = str;
			myLeft = left;
			myRight = right;
		}

		public boolean isLeaf() {
			if (myLeft == null && myRight == null) {
				return true;
			} else {
				return false;
			}
		}

		public boolean isEqual(ExpNode tn) {
			if (!this.myItem.equals(tn.myItem)) {
				return false;
			} else if (this.myRight == null && tn.myRight == null && this.myLeft == null && tn.myRight == null){
				return true;
			} else if (this.myRight != null && tn.myRight != null && !this.myRight.isEqual(tn.myRight)) {
				return false;
			} else if (this.myLeft != null && tn.myLeft != null && !this.myLeft.isEqual(tn.myLeft)) {
				return false;
			} else {
				return true;
			}
		}

	}
}
