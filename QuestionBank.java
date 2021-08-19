import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class QuestionBank extends Application {
	static Question[] questionsArray = readQuestionsFile();
	
	// this field will be used in the EditQuestion method to traverse through questions
    int index = 0;
    // this field will be used in the takeTheTest method to count the numbers of the correct choices
    int numOfCorrectAnswers = 0;
    
    @Override
    public void start(Stage primaryStage) {
        mainScene(primaryStage);
    }
    // The main Scene users sees when opening the program
    public void mainScene(Stage primaryStage) {
        // Setting the title and its properties
    	Label lblTitle = new Label("Welcome " + System.getProperty("user.name") 
        	+ "\nQuestion Bank Creater ", new ImageView("http://www.kfupm.edu.sa/Kfupm_Main/images1/logo.png")); 
        lblTitle.setTextAlignment(TextAlignment.CENTER);
        lblTitle.setContentDisplay(ContentDisplay.TOP);
        lblTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 30));
        
        // Button for the modify scene
        Button btModify = new Button("Modify The Test");
        btModify.setPrefSize(200, 80);
        btModify.setFont(Font.font("Georgia", 22));
        btModify.setOnAction(e -> modifyQuestionScene(primaryStage));
        
        // Button for the Test scene
        Button btTest = new Button("Take The Test");
        btTest.setPrefSize(200, 80);
        btTest.setFont(Font.font("Georgia", 22));
        btTest.setOnAction(e -> takeTestScene(primaryStage));

        // Checks if there is no questions in the array to disable the take a test button
        if (questionsArray.length == 0) {
			btTest.setDisable(true);
		}
        
        // Creating a HBox and adding the buttons to it
        HBox hbButtons = new HBox(80, btModify, btTest);
        hbButtons.setAlignment(Pos.CENTER);
        
        // Setting the border pane and its properties
        BorderPane paneForMenu = new BorderPane();
        paneForMenu.setPadding(new Insets(20));
        paneForMenu.setTop(lblTitle);
        paneForMenu.setCenter(hbButtons);
        BorderPane.setAlignment(lblTitle, Pos.CENTER);
        
        // Creating the scene and setting it to the stage
        Scene mainScene = new Scene(paneForMenu, 850, 400);
        primaryStage.setTitle("Test Bank Program"); 
	    primaryStage.setScene(mainScene); 
	    primaryStage.show();
    }
    
    // The scene where the user select to create, edit, view, and delete a question
    public void modifyQuestionScene(Stage primaryStage) {
    	
		// Setting up the title and its properties
    	Label lblTitle = new Label("Modify Menu");
		lblTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 30));

		// Creating the four main buttons 
		Button btAdd = new Button("Add a New Question");
		Button btView = new Button("View All Questions");
		Button btEdit = new Button("Edit Questions");
		Button btDelete = new Button("Delete Questions");

		// Setting the properties of the buttons
		for (Button bt : new Button[] { btAdd, btView, btEdit, btDelete }) {
			bt.setPrefSize(250, 80);
			bt.setTextAlignment(TextAlignment.CENTER);
			bt.setFont(Font.font("Georgia", 24));
			bt.setWrapText(true);
		}
		
		// if there's no questions in the file the view, edit, and delete buttons will be disabled
		if (questionsArray.length == 0) {
			btView.setDisable(true);
			btEdit.setDisable(true);
			btDelete.setDisable(true);
		}
		else {
			btView.setDisable(false);
			btEdit.setDisable(false);
			btDelete.setDisable(false);
		}

		// Creating the return button to return to the main menu
		Button btReturn = new Button("Return To Main Menu");
		btReturn.setPrefSize(170, 40);
		btReturn.setFont(Font.font("Georgia", 15));

		// Creating a grid pane and setting its properties
		GridPane paneForButton = new GridPane();
		paneForButton.setHgap(50);
		paneForButton.setVgap(40);
		paneForButton.setAlignment(Pos.CENTER);
		paneForButton.add(btAdd, 0, 0);
		paneForButton.add(btView, 1, 0);
		paneForButton.add(btEdit, 0, 1);
		paneForButton.add(btDelete, 1, 1);
		
		// Setting the properties of the buttons
		btAdd.setOnAction(e -> createQuestionScene(primaryStage));
		btView.setOnAction(e -> viewQuestionScene(primaryStage));
		btEdit.setOnAction(e -> editQuestionScene(primaryStage));
		btDelete.setOnAction(e -> deleteQuestionScene(primaryStage));
		btReturn.setOnAction(e -> mainScene(primaryStage));
		
		// Creating a border pane and setting its properties
		BorderPane modifyPane = new BorderPane();
		modifyPane.setPadding(new Insets(30));
		modifyPane.setTop(lblTitle);
		modifyPane.setCenter(paneForButton);
		modifyPane.setBottom(btReturn);
		BorderPane.setAlignment(lblTitle, Pos.CENTER);
		BorderPane.setAlignment(btReturn, Pos.CENTER);
		
		// Creating the scene and setting it to the stage
		Scene modifyScene = new Scene(modifyPane, 850, 400);
		primaryStage.setScene(modifyScene);

    }
    
    // the scene where the user adds a question to the question bank
    public void createQuestionScene(Stage primaryStage) {
    	// Setting the title and its properties
    	Label lblTitle = new Label("Add a New Question");
	    lblTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 30));
	    
	    // Creating Text fields to take the input from the user
	    TextField tfQuestion = new TextField();
    	TextField tfAnswer1 = new TextField();
		TextField tfAnswer2 = new TextField();
		TextField tfAnswer3 = new TextField();
		TextField tfAnswer4 = new TextField();
		
		// Setting the properties of the Text fields
		for (TextField tf : new TextField[] {tfQuestion, tfAnswer1, tfAnswer2, tfAnswer3, tfAnswer4}) {
			tf.setPrefSize(250, 30);
	        tf.setFont(Font.font("Georgia", 18));
		}
    	tfQuestion.setPrefSize(550, 30);
		
    	// Settings labels for question and answer and joining them with the text field
    	Label lblQuestion  = new Label("The Question: ", tfQuestion);
    	Label lblAnswer1 = new Label("Correct Choice", tfAnswer1);
    	Label lblAnswer2 = new Label("Wrong Choice", tfAnswer2);
    	Label lblAnswer3 = new Label("Wrong Choice", tfAnswer3);
    	Label lblAnswer4 = new Label("Wrong Choice", tfAnswer4);
    	
    	// Setting the properties of the labels
    	for (Label lbl : new Label[] {lblQuestion, lblAnswer1, lblAnswer2, lblAnswer3, lblAnswer4}) {
			lbl.setContentDisplay(ContentDisplay.BOTTOM);
	        lbl.setFont(Font.font("Georgia", 20));
		}
        
    	// Settings a grid pane for the answers
    	GridPane paneForAnswers = new GridPane();
        paneForAnswers.setHgap(40);
        paneForAnswers.setVgap(30);
        paneForAnswers.setAlignment(Pos.CENTER);
        
        paneForAnswers.add(lblAnswer1, 0, 0);
        paneForAnswers.add(lblAnswer2, 1, 0);
        paneForAnswers.add(lblAnswer3, 0, 1);
        paneForAnswers.add(lblAnswer4, 1, 1);
        
        // Creating a button to return to the the modify menu
        Button btReturn = new Button("Return To Modify Menu");
	    btReturn.setPrefSize(200, 40);
	    btReturn.setFont(Font.font("Georgia", 15));
	    btReturn.setOnAction(e -> {
        	modifyQuestionScene(primaryStage);
        });
	    
	    // Creating the confirm button that the user presses to create the question 
	    Button btConfirm = new Button("Confirm");
	    btConfirm.setPrefSize(200, 40);
	    btConfirm.setFont(Font.font("Georgia", 15));
	    btConfirm.setOnAction(e -> {
	    	// If one of the text fields is empty the program will ask the user to fill all the requirements
	    	if(tfQuestion.getText().length() == 0 || tfAnswer1.getText().length() == 0 || tfAnswer2.getText().length() == 0 ||
	    			tfAnswer3.getText().length() == 0 || tfAnswer4.getText().length() == 0) {
	    		btConfirm.setText("Please Fill All The Requirements");
	    	}
	    	else {
		    	String[] otherChoices = {tfAnswer2.getText(), 
		    						 tfAnswer3.getText(), tfAnswer4.getText()};

		    	Question q = new Question(tfQuestion.getText(), tfAnswer1.getText(), otherChoices);
	        	addQuestion(q);
	        
	        	btConfirm.setText("Question Created \u2714");
	        	btConfirm.setDisable(true);
	    	}
	    	
        });
	    
	    // Creating HBox for the buttons and setting its properties
	    HBox hbButtons = new HBox(20, btReturn, btConfirm);
    	hbButtons.setAlignment(Pos.CENTER);
    	
    	// Creating VBox for the interface and setting its properties
    	VBox paneForAddQuestion = new VBox(15, lblTitle, lblQuestion, paneForAnswers, hbButtons);
    	paneForAddQuestion.setAlignment(Pos.CENTER);
    	
    	// Creating the add a question scene and setting it to the stage
    	Scene addQuestionScene = new Scene(paneForAddQuestion, 850, 400);
	    primaryStage.setScene(addQuestionScene); 

    }

    // the scene where the user view all questions in the question bank
    public void viewQuestionScene(Stage primaryStage) {
    	
    	// Creating the title and setting its properties
    	Label lblTitle = new Label("View Questions");
	    lblTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 30));
    	
	    // Creating a VBox that will contain the questions
    	VBox paneForQuestions = new VBox(30, lblTitle);
	    paneForQuestions.setAlignment(Pos.CENTER);
	    
	    // Getting the question and answers to view them to the user
	    int n = 0;
	    for (Question q : questionsArray) {
	    	Label question = new Label("Q" + (n + 1) + ": " +  q.getQuestion());
	        question.setFont(Font.font("Georgia", 23));
	        question.setWrapText(true);
	        
	        // Creating labels for the question and answers
	        Label lblAnswer1 = new Label("A: " + q.getCorrectAnswer() + " \u2714");
	        Label lblAnswer2 = new Label("B: " + q.getChoices()[0]);
	        Label lblAnswer3 = new Label("C: " + q.getChoices()[1]);
	        Label lblAnswer4 = new Label("D: " + q.getChoices()[2]);	        
	        
	        // Settings the properties of the labels created above
	        for (Label i : new Label[] {lblAnswer1, lblAnswer2, lblAnswer3, lblAnswer4}) {
	        	i.setFont(Font.font("Georgia", 18));
	        	i.setWrapText(true);
	        }
	        
	        // Creating a pane for then answers and adding the answers to it
	        GridPane paneForAnswer = new GridPane();
	        paneForAnswer.setHgap(30);
	        paneForAnswer.setVgap(30);
	        paneForAnswer.setAlignment(Pos.CENTER);
	        
	        paneForAnswer.add(lblAnswer1, 0, 0);
	        paneForAnswer.add(lblAnswer2, 1, 0);
	        paneForAnswer.add(lblAnswer3, 0, 1);
	        paneForAnswer.add(lblAnswer4, 1, 1);

	        // Creating a VBox that contains all the question information
	        VBox paneFor1Question = new VBox(20, question, paneForAnswer);
	        paneFor1Question.setStyle("-fx-border-color: darkgrey; -fx-border-width: 1");
	    	paneFor1Question.setAlignment(Pos.CENTER);
	    	paneFor1Question.setPrefSize(700,200);
	    	
	    	// Adding the VBox created above to the interface
	    	paneForQuestions.getChildren().add(paneFor1Question);
	    	n++;
	    }
	    
	    // Creating a scroll pane and setting its properties
	    ScrollPane scrollPane = new ScrollPane();
	    scrollPane.setContent(paneForQuestions);
	    scrollPane.fitToWidthProperty().set(true);
	    scrollPane.setPadding(new Insets(20));
	    
        // Creating a button to return to the the modify menu
	    Button btReturn = new Button("Return To Modify Menu");
	    btReturn.setPrefSize(200, 40);
	    btReturn.setFont(Font.font("Georgia", 15));
	    btReturn.setOnAction(e -> modifyQuestionScene(primaryStage));
	    
	    // Creating a border pane that holds the scroll pane and the return button
	    BorderPane paneForView = new BorderPane();
	    paneForView.setCenter(scrollPane);
	    paneForView.setBottom(btReturn);
	    BorderPane.setAlignment(btReturn, Pos.CENTER);
	    BorderPane.setAlignment(scrollPane, Pos.CENTER);
	    
	    // Create the scene and setting it to the stage
	    Scene viewQScene = new Scene(paneForView, 850, 400);
	    primaryStage.setScene(viewQScene); 
    	
    }
    
    // the scene where the user deletes a question from the questions bank
    public void deleteQuestionScene(Stage primaryStage) {
    	// Creating the title and setting its properties
    	Label lblTitle = new Label("Delete Questions");
	    lblTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 30));
	    
	    // Creating a Check Box and setting that have the same length as the questionsArray
    	CheckBox[] checkBoxArr = new CheckBox[questionsArray.length];
    	
    	// Creating a VBox that contains the title and questions
    	VBox vbQuestions = new VBox(30,lblTitle);
	    vbQuestions.setAlignment(Pos.CENTER);
	    
	    // Getting the question and answers to view them to the user
	    int n = 0;
	    for (Question q : questionsArray) {
	    	Label lblQuestion = new Label("Q" + (n + 1) + ": " +  q.getQuestion());
	    	lblQuestion.setFont(Font.font("Georgia", 23));
	        lblQuestion.setWrapText(true);
	        
	        // Creating labels for the question and answers
	        Label lblAnswer1 = new Label("A: " + q.getCorrectAnswer() + " \u2714");
	        Label lblAnswer2 = new Label("B: " + q.getChoices()[0]);
	        Label lblAnswer3 = new Label("C: " + q.getChoices()[1]);
	        Label lblAnswer4 = new Label("D: " + q.getChoices()[2]);	        
	        
	        // Settings the properties of the labels created above
	        for (Label lbl : new Label[] {lblAnswer1, lblAnswer2, lblAnswer3, lblAnswer4}) {
	        	lbl.setFont(Font.font("Georgia", 18));
	        	lbl.setWrapText(true);
	        }
	        
	        // Creating a pane for then answers and adding the answers to it
	        GridPane paneForAnswers = new GridPane();
	        paneForAnswers.setHgap(30);
	        paneForAnswers.setVgap(30);
	        paneForAnswers.setAlignment(Pos.CENTER);
	        paneForAnswers.add(lblAnswer1, 0, 0);
	        paneForAnswers.add(lblAnswer2, 1, 0);
	        paneForAnswers.add(lblAnswer3, 0, 1);
	        paneForAnswers.add(lblAnswer4, 1, 1);
	        
	        // Creating a ChechBox To make the user select which question get deleted
	    	checkBoxArr[n] = new CheckBox("Q" + (n+1));
	    	checkBoxArr[n].setContentDisplay(ContentDisplay.BOTTOM);
	    	
	    	/* Creating a VBox with the question and answers, then creating an HBox that
	    	contain the VBox created above and the Check box for the corresponding question
	    	then setting its properties*/
	        VBox vb1Question = new VBox(20, lblQuestion, paneForAnswers);
	        HBox hbQuestionWithChk = new HBox(15, checkBoxArr[n], vb1Question);	      	        
	        hbQuestionWithChk.setAlignment(Pos.CENTER);
	        vb1Question.setStyle("-fx-border-color: darkgrey; -fx-border-width: 1");
	    	vb1Question.setAlignment(Pos.CENTER);
	    	vb1Question.setPrefSize(700,200);
	    	
	    	// adding the question to the vbQuestions
	    	vbQuestions.getChildren().addAll(hbQuestionWithChk);
	    	n++;
	    }
	    
	    // Creating a scroll pane and setting it's properties
    	ScrollPane scrollPane = new ScrollPane();
	    scrollPane.setContent(vbQuestions);
	    scrollPane.fitToWidthProperty().set(true);
	    scrollPane.setPadding(new Insets(20));
	    
        // Creating a button to return to the the modify menu
	    Button btReturn = new Button("Return To Modify Menu");
	    btReturn.setPrefSize(200, 40);
	    btReturn.setFont(Font.font("Georgia", 15));
	    btReturn.setOnAction(e -> {
        	modifyQuestionScene(primaryStage);
        });
	    
	    // Creating a button to delete the selected questions
	    Button btDelete = new Button("Delete Selected Questions");
	    btDelete.setPrefSize(200, 40);
	    btDelete.setFont(Font.font("Georgia", 15));
	    
	    // An algorithm To delete the questions selected by the user
	    btDelete.setOnAction(e -> {
	    	int questionIndex = 0;
	    	for (int chbIndex = 0 ; chbIndex < checkBoxArr.length ; chbIndex++) {
	    		if (checkBoxArr[chbIndex].isSelected()) {
	    			deleteQuestion(questionIndex);
	    		}
	    		else 
	    			questionIndex++;
	    	}
	    	deleteQuestionScene(primaryStage);
        });
	    
	    // Creating an HBox that contains the buttons
	    HBox hbButtons = new HBox(40, btReturn, btDelete);
	    hbButtons.setAlignment(Pos.CENTER);
	    
	    // Creating a border pane and settings its properties
	    BorderPane paneForScene = new BorderPane();
	    paneForScene.setCenter(scrollPane);
	    paneForScene.setBottom(hbButtons);
	    BorderPane.setAlignment(btReturn, Pos.CENTER);
	    BorderPane.setAlignment(scrollPane, Pos.CENTER);
	    
	    // Creating the scene and setting it to the stage
	    Scene sceneForDelete = new Scene(paneForScene, 850, 400);
	    primaryStage.setScene(sceneForDelete); 
    }
    
    // the scene where the user edits a question from the question bank
    public void editQuestionScene(Stage primaryStage) {
    	index = 0; // resets the index
    	// Creating the label and setting its properties
    	Label lblTitle = new Label("Edit a Question");
	    lblTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 30));

	    /* Creating text fields and getting the question and answer
	    in them so the user can edit the question*/
	    TextField tfQuestion = new TextField(questionsArray[0].getQuestion());
    	TextField tfAnswer1 = new TextField(questionsArray[0].getCorrectAnswer());
		TextField tfAnswer2 = new TextField(questionsArray[0].getChoices()[0]);
		TextField tfAnswer3 = new TextField(questionsArray[0].getChoices()[1]);
		TextField tfAnswer4 = new TextField(questionsArray[0].getChoices()[2]);
		
		// Setting the properties of the text fields
		tfQuestion.setPrefSize(550, 30);
		tfQuestion.setFont(Font.font("Georgia", 18));
		for (TextField i : new TextField[] {tfAnswer1, tfAnswer2, tfAnswer3, tfAnswer4}) {
			i.setPrefSize(250, 30);
	        i.setFont(Font.font("Georgia", 18));
		}
		
		/* Creating the labels for the question and answers
		and joining them with the text field */
    	Label lblQuestion  = new Label("The Question: ", tfQuestion);
    	Label lblAnswer1 = new Label("Correct Choice", tfAnswer1);
    	Label lblAnswer2 = new Label("Wrong Choice", tfAnswer2);
    	Label lblAnswer3 = new Label("Wrong Choice", tfAnswer3);
    	Label lblAnswer4 = new Label("Wrong Choice", tfAnswer4);
    	
        // Settings the properties of the labels created above
		for (Label lbl : new Label[] {lblQuestion, lblAnswer1, lblAnswer2, lblAnswer3, lblAnswer4}) {
			lbl.setContentDisplay(ContentDisplay.BOTTOM);
	        lbl.setFont(Font.font("Georgia", 20));
		}
        
		// Creating a pane and setting its properties
        GridPane paneForAnswers = new GridPane();
        paneForAnswers.setHgap(40);
        paneForAnswers.setVgap(30);
        paneForAnswers.setAlignment(Pos.CENTER);
        paneForAnswers.add(lblAnswer1, 0, 0);
        paneForAnswers.add(lblAnswer2, 1, 0);
        paneForAnswers.add(lblAnswer3, 0, 1);
        paneForAnswers.add(lblAnswer4, 1, 1);
        
        // Creating buttons for navigation
	    Button btNext = new Button("Next ->");
	    Button btPrevious = new Button("<- Previous");
	    btNext.setFont(Font.font("Georgia", 14));
	    btPrevious.setFont(Font.font("Georgia", 14));
	    btPrevious.setDisable(true);
	    
	    // Create an array so that the user can choose the any question specifically
	    String[] questionsNumbers = new String[questionsArray.length];
	    for (int i = 0 ; i < questionsArray.length ; i++) {
	    	questionsNumbers[i] = "Q" + (i + 1);
	    }
	    ComboBox<String> cboQuestionNumber = new ComboBox<>();
	    cboQuestionNumber.getItems().addAll(questionsNumbers);
    	cboQuestionNumber.setValue("Q1");
    	
    	// Checks if the array have only one question to disable the next Button and the combo box
	    if(questionsArray.length == 1) {
    		btNext.setDisable(true);
    		cboQuestionNumber.setDisable(true);
    	}
	    
        // Create a button to return to the modify menu
    	Button btReturn = new Button("Return To Modify Menu");
	    btReturn.setPrefSize(200, 40);
	    btReturn.setFont(Font.font("Georgia", 15));
	    btReturn.setOnAction(e -> modifyQuestionScene(primaryStage));
	    
	    // Create a button to confirm the changes to the question and set its properties
	    Button btConfirm = new Button("Confirm");
	    btConfirm.setPrefSize(200, 40);
	    btConfirm.setFont(Font.font("Georgia", 15));
	    btConfirm.setOnAction(e -> {
	    	// If one of the text fields is empty the program will ask the user to fill all the requirements
	    	if(tfQuestion.getText().length() == 0 || tfAnswer1.getText().length() == 0 || tfAnswer2.getText().length() == 0 ||
	    			tfAnswer3.getText().length() == 0 || tfAnswer4.getText().length() == 0) {
	    		btConfirm.setText("Please Fill All The Requirements");
	    	}
	    	else {
	    		questionsArray[index].setQuestion(tfQuestion.getText());
	        	questionsArray[index].setCorrectAnswer(tfAnswer1.getText());
	        	questionsArray[index].setOtherChoices(new String[]{tfAnswer2.getText(), tfAnswer3.getText(), tfAnswer4.getText()});
	        	
	        	btConfirm.setText("Change Successful \u2714");
	        	btConfirm.setDisable(true);
	    	}
        	
        });
	    
	    btNext.setOnAction(e -> {
	    	if (index < questionsArray.length - 1) {
	    		index++;
	    		btPrevious.setDisable(false);
	    		}
	    	
	    	tfQuestion.setText(questionsArray[index].getQuestion());
	    	tfAnswer1.setText(questionsArray[index].getCorrectAnswer());
	    	tfAnswer2.setText(questionsArray[index].getChoices()[0]);
	    	tfAnswer3.setText(questionsArray[index].getChoices()[1]);
	    	tfAnswer4.setText(questionsArray[index].getChoices()[2]);
	    	cboQuestionNumber.getSelectionModel().select("Q" + (index+1));
	    	
	    	// disable the button if the user is in the last question
	    	if (index == questionsArray.length - 1)
	    		btNext.setDisable(true);
	    });
	    
	    btPrevious.setOnAction(e -> {
			if (index > 0) {
				index--;
				btNext.setDisable(false);
			}
			
			tfQuestion.setText(questionsArray[index].getQuestion());
	    	tfAnswer1.setText(questionsArray[index].getCorrectAnswer());
	    	tfAnswer2.setText(questionsArray[index].getChoices()[0]);
	    	tfAnswer3.setText(questionsArray[index].getChoices()[1]);
	    	tfAnswer4.setText(questionsArray[index].getChoices()[2]);
	    	cboQuestionNumber.getSelectionModel().select("Q" + (index+1));
	    	
	    	// disable the button if the user is in the last question
			if (index == 0)
				btPrevious.setDisable(true);
		});
	    cboQuestionNumber.setOnAction(e -> {
	    	index = (cboQuestionNumber.getSelectionModel().getSelectedIndex());
	    	tfQuestion.setText(questionsArray[index].getQuestion());
	    	tfAnswer1.setText(questionsArray[index].getCorrectAnswer());
	    	tfAnswer2.setText(questionsArray[index].getChoices()[0]);
	    	tfAnswer3.setText(questionsArray[index].getChoices()[1]);
	    	tfAnswer4.setText(questionsArray[index].getChoices()[2]);
	    	cboQuestionNumber.getSelectionModel().select("Q" + (index+1));
	    	
	    	if (index == 0) {
	    		btPrevious.setDisable(true);
	    		btNext.setDisable(false);
	    	}
	    	else if (index == questionsArray.length - 1) {
	    		btNext.setDisable(true);
	    		btPrevious.setDisable(false);
	    	}
	    	else {
	    		btNext.setDisable(false);
	    		btPrevious.setDisable(false);
	    	}
	    });
	    
	    // Setting an HBox that holds the navigation buttons in it
	    HBox hbNavigationButtons = new HBox(15, btPrevious, cboQuestionNumber, btNext);
	    hbNavigationButtons.setAlignment(Pos.CENTER);
	    		
	    // Setting an HBox for the return and confirm button
	    HBox hbButtons = new HBox(20, btReturn, btConfirm);
    	hbButtons.setAlignment(Pos.CENTER);
    	
    	// Setting a VBox and putting all the question information in it and the navigation buttons
    	VBox paneForScene = new VBox(15, lblTitle, lblQuestion, paneForAnswers, hbNavigationButtons, hbButtons);
    	paneForScene.setAlignment(Pos.CENTER);
    	
    	// Create a scene and setting it to the stage
    	Scene sceneForEdit = new Scene(paneForScene, 850, 400);
	    primaryStage.setScene(sceneForEdit);   
    }
    
    // The scene where the user can take the test
    public void takeTestScene(Stage primaryStage) {
    	// Resetting the index and the number of the correct choices the users make
    	index = 0;
    	numOfCorrectAnswers = 0;
    	
    	// Creating an array list for the questions and coping the questionsArray into it and setting its properties
    	ArrayList<Question> questions = new ArrayList<Question>();
    	Collections.addAll(questions, questionsArray);
    	Collections.shuffle(questions);
    	
    	// Creating an array list for the answers and setting its properties
    	ArrayList<String> answers = new ArrayList<String>();
    	answers.add(questions.get(index).getCorrectAnswer());
    	answers.add(questions.get(index).getChoices()[0]);
    	answers.add(questions.get(index).getChoices()[1]);
    	answers.add(questions.get(index).getChoices()[2]);
    	Collections.shuffle(answers);
    	
    	// Creating four radio buttons and grouping them together and setting their properties
    	RadioButton rbAnswer1 = new RadioButton(answers.get(0));
    	RadioButton rbAnswer2 = new RadioButton(answers.get(1));
    	RadioButton rbAnswer3 = new RadioButton(answers.get(2));
    	RadioButton rbAnswer4 = new RadioButton(answers.get(3));
    	ToggleGroup group = new ToggleGroup();
    	for (RadioButton rb : new RadioButton[] {rbAnswer1, rbAnswer2, rbAnswer3, rbAnswer4}) {
    		rb.setToggleGroup(group);
    		rb.setFont(Font.font("Georgia", 20));
    	}
    	
    	// Creating a label for the question and setting its properties
		Label lblQuestion = new Label("Question: " + (index + 1) + " out of " + questions.size() 
									+ "\n\n" + questions.get(index).getQuestion());
		lblQuestion.setFont(Font.font("Georgia", 25));
		lblQuestion.setTextAlignment(TextAlignment.CENTER);
		
		// Creating a grip pane and setting its properties
    	GridPane paneForAnswers = new GridPane();
    	paneForAnswers.setHgap(30);
        paneForAnswers.setVgap(30);
        paneForAnswers.setAlignment(Pos.CENTER);
        paneForAnswers.add(rbAnswer1, 0, 0);
        paneForAnswers.add(rbAnswer2, 1, 0);
        paneForAnswers.add(rbAnswer3, 0, 1);
        paneForAnswers.add(rbAnswer4, 1, 1);
    	
        // Creating a VBox that holds the questions and the answers
    	VBox vbQuestion = new VBox(60, lblQuestion, paneForAnswers);
    	vbQuestion.setAlignment(Pos.CENTER);
    	
    	// Create a button to return to the main menu
    	Button btReturn = new Button("Return To Main Menu");
	    btReturn.setPrefSize(200, 40);
	    btReturn.setFont(Font.font("Georgia", 15));
	    btReturn.setOnAction(e -> mainScene(primaryStage));

		// Create a button to confirm the choice of the user
	    Button btConfirm = new Button("Submit Answer");
	    btConfirm.setPrefSize(200, 40);
	    btConfirm.setFont(Font.font("Georgia", 15));
	    // An algorithm that check whether the user choice is correct and move to the next question
	    btConfirm.setOnAction(e -> {
	    	for(RadioButton a : new RadioButton[] {rbAnswer1, rbAnswer2, rbAnswer3, rbAnswer4}) {
	    		if (a.isSelected() && a.getText() == questions.get(index).getCorrectAnswer()) {
	    			numOfCorrectAnswers++;
	    		}
	        	a.setSelected(false);
	    	}
    		index++;
    		
	    	if(index == questions.size()) {
	    		
	    		Label lblTitle = new Label("Your Score Is");
	    		lblTitle.setFont(Font.font("Georgia", 25));
	    		
	    		Label lblScore = new Label(numOfCorrectAnswers + "/" + questions.size());
	    		lblScore.setFont(Font.font("Georgia", 30));
	    		
	    		Button btView = new Button("View The Questions");
	    		btView.setPrefSize(250, 60);
	    		btView.setFont(Font.font("Georgia", 20));
	    		btView.setAlignment(Pos.CENTER);
	    		btView.setOnAction(d -> viewQuestionScene(primaryStage));

	    		VBox vbResult = new VBox(30, lblTitle, lblScore, btView);
	    		vbResult.setAlignment(Pos.CENTER);
	    		
	    		Scene resultScene = new Scene(vbResult, 850, 400);
	    	    primaryStage.setScene(resultScene); 

	    	}

	    	else {
	    		answers.clear();
	    		answers.add(questions.get(index).getCorrectAnswer());
	        	answers.add(questions.get(index).getChoices()[0]);
	        	answers.add(questions.get(index).getChoices()[1]);
	        	answers.add(questions.get(index).getChoices()[2]);
	        	Collections.shuffle(answers);
	    		
	        	lblQuestion.setText("Question: " + (index + 1) + " out of " + questions.size() 
				+ "\n" + questions.get(index).getQuestion());
	        	rbAnswer1.setText(answers.get(0));
	        	rbAnswer2.setText(answers.get(1));
	        	rbAnswer3.setText(answers.get(2));
	        	rbAnswer4.setText(answers.get(3));
	    	}
	    });
	    
	    // Creating an HBox that contains the confirm and return buttons
	    HBox hbButtons = new HBox(20, btReturn, btConfirm);
    	hbButtons.setAlignment(Pos.CENTER);
    	
    	// Create a border pane for the question prompt and choices
    	BorderPane paneForQuestion = new BorderPane();
		paneForQuestion.setCenter(vbQuestion);
    	paneForQuestion.setBottom(hbButtons);
		
    	    	
    	//Create a scene and setting it to the stage
    	Scene sceneForTest = new Scene(paneForQuestion, 850, 400);
	    primaryStage.setScene(sceneForTest); 
    }
    
	// Method for reading the questions object from the file and putting them in an array of questions
    public static Question[] readQuestionsFile() {
		File questionsFile = new File("QuestionBank.dat");
		
		// if the file doesn't exist the program will make a new file and write an empty array it in
		if (!questionsFile.exists()) {
			try (ObjectOutputStream binaryOutput = new ObjectOutputStream(new FileOutputStream(questionsFile))) {
				binaryOutput.writeObject(new Question[] {});
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		// Read the array object from the file
		try (ObjectInputStream binaryInput = new ObjectInputStream(new FileInputStream(questionsFile));) {
			return (Question[])binaryInput.readObject();
		}
		
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
    
    // Method for adding a question to the array of questions
    public static void addQuestion(Question questionObj) {
		// Create a new array with a bigger size to fit the new question
    	Question[] newArr = new Question[questionsArray.length + 1];
		
    	// Copying the previous array objects into the new array
		for (int i = 0 ; i < questionsArray.length ; i++) {
			newArr[i] = questionsArray[i];
		}
		
		// Putting the new question at the end of the new array
		newArr[newArr.length - 1] = questionObj;
		
		// Replace the previous array with the new array
		questionsArray = newArr.clone();
	}
    
    // Method for deleting a question from the array of questions
    
    // Method for deleting selected questions object from the array
    public static void deleteQuestion(int index) {
    	// Create a new array with less length than the previous array
		Question[] newArr = new Question[questionsArray.length - 1];
		
		int newArrIndex = 0;
		// Copying the previous array into the new array without the deleted question
		for (int oldArrIndex = 0 ; oldArrIndex < questionsArray.length ; oldArrIndex++) {
			if (oldArrIndex == index) {
				continue;
			}
			newArr[newArrIndex] = questionsArray[oldArrIndex];
			newArrIndex++;
		}
		// Replace the previous array with the new array
		questionsArray = newArr.clone();
	}
    
    
    
    // Main function to lunch the program
    public static void main(String[] args) {
        launch(args);
        
        // Write the questions array to the file upon exit
		File qFile = new File("QuestionBank.dat");
        try (ObjectOutputStream binaryOut = new ObjectOutputStream(new FileOutputStream(qFile))) {
			binaryOut.writeObject(questionsArray);
			System.out.println("Changes saved to QuestionBank.dat");
		}
		catch (Exception e) {
			System.out.println("ERROR");
		}
    }
    
    
    
}




@SuppressWarnings("serial")
class Question implements Serializable {
	private String question;
	private String correctAnswer;
	private String[] otherChoices;
	
	public Question(String question, String correctAnswer, String[] otherChoices) {
		this.question = question;
		this.correctAnswer = correctAnswer;
		this.otherChoices = otherChoices;
	}
	
	public String getQuestion() {
		return question;
	}
	public String getCorrectAnswer() {
		return correctAnswer;
	}
	public String[] getChoices() {
		return otherChoices;
	}
	
	public void setQuestion(String question) {
		this.question = question;
	}
	public void setCorrectAnswer(String correctAnswer) {
		this.correctAnswer = correctAnswer;
	}
	public void setOtherChoices(String[] otherChoices) {
		this.otherChoices = otherChoices;
	}	
}