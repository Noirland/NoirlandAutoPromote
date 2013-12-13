package nz.co.noirland.noirlandautopromote.database;

import nz.co.noirland.noirlandautopromote.util.Debug;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AsyncStatementTask extends BukkitRunnable {

    private PreparedStatement statement;

    public AsyncStatementTask(PreparedStatement statement) {
        this.statement = statement;
    }

    @Override
    public void run() {
        try {
            statement.execute();
            Debug.debug("Run statement async: " + statement.toString());
            statement.close();
        } catch (SQLException e) {
            Debug.debug("Could not execute statement " + statement.toString(), e);
        }
    }
}
