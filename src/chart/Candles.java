package chart;

import controller.Log;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

public class Candles extends ItemDraw {
	Vector2<Float> positionChart, dimensionsChart;
	Float maxValue, minValue;

	public Candles (Group groupChart, DataChart listDataChart, Integer serialNumber, Vector2<Float> positionChart, Vector2<Float> dimensionsChart, Float maxValue, Float minValue,  Float itemWidth) {
		try {
			this.positionChart = positionChart;
			this.dimensionsChart = dimensionsChart;
			this.maxValue = maxValue;
			this.minValue = minValue;

			ItemInfo item = listDataChart.getList().get(serialNumber);
			Rectangle openAndClose;
			Line highAndLow;
			boolean isGrow;
			Integer bottomPos, heightTop, lowPos, highPos;
			Float xPos;

			lowPos = dimensionsChart.getY().intValue() - getAbsolutePosY(item.getLow());
			highPos = dimensionsChart.getY().intValue() - getAbsolutePosY(item.getHigh());
			xPos = positionChart.getX().floatValue() + (dimensionsChart.getX().floatValue() - (itemWidth * serialNumber)) - itemWidth*0.5f;
			highAndLow = new Line(xPos, lowPos, xPos, highPos);
			highAndLow.setStrokeWidth(2);

			if (item.getOpen() < item.getClose()) {
				isGrow = true;
				bottomPos = dimensionsChart.getY().intValue() - getAbsolutePosY(item.getClose());
				heightTop = dimensionsChart.getY().intValue() - getAbsolutePosY(item.getOpen()) - bottomPos;
			} else {
				isGrow = false;
				bottomPos = dimensionsChart.getY().intValue() - getAbsolutePosY(item.getOpen());
				heightTop = dimensionsChart.getY().intValue() - getAbsolutePosY(item.getClose()) - bottomPos;
			}

			openAndClose = new Rectangle(xPos - (itemWidth * 0.35f), bottomPos, itemWidth * 0.7f, heightTop);
			if (isGrow) {
				openAndClose.setFill(Color.GREEN);
				openAndClose.setStroke(Color.GREEN);
				highAndLow.setStroke(Color.GREEN);
			} else {
				openAndClose.setFill(Color.RED);
				openAndClose.setStroke(Color.RED);
				highAndLow.setStroke(Color.RED);
			}

			groupChart.getChildren().addAll(openAndClose, highAndLow);
		} catch (Exception e) {
			new Log(e, this.getClass().getName() + " | Создание свечи на графике");
		}
	}

	private Integer getAbsolutePosY (Float value) {
		float onePercentChart = (dimensionsChart.getY().intValue() / 100);
		float onePercentValues = (maxValue - minValue) / 100;
		return (int) (value * (onePercentChart / onePercentValues) - positionChart.getY().intValue());
	}
}