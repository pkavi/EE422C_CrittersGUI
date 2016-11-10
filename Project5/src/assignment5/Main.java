/* CRITTERS Main.java
 * EE422C Project 5 submission by
 * Pranav Kavikondala
 * pk6994
 * 16470
 * Daniel John
 * dcj597
 * 16480
 * Slip days used: 0
 * Fall 2016
 */
package assignment5; // cannot be in default package
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import assignment5.InvalidCritterException;
import assignment5.Critter;
import assignment5.Critter.CritterShape;
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
    
    //Shape final constants to draw normalized to 1 by 1 square
    private final static double[][] triangle={{0.50,0.06},{0.06,0.78},{0.94,0.78}};
    private final static double[][] square={{0.12,0.12},{0.12,0.88},{0.88,0.88},{0.88,0.12}};
    private final static double[][] diamond={{0.50,0.06},{0.25,0.50},{0.50,0.94},{0.75,0.50}};
    private final static double[][] star={{0.20,0.95},{0.50,0.75},{0.80,0.95},{0.68,0.60},{0.95,0.40},{0.62,0.38},{0.50,0.05},{0.38,0.38},{0.05,0.40},{0.32,0.64}};
    
    //Oval/circle draw scaling to fit in center of circle
    private final static double scalingCircle=0.5;
   
    //Max size of board when automatically sized
    private final static double maxWidth=1000;
    private final static double maxHeight=1000;
    
    private final static double minWidth=200;
    private final static double minHeight=200;
    
    //Canvas adjustment; needed to adjust artifacts in drawing
    private final static double widthAdjust=16;
    private final static double heightAdjust=39;
    
    //Stats label
    public static Label statisticsDisplayLabel;
    public static ComboBox<String> statisticsComboBox;
    
    //Variables created for drawing the grid correctly depending on stage dimensions
    public static Canvas mainCanvas=null;//Main canvas which the world is displayed
    public static GraphicsContext mainGraphicsContext=null;
    public static int gridRows=10;//Reset to correct attribute in code
    public static int gridCols=10;
    public static double gridLineWidth=10;
    public static double screenHeight=800;
    public static double screenWidth=800;
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
    
    protected static void fxDisplayGrid(Critter[][] grid){
    	mainGraphicsContext.setFill(Color.MAROON);
    	mainGraphicsContext.fillRect(0,0,1920,1080);
    	mainGraphicsContext.setFill(Color.BLACK);
    	gridLineWidth=Math.min(screenWidth/gridCols*1.0, screenHeight/gridRows*1.0)/7;
    	double widthBetweenLines=(screenWidth-gridLineWidth*1.0)/(gridCols);
    	double heightBetweenLines=(screenHeight-gridLineWidth*1.0)/(gridRows);
    	for(int i=0;i<gridCols+1;i++){
    		mainGraphicsContext.fillRect(i*widthBetweenLines,0,gridLineWidth,screenHeight);
    	}
    	for(int i=0;i<gridRows+1;i++){
    		mainGraphicsContext.fillRect(0,i*heightBetweenLines,screenWidth,gridLineWidth);
    	}
    	
    	if(grid==null){
    		return;
    	}
    	drawCritters(grid,widthBetweenLines,heightBetweenLines);
    	statisticsEventHandler(statisticsComboBox.getValue());

    }

    protected static void drawCritters(Critter[][] grid,double widthBetweenLines, double heightBetweenLines){
    	for(int i=0;i<grid.length;i++){
    		for(int j=0;j<grid[0].length;j++){
    			if(grid[i][j]==null){
    				continue;
    			}
    			CritterShape val=grid[i][j].viewShape();
    			mainGraphicsContext.setFill(grid[i][j].viewFillColor());
    			mainGraphicsContext.setStroke(grid[i][j].viewOutlineColor());
    			mainGraphicsContext.setLineWidth(gridLineWidth/2);
    			if(val==CritterShape.CIRCLE){
    				mainGraphicsContext.strokeOval(j*widthBetweenLines+gridLineWidth+scalingCircle*(widthBetweenLines-gridLineWidth)/2.0,i*heightBetweenLines+gridLineWidth+scalingCircle*(heightBetweenLines-gridLineWidth)/2.0,(widthBetweenLines-gridLineWidth)*scalingCircle,(heightBetweenLines-gridLineWidth)*scalingCircle);
    				mainGraphicsContext.fillOval(j*widthBetweenLines+gridLineWidth+scalingCircle*(widthBetweenLines-gridLineWidth)/2.0,i*heightBetweenLines+gridLineWidth+scalingCircle*(heightBetweenLines-gridLineWidth)/2.0,(widthBetweenLines-gridLineWidth)*scalingCircle,(heightBetweenLines-gridLineWidth)*scalingCircle);
    			}
    			else if(val==CritterShape.DIAMOND){
    				drawPolygon(diamond,i,j,widthBetweenLines,heightBetweenLines);
    			}
    			else if(val==CritterShape.STAR){
    				drawPolygon(star,i,j,widthBetweenLines,heightBetweenLines);
    			}
    			else if(val==CritterShape.TRIANGLE){
    				drawPolygon(triangle,i,j,widthBetweenLines,heightBetweenLines);
    			}
    			else if(val==CritterShape.SQUARE){
    				drawPolygon(square,i,j,widthBetweenLines,heightBetweenLines);
    			}
    		}
    	}
    }

    private static void drawPolygon(double[][] shapeCoordinates,int row, int col, double widthBetweenLines,double heightBetweenLines){
    	double[] xCoords=new double[shapeCoordinates.length];
    	double[] yCoords=new double[shapeCoordinates.length];
    	double horizontalSize=widthBetweenLines-gridLineWidth;
    	double verticalSize=heightBetweenLines-gridLineWidth;
    	double horizontalOffset=widthBetweenLines*col+gridLineWidth;
    	double verticalOffset=heightBetweenLines*row+gridLineWidth;
    	for(int i=0;i<shapeCoordinates.length;i++){
    		xCoords[i]=shapeCoordinates[i][0]*horizontalSize+horizontalOffset;
    		yCoords[i]=shapeCoordinates[i][1]*verticalSize+verticalOffset;
    	}
    	mainGraphicsContext.strokePolygon(xCoords,yCoords,shapeCoordinates.length);
    	mainGraphicsContext.fillPolygon(xCoords,yCoords,shapeCoordinates.length);
    }
    
    @Override
    public void start(Stage primaryStage){
    	
    	Stage secondaryStage=new Stage();
    	secondaryStage.setTitle("Critter Controls");
    	
    	GridPane topLevelGridPane= new  GridPane();
    	topLevelGridPane.setHgap(10);
        topLevelGridPane.setVgap(10);
        topLevelGridPane.setPadding(new Insets(0, 10, 0, 10));
        
    	addMakeCritterGridPane(topLevelGridPane);
    	addSingleFrameTimeStepGridPane(topLevelGridPane);
    	addStatisticsGridPane(topLevelGridPane);
    	addSeedGridPane(topLevelGridPane);
    	addQuitButton(topLevelGridPane);
    	
    
    	
     
        secondaryStage.setScene(new Scene(topLevelGridPane));
        secondaryStage.setOnHiding(e->System.exit(0));
    	
        secondaryStage.show();
    	
    	
    	primaryStage.setTitle("Critter Display");
    	Group root=new Group();
    	mainCanvas=new Canvas(1920,1080);
    	mainGraphicsContext=mainCanvas.getGraphicsContext2D();
    	root.getChildren().add(mainCanvas);
    	primaryStage.setScene(new Scene(root));
    	
    	autoResizeEventHandler(primaryStage);
    
    	resizedWindowEventHandler(primaryStage);
    	fxDisplayGrid(null);
    	
    	primaryStage.show();
    	
    	primaryStage.widthProperty().addListener((obs,oldVal,newVal)->resizedWindowEventHandler(primaryStage));
    	primaryStage.heightProperty().addListener((obs,oldVal,newVal)->resizedWindowEventHandler(primaryStage));
    	
    	primaryStage.setOnHiding(e->System.exit(0));
    }

    private static void autoResizeEventHandler(Stage primaryStage){
    	gridRows=Params.world_height;
    	gridCols=Params.world_width;
    	screenWidth=Math.min(gridCols*15,maxWidth);
    	screenHeight=Math.min(gridRows*15, maxHeight);
    	screenWidth=Math.max(screenWidth, minWidth);
    	screenHeight=Math.max(screenHeight, minHeight);
    	primaryStage.setWidth(screenWidth+widthAdjust);
    	primaryStage.setHeight(screenHeight+heightAdjust);
    }
    
    private static void resizedWindowEventHandler(Stage primaryStage){
    	screenWidth=primaryStage.getWidth()-widthAdjust;
    	screenHeight=primaryStage.getHeight()-heightAdjust;
    	Critter.displayWorld();
    	//System.out.println("Screen Dim: " + primaryStage.getWidth()+"x"+primaryStage.getHeight());
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
        
        seedGridPane.setStyle("-fx-border: 100px solid; -fx-border-color: red;-fx-background-color: #C0C0C0;");
        
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
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Seed Set");
		alert.setHeaderText(null);
			alert.setContentText("Seed set to "+text.trim());
		
	
		alert.showAndWait();
    }
    private static ArrayList<String> getCrittersInPath(String path){
    	
    	
    	int oldWorldWidth = Params.world_width;
		int oldWorldHeight = Params.world_height;
		int oldWalkEnergyCost = Params.walk_energy_cost;
		int oldRunEnergyCost = Params.run_energy_cost;
		int oldRestEnergyCost = Params.rest_energy_cost;
		int oldMinReproduceEnergy = Params.min_reproduce_energy;
		int oldRefreshAlgaeCount = Params.refresh_algae_count;
		int oldPhotosynthesisEnergy = Params.photosynthesis_energy_amount;
		int oldStartEnergy = Params.start_energy;
		int oldLookEnergy = Params.look_energy_cost;

    	
    	File folder=new File(path);
    	ArrayList<File> classFiles=new ArrayList<File>();
    	getAllClassFilesRecursive(classFiles,path);
    	ArrayList<String> critterNames=new ArrayList<String>();
    	for(int i = 0; i < classFiles.size(); i++){
			try{
				String className = classFiles.get(i).getName().replace(".class", ""); 
				Class<?> testingClass = Class.forName(myPackage + "." + className); 
				if(testingClass.newInstance() instanceof Critter) {
					critterNames.add(className);
				}
				
			}
			catch(LinkageError ex){
				
			}
			catch(ClassCastException ex){ 
				//try the next file
			}
			catch(ClassNotFoundException cnf){ 
				//try the next file
			}
			catch (IllegalAccessException ec) {
				//try the next file
			} 
			catch(InstantiationException in) {
				//try the next file
			}
			catch (IllegalArgumentException ee) {
				//try the next file
			} 
		}
    	Params.world_width=oldWorldWidth;
		 Params.world_height=oldWorldHeight;
		Params.walk_energy_cost=oldWalkEnergyCost;
		Params.run_energy_cost=oldRunEnergyCost;
		Params.rest_energy_cost=oldRestEnergyCost;
		Params.min_reproduce_energy=oldMinReproduceEnergy;
		Params.refresh_algae_count=oldRefreshAlgaeCount ;
		Params.photosynthesis_energy_amount=oldPhotosynthesisEnergy;
		Params.start_energy=oldStartEnergy;
		Params.look_energy_cost=oldLookEnergy;
    	return critterNames;
    }
    private static void getAllClassFilesRecursive(ArrayList<File> f,String path){
        File folder = new File(path);
        //get all the files from a directory
        File[] filesList = folder.listFiles();
        for (File file : filesList){
            if (file.isDirectory()){
                getAllClassFilesRecursive(f,file.getAbsolutePath());
            }
            else if (file.isFile() && file.getName().endsWith(".class")){
                f.add(file);
            } 
        }
    }
    private static void addQuitButton(GridPane mainGridPane){
    	GridPane quitButtonGridPane=new GridPane();
        quitButtonGridPane.setHgap(10);
        quitButtonGridPane.setVgap(10);
        quitButtonGridPane.setPadding(new Insets(0, 10, 0, 10));
        
        Button seedButton = new Button();
        seedButton.setText("Quit");
        seedButton.setOnAction(e->System.exit(0));
        quitButtonGridPane.add(seedButton, 0, 0);
        
        
        quitButtonGridPane.setStyle("-fx-border: 100px solid; -fx-border-color: red;-fx-background-color: #C0C0C0;");
       
        
        mainGridPane.add(quitButtonGridPane, 0, 4);
        
    }
    private static void addSingleFrameTimeStepGridPane(GridPane mainGridPane){
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
        
        
        singleFrameTimeStepGridPane.setStyle("-fx-border: 100px solid; -fx-border-color: red;-fx-background-color: #C0C0C0;");
        
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
    	Critter.displayWorld();  	
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
    	
    	
    	
    	
    	statisticsComboBox=new ComboBox<String>();
    	statisticsGridPane.add(statisticsComboBox,1,1);
    	statisticsComboBox.getItems().addAll(
	            getCrittersInPath(".")
	        ); 
    	statisticsComboBox.setOnAction(e->statisticsEventHandler(statisticsComboBox.getValue()));
    	
    	statisticsDisplayLabel=new Label();
    	statisticsDisplayLabel.setText("Critter not selected");
    	statisticsGridPane.add(statisticsDisplayLabel,0, 2);
  
    	statisticsGridPane.setStyle("-fx-border: 100px solid; -fx-border-color: red;-fx-background-color: #C0C0C0;");
    	
        mainGridPane.add(statisticsGridPane,0,2);
    }
    private static void statisticsEventHandler(String text){
    	if(text==null){
    		statisticsDisplayLabel.setText("Critter not selected");
    		return;
    	}
    	String displayString;
    		 List<Critter> res;
    		 try{
    		 res=Critter.getInstances(text);
    		 displayString=runStatsMethod(text,res);
    		 }catch(InvalidCritterException ex){
    			 
    			 if(DEBUG){
    				 System.out.println("Problem in parseStatsMethod");
    			 }
    			 Alert alert = new Alert(AlertType.INFORMATION);
    				alert.setTitle("Error");
    				alert.setHeaderText(null);
    				alert.setContentText("Invalid Critter Exception");
    				alert.showAndWait();
    				return;
    			
    		 }
    	statisticsDisplayLabel.setText(displayString);
    	
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
	            getCrittersInPath(".")
	        ); 
	makeCritterGridPane.add(critterTypeComboBox,1,1);
	
	Label critterNumberLabel=new Label();
	critterNumberLabel.setText("Number");
	makeCritterGridPane.add(critterNumberLabel,0,2);
	
	TextField critterNumberTextField=new TextField();
	makeCritterGridPane.add(critterNumberTextField,1,2);
	
	Button makeCritterButton = new Button();
    makeCritterButton.setText("Add Critters");
    makeCritterButton.setOnAction(e->makeCritterHandler(critterTypeComboBox.getValue(),critterNumberTextField.getText()));
    makeCritterGridPane.add(makeCritterButton, 0,3 );
    
    makeCritterGridPane.setStyle("-fx-border: 100px solid; -fx-border-color: red;-fx-background-color: #C0C0C0;");
    
    mainGridPane.add(makeCritterGridPane, 0, 0);
}
private static void makeCritterHandler(String textComboBox,String textNumberField){
	if(textComboBox==null){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		alert.setContentText("Critter type not chosen");
		alert.showAndWait();
		return;
	}
	
	int numberOfCritters;
	try{
		numberOfCritters=Integer.parseInt(textNumberField.trim());
	}catch(NumberFormatException ex){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		if(textNumberField.trim().length()>0){
			alert.setContentText("Number not entered: "+textNumberField.trim());
		}
		else{
			alert.setContentText("No number entered");
		}
		alert.showAndWait();
		return;
	}
	try{
	for(int i=0;i<numberOfCritters;i++){
		Critter.makeCritter(textComboBox);
		
	}
	}catch(InvalidCritterException ex){
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Error");
		alert.setHeaderText(null);
		
			alert.setContentText("Invalid Critter Exception thrown");
		
	}
	Critter.displayWorld();
	
}

    //See http://stackoverflow.com/questions/9042740/call-static-method-given-a-class-object-in-java

    /**
     * Runs the stat method with res from getInstances with critter_class_name
     * @param critter_class_name Name of critter class to run Stats on
     * @param res List of critters of critter_class_name
     * @throws InvalidCritterException If some error 
     */
    protected static String runStatsMethod(String critter_class_name,List<Critter> res) throws InvalidCritterException{
    	String returnedValue;
    	try{
    	Class<?> inClass=Class.forName(myPackage+"."+critter_class_name);
    	Method inMethod=inClass.getMethod("runStats",List.class);
    			returnedValue=(String)inMethod.invoke(null,res);
    	}catch(Exception ex){
    		if(DEBUG){
    		ex.printStackTrace(System.out);
    		System.out.println("Problem in Stats method");
    		}
    		throw new InvalidCritterException(critter_class_name);
    		
    	}
    	return returnedValue;
    }
   
    
    
}
