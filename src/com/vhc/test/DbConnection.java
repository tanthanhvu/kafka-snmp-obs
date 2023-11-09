package com.vhc.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.vhc.model.StructAlarm;

public class DbConnection {
    private static final String DB_URL = "jdbc:oracle:thin:@14.160.91.174:1621:orcl";
    private static final String DB_USER = "hr";
    private static final String DB_PASSWORD = "hr";

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Load the Oracle JDBC driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Get a database connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void insertData(StructAlarm structAlarm) {
        String insertQuery = "INSERT INTO TEST_SNMP_OBS (cellid, nbiAlarmType, nbiPerceivedSeverity, nbiSpecificProblem, site, " +
                "ne, nbiAdditionalText, recordType, nbiAlarmId, neType, nbiClearTime, nbiAlarmTime, nbiObjectInstance, " +
                "alarmMappingId, alarmMappingName, isSendSms, isMonitor, network, region, province, district, dept, " +
                "team, nbiEventTime, nbiAckState, nbiProbableCause, nbiAckTime, nbiAckUser, nbiClearUser, ipAddress, " +
                "isMll, tgNhan, tgInsert) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {

            preparedStatement.setString(1, structAlarm.cellid);
            preparedStatement.setString(2, structAlarm.nbiAlarmType);
            preparedStatement.setString(3, structAlarm.nbiPerceivedSeverity);
            preparedStatement.setString(4, structAlarm.nbiSpecificProblem);
            preparedStatement.setString(5, structAlarm.site);
            preparedStatement.setString(6, structAlarm.ne);
            preparedStatement.setString(7, structAlarm.nbiAdditionalText);
            preparedStatement.setString(8, structAlarm.recordType);
            preparedStatement.setString(9, structAlarm.nbiAlarmId);
            preparedStatement.setString(10, structAlarm.neType);
            preparedStatement.setString(11, structAlarm.nbiClearTime);
            preparedStatement.setString(12, structAlarm.nbiAlarmTime);
            preparedStatement.setString(13, structAlarm.nbiObjectInstance);
            preparedStatement.setString(14, structAlarm.alarmMappingId);
            preparedStatement.setString(15, structAlarm.alarmMappingName);
            preparedStatement.setString(16, structAlarm.isSendSms);
            preparedStatement.setString(17, structAlarm.isMonitor);
            preparedStatement.setString(18, structAlarm.network);
            preparedStatement.setString(19, structAlarm.region);
            preparedStatement.setString(20, structAlarm.province);
            preparedStatement.setString(21, structAlarm.district);
            preparedStatement.setString(22, structAlarm.dept);
            preparedStatement.setString(23, structAlarm.team);
            preparedStatement.setString(24, structAlarm.nbiEventTime);
            preparedStatement.setString(25, structAlarm.nbiAckState);
            preparedStatement.setString(26, structAlarm.nbiProbableCause);
            preparedStatement.setString(27, structAlarm.nbiAckTime);
            preparedStatement.setString(28, structAlarm.nbiAckUser);
            preparedStatement.setString(29, structAlarm.nbiClearUser);
            preparedStatement.setString(30, structAlarm.ipAddress);
            preparedStatement.setString(31, structAlarm.isMll);
            preparedStatement.setString(32, structAlarm.tgNhan);
            
            LocalDateTime now = LocalDateTime.now();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String formattedDateTime = now.format(formatter);
            
            preparedStatement.setString(33, formattedDateTime);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Data inserted successfully!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}