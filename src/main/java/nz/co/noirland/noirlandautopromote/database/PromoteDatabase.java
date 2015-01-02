package nz.co.noirland.noirlandautopromote.database;

import nz.co.noirland.noirlandautopromote.NoirlandAutoPromote;
import nz.co.noirland.noirlandautopromote.PlayerTimeData;
import nz.co.noirland.noirlandautopromote.PromoteConfig;
import nz.co.noirland.noirlandautopromote.database.queries.GetAllTimesQuery;
import nz.co.noirland.noirlandautopromote.database.queries.UpdatePlayTimeQuery;
import nz.co.noirland.noirlandautopromote.database.schema.Schema1;
import nz.co.noirland.noirlandautopromote.database.schema.Schema2;
import nz.co.noirland.zephcore.Debug;
import nz.co.noirland.zephcore.database.mysql.MySQLDatabase;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PromoteDatabase extends MySQLDatabase {

    private static PromoteDatabase inst;

    private static final PromoteConfig config = PromoteConfig.inst();

    public static PromoteDatabase inst() {
        if(inst == null) {
            inst = new PromoteDatabase();
        }
        return inst;
    }

    private PromoteDatabase() {
        inst = this;
        schemas.put(1, new Schema1());
        schemas.put(2, new Schema2());
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
            long playTime = ((Number) row.get("playTime")).longValue();
            long totalPlayTime = ((Number) row.get("totalPlayTime")).longValue();
            times.add(new PlayerTimeData(player, playTime, totalPlayTime));
        }
        return times;
    }

    public void updatePlayerTimes(PlayerTimeData data) {
        new UpdatePlayTimeQuery(data).executeAsync();
    }
}
