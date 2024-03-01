package personal.css.UniversalSpringbootProject.common.utils.sql;

import cn.hutool.json.JSONException;
import org.springframework.stereotype.Component;
import personal.css.UniversalSpringbootProject.common.utils.CommonUtil;
import personal.css.UniversalSpringbootProject.common.vo.ListResult;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static personal.css.UniversalSpringbootProject.common.consts.MyConst.*;

/**
 * @Description: 读取指定数据库信息
 * @Author: CSS
 * @Date: 2023/11/24 9:17
 */

@Component
public class ExecuteSQL {

    /**
     * 导出指定数据表的sql文件到指定路径下
     *
     * @param databaseName
     * @param tableName
     * @param outputPath
     * @throws SQLException
     * @throws IOException
     */
    public static void exportTable(String databaseName, String tableName, String outputPath) throws SQLException, IOException {
        // 获取数据库连接
        Connection conn = DriverManager.getConnection(
                BASEURL + DATABASE,
                USERNAME,
                PASSWORD
        );

        // 创建输出文件
        FileWriter writer = new FileWriter(outputPath);
        Statement stmt = null;
        try {

            ResultSet rs = null;

            //查询表结构
            try {
                String queryTableSql = "SHOW CREATE TABLE " + tableName;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(queryTableSql);
                if (rs.next()) {
                    String createTableSql = rs.getString(2);
                    writer.write(createTableSql + ";\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (rs != null)
                    rs.close();
            }

            //查询表数据
            try {
                String queryDataSql = "SELECT * FROM " + tableName;
                rs = stmt.executeQuery(queryDataSql);
                //读取每一个记录
                while (rs.next()) {
                    StringBuilder rowData = new StringBuilder();
                    int columnCount = rs.getMetaData().getColumnCount();
                    //每一个元素
                    for (int i = 1; i <= columnCount; i++) {
                        rowData.append(rs.getString(i));
                        if (i < columnCount) {
                            rowData.append(",");
                        }
                    }
                    writer.write("INSERT INTO " + tableName + " VALUES (" + rowData.toString() + ");\n");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (rs != null)
                    rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (writer != null)
                writer.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
    }


    /**
     * 导出指定数据表的sql文件
     *
     * @param tableName
     * @throws SQLException
     * @throws IOException
     */
    public static String getTableSql(String tableName) throws SQLException {
        // 获取数据库连接
        Connection conn = DriverManager.getConnection(
                BASEURL + DATABASE,
                USERNAME,
                PASSWORD
        );

        // 创建输出数据
        StringBuilder sb = new StringBuilder();
        Statement stmt = null;
        try {

            ResultSet rs = null;

            //查询表结构
            try {
                String queryTableSql = "SHOW CREATE TABLE " + tableName;
                stmt = conn.createStatement();
                rs = stmt.executeQuery(queryTableSql);
                if (rs.next()) {
                    //结果包含两列，第一列为表名，第二列为建表语句
                    String createTableSql = rs.getString(2);
                    createTableSql = createTableSql.replaceAll("\\n", " ");
                    sb.append(createTableSql);
                    sb.append(";");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (rs != null)
                    rs.close();
            }

            //查询表数据
            try {
                String queryDataSql = "SELECT * FROM " + tableName;
                rs = stmt.executeQuery(queryDataSql);
                //读取每一个记录
                while (rs.next()) {
                    StringBuilder rowData = new StringBuilder();
                    //表数据
                    ResultSetMetaData metaData = rs.getMetaData();
                    //列数
                    int columnCount = metaData.getColumnCount();
                    //每一个元素
                    for (int i = 1; i <= columnCount; i++) {
                        //列类型
                        String columnTypeName = metaData.getColumnTypeName(i);
                        //列值
                        String string = rs.getString(i);
                        if ("VARCHAR".equals(columnTypeName) || "DATETIME".equals(columnTypeName))
                            string = "'" + string + "'";
                        rowData.append(string);
                        if (i < columnCount) {
                            rowData.append(",");
                        }
                    }
                    sb.append("INSERT INTO " + tableName + " VALUES (" + rowData.toString() + ");");
                }

                return sb.toString();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (rs != null)
                    rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
        return null;
    }


    /**
     * 执行查询sql
     *
     * @param sql
     * @throws SQLException
     * @throws IOException
     */
    public static ListResult<?> executeQuery(String sql) throws SQLException {
        // 获取数据库连接
        Connection conn = DriverManager.getConnection(
                BASEURL + DATABASE,
                USERNAME,
                PASSWORD
        );

        Statement stmt = null;
        ResultSet rs = null;

        try {
            //执行sql
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();
            List list = new ArrayList();
            // 创建输出数据
            while (rs.next()) {
                /*JSONObject entries = new JSONObject();
                for (int i = 1; i <= columnCount; i++) {
                    Object object = rs.getObject(i);
                    //JSONObject不支持将null值作为value
                    if (null == object)
                        entries.set(metaData.getColumnName(i), "Null");
                    else
                        entries.set(metaData.getColumnName(i), object);
                }*/

                HashMap<Object, Object> entries = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    entries.put(metaData.getColumnName(i), rs.getObject(i));
                }
                list.add(entries);
            }

            return new ListResult<>(list.size(), list);
        } catch (SQLException e) {
            throw new SQLException(e);
        } catch (JSONException e) {
            throw new JSONException(e);
        } finally {
            // 关闭资源
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
    }


    /**
     * 执行插入sql
     *
     * @param sql
     * @throws SQLException
     * @throws IOException
     */
    public static String executeInsert(String sql) throws SQLException {
        // 获取数据库连接
        Connection conn = DriverManager.getConnection(
                BASEURL + DATABASE,
                USERNAME,
                PASSWORD
        );

        Statement stmt = null;
        ResultSet rs = null;

        try {
            //执行sql
            stmt = conn.createStatement();
            int update = stmt.executeUpdate(sql);
            if (update == 0)
                return "执行失败！";
            else
                return "执行成功！";
        } catch (SQLException e) {
            throw new SQLException(e);
        } catch (JSONException e) {
            throw new JSONException(e);
        } finally {
            // 关闭资源
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
            if (conn != null)
                conn.close();
        }
    }

    public static void main(String[] args) {
        try {
            // 导出数据表为 SQL 文件
            String databaseName = CommonUtil.scanner("数据库名");
            String tableName = CommonUtil.scanner("数据表每");
            String outputPath = "F:\\personalItem\\myproject\\databasemanage\\src\\main\\resources\\output.sql";
            exportTable(databaseName, tableName, outputPath);
            System.out.println("导出成功！");
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }
    }
}
