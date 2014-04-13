package nz.co.noirland.noirlandautopromote.database;

import nz.co.noirland.noirlandautopromote.NoirlandAutoPromote;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class AsyncDatabaseUpdateTask extends BukkitRunnable {

    public final static CopyOnWriteArrayList<PreparedStatement> updates = new CopyOnWriteArrayList<PreparedStatement>();

    @Override
    public void run() {
        if(!updates.isEmpty()) {
            Iterator<PreparedStatement> it = updates.iterator();
            ArrayList<PreparedStatement> remove = new ArrayList<PreparedStatement>();
            while(it.hasNext()) {
                PreparedStatement statement = it.next();
                remove.add(statement);
                try {
                    if(statement.isClosed()) continue;
                    statement.execute();
                    NoirlandAutoPromote.debug().debug("Executed db update statement " + statement.toString());
                } catch (SQLException e) {
                    NoirlandAutoPromote.debug().warning("Failed to execute update statement " + statement.toString(), e);
                } finally {
                    try {
                        statement.close();
                    } catch (SQLException e) {
                        NoirlandAutoPromote.debug().warning("Could not close statement " + statement.toString(), e);
                    }
                }
            }
            updates.removeAll(remove);
        }
    }

}
