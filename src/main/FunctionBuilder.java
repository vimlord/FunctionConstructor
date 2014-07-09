/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

/**
 *
 * @author Christopher
 */
public class FunctionBuilder {
    static final ArrayList<Function> functions = new ArrayList<>();
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        loadTrigFunctions();
        
        int observed = 6;
        int desiredCycles;
        
        Scanner in = new Scanner(System.in);
        System.out.print("Input a number of cycles greater than zero: ");
        
           desiredCycles = in.nextInt();
        
        while(desiredCycles <= 0){
            System.err.println("ERROR: Invalid input!");
            System.out.print("Input a number of cycles greater than zero: ");
            
            desiredCycles = in.nextInt();
        }
        
        
        
        System.out.println("Initial function: " + functions.get(observed));
        System.out.println();
        System.out.println("Given equivalent functions: ");
        for(Function equal : functions)
            for(Function e : equal.getEquivalents())
                System.out.println(e + " = " + equal);
        System.out.println();
        
        System.out.println("New functions:");
        
        int offset = 0;
        int initialSize = functions.get(observed).getEquivalents().size();
        long startTime = System.currentTimeMillis();
        for(int i = 0; i < desiredCycles; i++){
            Function newEquivalent = (functions.get(observed).buildRandomEquivalent());
            functions.get(observed).addEquivalent(newEquivalent);
            try {
                String equation = (functions.get(observed).getEquivalents().get(i - offset + initialSize)).toString();
                if(i+1 < 10){
                    equation = "  " + equation;
                } else if(i+1 < 100){
                    equation = " " + equation;
                }
                //The division by 10.0 is based on my computer's run speed. The reccomended value may fluctuate.
                System.out.println("Created on cycle " + (i+1) + " after " + (System.currentTimeMillis() - startTime)/1000.0 + " seconds of runtime: " + equation);
                //System.out.println((System.currentTimeMillis() - startTime) + "," + (i - offset + initialSize));
                //functions.add(newEquivalent);
            } catch(Exception e){
                offset++;
            }
        }
        
        
        System.out.println();
        
        int newFunctions = (functions.get(observed).getEquivalents().size() - initialSize);
        
        if(newFunctions > 0)
            System.out.println("The program created " + newFunctions + " new functions using the given data.");
        else
            System.out.println("The program didn't create any new functions using the given data.");
        
        
    }
    
    /**
     * Adds a series of Trigonometric functions to the functions list.
     */
    public static void loadTrigFunctions(){
        functions.addAll(getTrigonometricFunctions());
    }

    private static ArrayList<Function> getTrigonometricFunctions() {
        ArrayList<Function> list = new ArrayList<>();
        //Creates the Trigonometric functions
        Value X = new Value("x");
        Function sin = new SingleFunction("sin", X);
        Function cos = new SingleFunction("cos", X);
        Function tan = new SingleFunction("tan", X);
        Function csc = new SingleFunction("csc", X);
        Function sec = new SingleFunction("sec", X);
        Function cot = new SingleFunction("cot", X);
        Function ONE = new Value(1);
        
        //sin(2@)
        ArrayList<Function> sin2xfncs = new ArrayList<>(); sin2xfncs.add(new Value(2)); sin2xfncs.add(X);
        Function sin2x = new SingleFunction("sin", new Product(sin2xfncs));
        
        ArrayList<Function> sin2xequiv = new ArrayList<>();
        sin2xequiv.add(new Value(2));
        sin2xequiv.add(sin);
        sin2xequiv.add(cos);
        sin2x.addEquivalent(new Product(sin2xequiv));
        
        //The inverse relations
        sin.addEquivalent(new Fraction(new Value(1),csc));
        cos.addEquivalent(new Fraction(new Value(1),sec));
        tan.addEquivalent(new Fraction(new Value(1),cot));
        
        csc.addEquivalent(new Fraction(new Value(1),sin));
        sec.addEquivalent(new Fraction(new Value(1),cos));
        cot.addEquivalent(new Fraction(new Value(1),tan));
        
        //   sin/cos = tan      cos/sin = cot
        tan.addEquivalent(new Fraction(sin,cos));
        cot.addEquivalent(new Fraction(cos,sin));
        
        //sinx^2 + cosx^2 = 1
        Exponent sineSquare = new Exponent(sin, new Value(2));
        Exponent cosineSquare = new Exponent(cos, new Value(2));
        ArrayList<Function> sincos2 = new ArrayList<>();
        sincos2.add(sineSquare); sincos2.add(cosineSquare);
        ONE.addEquivalent(new CombinedFunction(sincos2));
        
        
        list.add(sin);
        list.add(cos);
        list.add(tan);
        list.add(csc);
        list.add(sec);
        list.add(cot);
        list.add(ONE);
        list.add(sin2x);
        
        return list;
    }

    public static ArrayList<Function> getFunctionList() {
        return functions;
    }
    
    
}
