package chart;

import controller.Log;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Bars extends ItemDraw {
	public Bars (Group groupChart, DataChart listDataChart, Integer serialNumber, Vector2<Float> positionChart, Vector2<Float> dimensionsChart, Float maxValue, Float itemWidth) {
		try {
			Float height = listDataChart.getList().get(serialNumber).getValue().floatValue() / maxValue * dimensionsChart.getY();
			Float xPos = positionChart.getX() + (dimensionsChart.getX() - (itemWidth * serialNumber)) - itemWidth;
			Float yPos = positionChart.getY() + dimensionsChart.getY() - height;

			Rectangle rectangle = new Rectangle(xPos, yPos, itemWidth, height);

			rectangle.setStroke(Color.BLACK);
			rectangle.setFill(Color.WHITE);

			groupChart.getChildren().add(rectangle);
		} catch (Exception e) {
			new Log(e, this.getClass().getName() + " | Создание бара на графике");
		}
	}
}