package nz.co.noirland.noirlandautopromote.database;

public class Queries {

    public static final String GET_SCHEMA = "SELECT `version` FROM `" + DatabaseTables.SCHEMA.toString() + "`";

    public static final String GET_ALL_TIMES = "SELECT * FROM `" + DatabaseTables.TIMES.toString() + "`";
    public static final String UPDATE_PLAY_TIMES = "INSERT INTO `" + DatabaseTables.TIMES.toString() + "` VALUES(?,?,?) ON DUPLICATE KEY UPDATE `playTime`=VALUES(`playTime`) `totalPlayTime`=VALUES(`totalPlayTime`)";

}
