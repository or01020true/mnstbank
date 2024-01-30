package org.example.hacking02_sk.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    @Autowired
    public static void setConnection(DataSource dataSource) {
        try {
            connection = dataSource.getConnection();
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
    }

//    public MyDBConnection(DataSource dataSource) {
//        try {
//            connection = dataSource.getConnection();
//        }catch (SQLException e){
//            System.out.println(e.getMessage());
//        }
//    }

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
        }catch (SQLException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return connection;
    }
}
