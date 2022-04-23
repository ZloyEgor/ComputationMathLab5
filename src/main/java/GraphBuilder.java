import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class GraphBuilder extends Application {

    //0: x^2 - 2x - 4
    //1: sin(x)
    //2: sqrt(x)
    //other: -0.2x^3 + 1.3x^2 - 0.7x + 0.5

    public static final int FUNCTION_NUMBER = 3;

    public static final boolean MAKE_ERRORS = true;

    public static final int DOT_AMOUNT = 4;

    public static final double LEFT_INTERPOLATION_BORDER = -4;
    public static final double RIGHT_INTERPOLATION_BORDER = 5;

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 960;

    public static ArrayList<Spline> splines;

    public Function getFunction(int functionNumber) {
        Function function;

        switch (functionNumber) {
            case 0:
                function =  new Function() {
                    @Override
                    public double get(double x) {
                        return Math.pow(x, 2) - 2 * x - 4;
                    }

                    @Override
                    public String getFormula() {
                        return "x^2 - 2x - 4";
                    }

                };
                break;
            case 1:
                function = new Function() {
                    @Override
                    public double get(double x) {
                        return Math.sin(x);
                    }

                    @Override
                    public String getFormula() {
                        return "sin(x)";
                    }
                };
                break;
            case 2:
                function = new Function() {
                    @Override
                    public double get(double x) {
                        return Math.sqrt(x);
                    }

                    @Override
                    public String getFormula() {
                        return "sqrt(x)";
                    }
                };
                break;
            default:
                function = new Function() {
                    @Override
                    public double get(double x) {
                        return -0.2 * Math.pow(x, 3) + 1.3 * Math.pow(x, 2) - 0.7 * x + 0.5;
                    }

                    @Override
                    public String getFormula() {
                        return "-0.2x^3 + 1.3x^2 - 0.7x + 0.5";
                    }
                };
                break;
        }
        return function;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Оси
        NumberAxis xAxis = new NumberAxis(-10, 15, 0.1);
        xAxis.setLabel("x");
        NumberAxis yAxis = new NumberAxis(-10, 15, 0.1);
        yAxis.setLabel("y");

        LineChart lineChart = new LineChart(xAxis, yAxis);

        //График исходной функции
        XYChart.Series functionSeries = new XYChart.Series();
        functionSeries.setName("Original function: " + getFunction(FUNCTION_NUMBER).getFormula());

        ArrayList<Dot> dots = getFunction(FUNCTION_NUMBER).getDots(120, -7, 15, false);
        for (Dot dot: dots) {
            functionSeries.getData().add(new XYChart.Data<>(dot.getX(), dot.getY()));
        }

        lineChart.getData().add(functionSeries);

        //Точки
        XYChart.Series dotSeries = new XYChart.Series();
        dotSeries.setName("Dots to interpolate");
        if (FUNCTION_NUMBER == 2)
            dots = getFunction(FUNCTION_NUMBER).getDots(DOT_AMOUNT, .1, RIGHT_INTERPOLATION_BORDER, MAKE_ERRORS);
        else
            dots = getFunction(FUNCTION_NUMBER).getDots(DOT_AMOUNT, LEFT_INTERPOLATION_BORDER, RIGHT_INTERPOLATION_BORDER, MAKE_ERRORS);

        for (Dot dot: dots) {
            dotSeries.getData().add(new XYChart.Data<>(dot.getX(), dot.getY()));
        }

        lineChart.getData().add(dotSeries);

        //Интерполяция
        XYChart.Series interpolationSeries = new XYChart.Series();
        interpolationSeries.setName("Interpolation");

        SplineSolver solver = new SplineSolver();
        splines = solver.getSplines(dots);

        for(Spline spline: splines) {
            dots = spline.getDots(35, spline.getLeftBorder(), spline.getRightBorder(), false);
            for (Dot dot: dots) {
                interpolationSeries.getData().add(new XYChart.Data<>(dot.getX(), dot.getY()));
            }
        }

        lineChart.getData().add(interpolationSeries);



        VBox vb = new VBox();
        vb.setFillWidth(true);
        HBox hb = new HBox();
        hb.getChildren().add(lineChart);
        hb.setFillHeight(true);
        vb.getChildren().add(hb);

        HBox.setHgrow(lineChart, Priority.ALWAYS);
        VBox.setVgrow(hb, Priority.ALWAYS);


        Scene scene = new Scene(vb);
        scene.getStylesheets().add("style.css");
        //Setting title to the Stage
        primaryStage.setTitle("Line Chart");

        //Adding scene to the stage
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setWidth(SCREEN_WIDTH);
        primaryStage.setHeight(SCREEN_HEIGHT);
        primaryStage.setResizable(true);

        //Displaying the contents of the stage
        primaryStage.show();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Scanner in = new Scanner(System.in);
                while (true) {
                    System.out.println("Enter X value:");
                    String input = in.nextLine();
                    double x;
                    try {
                        x = Double.parseDouble(input);
                    } catch (NumberFormatException e){
                        System.out.println("Wrong input!");
                        continue;
                    }

                    boolean splineFound = false;
                    for (Spline spline: splines) {
                        if (spline.getLeftBorder() <= x && spline.getRightBorder() >= x) {
                            splineFound = true;
                            System.out.println("Function value: " + spline.get(x));
                        }
                    }
                    if(!splineFound) {
                        System.out.println("X value is not in spline area!");
                    }
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    public static void main(String[] args) {
        launch();


    }


}
