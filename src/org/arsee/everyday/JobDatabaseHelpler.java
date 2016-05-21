package org.arsee.everyday;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JobDatabaseHelpler extends SQLiteOpenHelper {

	private static final String DB_NAME = "arsee.everyday";
	private static final int VERSION = 4;
	
	public JobDatabaseHelpler(Context context){
		super(context, DB_NAME, null, VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(" CREATE TABLE `Job` ("+
				"`id` INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"`title` TEXT NOT NULL,"+
				"`type` TEXT NOT NULL)"
		);
		
		
		db.execSQL("CREATE TABLE `TimeArea` ("+
				    "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
				    "`begin` INT NOT NULL,"+
				    "`end` INT NOT NULL)"
		);		
		
		db.execSQL("CREATE TABLE `TimeArea_jobs` ("+
				    "`object_id` INT NOT NULL,"+
				    "`value` INT NULL,"+
				     "CONSTRAINT `TimeArea_jobs_object_id_fk`"+
				      "FOREIGN KEY (`object_id`)"+
				      "REFERENCES `TimeArea` (`id`)"+
				      "ON DELETE CASCADE)"
		);

		db.execSQL("CREATE TABLE Day ("+
				"id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
				"date TEXT NOT NULL,"+
				"whose BIGINT UNSIGNED NULL)"
			);
			  
		db.execSQL("CREATE TABLE Day_areas ("+
				"object_id INT NOT NULL,"+
				"value INT NULL,"+
				"CONSTRAINT Day_areas_object_id_fk "+
				"FOREIGN KEY (object_id)"+
				"REFERENCES Day (id)"+
				"ON DELETE CASCADE)"
			);
		
		db.execSQL(" CREATE TABLE `DayDid` ("+
				"`idx` INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"`date` TEXT NOT NULL,"+
				"`time` TEXT NOT NULL," +
				"`job_id` INTEGER NOT NULL)"
		);
		
		db.execSQL("CREATE TABLE `JobMarker` ("+
			    "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"+
			    "`job` INT NOT NULL,"+
			    "`area` INT NOT NULL,"+
			    "`day` INT NOT NULL)"
		);
		
		db.execSQL("CREATE TABLE `JobLearning` ("+
			    "`key` TEXT NOT NULL PRIMARY KEY ,"+
			    "`type` INT NOT NULL,"+
			    "`clock` INT NOT NULL,"+
			    "`refCount` INT NOT NULL DEFAULT 0,"+
			    "`whose` BIGINT UNSIGNED NOT NULL)"
		);

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub	
		db.execSQL("CREATE TABLE `JobLearning_clock` ("+
			    "`key` TEXT NOT NULL ,"+
			    "`clock` INT NOT NULL NOT NULL,"+
			    "`whose` BIGINT UNSIGNED 	Not NULL)"
		);		
	}

}
