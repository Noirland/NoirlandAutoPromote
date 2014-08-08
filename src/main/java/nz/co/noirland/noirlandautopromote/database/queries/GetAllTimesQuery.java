package nz.co.noirland.noirlandautopromote.database.queries;

public class GetAllTimesQuery extends PromoteQuery {

    private static final String QUERY = "SELECT * FROM `{PREFIX}_times` ORDER BY `totalPlayTime` DESC";

    public GetAllTimesQuery() {
        super(QUERY);
    }

}
