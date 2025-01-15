package com.prathamesh.app.utility;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.prathamesh.app.domain.User;

public class ExcelHelper {

	private final static String USER_NAME = "User Name";
	private final static String EMAIL = "Email";
	private final static String ROLE = "Role";

	public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	public static boolean hasExcelFormat(MultipartFile file) {

		if (!TYPE.equals(file.getContentType())) {

			return false;
		}

		return true;
	}

	public static List<User> excelToUser(InputStream inputStream) {

		try {

			Workbook workbook = new XSSFWorkbook(inputStream);

			Sheet sheet = workbook.getSheetAt(0);

			Iterator<Row> rows = sheet.iterator();

			List<User> users = new ArrayList<>();

			List<String> columnList = new ArrayList<String>();

			int rowNumber = 0;

			while (rows.hasNext()) {

				Row currentRow = rows.next();

				Iterator<Cell> cellsInRow = currentRow.iterator();

				if (rowNumber == 0) {

					rowNumber++;
					continue;
				}

				if (rowNumber == 1) {

					rowNumber++;

					while (cellsInRow.hasNext()) {

						Cell currentCell = cellsInRow.next();

						columnList.add(currentCell.getStringCellValue().trim());
					}

					continue;
				}

				User user = new User();

				for (int i = 0; i <= columnList.size(); i++) {

					Cell currentCell = currentRow.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);

					String column = columnList.get(i);

					switch (column) {

					case USER_NAME:
						user.setUserName(getStringValue(currentCell));
						break;

					case EMAIL:
						user.setEmail(getStringValue(currentCell));
						break;

					case ROLE:
						user.setRole(getStringValue(currentCell));
						break;

					default:
						break;

					}
				}

				users.add(user);
			}

			workbook.close();

			return users;

		} catch (

		IOException e) {

			throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
		}

	}

	public static Timestamp convertToTimestamp(String dateTimeStr) {

		if (StringUtils.isEmpty(dateTimeStr)) {

			return null;
		}

		try {

			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date parsedDate = dateFormat.parse(dateTimeStr);

			return new Timestamp(parsedDate.getTime());
		} catch (ParseException e) {

			e.printStackTrace();

			return null;
		}
	}

	public static String getStringValue(Cell currentCell) {

		if (currentCell.getCellType() == CellType.STRING) {

			return currentCell.getStringCellValue();
		} else {

			return currentCell.getNumericCellValue() + "";
		}
	}

	public static Double getNumericValue(Cell currentCell) {

		if (currentCell.getCellType() == CellType.NUMERIC) {

			return currentCell.getNumericCellValue();
		} else {

			if (StringUtils.isEmpty(getStringValue(currentCell)))

				return 0.0;
			else
				return Double.parseDouble(getStringValue(currentCell));
		}
	}
}
