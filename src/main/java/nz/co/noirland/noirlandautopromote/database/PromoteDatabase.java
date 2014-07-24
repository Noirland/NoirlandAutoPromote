package nz.co.noirland.noirlandautopromote.database;

import nz.co.noirland.noirlandautopromote.NoirlandAutoPromote;
import nz.co.noirland.noirlandautopromote.PlayerTimeData;
import nz.co.noirland.noirlandautopromote.config.PluginConfig;
import nz.co.noirland.noirlandautopromote.database.queries.GetAllTimesQuery;
import nz.co.noirland.noirlandautopromote.database.queries.UpdatePlayTimeQuery;
import nz.co.noirland.zephcore.Debug;
import nz.co.noirland.zephcore.database.MySQLDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PromoteDatabase extends MySQLDatabase {

    private static PromoteDatabase inst;

    private static final PluginConfig config = PluginConfig.inst();

    public static PromoteDatabase inst() {
        if(inst == null) {
            inst = new PromoteDatabase();
        }
        return inst;
    }

    @Override
    public Debug debug() {
        return NoirlandAutoPromote.debug();
    }

    @Override
    protected String getHost() {
        return config.getHost();
    }

    @Override
    protected int getPort() {
        return config.getPort();
    }

    @Override
    protected String getDatabase() {
        return config.getDatabase();
    }

    @Override
    protected String getUsername() {
        return config.getUsername();
    }

    @Override
    protected String getPassword() {
        return config.getPassword();
    }

    @Override
    public String getPrefix() {
        return config.getPrefix();
    }

    private PromoteDatabase() {
        inst = this;
    }

    public ArrayList<PlayerTimeData> getTimeData() {

        ArrayList<PlayerTimeData> times = new ArrayList<PlayerTimeData>();

        List<Map<String, Object>> res;
        try {
            res = new GetAllTimesQuery().executeQuery();
        } catch (SQLException e) {
            debug().warning("Could not fetch times from the database!", e);
            return times;
        }

        for(Map<String, Object> row : res) {
            UUID player = UUID.fromString((String) row.get("player"));
            long playTime = (Long) row.get("playTime");
            long totalPlayTime = (Long) row.get("totalPlayTime");
            times.add(new PlayerTimeData(player, playTime, totalPlayTime));
        }
        return times;
    }

    public void updatePlayerTimes(PlayerTimeData data) {
        new UpdatePlayTimeQuery(data).executeAsync();
    }
}
