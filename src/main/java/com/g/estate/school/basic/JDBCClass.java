package com.g.estate.school.basic;

import java.sql.*;

public class JDBCClass {

    public final static String PW = "";
    public final static String US = "";

    public static void main(String[] args) throws SQLException {
        testStatic();
        testprepared();

    }

    /**
     * this is a static jdbc query
     * @throws SQLException
     */
    public static void testStatic() throws SQLException {

        Connection connection = DriverManager.getConnection("", US, PW);

        String sql = "";
        Statement statement = connection.createStatement();

        ResultSet re = statement.executeQuery(sql);

        while (re.next()) {
            System.out.println(re.getString("column1"));
            System.out.println(re.getInt("column2"));
        }
        re.close();
        statement.close();
        connection.close();
    }

    /**
     * this is a prepared jdbc query
     * @throws SQLException
     */
    public static void testprepared() throws SQLException {

        Connection connection = DriverManager.getConnection("", US, PW);

        String sql = "select * from user where id = ? and name = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, "value1");
        statement.setString(2, "value2");

        ResultSet re = statement.executeQuery();

        while (re.next()) {
            System.out.println(re.getString("column1"));
            System.out.println(re.getInt("column2"));
        }
        re.close();
        statement.close();
        connection.close();
    }
}
