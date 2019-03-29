package com.example.lorebase.greenDao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import com.example.lorebase.bean.ShareHistory;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "SHARE_HISTORY".
*/
public class ShareHistoryDao extends AbstractDao<ShareHistory, Long> {

    public static final String TABLENAME = "SHARE_HISTORY";

    /**
     * Properties of entity ShareHistory.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id_share = new Property(0, Long.class, "id_share", true, "_id");
        public final static Property Title = new Property(1, String.class, "title", false, "TITLE");
        public final static Property Link = new Property(2, String.class, "link", false, "LINK");
        public final static Property Date = new Property(3, String.class, "date", false, "DATE");
        public final static Property ShareMan = new Property(4, String.class, "shareMan", false, "SHARE_MAN");
        public final static Property Is_collect = new Property(5, boolean.class, "is_collect", false, "IS_COLLECT");
        public final static Property Is_out = new Property(6, boolean.class, "is_out", false, "IS_OUT");
    }


    public ShareHistoryDao(DaoConfig config) {
        super(config);
    }
    
    public ShareHistoryDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"SHARE_HISTORY\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id_share
                "\"TITLE\" TEXT UNIQUE ," + // 1: title
                "\"LINK\" TEXT," + // 2: link
                "\"DATE\" TEXT," + // 3: date
                "\"SHARE_MAN\" TEXT," + // 4: shareMan
                "\"IS_COLLECT\" INTEGER NOT NULL ," + // 5: is_collect
                "\"IS_OUT\" INTEGER NOT NULL );"); // 6: is_out
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"SHARE_HISTORY\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ShareHistory entity) {
        stmt.clearBindings();
 
        Long id_share = entity.getId_share();
        if (id_share != null) {
            stmt.bindLong(1, id_share);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String link = entity.getLink();
        if (link != null) {
            stmt.bindString(3, link);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(4, date);
        }
 
        String shareMan = entity.getShareMan();
        if (shareMan != null) {
            stmt.bindString(5, shareMan);
        }
        stmt.bindLong(6, entity.getIs_collect() ? 1L: 0L);
        stmt.bindLong(7, entity.getIs_out() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ShareHistory entity) {
        stmt.clearBindings();
 
        Long id_share = entity.getId_share();
        if (id_share != null) {
            stmt.bindLong(1, id_share);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(2, title);
        }
 
        String link = entity.getLink();
        if (link != null) {
            stmt.bindString(3, link);
        }
 
        String date = entity.getDate();
        if (date != null) {
            stmt.bindString(4, date);
        }
 
        String shareMan = entity.getShareMan();
        if (shareMan != null) {
            stmt.bindString(5, shareMan);
        }
        stmt.bindLong(6, entity.getIs_collect() ? 1L: 0L);
        stmt.bindLong(7, entity.getIs_out() ? 1L: 0L);
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public ShareHistory readEntity(Cursor cursor, int offset) {
        ShareHistory entity = new ShareHistory( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id_share
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // title
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // link
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // date
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // shareMan
            cursor.getShort(offset + 5) != 0, // is_collect
            cursor.getShort(offset + 6) != 0 // is_out
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ShareHistory entity, int offset) {
        entity.setId_share(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setLink(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setDate(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setShareMan(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setIs_collect(cursor.getShort(offset + 5) != 0);
        entity.setIs_out(cursor.getShort(offset + 6) != 0);
     }
    
    @Override
    protected final Long updateKeyAfterInsert(ShareHistory entity, long rowId) {
        entity.setId_share(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(ShareHistory entity) {
        if(entity != null) {
            return entity.getId_share();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(ShareHistory entity) {
        return entity.getId_share() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}