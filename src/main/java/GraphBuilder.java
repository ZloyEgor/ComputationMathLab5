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
import java.util.Scanner;

public class GraphBuilder extends Application {

    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 960;


    public static DifferentialEquation equation;
    public static Function analyticSolution;
    public static ArrayList<Dot> methodSolutionDots;

    public static double startX;
    public static double endX;

    public static double minY = Double.MAX_VALUE;
    public static double maxY = Double.MIN_VALUE;

    public static final boolean PRINT_DOTS = false;

    public static ArrayList<Spline> splines;


    @Override
    public void start(Stage primaryStage) throws Exception {

        for (Dot dot: methodSolutionDots) {
            if (dot.getY() > maxY)
                maxY = dot.getY();
            if (dot.getY() < minY)
                minY = dot.getY();
        }

        //Оси
        NumberAxis xAxis = new NumberAxis(startX - 0.1*startX, endX + 0.1*endX, 0.1);
        xAxis.setLabel("x");
        NumberAxis yAxis = new NumberAxis(minY - 0.1*minY, maxY + 0.1*maxY, 0.1);
        yAxis.setLabel("y");

        LineChart lineChart = new LineChart(xAxis, yAxis);

        //График исходной функции
        XYChart.Series functionSeries = new XYChart.Series();
        functionSeries.setName("Original function: " + equation);

        ArrayList<Dot> dots = analyticSolution.getDots((int)(endX - startX)*25, startX, endX, false);

        for (Dot dot: dots) {
            functionSeries.getData().add(new XYChart.Data<>(dot.getX(), dot.getY()));
        }

        lineChart.getData().add(functionSeries);



        //Интерполяция
        XYChart.Series interpolationSeries = new XYChart.Series();
        interpolationSeries.setName("Interpolation");

        SplineSolver solver = new SplineSolver();
        splines = solver.getSplines(methodSolutionDots);

        for(Spline spline: splines) {
            dots = spline.getDots(6, spline.getLeftBorder(), spline.getRightBorder(), false);
            for (Dot dot: dots) {
                interpolationSeries.getData().add(new XYChart.Data<>(dot.getX(), dot.getY()));
            }
        }

        lineChart.getData().add(interpolationSeries);

        //Точки

        if(PRINT_DOTS) {
            XYChart.Series dotSeries = new XYChart.Series();
            dotSeries.setName("Dots to interpolate");

            for (Dot dot : methodSolutionDots) {
                dotSeries.getData().add(new XYChart.Data<>(dot.getX(), dot.getY()));
            }

            lineChart.getData().add(dotSeries);
        }



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

    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        ConsoleHandler handler = new ConsoleHandler();
        DifferentialEquationSolutionInformation info = handler.handleInput(in);

        equation = info.getEquation();
        analyticSolution = info.getAnalyticSolutionFunction();
        methodSolutionDots = info.getDots();
        startX = info.getStartX();
        endX = info.getEndX();
        System.out.println("Building graph...");
        in.close();

        launch();
    }


}
