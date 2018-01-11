package chart;

import controller.Log;
import controller.LogUnit;

import java.util.ArrayList;

public class DataChart {
	public enum TypeItem {Bars, Line, Candles}
	private TypeItem typeItem;
	private ArrayList<ItemInfo> dataItemInfo;

	public DataChart(TypeItem typeItem, ArrayList<ItemInfo> dataItemInfo) {
		this.dataItemInfo = new ArrayList<>();
		try {
			this.typeItem = typeItem;
			this.dataItemInfo = dataItemInfo;

			if (dataItemInfo == null)
				throw new Log(LogUnit.TypeLog.ERROR, "Список данных графика не инициализирован");
		} catch (Exception e) {
			new Log(e, this.getClass().getName() + " | Инициализация данных графика");
		} catch (Log log) {
			log.print();
		}
	}

	public void add (ArrayList<ItemInfo> dataItemInfo) { this.dataItemInfo.addAll(dataItemInfo); }

	public ArrayList<ItemInfo> getList() {
		return dataItemInfo;
	}
	public TypeItem getTypeItem() {
		return typeItem;
	}
}
