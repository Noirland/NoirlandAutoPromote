package nz.co.noirland.noirlandautopromote.database;

public class Queries {

    public static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `promote_times`(player VARCHAR(16), playTime BIGINT UNSIGNED, totalPlayTime BIGINT UNSIGNED, PRIMARY KEY (player))";
    public static String GET_ALL_TIMES = "SELECT * FROM `promote_times`";
    public static String UPDATE_PLAY_TIMES = "UPDATE `promote_times` SET `playTime` = ?, `totalPlayTime` = ? WHERE `player` = ?";
    public static String ADD_PLAYER = "INSERT INTO `promote_times` VALUES(?,?,?)";

}
