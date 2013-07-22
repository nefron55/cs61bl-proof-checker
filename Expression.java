import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Expression {
	private String reason;
	private String express;
	
	public Expression (String s) throws IllegalLineException {
		expressionParser(s);
	}
		

	public class expressionNode {
		private Object operator = null;
		private expressionNode variables = null;

			public expressionNode(Object operatorObj, expressionNode variables) {
					
			}

			public void addExpression(Object operatorObj, String data) {
					if (operator != null) {
						expressionNode.addExpressionHelper(operatorObj, data);
					}
			}

			public static void addExpressionHelper(Object operatorObj, String data) {
				if () {
				
				} else {
					Iterator<expressionNode> iter = expressionNode.variables.iterator();
					while (iter.hasNext()) {
						expressionNode.addExpressionHelper( iter.next());}
				}
			}
	}
				

			public void expressionParser (String x) {
				if(x.contains("print")){
					reason = "print";
				}else {
					if(x.contains("show")){
						reason = "show";
						express = x.substring(5);
					}
					if(x.contains("assume")) {
						reason = "assume";
						express = x.substring(7);
					}
					if(x.contains("mp")) {
						reason = "mp";
						express = x.substring(3);
					}
					if(x.contains("mt")) {
						reason = "mt";
						express = x.substring(3);
					}
					if(x.contains("co")) {
						reason = "co";
						express = x.substring(3);
					}
					if(x.contains("ic")) {
						reason = "ic";
						express = x.substring(3);
					}
					if(x.contains("repeat")) {
						reason = "repeat";
						express = x.substring(7);
					}
					//handle theorems
				}
			}
}
