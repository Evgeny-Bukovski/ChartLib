package controller;

import chart.Chart;
import chart.DataChart;
import chart.ItemInfo;
import chart.Vector2;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class Main extends Application {
    DataChart candlesChart, lineChart;

    @Override
    public void start(Stage primaryStage) throws Exception {
        //Свежие данные добавляются в начало, под индексом 0
        //Исторические данные добавляются в конец

        Chart chart = new Chart(new Date().getTime(), Chart.TimeInterval.min5, new Vector2<Float>(100f, 100f), new Vector2<Float>(750f, 500f));

        Group groupChart = new Group();
        groupChart.getChildren().addAll(chart.getGroup());
        Scene scene = new Scene(groupChart, 900, 650);
        scene.setOnScroll(event -> chart.changeScale(event.getDeltaY()));
        scene.setOnMouseMoved(event -> chart.drawMouseLine(event));

        ArrayList<ItemInfo> dataCandles = new ArrayList<>();
        ArrayList<ItemInfo> dataLines = new ArrayList<>();
        for (Integer i = 10; i < 50; i++) {
            Random random = new Random();
            int randNum = random.nextInt(5) + 3;
            Float open = i.floatValue() * 10f;
            dataCandles.add(0, new ItemInfo(ItemInfo.TypeValue.Open, open-randNum, open+randNum, open + (randNum*3), open - (randNum*3)));
            dataLines.add(0, new ItemInfo(ItemInfo.TypeValue.Open, open-randNum, open+randNum, open + (randNum*3), open - (randNum*3)));
        }
        candlesChart = new DataChart(DataChart.TypeItem.Candles, dataCandles);
        lineChart = new DataChart(DataChart.TypeItem.Line, dataLines);

        chart.addAll(candlesChart, lineChart);

        chart.draw();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
