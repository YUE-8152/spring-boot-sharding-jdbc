package com.cardlan.sharding.jdbc.common.utils;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import com.cardlan.sharding.jdbc.common.core.ExportColumn;
import com.sun.deploy.net.URLEncoder;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;

public class ExcelUtils<T> {

    private static final String HEADER_STYLE = "Header";    //表格头
    private static final String INFO_STYLE = "Info";        //表格内容
    private static final String DOWNLOAD_PATCH = "D:\\";    //文件路径

    /**
     * 根据HTTP请求导出excel文件，用于Web端
     *
     * @param response   HTTP请求
     * @param collection 对象集合
     * @param fileName   文件名
     * @param sheetName  工作簿名(可为空)
     * @param headTitles 自定义的导出文件的头名字
     * @param <T>
     */
    public static <T> void exportExcel(HttpServletResponse response, Collection<T> collection, String fileName, String sheetName, String[] headTitles) {
        try {
//          response.setContentType("application/vnd.ms-excel");
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");//要保存的文件名
            exportExcel(collection, response.getOutputStream(), sheetName, headTitles);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 传入对象集合，导出excel文件
     *
     * @param collection    对象集合
     * @param fileName      文件名(可为空)
     * @param sheetName     工作簿名(可为空)
     * @param exportColumns 要导出的字段，例：[{"cnName":"订单详情Id","enName":"id"},{"cnName":"订单Id","enName":"orderId"},{"cnName":"订单名","enName":"itemName"},{"cnName":"订单编码","enName":"itemCode"},{"cnName":"创建时间","enName":"createTime"},{"cnName":"创建人","enName":"createUser"}]
     */
    public static <T> void exportExcel(Collection<T> collection, String fileName, String sheetName, List<ExportColumn> exportColumns) {
        //导出文件名：fileName+时间
        String filename = null;
        // 时间格式化
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String format = now.format(formatter);
        // 文件名为空则默认文件名为当前时间
        if (StringUtils.isEmpty(fileName) || StringUtils.isBlank(fileName)) {
            filename = format + ".xlsx";
        } else {
            filename = fileName + "-" + format + ".xlsx";
        }

        //导出文件存放路径
        String filepath = DOWNLOAD_PATCH + filename;

        try {
            OutputStream outputStream = new FileOutputStream(filepath);
            matchData(collection, outputStream, sheetName, exportColumns);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 传入对象集合，导出excel文件
     *
     * @param collection 对象集合
     * @param fileName   文件名(可为空)
     * @param sheetName  工作簿名(可为空)
     * @param headTitles 自定义的导出文件的头名字
     */
    public static <T> void exportExcel(Collection<T> collection, String fileName, String sheetName, String[] headTitles) {
        //导出文件名：fileName+时间
        String filename = null;
        // 时间格式化
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String format = now.format(formatter);
        // 文件名为空则默认文件名为当前时间
        if (StringUtils.isEmpty(fileName) || StringUtils.isBlank(fileName)) {
            filename = format + ".xlsx";
        } else {
            filename = fileName + "-" + format + ".xlsx";
        }

        //导出文件存放路径
        String filepath = DOWNLOAD_PATCH + filename;

        try {
            OutputStream outputStream = new FileOutputStream(filepath);
            exportExcel(collection, outputStream, sheetName, headTitles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 传入对象集合，导出excel文件
     *
     * @param collection   对象集合
     * @param outputStream 输出流
     * @param sheetName    工作簿名(可为空)
     * @param headTitles   自定义的导出文件的头名字
     */
    private static <T> void exportExcel(Collection<T> collection, OutputStream outputStream, String sheetName, String[] headTitles) {
        SXSSFWorkbook workbook = new SXSSFWorkbook();    // 创建一个工作表
        Sheet sheet;
        //工作簿名字为空的时候为默认名称
        if (StringUtils.isEmpty(sheetName) || StringUtils.isBlank(sheetName)) {
            sheet = workbook.createSheet();
        } else {
            sheet = workbook.createSheet(sheetName);    //创建一个工作簿
        }
        sheet.setDefaultColumnWidth((short) 15);        // 设置表格默认列宽度为15个字节
        CellStyle style = workbook.createCellStyle();    //创建样式

        Row headerRow = sheet.createRow(0);            // 创建第一行
        Cell headerCell = null;                            //创建一个单元格
        // 创建表头
        for (int i = 0; i < headTitles.length; i++) {
            headerCell = headerRow.createCell(i);
            headerCell.setCellValue(new XSSFRichTextString(headTitles[i]));
            //设置样式
            headerCell.setCellStyle(setCellStyle(HEADER_STYLE, style));
        }

        // 遍历集合数据，产生数据行
        Iterator<T> iterator = collection.iterator();
        int index = 0;
        Field[] fields;
        String getMethodName;
        Class cls;
        Method method;
        Object value;
        Cell cell;

        //循环获取对象
        while (iterator.hasNext()) {
            index++;
            Row row = sheet.createRow(index);
            T t = (T) iterator.next();
            // 利用反射，根据JavaBean属性的先后顺序，动态调用getXxx()方法得到属性值
            fields = t.getClass().getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                getMethodName = getGetMethodName(fields[i]);
                cls = t.getClass();
                try {
                    method = cls.getMethod(getMethodName, new Class[]{});
                    value = method.invoke(t, new Object[]{});
                    cell = row.createCell(i);
                    //根据值的类型，写入单元格
                    setCellValue(value, cell);
                    //设置单元格样式
                    cell.setCellStyle(setCellStyle(INFO_STYLE, style));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 根据sql导出excel文件,用于Web端
     *
     * @param response   HTTP请求
     * @param conn       数据库连接
     * @param sql        sql语句
     * @param fileName   文件名
     * @param sheetName  工作簿名(可为空)
     * @param headTitles 自定义的表格头名字(可为空)
     */
    public static void exportExcel(HttpServletResponse response, Connection conn, String sql, String fileName, String sheetName, String[] headTitles) {
        try {
//          response.setContentType("application/vnd.ms-excel");
            response.setContentType("application/octet-stream; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xls");//要保存的文件名
            exportExcel(conn, sql, response.getOutputStream(), sheetName, headTitles);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据sql导出excel文件
     *
     * @param conn       数据库连接
     * @param sql        sql语句
     * @param fileName   文件名(可为空)
     * @param sheetName  工作簿名(可为空)
     * @param headTitles 自定义的表格头名字(可为空)
     */
    public static void exportExcel(Connection conn, String sql, String fileName, String sheetName, String[] headTitles) {
        if (StringUtils.isBlank(sql)) {
            throw new IllegalArgumentException("sql不能为空！！");
        } else {
            String filename = null;
            // 时间格式化
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String format = now.format(formatter);
            // 文件名为空则默认文件名为当前时间
            if (StringUtils.isEmpty(fileName) || StringUtils.isBlank(fileName)) {
                filename = format + ".xlsx";
            } else {
                filename = fileName + "-" + format + ".xlsx";
            }

            // 文件路径
            String filepath = DOWNLOAD_PATCH + filename;
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(filepath);
                exportExcel(conn, sql, outputStream, sheetName, headTitles);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据sql导出excel文件
     *
     * @param conn         数据库连接
     * @param sql          sql语句
     * @param outputStream 输出流
     * @param sheetName    工作簿名(可为空)
     * @param headTitles   自定义的表格头名字(可为空)
     */
    private static void exportExcel(Connection conn, String sql, OutputStream outputStream, String sheetName, String[] headTitles) {
        if (StringUtils.isBlank(sql)) {
            throw new IllegalArgumentException("sql不能为空！！");
        } else {
            List<Map<String, Object>> list = exeSqlToList(sql, conn, headTitles);

            // 获取sql中列名
            List<String> headers = new ArrayList<>();
            if (list == null || list.size() == 0) {
                throw new NullPointerException();
            }
            for (String m : list.get(0).keySet()) {
                headers.add(m);
            }

            // 创建一个工作表
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            // 创建一个工作簿
            Sheet sheet;
            //工作簿名字为空的时候为默认名称
            if (StringUtils.isEmpty(sheetName) || StringUtils.isBlank(sheetName)) {
                sheet = workbook.createSheet();
            } else {
                sheet = workbook.createSheet(sheetName);
            }

            // 设置表格默认列宽度为15个字节
            sheet.setDefaultColumnWidth((short) 15);
            //创建样式
            CellStyle style = workbook.createCellStyle();

            // 创建第一行
            Row headerRow = sheet.createRow(0);
            // 创建一个单元格
            Cell headerCell = null;
            // 创建表头
            for (int i = 0; i < headers.size(); i++) {
                headerCell = headerRow.createCell(i);
                headerCell.setCellValue(headers.get(i));
                //设置样式
                headerCell.setCellStyle(setCellStyle(HEADER_STYLE, style));
            }
            // 从第二行开始追加数据
            for (int i = 0; i < list.size(); i++) {
                Row row = sheet.createRow(i + 1);
                Map<String, Object> map = list.get(i);
                int j = 0;
                for (String key : map.keySet()) {
                    Cell cell = row.createCell(j);
                    setCellValue(map.get(key), cell);
                    cell.setCellStyle(setCellStyle(INFO_STYLE, style));
                    j++;
                }
            }
            try {
                workbook.write(outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据sql将查询结果封装成List<Map<String, Object>>集合
     *
     * @param sql
     * @param conn
     * @param headTitles
     * @return
     */
    private static List<Map<String, Object>> exeSqlToList(String sql, Connection conn, String[] headTitles) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        int size = 0;
        if (headTitles != null) {
            size = headTitles.length;
        }
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
                    if (size > 0) {
                        map.put(headTitles[i - 1], rs.getObject(i));
                    } else {
                        map.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                    }
                }
                result.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (ps != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 根据sql将查询结果封装成List<Map<String, Object>>集合
     *
     * @param sql
     * @param conn
     * @return
     */
    private static List<Map<String, Object>> exeSqlToList(String sql, Connection conn) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        try {
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            int columnCount = rs.getMetaData().getColumnCount();
            while (rs.next()) {
                Map<String, Object> map = new LinkedHashMap<String, Object>();
                for (int i = 1; i <= columnCount; i++) {
                    map.put(rs.getMetaData().getColumnName(i), rs.getObject(i));
                }
                result.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (ps != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    /**
     * 获取get方法名
     *
     * @param field
     * @return
     */
    private static String getGetMethodName(Field field) {
        String fieldName = field.getName();
        String getMethodName = "get" + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);
        return getMethodName;
    }


    /**
     * 根据值类型设置cell单元格值
     *
     * @param value
     * @param cell
     */
    private static void setCellValue(Object value, Cell cell) {
        if (value == null) {
            cell.setBlank();
        } else {
            if (value instanceof Integer) {
                cell.setCellValue(((Integer) value).intValue());
            } else if (value instanceof Number) {
                cell.setCellValue(((Number) value).intValue());
            } else if (value instanceof Double) {
                cell.setCellValue(((Double) value).doubleValue());
            } else if (value instanceof Float) {
                cell.setCellValue(((Float) value).floatValue());
            } else if (value instanceof String) {
                cell.setCellValue(value.toString());
            } else if (value instanceof Long) {
                cell.setCellValue(((Long) value).longValue());
            } else if (value instanceof Date) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                cell.setCellValue(format.format(value));
            } else if (value instanceof Boolean) {
                if ((boolean) value) {
                    cell.setCellValue("是");
                } else {
                    cell.setCellValue("否");
                }
            } else if (value instanceof RichTextString) {
                cell.setCellValue(((RichTextString) value).getString());
            }
        }
    }

    /**
     * 设置单元格样式
     *
     * @param cellType
     * @param style
     * @return
     */
    private static CellStyle setCellStyle(String cellType, CellStyle style) {
        //设置header样式
        if (HEADER_STYLE.equals(cellType)) {
            // 设置这些样式
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setAlignment(HorizontalAlignment.CENTER);
            // 生成一个字体
//			Font font = workbook.createFont();
//			font.setBold(true);
//			font.setFontHeightInPoints((short) 13);
            // 把字体应用到当前的样式
//			style.setFont(font);
        }
        if (INFO_STYLE.equals(cellType)) {
            // 设置内容样式
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            // 生成另一个字体
//			Font font2 = workbook.createFont();
//			font2.setFontName("微软雅黑");
            // 把字体应用到当前的样式
//			style.setFont(font2);
        }
        return style;
    }

    /**
     * 传入对象集合，导出excel文件
     *
     * @param collection    对象集合
     * @param outputStream  输出流
     * @param sheetName     工作簿名(可为空)
     * @param exportColumns 要导出的字段
     */
    private static <T> void matchData(Collection<T> collection, OutputStream outputStream, String sheetName, List<ExportColumn> exportColumns) {
        SXSSFWorkbook workbook = new SXSSFWorkbook();    // 创建一个工作表
        Sheet sheet;
        //工作簿名字为空的时候为默认名称
        if (StringUtils.isEmpty(sheetName) || StringUtils.isBlank(sheetName)) {
            sheet = workbook.createSheet();
        } else {
            sheet = workbook.createSheet(sheetName);    //创建一个工作簿
        }
        sheet.setDefaultColumnWidth((short) 15);        // 设置表格默认列宽度为15个字节
        CellStyle style = workbook.createCellStyle();    //创建样式

        Row headerRow = sheet.createRow(0);            // 创建第一行
        Cell headerCell = null;                            //创建一个单元格
        // 创建表头
        for (int i = 0; i < exportColumns.size(); i++) {
            headerCell = headerRow.createCell(i);
            headerCell.setCellValue(new XSSFRichTextString(exportColumns.get(i).getCnName()));
            //设置样式
            headerCell.setCellStyle(setCellStyle(HEADER_STYLE, style));
        }

        // 遍历集合数据，产生数据行
        Iterator<T> iterator = collection.iterator();
        int index = 0;
        Field[] fields;
        String getMethodName;
        Class cls;
        Method method;
        Object value;
        Cell cell;

        //循环获取对象
        while (iterator.hasNext()) {
            index++;
            Row row = sheet.createRow(index);
            T t = (T) iterator.next();
            // 利用反射，根据JavaBean属性的先后顺序，动态调用getXxx()方法得到属性值
            fields = t.getClass().getDeclaredFields();

            for (int i = 0; i < fields.length; i++) {
                //匹配导出字段
                for (int j = 0; j < exportColumns.size(); j++) {
                    if (exportColumns.get(j).getEnName().equals(fields[i].getName())) {
                        getMethodName = getGetMethodName(fields[i]);
                        cls = t.getClass();
                        try {
                            method = cls.getMethod(getMethodName, new Class[]{});
                            value = method.invoke(t, new Object[]{});
                            cell = row.createCell(i);
                            //根据值的类型，写入单元格
                            setCellValue(value, cell);
                            //设置单元格样式
                            cell.setCellStyle(setCellStyle(INFO_STYLE, style));
                        } catch (NoSuchMethodException e) {
                            e.printStackTrace();
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        try {
            workbook.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 导入Excel中的数据，并将数据映射到指定对象
     *
     * @param filePath 文件路径
     * @param object   指定的对象
     * @return List<Map < String, Object>>
     */
    public static List<Map<String, Object>> importExcel(String filePath, Object object) {
        InputStream inputStream = null;
        Workbook workbook = null;
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        try {
            String endStr = filePath.substring(filePath.lastIndexOf("."));
            inputStream = new FileInputStream(filePath);
            if (".xls".equals(endStr)) {
                workbook = new XSSFWorkbook(inputStream);
            } else if (".xlsx".equals(endStr)) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                workbook = null;
            }

            //获取第一个sheet页
            Sheet sheet = workbook.getSheetAt(0);

            //遍历行 从下标第一行开始（去除标题）
            for (int i = 1; i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    //装载obj
                    try {
                        list.add(dataObject(object, row));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 根据excel表格数据类型获取表格数据值
     *
     * @param cell
     * @return
     */
    private static Object getCellValue(Cell cell) {
        CellType cellType = cell.getCellType();
        Object cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        // 判断数据的类型
        switch (cellType) {
            case NUMERIC: // 数字
                cellValue = operationNumericType(cell);
                break;
            case STRING: // 字符串
                cellValue = cell.getStringCellValue();
                break;
            case BOOLEAN: // Boolean
                cellValue = cell.getBooleanCellValue();
                break;
            case FORMULA: // 公式
                cellValue = cell.getCellFormula();
                break;
            case BLANK: // 空值
                cellValue = null;
                break;
            case ERROR: // 故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }


    /**
     * 处理cell表格中的NUMERIC格式
     *
     * @param cell
     * @return
     */
    private static Object operationNumericType(Cell cell) {
        //根据cell.getCellStyle().getDataFormat() 方法  可以得到excel 格子中的short类型的值
        //格式 yyyy/MM/dd 的值是 14
        //格式 HH:mm:ss 的值是 21
        //格式 yyyy/MM/dd HH:mm:ss 的值是 22
        //格式 double 和 Int 都是 0
        Object cellValue = "";
        if (DateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
            SimpleDateFormat sdf = null;
            // 验证short值
            if (cell.getCellStyle().getDataFormat() == 14) {
                sdf = new SimpleDateFormat("yyyy/MM/dd");
            } else if (cell.getCellStyle().getDataFormat() == 21) {
                sdf = new SimpleDateFormat("HH:mm:ss");
            } else if (cell.getCellStyle().getDataFormat() == 22) {
                sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            } else {
                throw new RuntimeException("日期格式错误!!!");
            }
            Date date = cell.getDateCellValue();
            cellValue = sdf.format(date);
        } else if (cell.getCellStyle().getDataFormat() == 0) {//处理数值格式
            cellValue = cell.getNumericCellValue();
        }
        return cellValue;
    }

    /**
     * 将对象属性与excel表格的数据值进行映射
     *
     * @param obj
     * @param row
     * @return Map<String, Object>
     */
    private static Map<String, Object> dataObject(Object obj, Row row) {
        Class<?> rowClazz = obj.getClass();
        Field[] fields = rowClazz.getDeclaredFields();
        if (fields == null || fields.length < 1) {
            return null;
        }
        //容器
        Map<String, Object> map = new HashMap<String, Object>();

        //注意excel表格字段顺序要和obj字段顺序对齐 （如果有多余字段请另作特殊下标对应处理）
        for (int j = 0; j < fields.length; j++) {
            map.put(fields[j].getName(), getCellValue(row.getCell(j)));
        }
        return map;
    }
}
