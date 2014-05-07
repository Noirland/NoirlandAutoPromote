package nz.co.noirland.noirlandautopromote.database;

public class Queries {

    public static final String GET_SCHEMA = "SELECT `version` FROM `" + DatabaseTables.SCHEMA.toString() + "`";

    public static final String GET_ALL_TIMES = "SELECT * FROM `" + DatabaseTables.TIMES.toString() + "`";
    public static final String UPDATE_PLAY_TIMES = "UPDATE `" + DatabaseTables.TIMES.toString() + "` SET `playTime` = ?, `totalPlayTime` = ? WHERE `player` = ?";
    public static final String ADD_PLAYER = "INSERT INTO `" + DatabaseTables.TIMES.toString() + "` VALUES(?,?,?)";

}
