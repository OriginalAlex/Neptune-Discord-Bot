package com.originalalex.github.ranking;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.originalalex.github.helper.FileHelper;
import jdk.nashorn.internal.parser.JSONParser;

import java.nio.charset.Charset;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseCommunicator {

    private Connection connection;
    private boolean connected = false;
    private int cooldown = 0;
    private boolean cooldownHasBeenInitialized; // to inform certain methods that the cooldown has been set already (to avoid calling the same method repetitively)

    private static final String INSERT_INFO = "INSERT INTO ranks VALUES('%channel%', '%id%', %rating%, %posRatings%, %negRatings%)";
    private static final String SELECT_SPECIFIC = "SELECT * FROM ranks WHERE id LIKE '%id%' AND channel LIKE '%channel%'"; // Can limit to 1 because hopefully channel/id are unique
    private static final DatabaseCommunicator INSTANCE = new DatabaseCommunicator();

    public static DatabaseCommunicator getInstance() {
        return INSTANCE;
    }

    private DatabaseCommunicator() {
        connect();
        setCooldown();
    }

    private void connect() {
        try {
            String url = "jdbc:sqlite:src/main/resources/Discord.db";
            connection = DriverManager.getConnection(url);
            Statement statement = connection.createStatement();

            // Create the data table if it doesn't exist
            statement.execute("CREATE TABLE IF NOT EXISTS ranks(channel text, id text, rating int, posRatings int, negRatings int)"); // Do this in case it is their first installation

            statement.execute("CREATE TABLE IF NOT EXISTS cooldowns(ratingPerson text, ratedPerson text, timeRated long)");

            connected = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Rank aspect:

    public void setData(String channel, String id, int rating, int posRatings, int negRatings) {
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
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery(SELECT_SPECIFIC.replace("%id%", id).replaceAll("%channel%", channel));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean rowExists(String channel, String id) { // Needs to be an entry under a certain channel and id
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

    // Cooldown aspect:

    public void addCooldown(String rater, String rated) {
        try {
            Statement statement = connection.createStatement();
            if (getCooldownEntry(rater, rated).next()) {
                statement.executeUpdate("UPDATE cooldowns " + "SET timeRated = " + System.currentTimeMillis() + " WHERE ratingPerson LIKE " + rater + " AND ratedPerson LIKE " + rated);
            } else {
                statement.executeUpdate("INSERT INTO cooldowns VALUES('" + rater + "', '" + rated + "', " + System.currentTimeMillis() + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private ResultSet getCooldownEntry(String rater, String rated) {
        try {
            Statement statement = connection.createStatement();
            return statement.executeQuery("SELECT * FROM cooldowns WHERE ratingPerson LIKE '" + rater + "' AND ratedPerson like '" + rated + "'");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getCooldown(String rater, String rated) { // Returns the time in seconds remaining, or -1 if the cooldown is either over or no cooldown existed
        try {
            ResultSet result = getCooldownEntry(rater, rated);
            if (result.next()) { // an entry was found
                int value = cooldown - (int) (System.currentTimeMillis() - result.getLong("timeRated")) / 1000; // get the number of seconds remaining [cooldown set to 6 hours for now]
                return value < 0 ? -1 : value;
            } else {
                return -1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private void setCooldown() {
        String json = FileHelper.getFileAsString(Charset.defaultCharset());
        JsonParser parser = new JsonParser();
        JsonObject root = parser.parse(json).getAsJsonObject();
        cooldown = root.get("rep_cooldown_time_seconds").getAsInt();
    }

    // For leaderboards:

    public List<Object[]> getLeaderboard(int depth) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ranks ORDER BY rating DESC LIMIT "  + depth);
            List<Object[]> ranking = new ArrayList<>();
            while (resultSet.next()) { // While there are valid entries in the result set
                String id = resultSet.getString("id");
                int rating = resultSet.getInt("rating");
                ranking.add(new Object[]{id, rating});
            }
            return ranking;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
