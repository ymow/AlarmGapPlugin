package com.simasteam.phonegap.plugin.alarmgap;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DataBaseOpenHelper extends OrmLiteSqliteOpenHelper {
	
    private static final String DATABASE_NAME = "_alarmgap.db";

    private static final int DATABASE_VERSION = 1;
	
    private Dao<AlarmBean, Long> alarmDAO;
    private Dao<NotificationBean, Long> notificationDAO;
	
	public DataBaseOpenHelper(Context context) {
        super( context, DATABASE_NAME, null, DATABASE_VERSION );
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, NotificationBean.class);
			TableUtils.createTable(connectionSource, AlarmBean.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
	}
	
	@Override
	public void close() {
		super.close();
		alarmDAO = null;
		notificationDAO = null;
	}
	
	public Dao<AlarmBean, Long> getAlarmDao() throws SQLException {
        if ( alarmDAO == null ) {
        	alarmDAO = getDao( AlarmBean.class );
        }
        return alarmDAO;
	}
	
	public Dao<NotificationBean, Long> getNotificationDao() throws SQLException {
		if ( notificationDAO == null ) {
			notificationDAO = getDao( NotificationBean.class );
		}
		
		return notificationDAO;
	}
	
}
