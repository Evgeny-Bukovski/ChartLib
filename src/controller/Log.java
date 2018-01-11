package controller;

import java.io.PrintWriter;
import java.util.ArrayDeque;

public class Log extends Throwable {
	private final static String urlErrorLog = "";
	private PrintWriter errorTrace;
	public static ArrayDeque<LogUnit> logList = new ArrayDeque<>();

	public Log (LogUnit.TypeLog typeLog, String message) {
		LogUnit logUnit = new LogUnit(typeLog, message);
		logList.addFirst(logUnit);
	}
	public void print () {
		//Сделать принудительный вывод ошибок?
		while (!logList.isEmpty()) {
			LogUnit logUnitTmp = logList.poll();
			System.out.println(logUnitTmp.getTypeLog() + " - " + logUnitTmp.getMessage());
		}
	}
	public Log (Exception e, String message) {
		try {
			//Прикрутить: Отправка в лог на сервер
			//e.printStackTrace(errorTrace);
			System.out.println(message);
			e.printStackTrace();
			//Прикрутить: вывод сообщения об ошибке в ГУИ

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}