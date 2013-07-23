public class Expression {
	private ExpNode myRoot;	
	
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
	    if (expr.charAt (0) != '(') {
	    	// if not parenthesized, the only possible legal inputs are a or ~a
	    	if (expr.length() == 1 && Character.isLetter(expr.charAt(0))) {
	    		return new ExpNode(expr.charAt(0));
	    	} else if (expr.length() == 2 && expr.charAt(0) == '~' && Character.isLetter(expr.charAt(1))) {
	    		return new ExpNode(expr.charAt(0), null, new ExpNode(expr.charAt(1)));
	    	} else {
	    		throw new IllegalLineException("bad line");
	    	}
	    } else {
	        // expr is a parenthesized expression.
	        // Strip off the beginning and ending parentheses,
	        // find the main operator (an occurrence of &, |, or => 
	    	// not nested in parentheses), and construct the two subtrees.
	        int nesting = 0;
	        int opPos = 0;
	        boolean isFollows = false;
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
	        String opnd1 = expr.substring (1, opPos);
	        String opnd2, op;
	        if (isFollows) {
	        	opnd2 = expr.substring (opPos+2, expr.length()-1);
		        op = expr.substring (opPos, opPos+2);	        	
	        } else {
		        opnd2 = expr.substring (opPos+1, expr.length()-1);
		        op = expr.substring (opPos, opPos+1);	        	
	        }
	        return new ExpNode (op, expTreeHelper(opnd2), expTreeHelper(opnd1)); 
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
		if (t != null) {
			return toInorderString(t.myLeft) + t.myItem + toInorderString(t.myRight);
		} else {
			return "";
		}
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
	    if (root.myLeft != null) printHelper(root.myLeft, indent+1);
	    println (root.myItem, indent);
	    if (root.myRight != null) printHelper (root.myRight, indent+1) ;
	}
			
	private static void println (Object obj, int indent) {
	    for (int k=0; k<indent; k++) {
	        System.out.print (indent1);
	    }
	    System.out.println (obj);
	}
	

	private static class ExpNode {
		
		public Object myItem;
		public ExpNode myLeft;
		public ExpNode myRight;
		
		public ExpNode (Object obj) {
			myItem = obj;
			myLeft = myRight = null;
		}
		
		public ExpNode (Object obj, ExpNode left, ExpNode right) {
			myItem = obj;
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
		
		// not sure if this will be necessary
		public boolean isMyItemInt() {
			if (myItem instanceof Integer) return true;
			try {
				int x = Integer.parseInt((String) myItem);
				return true;
			} catch (NumberFormatException e) {
				return false;
			}
		}
		
		public boolean isEqual(ExpNode tn) {
			if (!this.myItem.equals(tn.myItem)) {
				return false;
			} else if (!this.myRight.isEqual(tn.myRight)) {
				return false;
			} else if (!this.myLeft.isEqual(tn.myLeft)) {
				return false;
			} else {
				return true;
			}
		}
				
	}


	public static boolean isLegal(String string) {
		// TODO Auto-generated method stub
		return true;		
	}
	
	
	public boolean isLeftBranchOf(Expression e) {
		return (this.myRoot.isEqual(e.myRoot.myLeft));
	}
	
	public boolean isRightBranchOf(Expression e) {
		return (this.myRoot.isEqual(e.myRoot.myRight));
	}
	
	
}