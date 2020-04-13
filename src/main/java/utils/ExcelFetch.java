package utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;

/**
 * @author sundar.siva
 *
 */
public class ExcelFetch {
	public String project = System.getProperty("user.dir");

	/**
	 * Fetches the test data from DataPool.xlsx sheet 
	 * @param testCaseName
	 * @param data
	 * @return
	 */
	public Map<String, String> getDataFromExcel(String testCaseName, String data) {
		String excelFilePath = project + "/data/DataPool.xlsx";

		Map<String, String> map = new HashMap<String, String>();
		try {
			FileInputStream fiStream = new FileInputStream(new File(excelFilePath));
			XSSFWorkbook workbook = new XSSFWorkbook(fiStream);

			XSSFSheet sheet = workbook.getSheet("TestCaseSheet");
			String dataSheetName = "";
			Iterator<Row> iterator = sheet.iterator();
			int count = 0;
			while (iterator.hasNext()) {
				Row nextRow = iterator.next();
				if (count == 0) {
					count++;
					continue;
				}
				if (nextRow.getCell(0).getStringCellValue().equals(testCaseName)
						&& nextRow.getCell(1).getStringCellValue().equals(data)) {
					dataSheetName = nextRow.getCell(2).getStringCellValue();

				}

			}
			if (dataSheetName.equals("")) {
				Assert.fail("No such sheet found!!");
			}
			XSSFSheet dataSheet = workbook.getSheet(dataSheetName);
			Iterator<Row> iterator2 = dataSheet.iterator();
			int count2 = 0;
			List<String> keys = new ArrayList<String>();
			List<String> values = new ArrayList<String>();
			Boolean flag = false;
			while (iterator2.hasNext()) {
				Row nextRow = iterator2.next();
				Iterator<Cell> iteratorCell = nextRow.iterator();
				if (count2 == 0) {

					while (iteratorCell.hasNext()) {
						Cell nextCell = iteratorCell.next();
						keys.add(nextCell.getStringCellValue());
					}
					count2++;
				} else {
					while (iteratorCell.hasNext()) {
						Cell nextCell = iteratorCell.next();
						if (nextCell.equals(null)
								|| !(nextRow.getCell(0).getStringCellValue().equalsIgnoreCase(data))) {
							break;
						}
						values.add(nextCell.getStringCellValue());
						flag = true;
					}
				}
				if (flag)
					break;
			}
			if (keys.size() == values.size()) {
				for (int index = 0; index < keys.size(); index++) {
					map.put(keys.get(index), values.get(index));
				}
			} else {
				Assert.fail(
						"Some error occured during data fetching from Excel. Please check your Excel sheet as some extra/less column/s are present for either keys or value");
			}
			
			workbook.close();

		} catch (Exception e) {
			}

		return map;
	}

}