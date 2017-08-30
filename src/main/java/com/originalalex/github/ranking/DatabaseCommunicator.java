package com.originalalex.github.ranking;

import java.sql.*;

public class DatabaseCommunicator {

    private Connection connection;
    private boolean connected = false;

    private static final String INSERT_INFO = "INSERT INTO ranks VALUES('%channel%', '%id%', %rating%, %posRatings%, %negRatings%)";
    private static final String SELECT_SPECIFIC = "SELECT * FROM ranks WHERE id LIKE '%id%' AND channel LIKE '%channel%'";
    private static final DatabaseCommunicator INSTANCE = new DatabaseCommunicator();

    public static DatabaseCommunicator getInstance() {
        return INSTANCE;
    }

    public void connect() {
        try {
            String url = "jdbc:sqlite:C:\\Users\\Alex\\Desktop\\SQLite\\Discord.db";
            connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();

            // Create the data table if it doesn't exist
            statement.execute("CREATE TABLE IF NOT EXISTS ranks(channel text, id text, rating int, posRatings int, negRatings int)");
            connected = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //String Channel, String id, int rating, int posRatings, int negRatings are the values for the rows

    public void setData(String channel, String id, int rating, int posRatings, int negRatings) {
        if (!connected) {
            connect();
        }
        try {
            Statement statement = connection.createStatement();
            if (!rowExists(channel, id)) {
                String insert = INSERT_INFO.replace("%channel%", channel).replace("%id%", id).replace("%rating%", String.valueOf(rating))
                        .replace("%posRatings%", String.valueOf(posRatings)).replace("%negRatings%", String.valueOf(negRatings)); // Replace the placeholders with real values
                statement.executeUpdate(insert);
            } else {
                String update = "UPDATE ranks " +
                        "SET (rating, posRatings, negRatings) = (" + rating + ", " + posRatings + ", " + negRatings + ")" +
                        " WHERE channel LIKE '" + channel +"' AND id LIKE '" + id + "'";
                statement.executeUpdate(update);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet fetchRow(String channel, String id) {
        if (!connected) {
            connect();
        }
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SELECT_SPECIFIC.replace("%id%", id).replaceAll("%channel%", channel));
            return resultSet;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean rowExists(String channel, String id) { // Needs to be an entry under a certain channel and id
        try {
            return fetchRow(channel, id).next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void clearTable(String tableName) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("DELETE FROM " + tableName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
