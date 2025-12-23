import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Arrays;
import java.util.InputMismatchException;

public class Interface extends Application {
    @Override
    public void start(Stage stage){
        Log logger = new Log();

        Group group = new Group();
        Group labelGroup = new Group();
        Group textFieldGroup = new Group();
        Group buttonGroup = new Group();

        Label primeNumberLabel = new Label("Prime:");
        Label numberLabel = new Label("Number:");
        Label powerLabel = new Label("Power:");
        TextField primeTF = new TextField();
        TextField numberTF = new TextField();
        TextField powerTF = new TextField();
        Button startButton = new Button("Check!");
        TextArea resultTextArea = new TextArea();
        TextArea logTextArea = new TextArea();

        primeNumberLabel.setLayoutX(10);
        primeNumberLabel.setLayoutY(10);
        primeNumberLabel.setFont(Font.font("Consolas",18));
        labelGroup.getChildren().add(primeNumberLabel);

        powerLabel.setLayoutX(250);
        powerLabel.setLayoutY(10);
        powerLabel.setFont(Font.font("Consolas",18));
        labelGroup.getChildren().add(powerLabel);

        numberLabel.setLayoutX(440);
        numberLabel.setLayoutY(10);
        numberLabel.setFont(Font.font("Consolas",18));
        labelGroup.getChildren().add(numberLabel);

        primeTF.setLayoutX(80);
        primeTF.setLayoutY(10);
        primeTF.setPromptText("Prime number...");
        primeTF.setPrefSize(150,20);
        textFieldGroup.getChildren().add(primeTF);

        powerTF.setLayoutX(320);
        powerTF.setLayoutY(10);
        powerTF.setPromptText("Power...");
        powerTF.setPrefSize(100,20);
        textFieldGroup.getChildren().add(powerTF);

        numberTF.setLayoutX(520);
        numberTF.setLayoutY(10);
        numberTF.setPromptText("Number...");
        numberTF.setPrefSize(150,20);
        textFieldGroup.getChildren().add(numberTF);

        startButton.setLayoutX(700);
        startButton.setLayoutY(10);
        startButton.setPrefSize(70,20);
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    int inputPrime = Integer.parseInt(primeTF.getText());
                    if (inputPrime>2 && inputPrime % 2 == 0) {
                        throw new Exception("Given number is even");
                    }
                    if(inputPrime>Math.pow(2,30)-1 || inputPrime < 2){
                        throw new InputMismatchException();
                    }

                    int inputNumber = ((Integer.parseInt(numberTF.getText())%inputPrime)+inputPrime)%inputPrime;
                    if(inputNumber>Math.pow(2,30)-1){
                        throw new InputMismatchException();
                    }

                    int inputPower = Integer.parseInt(powerTF.getText());
                    if (inputPower<2 || inputPower>16) {
                        throw new Exception("Incorrect power");
                    }

                    FiniteField finiteField = new FiniteField(inputPrime);
                    Backend b = new Backend(inputPrime,inputNumber,inputPower);

                    if(finiteField.residueCheck(inputNumber,b.getD())!=1){
                        throw new Exception("This number is not residue of power "+inputPower+".");
                    }

                    logger.initialEntry();

                    logger.writeLog("Prime number: "+inputPrime,true);
                    logger.writeLog("Number: "+inputNumber,true);
                    logger.writeLog("x^"+inputPower+" = "+inputNumber+" (mod "+inputPrime+")",true);

                    int result = b.run();
                    if(result==-1){
                        resultTextArea.setText(resultTextArea.getText()+"\nNo result for "
                                +"x^"+inputPower+" = "+inputNumber+" (mod "+inputPrime+")");
                    }else {
                        int[] solution = new int[b.getD()];
                        solution[0]=result;
                        int order = b.getOrder();

                        for(int i=1;i < solution.length;i++){
                            solution[i]=(solution[i-1]*order)%inputPrime;
                        }

                        resultTextArea.setText(resultTextArea.getText() + ("\nRESULT: " + "X=" + Arrays.toString(solution)
                                + "; " + result + "^"+inputPower+" = " + inputNumber + " (mod " + inputPrime + ")"));
                    }
                    String[] text=logger.test();
                    logTextArea.setText("");
                    if (text!=null){
                        for(String s : text) {
                            logTextArea.appendText(s + "\n");
                        }
                    }
                    logTextArea.setScrollTop(Double.MAX_VALUE);
                }catch (InputMismatchException e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("InputMismatchException");
                    alert.setContentText("Given number is lower than 2 or greater than 2^30-1 or not a number.");
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Error!");
                    alert.setContentText(String.valueOf(e));
                    alert.showAndWait().ifPresent(rs -> {
                        if (rs == ButtonType.OK) {
                            System.out.println("Pressed OK.");
                        }
                    });
                }
            }
        });
        buttonGroup.getChildren().add(startButton);

        resultTextArea.setLayoutX(10);
        resultTextArea.setLayoutY(50);
        resultTextArea.setMinHeight(100);
        resultTextArea.setMaxHeight(100);
        resultTextArea.setMinWidth(765);
        resultTextArea.setMaxWidth(765);
        resultTextArea.setWrapText(true);
        resultTextArea.setEditable(false);
        resultTextArea.setFont(Font.font("Consolas", 16));
        resultTextArea.end();
        textFieldGroup.getChildren().add(resultTextArea);

        logTextArea.setLayoutX(10);
        logTextArea.setLayoutY(170);
        logTextArea.setMinHeight(350);
        logTextArea.setMaxHeight(350);
        logTextArea.setMinWidth(765);
        logTextArea.setMaxWidth(765);
        logTextArea.setWrapText(false);
        logTextArea.setEditable(false);
        logTextArea.setFont(Font.font("Consolas", 14));
        logTextArea.end();
        textFieldGroup.getChildren().add(logTextArea);

        //initial parse
        String[] text = null;
        try {
            text = logger.test();
        } catch (IOException e) {
            System.out.println(e);
        }
        logTextArea.setText("");
        if (text!=null){
            for(String s : text) {
                logTextArea.appendText(s + "\n");
            }
        }
        logTextArea.setScrollTop(Double.MAX_VALUE);
        //

        group.getChildren().addAll(labelGroup,textFieldGroup,buttonGroup);

        Scene scene = new Scene(group, Color.rgb(245,245,245));
        stage.setScene(scene);
        stage.setTitle("Tonelli-Shanks");
        stage.setWidth(800);
        stage.setHeight(600);
        stage.setResizable(false);
        stage.show();
    }

    public static void show(){
        Application.launch();
    }
}
