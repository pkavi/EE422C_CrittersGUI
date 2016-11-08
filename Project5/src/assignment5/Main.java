/* CRITTERS Main.java
 * EE422C Project 4 submission by
 * Pranav Kavikondala
 * pk6994
 * 16470
 * Slip days used: 0
 * Fall 2016
 */
package assignment5; // cannot be in default package
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Method;


/*
 * Usage: java <pkgname>.Main <input file> test
 * input file is optional.  If input file is specified, the word 'test' is optional.
 * May not use 'test' argument without specifying input file.
 */
public class Main extends Application {

    static Scanner kb;	// scanner connected to keyboard input, or input file
    private static String inputFile;	// input file, used instead of keyboard input if specified
    static ByteArrayOutputStream testOutputString;	// if test specified, holds all console output
    private static String myPackage;	// package of Critter file.  Critter cannot be in default pkg.
    public static boolean DEBUG = false; // Use it or not, as you wish!
    static PrintStream old = System.out;	// if you want to restore output to console
    
    //Variables created for gui
    public static Canvas mainCanvas=null;//Main canvas which the world is displayed
    public static GraphicsContext mainGraphicsContext=null;
    public static int gridRows=10;
    public static int gridCols=10;
    public static double gridLineWidth=5;
    public static int screenHeight=600;
    public static int screenWidth=800;
    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

    /**
     * Main method.
     * @param args args can be empty.  If not empty, provide two parameters -- the first is a file name, 
     * and the second is test (for test output, where all output to be directed to a String), or nothing.
     */
    public static void main(String[] args) { 
    	launch(args);
    	
//        if (args.length != 0) {
//            try {
//                inputFile = args[0];
//                kb = new Scanner(new File(inputFile));			
//            } catch (FileNotFoundException e) {
//                System.out.println("USAGE: java Main OR java Main <input file> <test output>");
//                e.printStackTrace();
//            } catch (NullPointerException e) {
//                System.out.println("USAGE: java Main OR java Main <input file>  <test output>");
//            }
//            if (args.length >= 2) {
//                if (args[1].equals("test")) { // if the word "test" is the second argument to java
//                    // Create a stream to hold the output
//                    testOutputString = new ByteArrayOutputStream();
//                    PrintStream ps = new PrintStream(testOutputString);
//                    // Save the old System.out.
//                    old = System.out;
//                    // Tell Java to use the special stream; all console output will be redirected here from now
//                    System.setOut(ps);
//                }
//            }
//        } else { // if no arguments to main
//            kb = new Scanner(System.in); // use keyboard and console
//        }
//
//        /* Do not alter the code above for your submission. */
//        /* Write your code below. */
//        
//        runProgram();
//        /* Write your code above */
//        System.out.flush();
        

    }
    
    /**
     * Runs the whole program
     */
    protected static void runProgram(){
    	boolean quit=false;
    	String input;
    	String[] inputArgs;
    	String firstArg;
    	while(!quit){
    		//Output prompt and see if command valid
    		System.out.print("critters>");
    		input=kb.nextLine();
    		inputArgs=input.trim().split("\\s+");
    		firstArg=inputArgs[0];
    		if(firstArg.equals("quit")){
    			quit=parseQuit(inputArgs,input);
    		}
    		else if(firstArg.equals("show")){
    			parseShow(inputArgs, input);
    		}
    		else if(firstArg.equals("step")){
    			parseStep(inputArgs,input);
    		}
    		else if(firstArg.equals("seed")){
    			parseSeed(inputArgs,input);
    		}
    		else if(firstArg.equals("make")){
    			parseMake(inputArgs,input);
    		}else if(firstArg.equals("stats")){
    			parseStats(inputArgs,input);
    		}
    		else{
    			System.out.println("Invalid command");
    		}
    	}
    }
  
    /**
     * Parses any command that starts with show
     * @param inputArgs String array of the input tokens for line
     * @param input String of whole line input
     */
    protected static void parseShow(String[] inputArgs,String input){
    	if(inputArgs.length==1 && inputArgs[0].equals("show")){
    	Critter.displayWorld();
    	}
    	else{
    		System.out.println("error processing: "+input.trim());
    	}
    }
    
    /**
     * Parse any command that begins with quit
     * @param inputArgs String array of the input tokens for line
     * @param input  String of whole line input
     * @return boolean True if user wants to quit
     */
    protected static boolean parseQuit(String[] inputArgs,String input){
    	if(inputArgs.length==1 && inputArgs[0].equals("quit")){
    		return true;
    	}
    	else{
    		System.out.println("error processing: "+input.trim());
    		return false;
    	}
    }
    
    /**
     * Parses any command that begins with step
     * @param inputArgs String array of the input tokens for line
     * @param input String of whole line input
     */
    protected static void parseStep(String[] inputArgs,String input){
    	int step=0;
    	if(inputArgs.length==1 && inputArgs[0].equals("step")){
    		Critter.worldTimeStep();
    	}
    	else if(inputArgs.length==2 && inputArgs[0].equals("step")){
    		try{
    			step=Integer.parseInt(inputArgs[1]);
    		}catch(NumberFormatException ex){
    			System.out.println("error processing: "+input.trim());
    			return;
    		}
    		for(int i=0;i<step;i++){
    			Critter.worldTimeStep();
    		}
    	}
    	else{
    		System.out.println("error processing: "+input.trim());
    		
    	}
    }
    
    /**
     * Parses any command that begins with seed
     * @param inputArgs String array of the input tokens for line
     * @param input String of whole line input
     */
    protected static void parseSeed(String[] inputArgs,String input){
    	int seed=0;
    	 if(inputArgs.length==2 && inputArgs[0].equals("seed")){
    		try{
    			seed=Integer.parseInt(inputArgs[1]);
    		}catch(NumberFormatException ex){
    			System.out.println("error processing: "+input.trim());
    			return;
    		}
    		Critter.setSeed(seed);
    	}
    	else{
    		System.out.println("error processing: "+input.trim());
    		
    	}
    }
    
    /**
     * Parses any command that begins with make
     * @param inputArgs String array of the input tokens for line
     * @param input String of whole line input
     */
    protected static void parseMake(String[] inputArgs,String input){
    	String name;
    	int numCreated=0;
    	if(inputArgs.length==2  && inputArgs[0].equals("make")){
    		name=inputArgs[1];
    		try{
    			Critter.makeCritter(name);
    		}catch(InvalidCritterException ex){
    			System.out.println("error processing: "+input.trim());
    			return;
    		}
    	 } 
    	 else if(inputArgs.length==3  && inputArgs[0].equals("make")){
 			name=inputArgs[1];
 			try{
 				numCreated=Integer.parseInt(inputArgs[2]);
 			}catch(NumberFormatException ex){
 				System.out.println("error processing: "+input.trim());
 				return;
 			}
 			try{
 				for(int i=0;i<numCreated;i++){
 					Critter.makeCritter(name);
 				}
 			}catch(InvalidCritterException ex){
 				System.out.println("error processing: "+input.trim());
 				return;
 			}
    	} 
    	else{
    		System.out.println("error processing: "+input.trim());
    		
    	}
    }
    protected static void fxDisplayGrid(){
    	mainGraphicsContext.setFill(Color.WHITE);
    	mainGraphicsContext.fillRect(0,0,screenHeight,screenWidth);
    	mainGraphicsContext.setFill(Color.BLACK);
    	double widthBetweenLines=(800-gridLineWidth)/(gridCols);
    	double heightBetweenLines=(600-gridLineWidth)/(gridRows);
    	for(int i=0;i<gridCols+1;i++){
    		mainGraphicsContext.fillRect(i*widthBetweenLines,0,gridLineWidth,screenHeight);
    	}
    	for(int i=0;i<gridRows+1;i++){
    		mainGraphicsContext.fillRect(0,i*heightBetweenLines,screenWidth,gridLineWidth);
    	}
    //	mainGraphicsContext.
    	
    	
    	
    	
    	
    	
    }
    @Override
    public void start(Stage primaryStage){
    	primaryStage.setTitle("Grid");
    	Group root=new Group();
    	mainCanvas=new Canvas(screenWidth,screenHeight);
    	mainGraphicsContext=mainCanvas.getGraphicsContext2D();
    	
    fxDisplayGrid();
    root.getChildren().add(mainCanvas);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
    
    /*	primaryStage.setScene(scene);
    	primaryStage.show();
    	Group circles = new Group();
    	for (int i = 0; i < 30; i++) {
    	   Circle circle = new Circle(150, Color.web("white", 0.05));
    	   circle.setStrokeType(StrokeType.OUTSIDE);
    	   circle.setStroke(Color.web("white", 0.16));
    	   circle.setStrokeWidth(4);
    	   circles.getChildren().add(circle);
    	}
    	root.getChildren().add(circles);
    	*/
    	
    }
    /**
     * Parses any command that begins with stats
     * @param inputArgs String array of the input tokens for line
     * @param input String of whole line input
     */
    protected static void parseStats(String[] inputArgs,String input){
    	int seed=0;
    	 if(inputArgs.length==2 && inputArgs[0].equals("stats")){
    		 List<Critter> res;
    		 try{
    		 res=Critter.getInstances(inputArgs[1]);
    		 runStatsMethod(inputArgs[1],res);
    		 }catch(InvalidCritterException ex){
    			 //To finish
    			 if(DEBUG){
    				 System.out.println("Problem in parseStatsMethod");
    			 }
    			 System.out.println("error processing: "+input.trim());
    			 
    			 return;
    		 }
    		 
    	}
    	else{
    		System.out.println("error processing: "+input.trim());
    		
    	}
    }
    //See http://stackoverflow.com/questions/9042740/call-static-method-given-a-class-object-in-java

    /**
     * Runs the stat method with res from getInstances with critter_class_name
     * @param critter_class_name Name of critter class to run Stats on
     * @param res List of critters of critter_class_name
     * @throws InvalidCritterException If some error 
     */
    protected static void runStatsMethod(String critter_class_name,List<Critter> res) throws InvalidCritterException{
    	try{
    	Class<?> inClass=Class.forName(myPackage+"."+critter_class_name);
    	Method inMethod=inClass.getMethod("runStats",List.class);
    			inMethod.invoke(null,res);
    	}catch(Exception ex){
    		if(DEBUG){
    		ex.printStackTrace(System.out);
    		System.out.println("Problem in Stats method");
    		}
    		throw new InvalidCritterException(critter_class_name);
    		
    	}
    	
    }
   
    
    
}
