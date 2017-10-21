package jmetal.problems;

import java.util.Scanner;
import java.util.regex.Pattern;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s="xval/0/_ham_/01770.0e2c720e2d4abac3f8734b9914186f21	BAYES_00";
		Pattern regex = Pattern.compile("(\t.*)+");
	    Scanner input = new Scanner(s);
	    while (input.hasNext())
	    {
        String line = input.findInLine(regex);
        System.out.println(line);
        String[] aux = line.split("\t");        //split string by blank space
        //aux[1] = aux[1].trim();                //aux[1]=score
        //aux[2] = aux[2].trim();                //aux[2]=rule_name
        //System.out.println(aux[1]);
        //System.out.println(aux[2]);
	    }		
	}

}
