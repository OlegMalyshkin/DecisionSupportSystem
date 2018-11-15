package GUI;

import com.googlecode.fannj.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * List of parameters:
 * @param lengthOfRunway the size of the runway, stopped behind after a start in the moment of decision-making
 * @param countOfEngines the number of working engines (at least three)
 * @param angleofFlap  flap deviation angle (for runways covered with artificial surface
 * @param conditionOfRunway  condition of the runway.
 *
 */

public class MainViewController {

    @FXML private TextField txtRunway;
    @FXML private RadioButton rBtnArtificialRunway;
    @FXML private RadioButton rBtnSnowRunway;
    @FXML private TextField txtAngleOfFlap;
    @FXML private ComboBox<String> comboBox;
    @FXML private Button btnDecision;
    @FXML private TextField txtAnswer;
    private final ToggleGroup group = new ToggleGroup();
    private Integer countOfEngines;
    private Integer conditionOfRunway;
    private Integer angleofFlap;
    private Integer lengthOfRunway;

    @FXML
    public void initialize(){
        String path = System.getProperty("user.dir") + "/resources/libfann/FANN-2.2.0-Source/bin/";
        System.setProperty("jna.library.path", path);
        File file = new File(System.getProperty("jna.library.path") + "fannfloat.dll");
        System.load(file.getAbsolutePath());
        rBtnArtificialRunway.setToggleGroup(group);
        rBtnSnowRunway.setToggleGroup(group);
        comboBox.getItems().addAll("4", "3", "2", "1", "Всі двигуни відмовили");
        comboBox.setValue(comboBox.getItems().get(0));
        rBtnArtificialRunway.setSelected(true);
    }

    private void setCountOfGoodEngines(){
        switch (comboBox.getValue())
        {
            case "4": countOfEngines = 4;
                break;
            case "3": countOfEngines = 3;
                break;
            case "2": countOfEngines = 2;
                break;
            case "1": countOfEngines = 1;
                break;
            case "Всі двигуни відмовили": countOfEngines = 0;
                break;
        }
    }

    private void setAngleofFlap(){
        try{
            angleofFlap = Integer.valueOf(txtAngleOfFlap.getText());
        } catch (NumberFormatException e){
            showError("Перевірте дані, потрібно ввести чило");
            angleofFlap = 0;
        }
    }

    private void setLengthOfRunway(){
        try{
            lengthOfRunway = Integer.valueOf(txtRunway.getText());
        } catch (NumberFormatException e){
            showError("Перевірте дані, потрібно ввести чило");
            lengthOfRunway = 0;
        }
    }

    private void setStateOfRunway(){
        if(rBtnSnowRunway.isSelected()){
            conditionOfRunway = 873;
        } else {
            conditionOfRunway = 273;
        }
    }

    @FXML
    private void btnDecisionOnAction() {
        File file = new File(System.getProperty("user.dir") + "/resources/knowledge");
        if(file.exists()) {
            Fann fann = new Fann(System.getProperty("user.dir") + "/resources/knowledge");
            setAngleofFlap();
            setCountOfGoodEngines();
            setLengthOfRunway();
            setStateOfRunway();
            float[][] tests = {{lengthOfRunway, countOfEngines, conditionOfRunway, angleofFlap}};
            for (float[] test : tests) {
                txtAnswer.setText(makeDecision(fann.run(test)));
               //System.out.println(lengthOfRunway+" "+countOfEngines +" "+stateOfRunway+" "+angleofFlap);
            }
        } else {
            showError("Спочатку почніть тренування\nФайл -> Почати тренування");
        }

    }

    @FXML
    void menuItemStartStudingOnAction() {
        startStuding();
    }

    @FXML
    void menuItemExitOnAction() {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void menuItemAboutOnAction() {
        showAbout();
    }

    private static String makeDecision(float[] out){
        int i = 0;
        for (int j = 1; j < 2; j++) {
            if(out[i]<out[j]){
                i = j;
            }
        }
        switch (i){
            case 0: return "ЗЛІТАТИ";
            case 1: return "НЕ ЗЛІТАТИ";
        }
        return "";
    }

    private void startStuding(){
        File file = new File(System.getProperty("user.dir") + "/resources/lesson.data");
        if(file.exists()) {
            Platform.runLater(() -> {
                List<Layer> layerList = new ArrayList<>();
                layerList.add(Layer.create(4, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
                layerList.add(Layer.create(11, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
                layerList.add(Layer.create(2, ActivationFunction.FANN_SIGMOID_SYMMETRIC, 0.01f));
                Fann fann = new Fann(layerList);
                //Создаем тренера и определяем алгоритм обучения
                Trainer trainer = new Trainer(fann);
                trainer.setTrainingAlgorithm(TrainingAlgorithm.FANN_TRAIN_RPROP);
                /* Проведем обучение взяв уроки из файла, с максимальным колличеством
                   циклов 10000, показывая отчет каждую 100ю итерацию и добиваемся
                   ошибки меньше 0.0001 */
                trainer.train(new File(System.getProperty("user.dir") + "/resources/lesson.data").getAbsolutePath(), 10000, 100, 0.0001f);
                fann.save(System.getProperty("user.dir") + "/resources/knowledge");
            });
        } else {
            showError("В папці " + System.getProperty("user.dir") + "/resources" + " відсутній файл lesson.data");
        }
    }

    private void showAbout(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Про програму");
        alert.setHeaderText("Система допомоги прийняття рішення Ан-12");
        alert.setContentText(
                "Однією з найнебезпечніших ділянок польоту є зліт. \nВиникає нестача часу для оцінки проблеми, що виникла,\nта її вирішення.\n" +
                        "Початковими даними є \n" +
                        "- величина злітно-посадкової смуги, що лишилась після старту\nв момент прийняття рішення (не менше 500 м);\n" +
                        "- кількість робочих двигунів (не менше трьох);\n" +
                        "- кут відхилення закрилок (для ЗПС, що покрита штучним покриттям\nне менше 15˚, для ґрунтової або засніженої ЗПС – не менше 25˚);\n" +
                        "- стан злітно-посадкової смуги.\n" +
                        "Програма аналізує отримані дані і видає рішення на продовження зльоту чи його припинення\n«ЗЛІТАТИ» або «НЕ ЗЛІТАТИ»\n");
        alert.show();
    }

    private void showError(String error){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(error);
        alert.show();
    }





}
