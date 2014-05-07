package nz.co.noirland.noirlandautopromote.database.schema;

import nz.co.noirland.noirlandautopromote.NoirlandAutoPromote;
import nz.co.noirland.noirlandautopromote.config.PluginConfig;
import nz.co.noirland.noirlandautopromote.database.Database;
import nz.co.noirland.noirlandautopromote.database.DatabaseTables;
import nz.co.noirland.noirlandautopromote.database.Queries;
import nz.co.noirland.zephcore.UUIDFetcher;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

public class Schema2 extends Schema {

    private final Database db = Database.inst();

    @Override
    public void updateDatabase() {
        convertNamesToUUID();
        try {
            db.prepareStatement("UPDATE `" + PluginConfig.inst().getPrefix() + "schema` SET `version` = 2").execute();
        } catch (SQLException e) {
            NoirlandAutoPromote.debug().disable("Failed to update schema!");
        }
    }

    private void convertNamesToUUID() {
        PreparedStatement statement = db.prepareStatement(Queries.GET_ALL_TIMES);
        NoirlandAutoPromote.debug().warning("Updating database players to UUIDs.");
        try {
            ResultSet res = statement.executeQuery();
            ArrayList<String> names = new ArrayList<String>();
            while(res.next()) {
                names.add(res.getString("player"));
            }
            statement.close();

            db.prepareStatement("ALTER TABLE `" + DatabaseTables.TIMES.toString() + "` MODIFY COLUMN `player` VARCHAR(36)").execute();

            Map<String, UUID> uuids = UUIDFetcher.getUUIDs(names);
            for(Map.Entry<String, UUID> entry : uuids.entrySet()) {
                PreparedStatement s = db.prepareStatement("UPDATE " + DatabaseTables.TIMES.toString() + " SET player=? WHERE player=?");
                s.setString(1, entry.getValue().toString());
                s.setString(2, entry.getKey());
                s.execute();
            }
            NoirlandAutoPromote.debug().warning("Completed update to UUIDs.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
