package org.example.hacking02_sk.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Data
public class MyDBConnection {
    private static Connection connection;
    private static Statement statement;
    private static ResultSet resultSet;
    private static DataSource dataSource;

//    public static void setConnection(DataSource dataSource) {
//        try {
//            connection = dataSource.getConnection();
//        }catch (SQLException e){
//            e.printStackTrace();
//            System.out.println(e.getMessage());
//        }
//    }

    @Autowired
    public MyDBConnection(DataSource dataSource) {
        try {
            this.dataSource = dataSource;
            connection = this.dataSource.getConnection();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static Connection getConnection(){
        try{
            if(connection != null && !connection.isClosed()){
                statement = connection.createStatement();
                if(statement != null && !statement.isClosed()){
                    statement.close();
                    statement = null;
                }
                connection.close();
                connection = null;
            }
            connection = dataSource.getConnection();
//            Class.forName("com.mysql.jdbc.Driver");
//            connection =  DriverManager.getConnection(
//                    "jdbc:mysql://localhost:3306/myhacking?verifyServerCertificate=false&useSSL=false&useUnicode=true&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true&autoReconnect=true",
//                    "myhack",
//                    "1234"
//            );
        }catch (Exception e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return connection;
    }
}
