package chart;

import controller.Log;
import controller.LogUnit;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Chart {
	public enum TimeInterval {
		sec10(10), sec30(30),
		min1(60), min3(180), min5(300), min10(600), min15(900), min30(1800), min45(2700),
		hour1(3600), hour2(7200), hour4(14400), hour8(28800), hour12(43200),
		day1(86400), day3(259200), day5(432000), day7(604800), day10(864000), day15(1296000);

		private long valueSec;
		TimeInterval (long valueSec) { this.valueSec = valueSec; }

		public Long getSec () {
			return valueSec;
		}
	}
	private TimeInterval timeInterval;
	private ArrayList<DataChart> listDataChart;
	private Vector2<Float> positionChart, dimensionsChart;
	private Group groupChart;
	private Long startValueX;
	private Integer sizeData = 0, scale = 0, minScale = 20, maxScale = 300, selectedSerialNum = 1;
	private Float maxValue = 0f, minValue = 0f, itemWidth = 0f;
	private Line verticalLine, horizontalLine;
	private Text textValueX, textValueY;

	public Chart (Long startValueX, TimeInterval timeInterval, Vector2<Float> positionChart, Vector2<Float> dimensionsChart) {
		this.startValueX = startValueX;
		this.timeInterval = timeInterval;
		this.positionChart = positionChart;
		this.dimensionsChart = dimensionsChart;

		scale = minScale;
		listDataChart = new ArrayList<>();
		groupChart = new Group();
	}

	public void draw () {
		groupChart.getChildren().clear();
		drawFrame();

		for (int arrData = 0; arrData < listDataChart.size(); arrData++) {
			DataChart dataChart = listDataChart.get(arrData);
			ArrayList<ItemInfo> itemList = dataChart.getList();

			if (scale > itemList.size())
				scale = itemList.size();

			for (int arj = 0; arj < scale; arj++) {
				maxValue = Math.max(maxValue, itemList.get(arj).getMaxValue() * 1.01f);
				minValue = Math.min(minValue, itemList.get(arj).getMinValue() * 0.99f);
			}

			for (Integer arrItem = 0; arrItem < scale; arrItem++) {
				itemWidth = dimensionsChart.getX() / scale;

				switch (dataChart.getTypeItem()) {
					case Line: new chart.Line (groupChart, dataChart, arrItem, positionChart, dimensionsChart, maxValue, itemWidth); break;
					case Bars: new chart.Bars (groupChart, dataChart, arrItem, positionChart, dimensionsChart, maxValue, itemWidth); break;
					case Candles: new chart.Candles (groupChart, dataChart, arrItem, positionChart, dimensionsChart, maxValue, minValue, itemWidth); break;
				}
			}
		}
	}
	public void drawFrame () {
		Rectangle frame = new Rectangle(positionChart.getX().intValue(), positionChart.getY().intValue(), dimensionsChart.getX().intValue(), dimensionsChart.getY().intValue());
		frame.setFill(Color.WHITE);
		frame.setStroke(Color.BLACK);
		groupChart.getChildren().add(frame);
	}
	public void drawInfo () {
		if (sizeData > 0) {
			System.out.println("O: " + getSelectItem(0).getOpen() + " C: " + getSelectItem(0).getClose() + " L: " + getSelectItem(0).getLow() + " H: " + getSelectItem(0).getHigh());
		}
	}
	public void drawMouseLine (MouseEvent event) {
		drawInfo();
		groupChart.getChildren().remove(verticalLine);
		groupChart.getChildren().remove(horizontalLine);

		int yPos = (int)event.getY();
		int yPosEnd = positionChart.getY().intValue() + dimensionsChart.getY().intValue();
		int xPos = (int)event.getX();
		int xPosEnd = positionChart.getX().intValue() + dimensionsChart.getX().intValue();

		if (yPos < positionChart.getY().intValue()) yPos = positionChart.getY().intValue();
		if (yPos > positionChart.getY().intValue() + dimensionsChart.getY().intValue()) yPos = positionChart.getY().intValue() + dimensionsChart.getY().intValue();
		if (xPos < positionChart.getX().intValue()) xPos = positionChart.getX().intValue();
		if (xPos > positionChart.getX().intValue() + dimensionsChart.getX().intValue() - itemWidth) xPos = positionChart.getX().intValue() + dimensionsChart.getX().intValue() - itemWidth.intValue();

		Integer visibleChartNum = ((int)((xPos - positionChart.getX().intValue()) / itemWidth));
		Float xCenter = positionChart.getX().intValue() + (visibleChartNum * itemWidth) + itemWidth / 2;
		selectedSerialNum = sizeData - (scale - visibleChartNum) + 1;

		verticalLine = new Line (xCenter, positionChart.getY().floatValue(), xCenter, yPosEnd);
		horizontalLine = new Line (positionChart.getX().floatValue(), yPos, xPosEnd, yPos);

		verticalLine.getStrokeDashArray().addAll(3d, 5d);
		horizontalLine.getStrokeDashArray().addAll(3d, 5d);

		groupChart.getChildren().addAll(verticalLine, horizontalLine);

		drawValueX(yPosEnd, xCenter.intValue());
		drawValueY(yPos, xPosEnd);
	}

	public Group getGroup () { return groupChart; }
	public void addAll (DataChart ... arrDataChart) {
		try {
			for (DataChart dataChart : arrDataChart) {
				if (!checkItemsAmount(dataChart))
					throw new Log(LogUnit.TypeLog.MESSAGE, "Количество элементов в добавляемом графике не совпадает с остальными");
				listDataChart.add(dataChart);
			}
		} catch (Log log) {
			log.print();
		}
	}
	public void changeScale (Double deltaY) {
		try {
			scale += deltaY.intValue() / -10;
			if (scale < minScale)
				scale = minScale;
			if (scale > maxScale)
				scale = maxScale;
			draw();
		} catch (Exception e) {
			new Log(e, this.getClass().getName() + " | Изменение временного интервала графика");
		}
	}
	public TimeInterval getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(TimeInterval timeInterval) {
		this.timeInterval = timeInterval;
	}
	public void setStartValueX(Long startValueX) {
		this.startValueX = startValueX;
	}

	private void drawValueX (int yPos, int xPosCenter) {
		yPos += 15;
		xPosCenter -= 60;

		groupChart.getChildren().remove(textValueX);
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		textValueX = new Text (xPosCenter, yPos, simpleDateFormat.format(new Date(getValueX()*1000+startValueX)));
		groupChart.getChildren().add(textValueX);
	}
	private void drawValueY (int yPos, int xPosEnd) {
		xPosEnd += 3;
		yPos += 4;

		groupChart.getChildren().remove(textValueY);
		textValueY = new Text (xPosEnd, yPos, getValueY(yPos).toString());
		groupChart.getChildren().add(textValueY);
	}
	private Long getValueX () {
		return selectedSerialNum * timeInterval.getSec();
	}
	private Long getValueY (Integer yPos) {
		int yPosChartAbsolute = yPos - positionChart.getY().intValue();
		int yPosChartPercent = Math.abs(100 - (yPosChartAbsolute / (dimensionsChart.getY().intValue() / 100)));
		return (long)((maxValue - minValue) / 100 * yPosChartPercent);
	}
	private ItemInfo getSelectItem (int idChart) {
		return listDataChart.get(idChart).getList().get(sizeData - selectedSerialNum);
	}
	private boolean checkItemsAmount (DataChart ... arrDataChart) {
		//Проверяет чтобы количество элементов в списках было одинаковым
		for (DataChart dataChart : arrDataChart) {
			if (sizeData == 0) sizeData = dataChart.getList().size();
			if (sizeData != dataChart.getList().size()) return false;
		}
		return true;
	}
}
