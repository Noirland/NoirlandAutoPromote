package nz.co.noirland.noirlandautopromote.database.queries;

import nz.co.noirland.noirlandautopromote.database.PromoteDatabase;
import nz.co.noirland.zephcore.database.MySQLDatabase;
import nz.co.noirland.zephcore.database.queries.Query;

public class PromoteQuery extends Query {

    @Override
    protected MySQLDatabase getDB() {
        return PromoteDatabase.inst();
    }

    public PromoteQuery(int nargs, String query) {
        super(nargs, query);
    }

    public PromoteQuery(String query) {
        super(query);
    }

    public PromoteQuery(Object[] values, String query) {
        super(values, query);
    }
}
