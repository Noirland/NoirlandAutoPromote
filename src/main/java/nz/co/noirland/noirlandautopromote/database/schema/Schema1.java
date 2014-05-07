package nz.co.noirland.noirlandautopromote.database.schema;

import nz.co.noirland.noirlandautopromote.NoirlandAutoPromote;
import nz.co.noirland.noirlandautopromote.database.Database;
import nz.co.noirland.noirlandautopromote.database.DatabaseTables;

import java.sql.SQLException;

public class Schema1 extends Schema {

    private final Database db = Database.inst();

    public void updateDatabase() {
        createPlayersTable();
        createSchemaTable();
    }

    private void createSchemaTable() {
        String schemaTable = DatabaseTables.SCHEMA.toString();
        try{
            db.prepareStatement("CREATE TABLE `" + schemaTable + "` (`version` TINYINT UNSIGNED);").execute();
            db.prepareStatement("INSERT INTO `" + schemaTable + "` VALUES(1);").execute();
        }catch(SQLException e) {
            NoirlandAutoPromote.debug().disable("Could not create schema table!", e);
        }
    }

    private void createPlayersTable() {
        try {
            db.prepareStatement("CREATE TABLE `" + DatabaseTables.TIMES.toString() + "` (player VARCHAR(16), playTime BIGINT UNSIGNED, totalPlayTime BIGINT UNSIGNED, PRIMARY KEY (player))").execute();
        }catch(SQLException e) {
            NoirlandAutoPromote.debug().disable("Couldn't create times table!", e);
        }
    }
}
