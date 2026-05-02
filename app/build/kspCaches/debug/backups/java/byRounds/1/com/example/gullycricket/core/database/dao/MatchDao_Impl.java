package com.example.gullycricket.core.database.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.example.gullycricket.core.database.entity.BallEventEntity;
import com.example.gullycricket.core.database.entity.MatchEntity;
import com.example.gullycricket.core.database.entity.PlayerEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Integer;
import java.lang.Long;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class MatchDao_Impl implements MatchDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<MatchEntity> __insertionAdapterOfMatchEntity;

  private final EntityInsertionAdapter<PlayerEntity> __insertionAdapterOfPlayerEntity;

  private final EntityInsertionAdapter<BallEventEntity> __insertionAdapterOfBallEventEntity;

  private final EntityDeletionOrUpdateAdapter<MatchEntity> __deletionAdapterOfMatchEntity;

  private final EntityDeletionOrUpdateAdapter<MatchEntity> __updateAdapterOfMatchEntity;

  private final SharedSQLiteStatement __preparedStmtOfDeleteLastBallEventIfInCurrentOver;

  public MatchDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfMatchEntity = new EntityInsertionAdapter<MatchEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `matches` (`matchId`,`team1Name`,`team2Name`,`totalOvers`,`team1Players`,`team2Players`,`lastManStandingAllowed`,`currentInnings`,`targetScore`,`isComplete`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MatchEntity entity) {
        statement.bindLong(1, entity.getMatchId());
        statement.bindString(2, entity.getTeam1Name());
        statement.bindString(3, entity.getTeam2Name());
        statement.bindLong(4, entity.getTotalOvers());
        statement.bindLong(5, entity.getTeam1Players());
        statement.bindLong(6, entity.getTeam2Players());
        final int _tmp = entity.getLastManStandingAllowed() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getCurrentInnings());
        if (entity.getTargetScore() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getTargetScore());
        }
        final int _tmp_1 = entity.isComplete() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
      }
    };
    this.__insertionAdapterOfPlayerEntity = new EntityInsertionAdapter<PlayerEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `players` (`playerId`,`matchId`,`teamNumber`,`playerName`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final PlayerEntity entity) {
        statement.bindLong(1, entity.getPlayerId());
        statement.bindLong(2, entity.getMatchId());
        statement.bindLong(3, entity.getTeamNumber());
        statement.bindString(4, entity.getPlayerName());
      }
    };
    this.__insertionAdapterOfBallEventEntity = new EntityInsertionAdapter<BallEventEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `ball_events` (`eventId`,`matchId`,`overNumber`,`ballNumberInOver`,`strikerId`,`nonStrikerId`,`bowlerId`,`runsScored`,`extraType`,`extraRuns`,`wicketType`,`playerOutId`,`innings`,`timestamp`) VALUES (nullif(?, 0),?,?,?,?,?,?,?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final BallEventEntity entity) {
        statement.bindLong(1, entity.getEventId());
        statement.bindLong(2, entity.getMatchId());
        statement.bindLong(3, entity.getOverNumber());
        statement.bindLong(4, entity.getBallNumberInOver());
        statement.bindLong(5, entity.getStrikerId());
        statement.bindLong(6, entity.getNonStrikerId());
        statement.bindLong(7, entity.getBowlerId());
        statement.bindLong(8, entity.getRunsScored());
        statement.bindString(9, entity.getExtraType());
        statement.bindLong(10, entity.getExtraRuns());
        statement.bindString(11, entity.getWicketType());
        if (entity.getPlayerOutId() == null) {
          statement.bindNull(12);
        } else {
          statement.bindLong(12, entity.getPlayerOutId());
        }
        statement.bindLong(13, entity.getInnings());
        statement.bindLong(14, entity.getTimestamp());
      }
    };
    this.__deletionAdapterOfMatchEntity = new EntityDeletionOrUpdateAdapter<MatchEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `matches` WHERE `matchId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MatchEntity entity) {
        statement.bindLong(1, entity.getMatchId());
      }
    };
    this.__updateAdapterOfMatchEntity = new EntityDeletionOrUpdateAdapter<MatchEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `matches` SET `matchId` = ?,`team1Name` = ?,`team2Name` = ?,`totalOvers` = ?,`team1Players` = ?,`team2Players` = ?,`lastManStandingAllowed` = ?,`currentInnings` = ?,`targetScore` = ?,`isComplete` = ? WHERE `matchId` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final MatchEntity entity) {
        statement.bindLong(1, entity.getMatchId());
        statement.bindString(2, entity.getTeam1Name());
        statement.bindString(3, entity.getTeam2Name());
        statement.bindLong(4, entity.getTotalOvers());
        statement.bindLong(5, entity.getTeam1Players());
        statement.bindLong(6, entity.getTeam2Players());
        final int _tmp = entity.getLastManStandingAllowed() ? 1 : 0;
        statement.bindLong(7, _tmp);
        statement.bindLong(8, entity.getCurrentInnings());
        if (entity.getTargetScore() == null) {
          statement.bindNull(9);
        } else {
          statement.bindLong(9, entity.getTargetScore());
        }
        final int _tmp_1 = entity.isComplete() ? 1 : 0;
        statement.bindLong(10, _tmp_1);
        statement.bindLong(11, entity.getMatchId());
      }
    };
    this.__preparedStmtOfDeleteLastBallEventIfInCurrentOver = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "DELETE FROM ball_events WHERE eventId = (SELECT MAX(eventId) FROM ball_events WHERE matchId = ?) AND overNumber = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertMatch(final MatchEntity match, final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfMatchEntity.insertAndReturnId(match);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertPlayer(final PlayerEntity player,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfPlayerEntity.insert(player);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object insertBallEvent(final BallEventEntity ballEvent,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBallEventEntity.insert(ballEvent);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteMatch(final MatchEntity match, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfMatchEntity.handle(match);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object updateMatch(final MatchEntity match, final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfMatchEntity.handle(match);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object deleteLastBallEventIfInCurrentOver(final long matchId, final int currentOver,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfDeleteLastBallEventIfInCurrentOver.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, matchId);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, currentOver);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfDeleteLastBallEventIfInCurrentOver.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<MatchEntity> getMatch(final long matchId) {
    final String _sql = "SELECT * FROM matches WHERE matchId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, matchId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"matches"}, new Callable<MatchEntity>() {
      @Override
      @NonNull
      public MatchEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMatchId = CursorUtil.getColumnIndexOrThrow(_cursor, "matchId");
          final int _cursorIndexOfTeam1Name = CursorUtil.getColumnIndexOrThrow(_cursor, "team1Name");
          final int _cursorIndexOfTeam2Name = CursorUtil.getColumnIndexOrThrow(_cursor, "team2Name");
          final int _cursorIndexOfTotalOvers = CursorUtil.getColumnIndexOrThrow(_cursor, "totalOvers");
          final int _cursorIndexOfTeam1Players = CursorUtil.getColumnIndexOrThrow(_cursor, "team1Players");
          final int _cursorIndexOfTeam2Players = CursorUtil.getColumnIndexOrThrow(_cursor, "team2Players");
          final int _cursorIndexOfLastManStandingAllowed = CursorUtil.getColumnIndexOrThrow(_cursor, "lastManStandingAllowed");
          final int _cursorIndexOfCurrentInnings = CursorUtil.getColumnIndexOrThrow(_cursor, "currentInnings");
          final int _cursorIndexOfTargetScore = CursorUtil.getColumnIndexOrThrow(_cursor, "targetScore");
          final int _cursorIndexOfIsComplete = CursorUtil.getColumnIndexOrThrow(_cursor, "isComplete");
          final MatchEntity _result;
          if (_cursor.moveToFirst()) {
            final long _tmpMatchId;
            _tmpMatchId = _cursor.getLong(_cursorIndexOfMatchId);
            final String _tmpTeam1Name;
            _tmpTeam1Name = _cursor.getString(_cursorIndexOfTeam1Name);
            final String _tmpTeam2Name;
            _tmpTeam2Name = _cursor.getString(_cursorIndexOfTeam2Name);
            final int _tmpTotalOvers;
            _tmpTotalOvers = _cursor.getInt(_cursorIndexOfTotalOvers);
            final int _tmpTeam1Players;
            _tmpTeam1Players = _cursor.getInt(_cursorIndexOfTeam1Players);
            final int _tmpTeam2Players;
            _tmpTeam2Players = _cursor.getInt(_cursorIndexOfTeam2Players);
            final boolean _tmpLastManStandingAllowed;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfLastManStandingAllowed);
            _tmpLastManStandingAllowed = _tmp != 0;
            final int _tmpCurrentInnings;
            _tmpCurrentInnings = _cursor.getInt(_cursorIndexOfCurrentInnings);
            final Integer _tmpTargetScore;
            if (_cursor.isNull(_cursorIndexOfTargetScore)) {
              _tmpTargetScore = null;
            } else {
              _tmpTargetScore = _cursor.getInt(_cursorIndexOfTargetScore);
            }
            final boolean _tmpIsComplete;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsComplete);
            _tmpIsComplete = _tmp_1 != 0;
            _result = new MatchEntity(_tmpMatchId,_tmpTeam1Name,_tmpTeam2Name,_tmpTotalOvers,_tmpTeam1Players,_tmpTeam2Players,_tmpLastManStandingAllowed,_tmpCurrentInnings,_tmpTargetScore,_tmpIsComplete);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<PlayerEntity>> getPlayersForMatch(final long matchId) {
    final String _sql = "SELECT * FROM players WHERE matchId = ?";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, matchId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"players"}, new Callable<List<PlayerEntity>>() {
      @Override
      @NonNull
      public List<PlayerEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfPlayerId = CursorUtil.getColumnIndexOrThrow(_cursor, "playerId");
          final int _cursorIndexOfMatchId = CursorUtil.getColumnIndexOrThrow(_cursor, "matchId");
          final int _cursorIndexOfTeamNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "teamNumber");
          final int _cursorIndexOfPlayerName = CursorUtil.getColumnIndexOrThrow(_cursor, "playerName");
          final List<PlayerEntity> _result = new ArrayList<PlayerEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final PlayerEntity _item;
            final long _tmpPlayerId;
            _tmpPlayerId = _cursor.getLong(_cursorIndexOfPlayerId);
            final long _tmpMatchId;
            _tmpMatchId = _cursor.getLong(_cursorIndexOfMatchId);
            final int _tmpTeamNumber;
            _tmpTeamNumber = _cursor.getInt(_cursorIndexOfTeamNumber);
            final String _tmpPlayerName;
            _tmpPlayerName = _cursor.getString(_cursorIndexOfPlayerName);
            _item = new PlayerEntity(_tmpPlayerId,_tmpMatchId,_tmpTeamNumber,_tmpPlayerName);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<BallEventEntity>> getBallEventsForMatch(final long matchId) {
    final String _sql = "SELECT * FROM ball_events WHERE matchId = ? ORDER BY timestamp ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, matchId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"ball_events"}, new Callable<List<BallEventEntity>>() {
      @Override
      @NonNull
      public List<BallEventEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfEventId = CursorUtil.getColumnIndexOrThrow(_cursor, "eventId");
          final int _cursorIndexOfMatchId = CursorUtil.getColumnIndexOrThrow(_cursor, "matchId");
          final int _cursorIndexOfOverNumber = CursorUtil.getColumnIndexOrThrow(_cursor, "overNumber");
          final int _cursorIndexOfBallNumberInOver = CursorUtil.getColumnIndexOrThrow(_cursor, "ballNumberInOver");
          final int _cursorIndexOfStrikerId = CursorUtil.getColumnIndexOrThrow(_cursor, "strikerId");
          final int _cursorIndexOfNonStrikerId = CursorUtil.getColumnIndexOrThrow(_cursor, "nonStrikerId");
          final int _cursorIndexOfBowlerId = CursorUtil.getColumnIndexOrThrow(_cursor, "bowlerId");
          final int _cursorIndexOfRunsScored = CursorUtil.getColumnIndexOrThrow(_cursor, "runsScored");
          final int _cursorIndexOfExtraType = CursorUtil.getColumnIndexOrThrow(_cursor, "extraType");
          final int _cursorIndexOfExtraRuns = CursorUtil.getColumnIndexOrThrow(_cursor, "extraRuns");
          final int _cursorIndexOfWicketType = CursorUtil.getColumnIndexOrThrow(_cursor, "wicketType");
          final int _cursorIndexOfPlayerOutId = CursorUtil.getColumnIndexOrThrow(_cursor, "playerOutId");
          final int _cursorIndexOfInnings = CursorUtil.getColumnIndexOrThrow(_cursor, "innings");
          final int _cursorIndexOfTimestamp = CursorUtil.getColumnIndexOrThrow(_cursor, "timestamp");
          final List<BallEventEntity> _result = new ArrayList<BallEventEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final BallEventEntity _item;
            final long _tmpEventId;
            _tmpEventId = _cursor.getLong(_cursorIndexOfEventId);
            final long _tmpMatchId;
            _tmpMatchId = _cursor.getLong(_cursorIndexOfMatchId);
            final int _tmpOverNumber;
            _tmpOverNumber = _cursor.getInt(_cursorIndexOfOverNumber);
            final int _tmpBallNumberInOver;
            _tmpBallNumberInOver = _cursor.getInt(_cursorIndexOfBallNumberInOver);
            final long _tmpStrikerId;
            _tmpStrikerId = _cursor.getLong(_cursorIndexOfStrikerId);
            final long _tmpNonStrikerId;
            _tmpNonStrikerId = _cursor.getLong(_cursorIndexOfNonStrikerId);
            final long _tmpBowlerId;
            _tmpBowlerId = _cursor.getLong(_cursorIndexOfBowlerId);
            final int _tmpRunsScored;
            _tmpRunsScored = _cursor.getInt(_cursorIndexOfRunsScored);
            final String _tmpExtraType;
            _tmpExtraType = _cursor.getString(_cursorIndexOfExtraType);
            final int _tmpExtraRuns;
            _tmpExtraRuns = _cursor.getInt(_cursorIndexOfExtraRuns);
            final String _tmpWicketType;
            _tmpWicketType = _cursor.getString(_cursorIndexOfWicketType);
            final Long _tmpPlayerOutId;
            if (_cursor.isNull(_cursorIndexOfPlayerOutId)) {
              _tmpPlayerOutId = null;
            } else {
              _tmpPlayerOutId = _cursor.getLong(_cursorIndexOfPlayerOutId);
            }
            final int _tmpInnings;
            _tmpInnings = _cursor.getInt(_cursorIndexOfInnings);
            final long _tmpTimestamp;
            _tmpTimestamp = _cursor.getLong(_cursorIndexOfTimestamp);
            _item = new BallEventEntity(_tmpEventId,_tmpMatchId,_tmpOverNumber,_tmpBallNumberInOver,_tmpStrikerId,_tmpNonStrikerId,_tmpBowlerId,_tmpRunsScored,_tmpExtraType,_tmpExtraRuns,_tmpWicketType,_tmpPlayerOutId,_tmpInnings,_tmpTimestamp);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<MatchEntity>> getAllMatches() {
    final String _sql = "SELECT * FROM matches ORDER BY matchId DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"matches"}, new Callable<List<MatchEntity>>() {
      @Override
      @NonNull
      public List<MatchEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfMatchId = CursorUtil.getColumnIndexOrThrow(_cursor, "matchId");
          final int _cursorIndexOfTeam1Name = CursorUtil.getColumnIndexOrThrow(_cursor, "team1Name");
          final int _cursorIndexOfTeam2Name = CursorUtil.getColumnIndexOrThrow(_cursor, "team2Name");
          final int _cursorIndexOfTotalOvers = CursorUtil.getColumnIndexOrThrow(_cursor, "totalOvers");
          final int _cursorIndexOfTeam1Players = CursorUtil.getColumnIndexOrThrow(_cursor, "team1Players");
          final int _cursorIndexOfTeam2Players = CursorUtil.getColumnIndexOrThrow(_cursor, "team2Players");
          final int _cursorIndexOfLastManStandingAllowed = CursorUtil.getColumnIndexOrThrow(_cursor, "lastManStandingAllowed");
          final int _cursorIndexOfCurrentInnings = CursorUtil.getColumnIndexOrThrow(_cursor, "currentInnings");
          final int _cursorIndexOfTargetScore = CursorUtil.getColumnIndexOrThrow(_cursor, "targetScore");
          final int _cursorIndexOfIsComplete = CursorUtil.getColumnIndexOrThrow(_cursor, "isComplete");
          final List<MatchEntity> _result = new ArrayList<MatchEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final MatchEntity _item;
            final long _tmpMatchId;
            _tmpMatchId = _cursor.getLong(_cursorIndexOfMatchId);
            final String _tmpTeam1Name;
            _tmpTeam1Name = _cursor.getString(_cursorIndexOfTeam1Name);
            final String _tmpTeam2Name;
            _tmpTeam2Name = _cursor.getString(_cursorIndexOfTeam2Name);
            final int _tmpTotalOvers;
            _tmpTotalOvers = _cursor.getInt(_cursorIndexOfTotalOvers);
            final int _tmpTeam1Players;
            _tmpTeam1Players = _cursor.getInt(_cursorIndexOfTeam1Players);
            final int _tmpTeam2Players;
            _tmpTeam2Players = _cursor.getInt(_cursorIndexOfTeam2Players);
            final boolean _tmpLastManStandingAllowed;
            final int _tmp;
            _tmp = _cursor.getInt(_cursorIndexOfLastManStandingAllowed);
            _tmpLastManStandingAllowed = _tmp != 0;
            final int _tmpCurrentInnings;
            _tmpCurrentInnings = _cursor.getInt(_cursorIndexOfCurrentInnings);
            final Integer _tmpTargetScore;
            if (_cursor.isNull(_cursorIndexOfTargetScore)) {
              _tmpTargetScore = null;
            } else {
              _tmpTargetScore = _cursor.getInt(_cursorIndexOfTargetScore);
            }
            final boolean _tmpIsComplete;
            final int _tmp_1;
            _tmp_1 = _cursor.getInt(_cursorIndexOfIsComplete);
            _tmpIsComplete = _tmp_1 != 0;
            _item = new MatchEntity(_tmpMatchId,_tmpTeam1Name,_tmpTeam2Name,_tmpTotalOvers,_tmpTeam1Players,_tmpTeam2Players,_tmpLastManStandingAllowed,_tmpCurrentInnings,_tmpTargetScore,_tmpIsComplete);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
