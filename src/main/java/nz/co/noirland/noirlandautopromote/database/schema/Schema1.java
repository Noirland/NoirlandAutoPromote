package nz.co.noirland.noirlandautopromote.database.schema;

import nz.co.noirland.noirlandautopromote.NoirlandAutoPromote;
import nz.co.noirland.noirlandautopromote.database.queries.PromoteQuery;
import nz.co.noirland.zephcore.database.Schema;

import java.sql.SQLException;

public class Schema1 implements Schema {

    public void run() {
        try {
            createPlayersTable();
            createSchemaTable();
        } catch (SQLException e) {
            NoirlandAutoPromote.debug().disable("Unable to update database to schema 1!", e);
        }
    }

    private void createSchemaTable() throws SQLException {
        new PromoteQuery("CREATE TABLE `{PREFIX}_schema` (`version` TINYINT UNSIGNED);").execute();
        new PromoteQuery("INSERT INTO `{PREFIX}_schema` VALUES(1);").execute();
    }

    private void createPlayersTable() throws SQLException {
        new PromoteQuery("CREATE TABLE `{PREFIX}_times` (player VARCHAR(16), playTime BIGINT UNSIGNED, totalPlayTime BIGINT UNSIGNED, PRIMARY KEY (player))").execute();
    }
}
