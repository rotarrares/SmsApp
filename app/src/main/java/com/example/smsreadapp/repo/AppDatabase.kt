package com.example.smsreadapp.repo
import androidx.annotation.NonNull;
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity(tableName= "msg_table")

data class Msg(
    @PrimaryKey
    @NotNull
    val message_id : String,
    @ColumnInfo(name = "from") val from: String?,
    @ColumnInfo(name = "sent_timestamp") val timestamp: String?,
    @ColumnInfo(name = "message") val message: String?
)

@Dao
interface MsgDao {

    @Query("SELECT * from msg_table")
    fun getLiveAll(): LiveData<List<Msg>>

    @Query("SELECT * FROM msg_table")
    suspend fun getAll(): List<Msg>

    @Query("SELECT * FROM msg_table WHERE message_id IN (:msgIds)")
    suspend fun loadAllByIds(msgIds: IntArray): List<Msg>

    @Query("SELECT * FROM msg_table WHERE message_id LIKE :id LIMIT 1")
    suspend fun findById(id:String): Msg

    @Insert
    suspend fun insertAll(vararg msg: Msg)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(msg: Msg)

    @Delete
    suspend fun delete(msg: Msg)

    @Query("DELETE FROM msg_table")
    suspend fun deleteAll()
}


@Database(entities = arrayOf(Msg::class), version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun msgDao(): MsgDao

    // Just for adding dummy text

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(
            context: Context
        ): AppDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "msg_database"
                )
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

    }
}
