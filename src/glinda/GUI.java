
package glinda;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GUI extends Application{
    Group group;
    Scene scene;
    Rectangle problemImg;
    Label problemText;
    // USER RESOLUTION DATA
    Rectangle2D screen_data = Screen.getPrimary().getBounds();
    
    public static String definedUsername;
    public static String definedPassword;  // FOR EXTRA SECUTITY & ONE TIME PADDING
    public GUI(){
        definedUsername = null;
        definedPassword = null;     // ASK USER IF HE USE EXTRA PASSWORD
    }
    
    public static void startGUI(){
        launch(null);
    }

    @Override
    public void start(Stage stage) {
        // ALLOWED WINDOW SIZE
        double WINDOW_HEIGHT = screen_data.getHeight() * 88 / 100;
        double WINDOW_WIDTH = screen_data.getWidth() * 80 / 100;
        // LEFT TOP DIV DATA
        double LEFT_TOP_HEIGHT = WINDOW_HEIGHT * 10 / 100;
        double LEFT_TOP_WIDTH = WINDOW_WIDTH * 10 / 100;
        double LEFT_TOP_START_X = WINDOW_WIDTH * 0 / 100;
        double LEFT_TOP_START_Y = WINDOW_HEIGHT * 0 / 100;
        int LEFT_TOP_RED = 45;
        int LEFT_TOP_GREEN = 150;
        int LEFT_TOP_BLUE = 245;
        // LEFT TOP RIGHT DATA
        double RIGHT_TOP_HEIGHT = WINDOW_HEIGHT * 10 / 100;
        double RIGHT_TOP_WIDTH = WINDOW_WIDTH * 90 / 100;
        double RIGHT_TOP_START_X = WINDOW_WIDTH * 10 / 100;
        double RIGHT_TOP_START_Y = WINDOW_HEIGHT * 0 / 100;
        int RIGHT_TOP_RED = 80;
        int RIGHT_TOP_GREEN = 170;
        int RIGHT_TOP_BLUE = 250;
        // LEFT MIDDLE MIDDLE DATA
        double MIDDLE_HEIGHT = WINDOW_HEIGHT * 90 / 100;
        double MIDDLE_WIDTH = WINDOW_WIDTH * 100 / 100;
        double MIDDLE_START_X = WINDOW_WIDTH * 0 / 100;
        double MIDDLE_START_Y = WINDOW_HEIGHT * 10 / 100;
        // RESULT PAGE DATA
        double RESULT_HEIGHT = WINDOW_HEIGHT * 90 / 100;
        double RESULT_WIDTH = WINDOW_WIDTH * 80 / 100;
        double RESULT_START_X = WINDOW_WIDTH * 10 / 100;
        double RESULT_START_Y = WINDOW_HEIGHT * 15 / 100;
        double RESULT_OPACITY = 0.8;
        // EXIT DATA
        double EXIT_HEIGHT = WINDOW_HEIGHT * 90 / 100;
        double EXIT_WIDTH = WINDOW_WIDTH * 80 / 100;
        double EXIT_START_X = WINDOW_WIDTH * 10 / 100;
        double EXIT_START_Y = WINDOW_HEIGHT * 15 / 100;
        // TEXT FIELD DATA ( FOR SEARCH AREA )
        double TEXTFIELD_WIDTH = RIGHT_TOP_WIDTH * 70 / 100;
        double TEXTFIELD_HEIGHT = RIGHT_TOP_HEIGHT * 60 / 100;
        double TEXTFIELD_START_X = LEFT_TOP_WIDTH + WINDOW_WIDTH * 5 / 100;
        double TEXTFIELD_START_Y = LEFT_TOP_HEIGHT * 20 / 100;
        // OPTION DATA
        int OPTION_BOX_WIDTH = 120;
        int OPTION_BOX_START_X = -46;
        int OPTION_BOX_START_Y = -38;
        double OPTION_SCALE_FACTOR = 1.6;
        // SEARCH BUTTON DATA
        int SEARCH_BUTT_START_X = -45;
        int SEARCH_BUTT_START_Y = 8;
        double SEARCH_BUTT_OPACITY = 0.6;
        // OTHER
        double LEFT_TOP_LABEL_OPACITY = 0.9;
        // PROBLEMIMG DATA
        int PROBLEMIMG_WIDTH = 200;
        int PROBLEMIMG_HEIGHT = 200;
        int PROBLEMIMG_MARGIN_TOP = 100;
        double PROBLEMIMG_OPACITY = 0.7;
        // PROBLEM TEXT DATA
        int PROBLEMTEXT_HEIGHT = 40;
        double PROBLEMTEXT_OPACITY = 0.6;
        
        stage.setTitle("Browser 0.1");
        stage.initStyle(StageStyle.UNDECORATED);
        
        group = new Group();
        
        // LEFT TOP DIV
        Rectangle leftTop = new Rectangle();
        leftTop.setX(LEFT_TOP_START_X);
        leftTop.setY(LEFT_TOP_START_Y);
        leftTop.setHeight(LEFT_TOP_HEIGHT);
        leftTop.setWidth(LEFT_TOP_WIDTH);
        leftTop.setFill(Color.rgb(LEFT_TOP_RED, LEFT_TOP_GREEN, LEFT_TOP_BLUE));
        group.getChildren().add(leftTop);
        
        // 'CURRENT BLOCK' TEXT
        String labelContent_leftTop = "Current Block";
        Label leftTopLabel = new Label(labelContent_leftTop);
        leftTopLabel.setFont(Font.font("Sans-serif", FontWeight.SEMI_BOLD, FontPosture.REGULAR, 24));
        leftTopLabel.setTranslateX(4);
        leftTopLabel.setTranslateY(9);
        leftTopLabel.setTextFill(Color.WHITE);
        leftTopLabel.toFront();
        leftTopLabel.setOpacity(LEFT_TOP_LABEL_OPACITY);
        group.getChildren().add(leftTopLabel);
        
        // CURRENT BLOCK NUMBER
        String labelContent2_leftTop = String.valueOf(Mining.LAST_VALIDATED_BLOCK);
        Label leftTopLabel2 = new Label(labelContent2_leftTop);
        leftTopLabel2.setFont(Font.font("Sans-serif", FontWeight.MEDIUM, FontPosture.REGULAR, 40));
        leftTopLabel2.setTranslateX(63);
        leftTopLabel2.setTranslateY(42);
        leftTopLabel2.setTextFill(Color.WHITE);
        leftTopLabel2.toFront();
        leftTopLabel2.setOpacity(LEFT_TOP_LABEL_OPACITY);
        group.getChildren().add(leftTopLabel2);
        
        // RIGHT TOP DIV
        Rectangle rightTop = new Rectangle();
        rightTop.setX(RIGHT_TOP_START_X);
        rightTop.setY(RIGHT_TOP_START_Y);
        rightTop.setHeight(RIGHT_TOP_HEIGHT);
        rightTop.setWidth(RIGHT_TOP_WIDTH);
        rightTop.setFill(Color.rgb(RIGHT_TOP_RED, RIGHT_TOP_GREEN, RIGHT_TOP_BLUE));
        group.getChildren().add(rightTop);
        
        // MIDDLE DIV
        Image middleBg = new Image("2.png");
        Rectangle middle = new Rectangle();
        middle.setX(MIDDLE_START_X);
        middle.setY(MIDDLE_START_Y);
        middle.setHeight(MIDDLE_HEIGHT);
        middle.setWidth(MIDDLE_WIDTH);
        middle.setFill(new ImagePattern(middleBg));
        group.getChildren().add(middle);
        
        // RESULT PAGE IN MIDDLE DIV
        Rectangle result = new Rectangle();
        result.setX(RESULT_START_X);
        result.setY(RESULT_START_Y);
        result.setHeight(RESULT_HEIGHT);
        result.setWidth(RESULT_WIDTH);
        result.setOpacity(RESULT_OPACITY);
        result.setFill(Color.rgb(255, 255, 255));
        group.getChildren().add(result);
        
        // EXIT BUTTON
        Image exitBg = new Image("exit4.png");
        Rectangle exit = new Rectangle();
        exit.setX(WINDOW_WIDTH - 42 );
        exit.setY(2);
        exit.setHeight(40);
        exit.setWidth(40);
        exit.setFill(new ImagePattern(exitBg));
        group.getChildren().add(exit);
        
        // EXIT BUTTON EVENT
        EventHandler<MouseEvent> exitButtonEvent = new EventHandler<MouseEvent>() { 
            @Override 
            public void handle(MouseEvent e) { 
                stage.close();
            } 
        }; 
        exit.addEventFilter(MouseEvent.MOUSE_CLICKED, exitButtonEvent);
        
        // SEARCH BAR
        TextField textField = new TextField("TYPE A DOMAIN OR SET OF WORDS YOU WANT TO LOOK FOR...");
        textField.setMinWidth(TEXTFIELD_WIDTH);
        textField.setMinHeight(TEXTFIELD_HEIGHT);
        textField.setFont(Font.font("Sans-serif", FontWeight.MEDIUM, FontPosture.REGULAR, 26));
        textField.setStyle("-fx-text-inner-color: #BBB;");
        textField.setFocusTraversable(false);
        StackPane stackPane = new StackPane();
        stackPane.setTranslateX(TEXTFIELD_START_X);
        stackPane.setTranslateY(TEXTFIELD_START_Y);
        stackPane.getChildren().add(textField);
        group.getChildren().add(stackPane);
        
        textField.focusedProperty().addListener(new ChangeListener<Boolean>(){
            @Override
            public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue){
                if (newPropertyValue){
                    textField.setText("");
                    textField.setStyle("-fx-text-inner-color: #000;");
                }else{
                    textField.setStyle("-fx-text-inner-color: #AAA;");
                    if(textField.getText().equals("") || textField.getText().equals(" ")){
                        textField.setText("TYPE A DOMAIN OR SET OF WORDS YOU WANT TO LOOK FOR...");
                    }
                }
            }
        });
        
        // SEARCH BUTTON
        Image searchBg = new Image("search.png");
        Rectangle search = new Rectangle();
        search.setX(TEXTFIELD_START_X + TEXTFIELD_WIDTH + SEARCH_BUTT_START_X);
        search.setY(TEXTFIELD_START_Y + SEARCH_BUTT_START_Y);
        search.setOpacity(SEARCH_BUTT_OPACITY);
        search.setHeight(40);
        search.setWidth(40);
        search.setFill(new ImagePattern(searchBg));
        group.getChildren().add(search);
        
        // SEARCH BUTTON EVENT
        EventHandler<MouseEvent> searchButtonEvent = new EventHandler<MouseEvent>() { 
            @Override 
            public void handle(MouseEvent e) { 
                if(Mining.connected){
                    // UPDATE AREA
                    problemImg.setVisible(false);
                    problemText.setVisible(false);
                }else{
                    problemImg.setVisible(true);
                    problemText.setVisible(true);
                }
            } 
        }; 
        search.addEventFilter(MouseEvent.MOUSE_CLICKED, searchButtonEvent);
        
        // PROBLEM IMAGE
        Image problemBg = new Image("problem2.png");
        problemImg = new Rectangle();
        problemImg.setX(MIDDLE_START_X + MIDDLE_WIDTH/2 - PROBLEMIMG_WIDTH/2);
        problemImg.setY(MIDDLE_START_Y + MIDDLE_HEIGHT/2 - PROBLEMIMG_HEIGHT/2 - 100 + PROBLEMIMG_MARGIN_TOP);
        problemImg.setOpacity(PROBLEMIMG_OPACITY);
        problemImg.setHeight(PROBLEMIMG_WIDTH);
        problemImg.setWidth(PROBLEMIMG_HEIGHT);
        problemImg.setFill(new ImagePattern(problemBg));
        problemImg.setVisible(false);
        group.getChildren().add(problemImg);
        
        // PROBLEM TEXT
        String problemTextLabel = "Please fix your connection to the network!";
        problemText = new Label(problemTextLabel);
        problemText.setFont(Font.font("Sans-serif", FontWeight.MEDIUM, FontPosture.REGULAR, 32));
        problemText.setTranslateX(MIDDLE_START_X + MIDDLE_WIDTH*30/100);
        problemText.setTranslateY(MIDDLE_START_Y + MIDDLE_HEIGHT/2 + PROBLEMTEXT_HEIGHT + PROBLEMIMG_MARGIN_TOP);
        problemText.setTextFill(Color.BLACK);
        problemText.setOpacity(PROBLEMTEXT_OPACITY);
        problemText.setVisible(false);
        problemText.toFront();
        group.getChildren().add(problemText);
        
        // OPTIONS FOR SEARCH
        CheckBox option_ascending = new CheckBox("ASC");
        option_ascending.setTranslateX(TEXTFIELD_START_X + TEXTFIELD_WIDTH + OPTION_BOX_START_X + OPTION_BOX_WIDTH);
        option_ascending.setTranslateY(TEXTFIELD_START_Y + TEXTFIELD_HEIGHT + OPTION_BOX_START_Y);
        option_ascending.setTextFill(Color.rgb(255, 255, 255));
        option_ascending.setScaleX(OPTION_SCALE_FACTOR);
        option_ascending.setScaleY(OPTION_SCALE_FACTOR);
        group.getChildren().add(option_ascending);
        
        CheckBox option_descending = new CheckBox("DESC");
        option_descending.setTranslateX(TEXTFIELD_START_X + TEXTFIELD_WIDTH + OPTION_BOX_START_X + OPTION_BOX_WIDTH*2);
        option_descending.setTranslateY(TEXTFIELD_START_Y + TEXTFIELD_HEIGHT + OPTION_BOX_START_Y);
        option_descending.setTextFill(Color.rgb(255, 255, 255));
        option_descending.setScaleX(OPTION_SCALE_FACTOR);
        option_descending.setScaleY(OPTION_SCALE_FACTOR);
        group.getChildren().add(option_descending);
        
        option_ascending.selectedProperty().addListener(new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue){
                option_descending.setSelected(false);
            }
        }
        });
        option_descending.selectedProperty().addListener(new ChangeListener<Boolean>() {
        @Override
        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
            if(newValue){
                option_ascending.setSelected(false);
            }
        }
        });
        
        scene = new Scene(group,WINDOW_WIDTH,WINDOW_HEIGHT);
        stage.setScene(scene);
        stage.show();
    }
}
