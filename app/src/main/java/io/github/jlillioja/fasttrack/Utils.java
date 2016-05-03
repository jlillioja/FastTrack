package io.github.jlillioja.fasttrack;

import android.content.ContentValues;

/**
 * Created by jacob on 5/3/2016.
 */
public class Utils {

    public static ContentValues formatAgent(int agentId, String field, String newValue) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseContract.Agent._ID, agentId);
        contentValues.put(field, newValue);
        return contentValues;
    }

}
