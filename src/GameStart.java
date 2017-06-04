import java.io.InputStream;
import java.net.Socket;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.text.*;
import javafx.scene.text.Font;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.*;
import javafx.geometry.*;
import javafx.event.*;
import javafx.scene.effect.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.util.Random;
import javafx.scene.input.*;
import javafx.scene.control.ToggleGroup;

public class GameStart extends Application {

    int width = 600;
    int height = 600;
    String msg;
    Thread th;
    Socket socket;
    String[] online;
    private static String name;
    private static String[] on;
    public static boolean xIsNext = true;
    public static boolean oIsNext = false;
    //Game Scene Buttons
    Button btnNew = new Button("New Game");
    Button btnSave = new Button("Save Game");
    Button btnQuit = new Button("Quit");
    int[] pos=new int[9];
    String[] move=new String[9];
    public static int count=0;
    private Stage primaryStage;
    private char whoseTurn = 'X';
    private Cell[][] cell = new Cell[3][3];
    private Label lblStatus = new Label("X's turn to play");
    ClientController clientController = new ClientController();
    public static final ObservableList names = FXCollections.observableArrayList();
    public static final ObservableList data = FXCollections.observableArrayList();
    
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        grid.setId("grid");

        //Create loginScene with all components in grid
        Scene sceneLogin = new Scene(grid, width, height);
        sceneLogin.getStylesheets().add(getClass().getResource("styles/main.css").toString());

        //Load Fonts
        Font.loadFont(GameStart.class.getResource("fonts/New_Rocker/NewRocker-Regular.ttf").toExternalForm(), 20);
        Font.loadFont(GameStart.class.getResource("fonts/Jolly_Lodger/JollyLodger-Regular.ttf").toExternalForm(), 20);

        //Reflection "welcome"
        Reflection reflection = new Reflection();
        reflection.setFraction(0.5);

        //title Welcome
        Text scenetitle = new Text("Welcome To X-O");
        scenetitle.setId("welcome");
        scenetitle.setEffect(reflection);
        grid.setHalignment(scenetitle, HPos.CENTER);
        grid.add(scenetitle, 0, 0, 2, 1);

        //username label
        Label userName = new Label("Name:");
        userName.setId("userName");
        grid.add(userName, 0, 3);

        //username textfield
        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 3);

        //RadioButtons
        final String[] names = new String[]{"Vs Computer", "Vs Friend Offline", "Vs Friend Online"};
        final RadioButton[] rbs = new RadioButton[names.length];
        final ToggleGroup group = new ToggleGroup();

        //LoadImages
        InputStream is1 = getClass().getResourceAsStream("/img/offline.jpg");
        Image img1 = new Image(is1);
        InputStream is2 = getClass().getResourceAsStream("/img/online.png");
        Image img2 = new Image(is2);
        InputStream is3 = getClass().getResourceAsStream("/img/com.png");
        Image img3 = new Image(is3);

        RadioButton rb1 = new RadioButton("Vs Friend Offline");
        rb1.setToggleGroup(group);
        //rb1.setSelected(true);
        rb1.setId("rbBtn");
        rb1.setGraphic(new ImageView(img1));
        grid.add(rb1, 1, 7);

        RadioButton rb2 = new RadioButton("Vs Friend Online");
        rb2.setToggleGroup(group);
        rb2.setId("rbBtn");
        rb2.setGraphic(new ImageView(img2));
        grid.add(rb2, 1, 8);

        RadioButton rb3 = new RadioButton("Vs AI Computer");
        rb3.setToggleGroup(group);
        rb3.setId("rbBtn");
        rb3.setGraphic(new ImageView(img3));
        grid.add(rb3, 1, 9);

        //GameStart button
        Button btn = new Button("Play");
        btn.setId("hidebtn");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.CENTER);
        hbBtn.getChildren().add(btn);
        hbBtn.setId("btn");
        grid.add(hbBtn, 1, 10);

        group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            @Override
            public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {
                // Cast object to radio button
                RadioButton selectedRadio = (RadioButton) t1.getToggleGroup().getSelectedToggle();
                System.out.println("Selected Radio Button - " + selectedRadio.getText());

                //play button
                btn.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent event) {
                        
                        
                        
                        if (selectedRadio.getText() == "Vs Friend Offline") {
                            System.out.println("TicTacToeOffLine");
                            TicTacToeOffLine game1;
                            game1 = new TicTacToeOffLine();
                            Scene sceneGame1 = game1.getScene();
                            primaryStage.setScene(sceneGame1);
                        } else if (selectedRadio.getText() == "Vs Friend Online") {
                            System.out.println("send here");
                            String name = userTextField.getText();
                            String onmsg = "startonline:" + name;
                            clientController.send(onmsg);
                            Platform.runLater(() -> {
                                System.out.println("Vs Friend Online");
                            TicTacToeOnLine game2;
                            game2 = new TicTacToeOnLine();
                            Scene sceneGame2 = game2.getScene();
                            primaryStage.setScene(sceneGame2);
                            });
                            
                        } else if (selectedRadio.getText() == ("Vs AI Computer")) {
                            System.out.println("Vs AI Computer");
                            TicTacToeAI game3;
                            game3 = new TicTacToeAI();
                            Scene sceneGame3 = game3.getScene();
                            primaryStage.setScene(sceneGame3);
                        }
                    }
                });
            }
        });

        //new game button
        btnNew.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                primaryStage.setTitle("Tic-Tac-Toe GameStart");
                primaryStage.setScene(sceneLogin);
            }
        });
        //save game button
        btnSave.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                // save actions 
            }
        });
        //exit game button
        btnQuit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Platform.exit();
            }
        });

        primaryStage.setScene(sceneLogin);
        primaryStage.setTitle("Tic-Tac-Toe GameStart");
        primaryStage.show();
    }

    public class TicTacToeOffLine {

        private char whoseTurn = 'X';
        private Cell[][] cell = new Cell[3][3];
        private Label lblStatus = new Label("X's turn to play");
        private Video myvideo = new Video();
        public GridPane pane;

        public Scene getScene() {

            lblStatus.setId("lbl");
            lblStatus.setPrefWidth(600);

            pane = new GridPane();
            pane.setId("scn");

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    pane.add(cell[i][j] = new Cell(), j, i);
                }
            }

            BorderPane borderPane = new BorderPane();
            ToolBar toolbar = new ToolBar();
            toolbar.setId("toolbar");

            //ToolBar of Buttons
            toolbar.getItems().addAll(
                    btnNew,
                    btnSave,
                    btnQuit
            );
            borderPane.setTop(toolbar);
            borderPane.setCenter(pane);
            borderPane.setBottom(lblStatus);

            // Create a scene and place it in the stage
            Scene scene = new Scene(borderPane, 600, 600, Color.BEIGE);
            scene.getStylesheets().add(getClass().getResource("styles/main.css").toString());

            return scene;
        }

        /**
         * Determine if the cell are all occupied
         */
        public boolean isFull() {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (cell[i][j].getToken() == ' ') {
                        return false;
                    }
                }
            }

            return true;
        }

        /**
         * Determine if the player with the specified token wins
         */
        public boolean isWon(char token) {
            for (int i = 0; i < 3; i++) {
                if (cell[i][0].getToken() == token && cell[i][1].getToken() == token && cell[i][2].getToken() == token) {
                    return true;
                }
            }

            for (int j = 0; j < 3; j++) {
                if (cell[0][j].getToken() == token && cell[1][j].getToken() == token && cell[2][j].getToken() == token) {
                    return true;
                }
            }

            if (cell[0][0].getToken() == token && cell[1][1].getToken() == token && cell[2][2].getToken() == token) {
                return true;
            }

            if (cell[0][2].getToken() == token && cell[1][1].getToken() == token && cell[2][0].getToken() == token) {
                return true;
            }

            return false;
        }

        // An inner class for a cell
        public class Cell extends Pane {

            // Token used for this cell

            private char token = ' ';

            public Cell() {
                setStyle("-fx-border-color: CHOCOLATE");
                this.setPrefSize(200, 200);
                this.setOnMouseClicked(e -> handleMouseClick());
            }

            /**
             * Return token
             */
            public char getToken() {
                return token;
            }

            /**
             * Set a new token
             */
            public void setToken(char c) {
                token = c;

                if (token == 'X') {
                    Line line1 = new Line(15, 15, this.getWidth() - 15, this.getHeight() - 15);
                    line1.endXProperty().bind(this.widthProperty().subtract(15));
                    line1.endYProperty().bind(this.heightProperty().subtract(15));
                    line1.setStrokeWidth(8);
                    line1.setStroke(Color.BROWN);

                    Line line2 = new Line(15, this.getHeight() - 15, this.getWidth() - 15, 15);
                    line2.startYProperty().bind(this.heightProperty().subtract(15));
                    line2.endXProperty().bind(this.widthProperty().subtract(15));
                    line2.setStrokeWidth(8);
                    line2.setStroke(Color.BROWN);

                    // Add the lines to the pane
                    this.getChildren().addAll(line1, line2);
                } else if (token == 'O') {
                    Ellipse ellipse = new Ellipse(this.getWidth() / 2, this.getHeight() / 2, this.getWidth() / 2 - 15, this.getHeight() / 2 - 15);
                    ellipse.centerXProperty().bind(this.widthProperty().divide(2));
                    ellipse.centerYProperty().bind(this.heightProperty().divide(2));
                    ellipse.radiusXProperty().bind(this.widthProperty().divide(2).subtract(15));
                    ellipse.radiusYProperty().bind(this.heightProperty().divide(2).subtract(15));
                    ellipse.setStrokeWidth(4);
                    ellipse.setStroke(Color.BLUE);
                    ellipse.setFill(Color.CYAN);

                    // Add the ellipse to the pane
                    getChildren().add(ellipse);
                }
            }

            /* Handle a mouse click event */
            public void handleMouseClick() {
                // If cell is empty and game is not over
                if (token == ' ' && whoseTurn != ' ') {
                    // Set token in the cell
                    setToken(whoseTurn);

                    // Check game status
                    if (isWon(whoseTurn)) {
                        lblStatus.setText(whoseTurn + " won! The game is over");
                        whoseTurn = ' '; // Game is over
                        Scene sceneVideo = myvideo.winnerVideo();
                        Platform.runLater(() -> {
                            primaryStage.setScene(sceneVideo);
                        });

                    } else if (isFull()) {
                        lblStatus.setText("Draw! The game is over");
                        whoseTurn = ' '; // Game is over
                        Scene sceneVideo = myvideo.loserVideo();
                        Platform.runLater(() -> {
                            primaryStage.setScene(sceneVideo);
                        });

                    } else {
                        // Change the turn
                        whoseTurn = (whoseTurn == 'X') ? 'O' : 'X';
                        // Display whose turn
                        lblStatus.setText(whoseTurn + "'s turn");
                    }
                }
            }
        }
    }

    public class TicTacToeOnLine {

        private char whoseTurn0 = 'X';
        private Cell[][] cell = new Cell[3][3];
        private Label lblStatus0 = new Label("X's turn to play");
        private Video myvideo = new Video();
        GridPane pane0;
        public Scene getScene() {
            name = clientController.getOnline();
            lblStatus0.setId("lbl0");
            lblStatus0.setPrefWidth(600);

            pane0 = new GridPane();
            pane0.setId("scn");

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    pane0.add(cell[i][j] = new Cell(), j, i);
                }
            }

            BorderPane borderPane0 = new BorderPane();

            final ListView listView = new ListView(data);
            listView.setId("listView");
            listView.setPrefSize(150, 100);
            listView.setEditable(true);
            on = name.split(":");
            for (int i = 1; i < on.length; i++) {
                names.add(on[i]);
            }
   

            listView.setItems(names);

            listView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
                public void changed(ObservableValue<? extends String> ov,
                        String old_val, String new_val) {
                    //get selected data from list
                    System.out.println(old_val);
                    System.out.println(new_val);

                }
            });

            //ToolBar of Buttons
            ToolBar toolbar0 = new ToolBar();
            toolbar0.setId("toolbar");
            toolbar0.getItems().addAll(
                    btnNew,
                    btnSave,
                    btnQuit
            );
            borderPane0.setTop(toolbar0);
            borderPane0.setRight(listView);
            borderPane0.setCenter(pane0);
            borderPane0.setBottom(lblStatus0);

            Scene scene0 = new Scene(borderPane0, 600, 600, Color.BEIGE);
            scene0.getStylesheets().add(getClass().getResource("styles/main.css").toString());

            return scene0;
        }

        /**
         * Determine if the cell are all occupied
         */
        public boolean isFull() {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (cell[i][j].getToken() == ' ') {
                        return false;
                    }
                }
            }

            return true;
        }

        /**
         * Determine if the player with the specified token wins
         */
        public boolean isWon(char token) {
            for (int i = 0; i < 3; i++) {
                if (cell[i][0].getToken() == token && cell[i][1].getToken() == token && cell[i][2].getToken() == token) {
                    return true;
                }
            }

            for (int j = 0; j < 3; j++) {
                if (cell[0][j].getToken() == token && cell[1][j].getToken() == token && cell[2][j].getToken() == token) {
                    return true;
                }
            }

            if (cell[0][0].getToken() == token && cell[1][1].getToken() == token && cell[2][2].getToken() == token) {
                return true;
            }

            if (cell[0][2].getToken() == token && cell[1][1].getToken() == token && cell[2][0].getToken() == token) {
                return true;
            }

            return false;
        }

        // An inner class for a cell
        public class Cell extends Pane {

            // Token used for this cell

            private char token = ' ';

            public Cell() {
                setStyle("-fx-border-color: CHOCOLATE");
                this.setPrefSize(200, 200);
                this.setOnMouseClicked(e -> handleMouseClick());
            }

            /**
             * Return token
             */
            public char getToken() {
                return token;
            }

            
          
            public void setToken(char c) {
                token = c;

                if (token == 'X'&& xIsNext) {
                    Line line1 = new Line(15, 15, this.getWidth() - 15, this.getHeight() - 15);
                    Line line2 = new Line(15, this.getHeight() - 15, this.getWidth() - 15, 15);
                    String msgx = "x:" + (this.getWidth() - 15) + ":" + (this.getHeight() - 15);
                    pos[count]=pane0.getChildren().indexOf(this);
                    move[count]="x";
                    count++;
                    clientController.send(msgx);
                    oIsNext=true;
                    
                    Platform.runLater(() -> {
                        line1.endXProperty().bind(this.widthProperty().subtract(15));
                        line1.endYProperty().bind(this.heightProperty().subtract(15));
                        line1.setStrokeWidth(8);
                        line1.setStroke(Color.BROWN);

                        line2.startYProperty().bind(this.heightProperty().subtract(15));
                        line2.endXProperty().bind(this.widthProperty().subtract(15));
                        line2.setStrokeWidth(8);
                        line2.setStroke(Color.BROWN);
                    });

                    // Add the lines to the pane
                    this.getChildren().addAll(line1, line2);
                } else if (token == 'O'&& oIsNext) {
                    Ellipse ellipse = new Ellipse(this.getWidth() / 2, this.getHeight() / 2, this.getWidth() / 2 - 15, this.getHeight() / 2 - 15);
                    ClientController clientController = new ClientController();
                    String msgo = "o:" + (this.getWidth() - 15) + ":" + (this.getHeight() - 15);
                    clientController.send(msgo);
                    pos[count]=pane0.getChildren().indexOf(this);
                    move[count]="o";
                    count++;
                    xIsNext=true;
                    Platform.runLater(() -> {
                        ellipse.centerXProperty().bind(this.widthProperty().divide(2));
                        ellipse.centerYProperty().bind(this.heightProperty().divide(2));
                        ellipse.radiusXProperty().bind(this.widthProperty().divide(2).subtract(15));
                        ellipse.radiusYProperty().bind(this.heightProperty().divide(2).subtract(15));
                        ellipse.setStrokeWidth(4);
                        ellipse.setStroke(Color.BLUE);
                        ellipse.setFill(Color.CYAN);

                        // Add the ellipse to the pane
                        getChildren().add(ellipse);
                    });
                }
            }

            /* Handle a mouse click event */
            private void handleMouseClick() {
                // If cell is empty and game is not over
                if (token == ' ' && whoseTurn != ' ') {
                    // Set token in the cell
                    setToken(whoseTurn);

                    // Check game status
                    if (isWon(whoseTurn)) {
                        lblStatus0.setText(whoseTurn + " won! The game is over");
                        Scene sceneVideo2 = myvideo.winnerVideo();
                        Platform.runLater(() -> {
                            primaryStage.setScene(sceneVideo2);
                            whoseTurn = ' '; // Game is over
                        });
                    } else if (isFull()) {
                        lblStatus0.setText("Draw! The game is over");
                        Scene sceneVideo2 = myvideo.loserVideo();
                        Platform.runLater(() -> {
                            primaryStage.setScene(sceneVideo2);
                        });
                        whoseTurn = ' '; // Game is over
                    } else {
                        // Change the turn
                        whoseTurn = (whoseTurn == 'X') ? 'O' : 'X';
                        // Display whose turn
                        lblStatus.setText(whoseTurn + "'s turn");
                    }
                }
            }
        }
    }

    public class TicTacToeAI {

        private Video myvideo = new Video();
        static final char BLANK = ' ', O = 'O', X = 'X';
        public char position[] = { // Board position (BLANK, O, or X)
            BLANK, BLANK, BLANK,
            BLANK, BLANK, BLANK,
            BLANK, BLANK, BLANK};
        public int wins = 0, losses = 0, draws = 0;  // game count by user
        public Random random = new Random();
        public int rows[][] = {{0, 2}, {3, 5}, {6, 8}, {0, 6}, {1, 7}, {2, 8}, {0, 8}, {2, 6}};

        private char whoseTurn = 'X';
        private Cell[][] cell = new Cell[3][3];
        private Label lblStatus = new Label("X's turn to play");
        //public Video myvideo = new Video();
        public GridPane pane;

        public Scene getScene() {

            lblStatus.setId("lbl");
            lblStatus.setPrefWidth(600);

            pane = new GridPane();
            pane.setId("scn");

            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    pane.add(cell[i][j] = new Cell(), j, i);
                }
            }

            BorderPane borderPane = new BorderPane();
            ToolBar toolbar = new ToolBar();
            toolbar.setId("toolbar");

            //ToolBar of Buttons
            toolbar.getItems().addAll(
                    btnNew,
                    btnSave,
                    btnQuit
            );
            borderPane.setTop(toolbar);
            borderPane.setCenter(pane);
            borderPane.setBottom(lblStatus);

            // Create a scene and place it in the stage
            Scene scene = new Scene(borderPane, 600, 600, Color.BEIGE);
            scene.getStylesheets().add(getClass().getResource("styles/main.css").toString());

            return scene;
        }

        /**
         * Determine if the cell are all occupied
         */
        public boolean isFull() {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (cell[i][j].getToken() == ' ') {
                        return false;
                    }
                }
            }

            return true;
        }

        /**
         * Determine if the player with the specified token wins
         */
        public boolean isWon(char token) {
            for (int i = 0; i < 3; i++) {
                if (cell[i][0].getToken() == token && cell[i][1].getToken() == token && cell[i][2].getToken() == token) {
                    return true;
                }
            }

            for (int j = 0; j < 3; j++) {
                if (cell[0][j].getToken() == token && cell[1][j].getToken() == token && cell[2][j].getToken() == token) {
                    return true;
                }
            }

            if (cell[0][0].getToken() == token && cell[1][1].getToken() == token && cell[2][2].getToken() == token) {
                return true;
            }

            if (cell[0][2].getToken() == token && cell[1][1].getToken() == token && cell[2][0].getToken() == token) {
                return true;
            }

            return false;
        }

        // An inner class for a cell
        public class Cell extends Pane {

            // Token used for this cell

            private char token = ' ';

            public Cell() {
                setStyle("-fx-border-color: CHOCOLATE");
                this.setPrefSize(200, 200);
                this.setOnMouseClicked(e -> handleMouseClick());
            }

            /**
             * Return token
             */
            public char getToken() {
                return token;
            }

            /**
             * Set a new token
             */
            public void setToken(char c) {
                token = c;

                if (token == 'X') {
                    Line line1 = new Line(15, 15, this.getWidth() - 15, this.getHeight() - 15);
                    line1.endXProperty().bind(this.widthProperty().subtract(15));
                    line1.endYProperty().bind(this.heightProperty().subtract(15));
                    line1.setStrokeWidth(8);
                    line1.setStroke(Color.BROWN);

                    Line line2 = new Line(15, this.getHeight() - 15, this.getWidth() - 15, 15);
                    line2.startYProperty().bind(this.heightProperty().subtract(15));
                    line2.endXProperty().bind(this.widthProperty().subtract(15));
                    line2.setStrokeWidth(8);
                    line2.setStroke(Color.BROWN);

                    // Add the lines to the pane
                    this.getChildren().addAll(line1, line2);
                    //get the index of played X
                    int indx = pane.getChildren().indexOf(this);
                    position[indx] = 'X';

                } else if (token == 'O') {

                    Ellipse ellipse = new Ellipse(this.getWidth() / 2, this.getHeight() / 2, this.getWidth() / 2 - 15, this.getHeight() / 2 - 15);
                    ellipse.centerXProperty().bind(this.widthProperty().divide(2));
                    ellipse.centerYProperty().bind(this.heightProperty().divide(2));
                    ellipse.radiusXProperty().bind(this.widthProperty().divide(2).subtract(15));
                    ellipse.radiusYProperty().bind(this.heightProperty().divide(2).subtract(15));
                    ellipse.setStrokeWidth(4);
                    ellipse.setStroke(Color.BLUE);
                    ellipse.setFill(Color.CYAN);

                    // Add the ellipse to the pane
                    this.getChildren().add(ellipse);
                }
            }

            /* Handle a mouse click event */
            public void handleMouseClick() {
                // If cell is empty and game is not over
                if (token == ' ' && whoseTurn != ' ') {
                    // Set token in the cell
                    setToken(whoseTurn);

                    // Check game status
                    if (isWon(whoseTurn)) {
                        lblStatus.setText(whoseTurn + " won! The game is over");
                        whoseTurn = ' '; // Game is over
                        Scene sceneVideo = myvideo.winnerVideo();
                        Platform.runLater(() -> {
                            primaryStage.setScene(sceneVideo);
                        });

                    } else if (isFull()) {
                        lblStatus.setText("Draw! The game is over");
                        whoseTurn = ' '; // Game is over
                        Scene sceneVideo = myvideo.loserVideo();
                        Platform.runLater(() -> {
                            primaryStage.setScene(sceneVideo);
                        });

                    } else {
                        // Change the turn
                        whoseTurn = (whoseTurn == 'X') ? 'O' : 'X';
                        // Display whose turn
                        lblStatus.setText(whoseTurn + "'s turn");

                        if (whoseTurn == 'O') {
                            MouseEvent mouseEvent = new MouseEvent(MouseEvent.MOUSE_CLICKED, 1, 2, 3, 4, MouseButton.PRIMARY, 5, true, true, true, true, true, true, true, true, true, true, null);
                            pane.getChildren().get(nextMove()).fireEvent(mouseEvent);
                        }
                    }
                }

            }
        }

        // Return true if player has won
        boolean won(char player) {
            for (int i = 0; i < 8; ++i) {
                if (testRow(player, rows[i][0], rows[i][1])) {
                    return true;
                }
            }
            return false;
        }

        // Has player won in the row from position[a] to position[b]?
        boolean testRow(char player, int a, int b) {
            return position[a] == player && position[b] == player && position[(a + b) / 2] == player;
        }

        // Play X in the best place
        int nextMove() {
            int r = findRow(O);  // complete a row of O and win if possible
            if (r < 0) {
                r = findRow(X);  // or try to block X from winning
            }
            if (r < 0) {  // otherwise move randomly
                do {
                    r = random.nextInt(9);
                } while (position[r] != BLANK);
            }
            position[r] = O;
            return r;
        }

		// Return 0-8 for the position of a blank spot in a row if the
        // other 2 spots are occupied by player, or -1 if no spot exists
        int findRow(char player) {
            for (int i = 0; i < 8; ++i) {
                int result = find1Row(player, rows[i][0], rows[i][1]);
                if (result >= 0) {
                    return result;
                }
            }
            return -1;
        }

        int find1Row(char player, int a, int b) {
            int c = (a + b) / 2;  // middle spot
            if (position[a] == player && position[b] == player && position[c] == BLANK) {
                return c;
            }
            if (position[a] == player && position[c] == player && position[b] == BLANK) {
                return b;
            }
            if (position[b] == player && position[c] == player && position[a] == BLANK) {
                return a;
            }
            return -1;
        }

        // Are all 9 spots filled?
        boolean isDraw() {
            for (int i = 0; i < 9; ++i) {
                if (position[i] == BLANK) {
                    return false;
                }
            }
            return true;
        }
    }

    public class Video {

        public Scene winnerVideo() {
            StackPane root = new StackPane();
            MediaPlayer player = new MediaPlayer(new Media(getClass().getResource("videos/Drum.mp4").toExternalForm()));
            MediaView mediaView = new MediaView(player);
            root.getChildren().add(mediaView);
            Scene scene = new Scene(root, 600, 600);
            player.play();
            return scene;
        }

        public Scene loserVideo() {
            StackPane root = new StackPane();
            MediaPlayer player = new MediaPlayer(new Media(getClass().getResource("videos/Loser.mp4").toExternalForm()));
            MediaView mediaView = new MediaView(player);
            root.getChildren().add(mediaView);
            Scene scene = new Scene(root, 600, 600);
            player.play();
            return scene;
        }
    }

}
