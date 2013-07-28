public class Expression {
	public ExpNode myRoot;	

	// not sure if this will be necessary
	public Expression ( ) {
		myRoot = null;
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
	        // strip off any extra enclosing parentheses TODO - should they be legal?
			if (expr.startsWith("((") && expr.endsWith("))")) {
				try {
					return expTreeHelper(expr.substring(1, expr.length()-1));
				} catch (IllegalLineException e) {
					System.out.println("FAIL: " + e.getMessage());
					System.out.println("When trying to parse " + expr.substring(1, expr.length()-1));
					// move on and try to parse without stripping off enclosing parens
				}
			} 
			// this is to try to find (~(p=>q)) - although not sure this is actually a legal exp
			if (expr.startsWith("(~(") && expr.endsWith("))")) {
				try {
					return new ExpNode("~", null, expTreeHelper(expr.substring(2, expr.length()-1)));
				} catch (IllegalLineException e) {
					// move on and try to parse without stripping off enclosing parens  
				}
			}
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


	// Print the values in the tree in preorder: root value first,
	// then values in the left subtree (in preorder), then values
	// in the right subtree (in preorder).
	public String toPreorderString( ) {
		return toPreorderString (myRoot);
	}

	private static String toPreorderString (ExpNode t) {
		if (t != null) {
			return t.myItem + " " + toPreorderString(t.myLeft) + toPreorderString(t.myRight);
		} else {
			return "";
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

//	public void fillSampleTree1 ( ) {
//		myRoot =
//		    new TreeNode ("a",
//			new TreeNode ("b"),
//			new TreeNode ("c"));
//	}

	public static int height(ExpNode n) {
		if (n == null) {
			return 0;
		} else if (n.isLeaf()) {
			return 1;
        } else {
            return Math.max(height(n.myLeft), height(n.myRight)) + 1;
        }
	}

	public boolean isCompletelyBalanced() {
		if (myRoot == null) {
			return true;
		} else {
			return isCompletelyBalancedHelper(myRoot);
		}
	}

	private boolean isCompletelyBalancedHelper(ExpNode n) {
		if (n.myLeft == null && n.myRight == null) {
			return true;
		} else if ((n.myLeft == null && n.myRight != null) ||
					(n.myLeft != null && n.myRight == null)) {
			return false;
		} else {
			return (isCompletelyBalancedHelper(n.myLeft) 
					&& isCompletelyBalancedHelper(n.myRight));
		}

	}

	public void print ( ) {
	    if (myRoot != null) {
	        printHelper (myRoot, 0);
	    }
	}

	private static final String indent1 = "    ";

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


	public static boolean isLegal(String string) {
		// TODO OR NOTTODO Auto-generated method stub
		return true;		
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

}
