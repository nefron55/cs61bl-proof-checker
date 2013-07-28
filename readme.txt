1) There are three test classes in our project--LineNumberTest, ExpressionTest and ProofTest.

LineNumberTest:

testIncrement: testIncrement tests the Increment and AddPoint methods in the LineNumber class which are called in extendProof each time a new line
of input is given to ProofChecker.
 
testIsLegal: testIsLegal tests the IsLegal method in the LineNumber class which is called in extendProof each time a new line of input is given. IsLegal checks
to see if the line is accessible based on the stacking of subproofs and additionally, if the LineNumber is constructed correctly. 

testLineLevel: testLineLevel tests the LineLevel method in the LineNumber class which returns the number of points in a given LineNumber which reflects the stacking
of subproofs. This is used in extendProof to check, if upon the completion of a subproof, a LineNumber needs to have the resetProof method called on it.

ProofTest:

  