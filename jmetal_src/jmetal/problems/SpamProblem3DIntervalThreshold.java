//  Spam.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.

package jmetal.problems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Pattern;

import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.Variable;
import jmetal.encodings.solutionType.ArrayRealSolutionType;
import jmetal.encodings.solutionType.BinaryRealSolutionType;
import jmetal.encodings.solutionType.RealSolutionType;
import jmetal.util.JMException;

/** 
 * Class representing problem SpamProblem3D_Threshold
 */
public class SpamProblem3DIntervalThreshold extends Problem {

	String filePath = "./";
	HashMap<String, Integer> regras = new HashMap<String, Integer>();
	HashMap<String, Integer> usedRules = new HashMap<String, Integer>();
	HashMap<Integer, String> usedRulesIndex = new HashMap<Integer, String>();
	HashMap<Integer, String[]> rulesHam = new HashMap<Integer, String[]>();
	HashMap<Integer, String[]> rulesSpam = new HashMap<Integer, String[]>();
	HashMap<String, Double> scores = new HashMap<String, Double>();
	HashMap<String, Double> scoresIndex = new HashMap<String, Double>();
	HashMap<Double, Integer> freq = new HashMap<Double, Integer>();
	int rules = 0;
	int auxTmp = 0;
	int totalNumberOfHam = 0;
	int totalNumberOfSpam = 0;
	int count = 0;
	//double threshold = 1.0D; // For this problem formulation there is threshold_lowerbound and threshold_upperbound (defined and used in the evaluation method
	//double lower = -0.5D;
	//double high = 1.5D;
	double soma = 0.0D;
	int trueNegative = 0;
	int truePositive = 0;
	//double parsimony_rule_weight_delta_around_zero_for_rule_to_be_considered_irrelevant = 0; // Rules with zero weight will be considered irrelevant for classification purposes when parsimony is considered (minimizing the rules with zero weight, or some low value close to zero) 
	
	
	public SpamProblem3DIntervalThreshold(String solutionType) throws ClassNotFoundException, IOException {
	    //this.lower = -5;
	    //this.high = 5;
	    this.trueNegative = 0;
	    this.truePositive = 0;
	    //this.parsimony_rule_weight_delta_around_zero_for_rule_to_be_considered_irrelevant = 0.01; // Rules with zero +- parsimony_rules_weight_tolerance_around_zero will be considered irrelevant for classification purposes when parsimony is considered (minimizing the rules with zero weight, or some low value close to zero)
	    //this.filePath = filePath;
	    this.filePath = "./";
	    File scoresF = new File(filePath + "50_scores.cf");
	    Pattern regex = Pattern.compile(".*");
	    Scanner input = new Scanner(scoresF);
//	    while (input.hasNext())
//	    {
//	        String line = input.findInLine(regex);
//	        line = line.trim();
//	        String[] aux = line.split(" ");
//	        aux[1] = aux[1].trim();
//	        aux[2] = aux[2].trim();
//	        this.scores.put(aux[1], Double.valueOf(Double.parseDouble(aux[2])));
//	        input.nextLine();
//	    } // while
	    File f = new File(filePath + "usedRules.log");
	    f.createNewFile();
	    FileWriter fw = new FileWriter(f);
	    BufferedWriter out = new BufferedWriter(fw);

	    File hamFile = new File(filePath + "ham.log");
	    this.auxTmp = 0;
	    try {
	        regex = Pattern.compile("(\t.*)+");
	        input = new Scanner(hamFile);
	        while (input.hasNext())
	        {
	            String line = input.findInLine(regex);
	            line = line.trim();
	            String[] auxRules = line.split("\t");
	            String[] help = new String[auxRules.length];
	            for (int i = 0; i < auxRules.length; i++) {
	                auxRules[i] = auxRules[i].trim();
	                help[i] = auxRules[i];
	                if (this.usedRules.containsKey(auxRules[i]))
	                    continue;
	                this.usedRulesIndex.put(Integer.valueOf(this.rules), auxRules[i]);
	                this.usedRules.put(auxRules[i], Integer.valueOf(this.rules));
	                out.write(auxRules[i] + "\n");
//	                if (this.freq.containsKey(this.scores.get(auxRules[i])))
//	                {
//	                    this.freq.put((Double)this.scores.get(auxRules[i]), Integer.valueOf(((Integer)this.freq.get(this.scores.get(auxRules[i]))).intValue() + 1));
//	                }
//	                else
//	                {
//	                    this.freq.put((Double)this.scores.get(auxRules[i]), Integer.valueOf(1));
//	                }
	                this.rules += 1;
	            } // for
	            this.rulesHam.put(Integer.valueOf(this.auxTmp), help);
	            this.auxTmp += 1;
	            input.nextLine();
	        } // while
			this.totalNumberOfHam = this.auxTmp;
	    }
	    catch (Exception e) {
	        System.out.println(e.getMessage());
	    }

	    File spamFile = new File(filePath + "spam.log");
	    this.auxTmp = 0;
	    try {
	        regex = Pattern.compile("(\t.*)+");
	        input = new Scanner(spamFile);
	        while (input.hasNext()) {
	            String line = input.findInLine(regex);
	            line = line.trim();
	            String[] auxRules = line.split("\t");
	            String[] help = new String[auxRules.length];
	            for (int i = 0; i < auxRules.length; i++) {
	                auxRules[i] = auxRules[i].trim();
	                help[i] = auxRules[i];
	                if (this.usedRules.containsKey(auxRules[i]))
	                    continue;
	                this.usedRulesIndex.put(Integer.valueOf(this.rules), auxRules[i]);
	                this.usedRules.put(auxRules[i], Integer.valueOf(this.rules));
	                out.write(auxRules[i] + "\n");
//	                if (this.freq.containsKey(this.scores.get(auxRules[i])))
//	                {
//	                    this.freq.put((Double)this.scores.get(auxRules[i]), Integer.valueOf(((Integer)this.freq.get(this.scores.get(auxRules[i]))).intValue() + 1));
//	                }
//	                else
//	                {
//	                    this.freq.put((Double)this.scores.get(auxRules[i]), Integer.valueOf(1));
//	                }
	                    this.rules += 1;
	            } // for
	            this.rulesSpam.put(Integer.valueOf(this.auxTmp), help);
	            this.auxTmp += 1;
	            input.nextLine();
	        } // while
			this.totalNumberOfSpam = this.auxTmp;
	    } catch (Exception e) {
	        System.out.println(e.getMessage());
	    }
	    out.close();
    
//	    File f1 = new File(filePath + "freq.log");
//	    f1.createNewFile();
//	    FileWriter fw1 = new FileWriter(f1);
//	    BufferedWriter out1 = new BufferedWriter(fw1);
//	    for (Entry<Double, Integer> key : freq.entrySet()) {
//	        out1.write(key.getKey() + "\t" + key.getValue() + "\n");
//	    } // for
//	    out1.close();
	    numberOfVariables_ = this.usedRules.size() + 2;  // numberOfVariables_ + 2 , the two last variables are used to set the interval threshold_lowerbound and threshold_upperbound where messages are classified as unknown (not spam not ham). Below threshold_lowerbound are classified as HAM and above threshold_upperbound are classified as SPAM
	    //numberOfVariables_  = numberOfVariables.intValue();
	    numberOfObjectives_ = 3;
	    numberOfConstraints_ = 0;
	    problemName_ = "SpamProblem3DIntervalThreshold";
	    lowerLimit_ = new double[this.numberOfVariables_];
	    upperLimit_ = new double[this.numberOfVariables_];
	    for (int i = 0; i < /*this.*/numberOfVariables_; i++) { 
	        lowerLimit_[i] = -5;//this.lower;
	        upperLimit_[i] = 5;//this.high;
	    } // for
	    lowerLimit_[numberOfVariables_ -2] = 0; upperLimit_[numberOfVariables_ -2] = 1; // interval of unknow messages classified is [o-1] - This variable generates the interval start limit
	    lowerLimit_[numberOfVariables_ -1] = 0; upperLimit_[numberOfVariables_ -1] = 1; // interval of unknow messages classified is [o-1] - This variable generates the interval end limit
	    // The unknow messages classification interval start at x1 = variable[numberOfVariables_ -1]
	    // The unknow messages classification interval ends at x2 = x1 + variable[numberOfVariables_] (1-x1)

	    if (solutionType.compareTo("BinaryReal") == 0) {
	        this.solutionType_ = new BinaryRealSolutionType(this);
	    } else if (solutionType.compareTo("Real") == 0) {
	        this.solutionType_ = new RealSolutionType(this);
	    } else if (solutionType.compareTo("ArrayReal") == 0) {
	        this.solutionType_ = new ArrayRealSolutionType(this);
	    } else {
	        System.out.println("Error: solution type " + solutionType + " invalid");
	        System.exit(-1);
	    }		
	} //Spam

	/** 
	 * Evaluates a solution 
	 * @param solution The solution to evaluate
	 * @throws JMException 
	 */        
	public void evaluate(Solution solution) throws JMException {
	    Variable[] variable = solution.getDecisionVariables();
	    HashMap<String, Double> rulesWeight = new HashMap<String, Double>();
	    HashMap<Integer, String[]> HamMsgs = new HashMap<Integer, String[]>();
	    HashMap<Integer, String[]> SpamMsgs = new HashMap<Integer, String[]>();
	    int falsePositive = 0;
	    int falseNegative = 0;
	    int unknown = 0;
	    double threshold_lowerbound = variable[numberOfVariables_ - 2].getValue();
	    double threshold_upperbound = threshold_lowerbound + ( variable[numberOfVariables_ - 1].getValue() * (1 - threshold_lowerbound) );
		// The last 2 variables are generated for the threshold_lowerbound and threshold_upperbound. Variable vector index starts at 0
		// The unknow messages classification interval starts at x1 = variable[numberOfVariables_ -1]
	    // The unknow messages classification interval ends at x2 = x1 + variable[numberOfVariables_] (1-x1)
	    double sum = 0.0D;
	    double[] f = new double[this.numberOfObjectives_];
	    rulesWeight.clear();
		//int number_of_rules_with_relevant_weight = 0; // Rules with weight different from zero or absolute value bigger than the delta tolerance for parsimony consideration purposes
	    for (Entry<String, Integer> key : usedRules.entrySet()) {
	        rulesWeight.put(key.getKey(), variable[usedRules.get(key.getKey())].getValue());

/*	        if (Math.abs(variable[usedRules.get(key.getKey())].getValue()) > this.parsimony_rule_weight_delta_around_zero_for_rule_to_be_considered_irrelevant) 
	        {	        	
	        	number_of_rules_with_relevant_weight++;
	        } 
	        else {
	        	variable[usedRules.get(key.getKey())].setValue(0.0); // Rules with weight lower than delta tolerance for parsimony purpose are set weight=0
	        	rulesWeight.put(key.getKey(), variable[usedRules.get(key.getKey())].getValue());
	        }
*/
	    }
	    for (Entry<Integer, String[]> key : rulesHam.entrySet()) {
	        HamMsgs.put(key.getKey(), key.getValue());
	    }
	    for (Entry<Integer, String[]> key : rulesSpam.entrySet()) {
	        SpamMsgs.put(key.getKey(), key.getValue());
	    }
	    for (Entry<Integer, String[]> key :HamMsgs.entrySet()) {
	        sum = 0.0D;
	        for (String string : (String[])key.getValue()) {
	            sum += ((Double)rulesWeight.get(string)).doubleValue();
	        }
	        //if (sum < th) {trueNegative++;} else {falsePositive++;}        
	        if (sum < threshold_lowerbound) {trueNegative++;}
	        if (sum > threshold_upperbound) {falsePositive++;}
	        if ((sum >= threshold_lowerbound) && (sum <= threshold_upperbound)) {unknown++;}
	    }
	    for (Entry<Integer, String[]> key :SpamMsgs.entrySet()) {
	        sum = 0.0D;
	        for (String string : (String[])key.getValue()) {
	            sum += ((Double)rulesWeight.get(string)).doubleValue();
	        }
	        //if (sum >= th) {truePositive++;} else {falseNegative++;}
	        if (sum < threshold_lowerbound) {falseNegative++;}
	        if (sum > threshold_upperbound) {truePositive++;}
	        if ((sum >= threshold_lowerbound) && (sum <= threshold_upperbound)) {unknown++;}	        
	    }
	    this.count += 1;
	    
	    f[0] = (double) falseNegative / (double) this.totalNumberOfSpam; //fnr = false negatives/ total number of positives(SPAM)
	    f[1] = (double) falsePositive / (double) this.totalNumberOfHam; //fpr = false positives/total number of negatives(HAM)
	    f[2] = (double) unknown / ((double) this.totalNumberOfSpam + (double) this.totalNumberOfHam) ; // unknown messages rate
	    
	    //f[2] = number_of_rules_with_relevant_weight;
	    double aux = f[1] + f[0];
	    this.soma = aux;

	    solution.setObjective(0, f[0]);
	    solution.setObjective(1, f[1]);
	    solution.setObjective(2, f[2]);
	} // evaluate
} // Spam
