import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class ExamDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    companion object {
        const val DATABASE_NAME = "exams.db"
        const val DATABASE_VERSION = 1

        const val TABLE_EXAMS = "exams"
        const val COLUMN_ID = "id"
        const val COLUMN_NAME = "name"
        const val COLUMN_TARGET_DATE = "target_date"
        const val COLUMN_IS_PINNED = "is_pinned"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTableSQL = ("CREATE TABLE $TABLE_EXAMS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_NAME TEXT NOT NULL, "
                + "$COLUMN_TARGET_DATE INTEGER NOT NULL, "
                + "$COLUMN_IS_PINNED INTEGER DEFAULT 0)")
        db?.execSQL(createTableSQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_EXAMS")
        onCreate(db)
    }
}