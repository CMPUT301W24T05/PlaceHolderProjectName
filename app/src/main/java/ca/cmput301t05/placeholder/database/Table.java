package ca.cmput301t05.placeholder.database;

import android.content.Context;

public abstract class Table {
    protected Context context;
    protected DatabaseManager databaseManager;

    public Table(Context context){
        this.context = context;
        this.databaseManager = DatabaseManager.getInstance();
    }
}
