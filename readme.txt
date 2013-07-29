1) There are three test classes in our project--LineNumberTest, ExpressionTest and ProofTest.

ExpressionTest:
===============
We've included tests for all pertinent methods in Expression.java. 
*   testExpression() tests constructing an Expression from a given String object. To that end, we provide
two arrays of Strings: legal and illegal. The test than asserts that constructing an Expression from a legal
string does not throw an exception, and that the resulting expression, once converted back to a String, matches
the String it was constructed from. For illegal strings, we assert that exceptions are thrown, as expected.
*   testInorderString() uses the same strings, test runs in much the same manner as testExpression().
*   testIsApplicable() tests that given a theorem and an expression, the expression is the correct application 
of the theorem.
*   all other tests use the same array of expression strings to verify that the various helper methods of 
Expression class work as expected.

All tests in ExpressionTest rely on the core ability to construct an Expression from a legal String, 
which is tested in testExpression(). The tests verify that IllegalLineException is thrown whenever the String
is illegal, which is what the constructor does.   


LineNumberTest:
===============

testIncrement: testIncrement tests the Increment and AddPoint methods in the LineNumber class which are called in extendProof each time a new line
of input is given to ProofChecker.
 
testIsLegal: testIsLegal tests the IsLegal method in the LineNumber class which is called in extendProof each time a new line of input is given. IsLegal checks
to see if the line is accessible based on the stacking of subproofs and additionally, if the LineNumber is constructed correctly. 

testLineLevel: testLineLevel tests the LineLevel method in the LineNumber class which returns the number of points in a given LineNumber which reflects the stacking
of subproofs. This is used in extendProof to check, if upon the completion of a subproof, a LineNumber needs to have the resetProof method called on it.

ProofTest:
==========
  