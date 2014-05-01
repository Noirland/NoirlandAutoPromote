package nz.co.noirland.noirlandautopromote.database;

import nz.co.noirland.noirlandautopromote.NoirlandAutoPromote;
import nz.co.noirland.noirlandautopromote.PlayerTimeData;
import nz.co.noirland.noirlandautopromote.config.PluginConfig;
import nz.co.noirland.noirlandautopromote.database.schema.Schema;
import nz.co.noirland.zephcore.database.AsyncDatabaseUpdateTask;

import java.sql.*;
import java.util.ArrayList;
import java.util.UUID;

public class Database {

    private static Database inst;
    private Connection con;

    public static Database inst() {
        if(inst == null) {
            inst = new Database();
        }
        return inst;
    }

    private Database() {
        open();
    }

    public ArrayList<PlayerTimeData> getTimeData() {

        ArrayList<PlayerTimeData> times = new ArrayList<PlayerTimeData>();

        PreparedStatement statement = prepareStatement(Queries.GET_ALL_TIMES);
        ResultSet res;
        try {
            res = statement.executeQuery();
            while(res.next()) {
                UUID player = UUID.fromString(res.getString("player"));
                long playTime = res.getLong("playTime");
                long totalPlayTime = res.getLong("totalPlayTime");
                times.add(new PlayerTimeData(player, playTime, totalPlayTime));
            }
            res.close();
            NoirlandAutoPromote.debug().debug("Found " + times.size() + " time entries.");
        } catch (SQLException e) {
            NoirlandAutoPromote.debug().disable("Couldn't get play times!", e);
            return null;
        }
        return times;
    }

    public void updatePlayerTimes(UUID player, long playTime, long totalPlayTime, boolean thread) {

        PreparedStatement statement = prepareStatement(Queries.UPDATE_PLAY_TIMES);
        try {
            statement.setLong(1, playTime);
            statement.setLong(2, totalPlayTime);
            statement.setString(3, player.toString());
            if(thread) {
                runStatementAsync(statement);
            }else{
                statement.execute();
            }
        } catch (SQLException e) {
            NoirlandAutoPromote.debug().debug("Could not create statement for updating player times for " + player, e);
        }
    }

    public void addPlayer(UUID player, long playTime, long totalPlayTime) {
        PreparedStatement statement = prepareStatement(Queries.ADD_PLAYER);
        try {
            statement.setString(1, player.toString());
            statement.setLong(2, playTime);
            statement.setLong(3, totalPlayTime);
            runStatementAsync(statement);
        }catch (SQLException e) {
            NoirlandAutoPromote.debug().debug("Could not create statement for creating entry for " + player, e);
        }
    }

    public boolean isTable(String table) {
        try {
            prepareStatement("SELECT * FROM " + table).execute();
            return true; // Result can never be null, bad logic from earlier versions.
        } catch (SQLException e) {
            return false; // Query failed, table does not exist.
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
            NoirlandAutoPromote.debug().disable("Could not create statement for database!", e);
            return null;
        }
    }

    public void checkSchema() {
        int version = getSchema();
        int latest = Schema.getCurrentSchema();
        if(version == latest) {
            return;
        }
        if(version > latest) {
            NoirlandAutoPromote.debug().disable("Database schema is newer than this plugin version!");
        }

        for(int i = version + 1; i <= latest; i++) {
            Schema.getSchema(i).updateDatabase();
        }

    }

    private int getSchema() {
        try {
            if(isTable(DatabaseTables.SCHEMA.toString())) {
                ResultSet res = con.prepareStatement(Queries.GET_SCHEMA).executeQuery();
                res.first();
                return res.getInt("version");
            }else{
                // SCHEMA table does not exist, tables not set up
                return 0;
            }
        } catch (SQLException e) {
            NoirlandAutoPromote.debug().disable("Could not get database schema!", e);
            return 0;
        }
    }

    public void runStatementAsync(PreparedStatement statement) {
        AsyncDatabaseUpdateTask.updates.add(statement);
    }

    public void open() {
        PluginConfig config = PluginConfig.inst();
        String url = "jdbc:mysql://" + config.getHost() + ":" + config.getPort() + "/" + config.getDatabase();
        try {
            con = DriverManager.getConnection(url, config.getUsername(), config.getPassword());
        } catch (SQLException e) {
            NoirlandAutoPromote.debug().disable("Couldn't connect to database!", e);
        }
    }

    public void close() {
        try {
            con.close();
        } catch (SQLException e) {
            NoirlandAutoPromote.debug().debug("Couldn't close connection to database.", e);
        }
    }

    // -- UTILITY FUNCTIONS -- //

    public static int getNumRows(ResultSet rs) {
        try {
            rs.last();
            return rs.getRow();
        } catch (SQLException e) {
            NoirlandAutoPromote.debug().debug("Could not get number of rows of result set!", e);
            return -1;
        }
    }
}
