package edu.gdpu.countdown.data

import ExamDatabaseHelper
import android.content.ContentValues
import android.content.Context
import android.database.Cursor


class ExamDao(private val context: Context) {

    private val dbHelper = ExamDatabaseHelper(context)

    // 插入一条考试记录
    fun insertExam(exam: Exam): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(ExamDatabaseHelper.COLUMN_NAME, exam.name)
            put(ExamDatabaseHelper.COLUMN_TARGET_DATE, exam.targetDate)
            put(ExamDatabaseHelper.COLUMN_IS_PINNED, if (exam.isPinned) 1 else 0)
        }
        return db.insert(ExamDatabaseHelper.TABLE_EXAMS, null, values)
    }

    // 更新一条考试记录
    fun updateExam(exam: Exam): Int {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(ExamDatabaseHelper.COLUMN_NAME, exam.name)
            put(ExamDatabaseHelper.COLUMN_TARGET_DATE, exam.targetDate)
            put(ExamDatabaseHelper.COLUMN_IS_PINNED, if (exam.isPinned) 1 else 0)
        }
        return db.update(
            ExamDatabaseHelper.TABLE_EXAMS,
            values,
            "${ExamDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(exam.id.toString())
        )
    }

    // 删除一条考试记录
    fun deleteExam(id: Int): Int {
        val db = dbHelper.writableDatabase
        return db.delete(
            ExamDatabaseHelper.TABLE_EXAMS,
            "${ExamDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    // 根据ID获取单个考试
    fun getExamById(id: Int): Exam? {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            ExamDatabaseHelper.TABLE_EXAMS,
            null,
            "${ExamDatabaseHelper.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
            "1"  // 只返回一条记录
        )

        return if (cursor.moveToFirst()) {
            val examId = cursor.getInt(cursor.getColumnIndexOrThrow(ExamDatabaseHelper.COLUMN_ID))
            val name =
                cursor.getString(cursor.getColumnIndexOrThrow(ExamDatabaseHelper.COLUMN_NAME))
            val targetDate =
                cursor.getLong(cursor.getColumnIndexOrThrow(ExamDatabaseHelper.COLUMN_TARGET_DATE))
            val isPinned =
                cursor.getInt(cursor.getColumnIndexOrThrow(ExamDatabaseHelper.COLUMN_IS_PINNED)) == 1
            cursor.close()
            Exam(examId, name, targetDate, isPinned)
        } else {
            cursor.close()
            null
        }
    }

    // 查询所有考试记录
    fun getAllExams(): List<Exam> {
        val exams = mutableListOf<Exam>()
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            ExamDatabaseHelper.TABLE_EXAMS,
            null,
            null,
            null,
            null,
            null,
            "${ExamDatabaseHelper.COLUMN_IS_PINNED} DESC, ${ExamDatabaseHelper.COLUMN_TARGET_DATE} ASC"
        )
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(ExamDatabaseHelper.COLUMN_ID))
            val name =
                cursor.getString(cursor.getColumnIndexOrThrow(ExamDatabaseHelper.COLUMN_NAME))
            val targetDate =
                cursor.getLong(cursor.getColumnIndexOrThrow(ExamDatabaseHelper.COLUMN_TARGET_DATE))
            val isPinned =
                cursor.getInt(cursor.getColumnIndexOrThrow(ExamDatabaseHelper.COLUMN_IS_PINNED)) == 1
            exams.add(Exam(id, name, targetDate, isPinned))
        }
        cursor.close()
        return exams
    }
}

