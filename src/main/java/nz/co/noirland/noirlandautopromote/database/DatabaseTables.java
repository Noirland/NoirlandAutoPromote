package nz.co.noirland.noirlandautopromote.database;

import nz.co.noirland.noirlandautopromote.config.PluginConfig;

public enum DatabaseTables {
    TIMES("times"),
    SCHEMA("schema");

    private final String name;

    private DatabaseTables(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return PluginConfig.inst().getPrefix() + name;
    }
}
