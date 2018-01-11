package chart;

public class ItemInfo {
	public enum TypeValue {Open, Close, High, Low}

	private TypeValue typeValue;
	private Float open, close, high, low;

	public ItemInfo(TypeValue typeValue, Float open, Float close, Float high, Float low) {
		this.typeValue = typeValue;
		this.open = open;
		this.close = close;
		this.high = high;
		this.low = low;
	}

	public Float getValue () {
		Float value = 0f;
		switch (typeValue) {
			case Open:
				value = open;
				break;
			case Close:
				value =  close;
				break;
			case High:
				value =  high;
				break;
			case Low:
				value =  low;
				break;
		}
		return value;
	}
	public Float getMinValue () {
		Float minValue = high;
		minValue = Math.min(minValue, open);
		minValue = Math.min(minValue, close);
		minValue = Math.min(minValue, low);
		return minValue;
	}
	public Float getMaxValue () {
		Float maxValue = low;
		maxValue = Math.max(maxValue, open);
		maxValue = Math.max(maxValue, close);
		maxValue = Math.max(maxValue, high);
		return maxValue;
	}

	public Float getOpen() {
		return open;
	}
	public Float getClose() {
		return close;
	}
	public Float getHigh() {
		return high;
	}
	public Float getLow() {
		return low;
	}
}
