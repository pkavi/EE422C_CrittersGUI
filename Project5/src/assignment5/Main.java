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

import assignment5.Critter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
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
    public static int gridRows=5;
    public static int gridCols=10;
    public static double gridLineWidth=10;
    public static int screenHeight=800;
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
    	

        

    }
    
    /**
     * Runs the whole program
     */



    protected static void fxDisplayGrid(Critter grid[][]){
    	mainGraphicsContext.setFill(Color.WHITE);
    	mainGraphicsContext.fillRect(0,0,screenHeight,screenWidth);
    	mainGraphicsContext.setFill(Color.BLACK);
    	double widthBetweenLines=(screenWidth-gridLineWidth*1.0)/(gridCols);
    	double heightBetweenLines=(screenHeight-gridLineWidth*1.0)/(gridRows);
    	for(int i=0;i<gridCols+1;i++){
    		mainGraphicsContext.fillRect(i*widthBetweenLines,0,gridLineWidth,screenHeight);
    	}
    	for(int i=0;i<gridRows+1;i++){
    		mainGraphicsContext.fillRect(0,i*heightBetweenLines,screenWidth,gridLineWidth);
    	}
    	
    	System.out.println("dsad");
    	if(grid==null){
    		return;
    	}
    	for(int i=0;i<grid.length;i++){
    		for(int j=0;j<grid[0].length;j++){
    			
    		}
    	}
    	
    	
    	
    	
    	
    	
    }
    @Override
    public void start(Stage primaryStage){
    	
    	Stage s=new Stage();
    	s.setTitle("dasd");
    	
    	
    	
    	
    	GridPane topLevelGridPane= new  GridPane();
    	topLevelGridPane.setHgap(10);
        topLevelGridPane.setVgap(10);
        topLevelGridPane.setPadding(new Insets(0, 10, 0, 10));
    	
    	
 
        
        
        
        
    	
    	
    	
    	addMakeCritterGridPane(topLevelGridPane);
    	addSingleTimeStepGridPane(topLevelGridPane);
    	addStatisticsGridPane(topLevelGridPane);
    	addSeedGridPane(topLevelGridPane);
    	
    	
    	
    	topLevelGridPane.setGridLinesVisible(true);
    	
    	
    s.setScene(new Scene(topLevelGridPane));
    	s.show();
    	
    	
    	
    	
           
           
           
    	primaryStage.setTitle("Grid");
    	Group root=new Group();
    	mainCanvas=new Canvas(screenWidth,screenHeight);
    	mainGraphicsContext=mainCanvas.getGraphicsContext2D();
    
    	
    	
    fxDisplayGrid(null);
    root.getChildren().add(mainCanvas);
    primaryStage.setScene(new Scene(root));
    primaryStage.show();
    

    	
    }
    private static void addSeedGridPane(GridPane mainGridPane){
    	GridPane seedGridPane=new GridPane();
        seedGridPane.setHgap(10);
        seedGridPane.setVgap(10);
        seedGridPane.setPadding(new Insets(0, 10, 0, 10));
       
        Label seedLabel=new Label();
    	seedLabel.setText("Set Seed Tool");
    	seedGridPane.add(seedLabel, 0,0);
    	
    	Label seedNumberTextLabel=new Label();
    	seedNumberTextLabel.setText("Seed Number");
    	seedGridPane.add(seedNumberTextLabel,0,1);
    	
    	TextField seedNumberTextField=new TextField();
    	seedGridPane.add(seedNumberTextField,1,1);
    	
    	
    	Button seedButton = new Button();
        seedButton.setText("Set seed");
        seedButton.setOnAction(e->seedEventHandler(seedNumberTextField.getText()));
        seedGridPane.add(seedButton, 0, 2);
        
        mainGridPane.add(seedGridPane, 0,3);
    	
    	
    }
    private static void seedEventHandler(String text){
    	int seed;
    	try{
			seed=Integer.parseInt(text);
		}catch(NumberFormatException ex){
			Alert alert = new Alert(AlertType.INFORMATION);
			
			alert.setTitle("Error");
			alert.setHeaderText(null);
			if(text.trim().length()>0){
			alert.setContentText("Number not entered: "+text.trim());
			}
			else{
				alert.setContentText("No number entered");
			}
			alert.showAndWait();
			return;
		}
		Critter.setSeed(seed);
    	
    	
    }
    private static void addSingleTimeStepGridPane(GridPane mainGridPane){
    	 
        GridPane singleFrameTimeStepGridPane=new GridPane();
        singleFrameTimeStepGridPane.setHgap(10);
        singleFrameTimeStepGridPane.setVgap(10);
        singleFrameTimeStepGridPane.setPadding(new Insets(0, 10, 0, 10));
       
        Label singleFrameTimeStepLabel=new Label();
    	singleFrameTimeStepLabel.setText("Single Frame Time Step Tool");
    	singleFrameTimeStepGridPane.add(singleFrameTimeStepLabel, 0,0);
    	
    	Label singleFrameTimeStepNumberLabel=new Label();
    	singleFrameTimeStepNumberLabel.setText("Number of Steps");
    	singleFrameTimeStepGridPane.add(singleFrameTimeStepNumberLabel,0,1);
    	
    	TextField singleFrameTimeStepNumberTextField=new TextField();
    	singleFrameTimeStepGridPane.add(singleFrameTimeStepNumberTextField,1,1);
    	
    	
    	Button singleFrameTimeStepButton = new Button();
        singleFrameTimeStepButton.setText("Step");
        singleFrameTimeStepButton.setOnAction(e->singleFrameTimeStepEventHandler(singleFrameTimeStepNumberTextField.getText()));
        singleFrameTimeStepGridPane.add(singleFrameTimeStepButton, 0, 2);
        
        
        mainGridPane.add(singleFrameTimeStepGridPane, 0,1 );
    	
    	
    	
    	
    	
    }

    private static void singleFrameTimeStepEventHandler(String text){
    	int step;
    	try{
			step=Integer.parseInt(text);
		}catch(NumberFormatException ex){
			
			Alert alert = new Alert(AlertType.INFORMATION);
			
			alert.setTitle("Error");
			alert.setHeaderText(null);
			if(text.trim().length()>0){
			alert.setContentText("Number not entered: "+text.trim());
			}
			else{
				alert.setContentText("No number entered");
			}
			alert.showAndWait();
			return;
		}
		for(int i=0;i<step;i++){
			Critter.worldTimeStep();
		}
    	
    	
    }
    private static void addStatisticsGridPane(GridPane mainGridPane){
    	GridPane statisticsGridPane=new GridPane();
        statisticsGridPane.setHgap(10);
        statisticsGridPane.setVgap(10);
        statisticsGridPane.setPadding(new Insets(0, 10, 0, 10));
       
        Label statisticsLabel=new Label();
    	statisticsLabel.setText("Statistics Tool");
    	statisticsGridPane.add(statisticsLabel, 0,0);
    	
    	Label statisticsTypeLabel=new Label();
    	statisticsTypeLabel.setText("Type");
    	statisticsGridPane.add(statisticsTypeLabel,0,1);
    	
    	ComboBox<String> statisticsComboBox=new ComboBox<String>();
    	statisticsGridPane.add(statisticsComboBox,1,1);
    	statisticsComboBox.getItems().addAll(
	            "Highest",
	            "High",
	            "Normal",
	            "Low",
	            "Lowest" 
	        ); 
    	
    statisticsComboBox.setOnAction(e->statisticsEventHandler(statisticsComboBox.getValue()));
        
        mainGridPane.add(statisticsGridPane, 0,2 );
    	
    	
    	
    }
    private static void statisticsEventHandler(String text){
    	
    	
    }
private static void addMakeCritterGridPane(GridPane mainGridPane){
	GridPane makeCritterGridPane=new GridPane();
    makeCritterGridPane.setHgap(10);
    makeCritterGridPane.setVgap(10);
    makeCritterGridPane.setPadding(new Insets(0, 10, 0, 10));
	
	Label makeCritterLabel=new Label();
	makeCritterLabel.setText("Make Critter Tool");
	makeCritterGridPane.add(makeCritterLabel, 0,0);
	
	Label critterTypeLabel=new Label();
	critterTypeLabel.setText("Type");
	makeCritterGridPane.add(critterTypeLabel, 0,1);
	
	ComboBox<String> critterTypeComboBox=new ComboBox<String>();
	 critterTypeComboBox.getItems().addAll(
	            "Highest",
	            "High",
	            "Normal",
	            "Low",
	            "Lowest" 
	        ); 
	makeCritterGridPane.add(critterTypeComboBox,1,1);
	
	
	
	Label critterNumberLabel=new Label();
	critterNumberLabel.setText("Number");
	makeCritterGridPane.add(critterNumberLabel,0,2);
	
	TextField critterNumberTextField=new TextField();
	makeCritterGridPane.add(critterNumberTextField,1,2);
	
	
	Button makeCritterButton = new Button();
    makeCritterButton.setText("Add Critters");
    makeCritterButton.setOnAction(e->makeCritterHandler(critterTypeComboBox.getValue()));
    makeCritterGridPane.add(makeCritterButton, 0,3 );
    
    
    mainGridPane.add(makeCritterGridPane, 0, 0);
	
	
}
private static void makeCritterHandler(String text){
	
	
	
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
