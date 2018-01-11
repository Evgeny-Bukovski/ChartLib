package chart;

import controller.Log;
import javafx.scene.Group;
import javafx.scene.paint.Color;

public class Line extends ItemDraw {
	public Line (Group groupChart, DataChart listDataChart, Integer serialNumber, Vector2<Float> positionChart, Vector2<Float> dimensionsChart, Float maxValue, Float itemWidth) {
		try {
			Float height = 0f, xPos = 0f, yPos = 0f, heightOld = 0f, xPosOld = 0f, yPosOld = 0f;
			height = listDataChart.getList().get(serialNumber).getValue().floatValue() / maxValue * dimensionsChart.getY().floatValue();
			xPos = positionChart.getX() + (dimensionsChart.getX() - (itemWidth * serialNumber)) - itemWidth * 0.5f;
			yPos = positionChart.getY() + dimensionsChart.getY() - height;

			if (serialNumber > 0) {
				heightOld = listDataChart.getList().get(serialNumber - 1).getValue() / maxValue * dimensionsChart.getY().floatValue();
				xPosOld = positionChart.getX() + (dimensionsChart.getX() - (itemWidth * (serialNumber - 1))) - itemWidth * 0.5f;
				yPosOld = positionChart.getY() + dimensionsChart.getY() - heightOld;
			} else {
				xPosOld = xPos;
				yPosOld = yPos;
			}

			javafx.scene.shape.Line line = new javafx.scene.shape.Line(xPosOld, yPosOld, xPos, yPos);

			//Сделать параметры настраиваемыми
			line.setStrokeWidth(4);
			line.setStroke(Color.BLUE);

			groupChart.getChildren().add(line);
		} catch (Exception e) {
			new Log(e, this.getClass().getName() + " | Создание линии графика");
		}
	}
}
