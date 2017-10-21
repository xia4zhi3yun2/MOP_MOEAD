package jmetal.problems;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.encodings.variable.ArrayReal;
import jmetal.util.JMException;




public class practice extends Problem {
	 public practice(String solutionType) throws ClassNotFoundException {
		  	this(solutionType, 512) ;
		  }
	
	 public practice(String solutionType, Integer numberOfBits) {
		    numberOfVariables_  = 3;
		    numberOfObjectives_ = 3;
		    numberOfConstraints_= 0;
		    problemName_        = "practice1";
		    
		    upperLimit_ = new double[numberOfVariables_];
		    lowerLimit_ = new double[numberOfVariables_];
		    
		    for (int var = 0; var < numberOfVariables_; var++){
		      lowerLimit_[var] = 0;
		      upperLimit_[var] = 1;
		    }
		    

		    solutionType_ = new RealSolutionType(this) ;
		    
		    if (solutionType.compareTo("Real") == 0)
		    	solutionType_ = new RealSolutionType(this) ;
		    else {
		    	System.out.println("practice: solution type " + solutionType + " invalid") ;
		    	System.exit(-1) ;
		    }  
		    
		  } 
		    

	@Override
	public void evaluate(Solution solution) throws JMException {
	
	    double [] x = new double[3] ; // decision variable
	    double [] f = new double[3] ; // 3 distance functions
	   
	    
	    x[0] = solution.getDecisionVariables()[0].getValue();
	    x[1] = solution.getDecisionVariables()[1].getValue();
	    x[2] = solution.getDecisionVariables()[2].getValue();

	
	    f[0] = Math.pow((x[0]-0),2) + Math.pow((x[1]-0),2) + Math.pow((x[2]-1),2) ;
	    f[1] = Math.pow((x[0]-0),2) + Math.pow((x[1]-1),2) + Math.pow((x[2]-0),2) ;
	    f[2] = Math.pow((x[0]-1),2) + Math.pow((x[1]-0),2) + Math.pow((x[2]-0),2) ;
	   
	   
	             
	    solution.setObjective(0,f[0]);    
	    solution.setObjective(1,f[1]);
	    solution.setObjective(2,f[2]);
	       
	  } // evaluate

	

}