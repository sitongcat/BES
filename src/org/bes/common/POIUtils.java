package org.bes.common;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class POIUtils {

	public static String getCellValue(Cell cell) {
		if (cell == null) {
			return null;
		}

		switch (cell.getCellTypeEnum()) {
			case BLANK:
			case _NONE:
				return null;
			case STRING:
				return cell.getStringCellValue();
			case BOOLEAN:
				return cell.getStringCellValue();
			case NUMERIC:
				return String.format("%.2f", cell.getNumericCellValue());
			default:
				return cell.getStringCellValue();
		}
	}

	public static String getDateCellValue(Cell cell, String format) {
		if (cell == null) {
			return null;
		}

		switch (cell.getCellTypeEnum()) {
			case BLANK:
			case _NONE:
				return null;
			case STRING:
				return cell.getStringCellValue();
			case BOOLEAN:
				return cell.getStringCellValue();
			case NUMERIC:
				if (DateUtil.isCellDateFormatted(cell)) {
					Date date = cell.getDateCellValue();
					return (new SimpleDateFormat(format).format(date));
				}
				double cellValue = cell.getNumericCellValue();
				return String.format("%f", cellValue);
			default:
				return cell.getStringCellValue();
		}
	}

}
