package nz.co.noirland.noirlandautopromote.database;

import nz.co.noirland.noirlandautopromote.NoirlandAutoPromote;
import nz.co.noirland.noirlandautopromote.PlayerTimeData;
import nz.co.noirland.noirlandautopromote.config.PluginConfig;
import nz.co.noirland.noirlandautopromote.util.Debug;

import java.sql.*;
import java.util.ArrayList;

public class Database {

    private static Database inst;
    private NoirlandAutoPromote plugin = NoirlandAutoPromote.inst();
    private Connection con;

    public static Database inst() {
        if(inst == null) {
            inst = new Database();
        }
        return inst;
    }

    private Database() {
        open();
        createDatabase();
    }

    private void createDatabase() {

        try {
            PreparedStatement statement = prepareStatement(Queries.CREATE_TABLE);
            statement.execute();
        } catch (SQLException e) {
            plugin.disable("Could not create/check main table!", e);
        }

    }

    public ArrayList<PlayerTimeData> getTimeData() {

        ArrayList<PlayerTimeData> times = new ArrayList<PlayerTimeData>();

        PreparedStatement statement = prepareStatement(Queries.GET_ALL_TIMES);
        ResultSet res;
        try {
            res = statement.executeQuery();
            while(res.next()) {
                String player = res.getString("player");
                long playTime = res.getLong("playTime");
                long totalPlayTime = res.getLong("totalPlayTime");
                times.add(new PlayerTimeData(player, playTime, totalPlayTime));
            }
            res.close();
            Debug.debug("Found " + times.size() + " time entries.");
        } catch (SQLException e) {
            plugin.disable("Couldn't get play times!", e);
            return null;
        }
        return times;
    }

    public void updatePlayerTimes(String player, long playTime, long totalPlayTime, boolean thread) {

        PreparedStatement statement = prepareStatement(Queries.UPDATE_PLAY_TIMES);
        try {
            statement.setLong(1, playTime);
            statement.setLong(2, totalPlayTime);
            statement.setString(3, player);
            if(thread) {
                runStatementAsync(statement);
            }else{
                statement.execute();
            }
        } catch (SQLException e) {
            Debug.debug("Could not create statement for updating player times for " + player, e);
        }
    }

    public void addPlayer(String player, long playTime, long totalPlayTime) {
        PreparedStatement statement = prepareStatement(Queries.ADD_PLAYER);
        try {
            statement.setString(1, player);
            statement.setLong(2, playTime);
            statement.setLong(3, totalPlayTime);
            runStatementAsync(statement);
        }catch (SQLException e) {
            Debug.debug("Could not create statement for creating entry for " + player, e);
        }
    }

    // -- DATABASE FUNCTIONS -- //

    public PreparedStatement prepareStatement(String query) {
        try {
            if(query.startsWith("INSERT")) {
                return con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            }
            return con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        } catch (SQLException e) {
            plugin.disable("Could not create statement for database!", e);
            return null;
        }
    }

    public void runStatementAsync(PreparedStatement statement) {
        new AsyncStatementTask(statement).runTaskAsynchronously(plugin);
    }

    public void open() {
        PluginConfig config = PluginConfig.inst();
        String url = "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabase();
        try {
            con = DriverManager.getConnection(url, config.getUsername(), config.getPassword());
        } catch (SQLException e) {
            plugin.disable("Couldn't connect to database!", e);
        }
    }

    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            Debug.debug("Couldn't close connection to database.", e);
        }
    }

    // -- UTILITY FUNCTIONS -- //

    public static int getNumRows(ResultSet rs) {
        try {
            rs.last();
            return rs.getRow();
        } catch (SQLException e) {
            Debug.debug("Could not get number of rows of result set!", e);
            return -1;
        }
    }
}
