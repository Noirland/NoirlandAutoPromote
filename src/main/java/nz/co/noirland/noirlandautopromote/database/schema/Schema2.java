package nz.co.noirland.noirlandautopromote.database.schema;

import nz.co.noirland.noirlandautopromote.NoirlandAutoPromote;
import nz.co.noirland.noirlandautopromote.database.queries.GetAllTimesQuery;
import nz.co.noirland.noirlandautopromote.database.queries.PromoteQuery;
import nz.co.noirland.zephcore.UUIDFetcher;
import nz.co.noirland.zephcore.database.Schema;
import nz.co.noirland.zephcore.database.queries.Query;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Schema2 implements Schema {

    @Override
    public void run() {
        try {
            convertNamesToUUID();
            updateSchema();
        } catch (SQLException e) {
            NoirlandAutoPromote.debug().disable("Failed to update schema!");
        }
    }

    private void updateSchema() throws SQLException {
        new PromoteQuery("UPDATE `{PREFIX}_schema` SET `version` = 2").execute();
    }

    private void convertNamesToUUID() throws SQLException {
        NoirlandAutoPromote.debug().warning("Updating database players to UUIDs (this may take a while)");
        List<Map<String, Object>> res = new GetAllTimesQuery().executeQuery();

        ArrayList<String> names = new ArrayList<String>();
        for(Map<String, Object> row : res) {
            names.add((String) row.get("player"));
        }

        Map<String, UUID> uuids = UUIDFetcher.getUUIDs(names);

        new PromoteQuery("ALTER TABLE `{PREFIX}_times` MODIFY COLUMN `player` VARCHAR(36)").execute();

        Query updateQuery = new PromoteQuery(2, "UPDATE {PREFIX}_times SET player=? WHERE player=?");

        for(Map.Entry<String, UUID> entry : uuids.entrySet()) {
            updateQuery.setValue(1, entry.getValue().toString());
            updateQuery.setValue(2, entry.getKey());
            try {
                updateQuery.execute();
            } catch (SQLException e) {
                NoirlandAutoPromote.debug().warning("Could not update " + entry.getKey() + " to UUID!", e);
            }
        }

        NoirlandAutoPromote.debug().warning("Completed update to UUIDs.");
    }
}
