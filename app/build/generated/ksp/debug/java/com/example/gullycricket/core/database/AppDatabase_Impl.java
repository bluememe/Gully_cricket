package com.example.gullycricket.core.database;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.example.gullycricket.core.database.dao.MatchDao;
import com.example.gullycricket.core.database.dao.MatchDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class AppDatabase_Impl extends AppDatabase {
  private volatile MatchDao _matchDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(3) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `matches` (`matchId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `team1Name` TEXT NOT NULL, `team2Name` TEXT NOT NULL, `totalOvers` INTEGER NOT NULL, `team1Players` INTEGER NOT NULL, `team2Players` INTEGER NOT NULL, `lastManStandingAllowed` INTEGER NOT NULL, `currentInnings` INTEGER NOT NULL, `targetScore` INTEGER, `isComplete` INTEGER NOT NULL)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `ball_events` (`eventId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `matchId` INTEGER NOT NULL, `overNumber` INTEGER NOT NULL, `ballNumberInOver` INTEGER NOT NULL, `strikerId` INTEGER NOT NULL, `nonStrikerId` INTEGER NOT NULL, `bowlerId` INTEGER NOT NULL, `runsScored` INTEGER NOT NULL, `extraType` TEXT NOT NULL, `extraRuns` INTEGER NOT NULL, `wicketType` TEXT NOT NULL, `playerOutId` INTEGER, `innings` INTEGER NOT NULL, `timestamp` INTEGER NOT NULL, FOREIGN KEY(`matchId`) REFERENCES `matches`(`matchId`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_ball_events_matchId` ON `ball_events` (`matchId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `players` (`playerId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `matchId` INTEGER NOT NULL, `teamNumber` INTEGER NOT NULL, `playerName` TEXT NOT NULL, FOREIGN KEY(`matchId`) REFERENCES `matches`(`matchId`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_players_matchId` ON `players` (`matchId`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, 'e1b8b269af094918e1b83304d831b87e')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `matches`");
        db.execSQL("DROP TABLE IF EXISTS `ball_events`");
        db.execSQL("DROP TABLE IF EXISTS `players`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsMatches = new HashMap<String, TableInfo.Column>(10);
        _columnsMatches.put("matchId", new TableInfo.Column("matchId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("team1Name", new TableInfo.Column("team1Name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("team2Name", new TableInfo.Column("team2Name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("totalOvers", new TableInfo.Column("totalOvers", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("team1Players", new TableInfo.Column("team1Players", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("team2Players", new TableInfo.Column("team2Players", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("lastManStandingAllowed", new TableInfo.Column("lastManStandingAllowed", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("currentInnings", new TableInfo.Column("currentInnings", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("targetScore", new TableInfo.Column("targetScore", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsMatches.put("isComplete", new TableInfo.Column("isComplete", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysMatches = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesMatches = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoMatches = new TableInfo("matches", _columnsMatches, _foreignKeysMatches, _indicesMatches);
        final TableInfo _existingMatches = TableInfo.read(db, "matches");
        if (!_infoMatches.equals(_existingMatches)) {
          return new RoomOpenHelper.ValidationResult(false, "matches(com.example.gullycricket.core.database.entity.MatchEntity).\n"
                  + " Expected:\n" + _infoMatches + "\n"
                  + " Found:\n" + _existingMatches);
        }
        final HashMap<String, TableInfo.Column> _columnsBallEvents = new HashMap<String, TableInfo.Column>(14);
        _columnsBallEvents.put("eventId", new TableInfo.Column("eventId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBallEvents.put("matchId", new TableInfo.Column("matchId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBallEvents.put("overNumber", new TableInfo.Column("overNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBallEvents.put("ballNumberInOver", new TableInfo.Column("ballNumberInOver", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBallEvents.put("strikerId", new TableInfo.Column("strikerId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBallEvents.put("nonStrikerId", new TableInfo.Column("nonStrikerId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBallEvents.put("bowlerId", new TableInfo.Column("bowlerId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBallEvents.put("runsScored", new TableInfo.Column("runsScored", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBallEvents.put("extraType", new TableInfo.Column("extraType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBallEvents.put("extraRuns", new TableInfo.Column("extraRuns", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBallEvents.put("wicketType", new TableInfo.Column("wicketType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBallEvents.put("playerOutId", new TableInfo.Column("playerOutId", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBallEvents.put("innings", new TableInfo.Column("innings", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsBallEvents.put("timestamp", new TableInfo.Column("timestamp", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysBallEvents = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysBallEvents.add(new TableInfo.ForeignKey("matches", "CASCADE", "NO ACTION", Arrays.asList("matchId"), Arrays.asList("matchId")));
        final HashSet<TableInfo.Index> _indicesBallEvents = new HashSet<TableInfo.Index>(1);
        _indicesBallEvents.add(new TableInfo.Index("index_ball_events_matchId", false, Arrays.asList("matchId"), Arrays.asList("ASC")));
        final TableInfo _infoBallEvents = new TableInfo("ball_events", _columnsBallEvents, _foreignKeysBallEvents, _indicesBallEvents);
        final TableInfo _existingBallEvents = TableInfo.read(db, "ball_events");
        if (!_infoBallEvents.equals(_existingBallEvents)) {
          return new RoomOpenHelper.ValidationResult(false, "ball_events(com.example.gullycricket.core.database.entity.BallEventEntity).\n"
                  + " Expected:\n" + _infoBallEvents + "\n"
                  + " Found:\n" + _existingBallEvents);
        }
        final HashMap<String, TableInfo.Column> _columnsPlayers = new HashMap<String, TableInfo.Column>(4);
        _columnsPlayers.put("playerId", new TableInfo.Column("playerId", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayers.put("matchId", new TableInfo.Column("matchId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayers.put("teamNumber", new TableInfo.Column("teamNumber", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsPlayers.put("playerName", new TableInfo.Column("playerName", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysPlayers = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysPlayers.add(new TableInfo.ForeignKey("matches", "CASCADE", "NO ACTION", Arrays.asList("matchId"), Arrays.asList("matchId")));
        final HashSet<TableInfo.Index> _indicesPlayers = new HashSet<TableInfo.Index>(1);
        _indicesPlayers.add(new TableInfo.Index("index_players_matchId", false, Arrays.asList("matchId"), Arrays.asList("ASC")));
        final TableInfo _infoPlayers = new TableInfo("players", _columnsPlayers, _foreignKeysPlayers, _indicesPlayers);
        final TableInfo _existingPlayers = TableInfo.read(db, "players");
        if (!_infoPlayers.equals(_existingPlayers)) {
          return new RoomOpenHelper.ValidationResult(false, "players(com.example.gullycricket.core.database.entity.PlayerEntity).\n"
                  + " Expected:\n" + _infoPlayers + "\n"
                  + " Found:\n" + _existingPlayers);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "e1b8b269af094918e1b83304d831b87e", "ebb16e79a5719ad979f9867fdb7b6756");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "matches","ball_events","players");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `matches`");
      _db.execSQL("DELETE FROM `ball_events`");
      _db.execSQL("DELETE FROM `players`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(MatchDao.class, MatchDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public MatchDao matchDao() {
    if (_matchDao != null) {
      return _matchDao;
    } else {
      synchronized(this) {
        if(_matchDao == null) {
          _matchDao = new MatchDao_Impl(this);
        }
        return _matchDao;
      }
    }
  }
}
