package io.jayms.dbsc.server.spreadsheet;

import io.jayms.dbsc.interfaces.model.Database;
import io.jayms.dbsc.interfaces.model.Report;
import io.jayms.dbsc.interfaces.model.Spreadsheet;
import io.jayms.dbsc.interfaces.spreadsheet.SpreadsheetGenerator;
import org.apache.poi.EmptyFileException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ws.rs.NotFoundException;
import java.io.*;
import java.sql.*;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
public class BasicSpreadsheetGenerator implements SpreadsheetGenerator {

    //private static final Logger LOG = Logger.getLogger(BasicSpreadsheetGenerator.class.getName());
    private static final DateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH_mm_ss");

    @Override
    public Spreadsheet generate(Report report) {
        String query = report.getQuery();
        Database database = report.getDatabase();
        try {
            Connection connection = database.connection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            String destination = report.getDestination();

            Instant instant = Instant.now();
            Date date = Date.from(instant);
            String formattedDate = formatter.format(date);

            String reportName = report.getName();
            String spreadsheetName = reportName + "_" + formattedDate;
            File workbookFile = new File(destination, spreadsheetName + ".xlsx");

            FileInputStream workbookInput = getFileInputStream(workbookFile);

            String location = workbookFile.getPath();
            Spreadsheet spreadsheet = new Spreadsheet();
            spreadsheet.setLocation(location);
            spreadsheet.setCreation(instant);
            spreadsheet.setReport(report);

            XSSFWorkbook workbook;
            try {
                workbook = new XSSFWorkbook(workbookInput);
            } catch (EmptyFileException ex) {
                workbook = new XSSFWorkbook();
            }
            CreationHelper creationHelper = workbook.getCreationHelper();

            XSSFSheet sheet = workbook.createSheet();
            workbook.setSheetName(workbook.getSheetIndex(sheet), spreadsheetName);

            ResultSetMetaData metaData = resultSet.getMetaData();
            Row row = sheet.createRow(0);
            DatabaseColumn[] columns = DBTools.getDatabaseColumns(metaData);
            for (DatabaseColumn column : columns) {
                Cell cell = row.createCell(column.getIndex());
                RichTextString titleCell = creationHelper.createRichTextString(column.getName() + " (" + column.getTableName() + ")");
                cell.setCellValue(titleCell);
            }

            int rownum = 1;
            while (resultSet.next()) {
                row = sheet.createRow(rownum++);
                for (int i = 0; i < columns.length; i++) {
                    DatabaseColumn column = columns[i];
                    String label = column.getLabel();
                    int type = column.getType();
                    Cell cell = row.createCell(i);
                    DBTools.setCellValue(cell, resultSet, label, type);
                }
            }

            try (FileOutputStream outputStream = new FileOutputStream(workbookFile)) {
                workbook.write(outputStream);
                outputStream.flush();
            }

            return spreadsheet;
        } catch (SQLException ex) {
            ex.printStackTrace();
            return null;
        } catch (IOException ex) {
            ex.printStackTrace();;
            return null;
        }
    }

    private FileInputStream getFileInputStream(File file) throws IOException {
        if (!file.exists()) {
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            if (file.createNewFile()) {
                System.out.println("Created new workbook file at: " + file.getPath());
            }
        }
        return new FileInputStream(file);
    }

}
