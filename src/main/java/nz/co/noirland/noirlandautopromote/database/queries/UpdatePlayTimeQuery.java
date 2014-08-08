package nz.co.noirland.noirlandautopromote.database.queries;

import nz.co.noirland.noirlandautopromote.PlayerTimeData;

public class UpdatePlayTimeQuery extends PromoteQuery {

    private static final String QUERY = "INSERT INTO {PREFIX}_times VALUES(?,?,?) ON DUPLICATE KEY UPDATE playTime=VALUES(playTime), totalPlayTime=VALUES(totalPlayTime)";

    public UpdatePlayTimeQuery(PlayerTimeData data) {
        super(3, QUERY);
        setValue(1, data.getPlayer().toString());
        setValue(2, data.getPlayTime());
        setValue(3, data.getTotalPlayTime());
    }

}
