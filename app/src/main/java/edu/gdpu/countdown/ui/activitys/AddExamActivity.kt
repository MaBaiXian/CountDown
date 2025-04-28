package edu.gdpu.countdown.ui.activitys

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.gdpu.countdown.R
import edu.gdpu.countdown.data.Exam
import edu.gdpu.countdown.data.ExamDao
import edu.gdpu.countdown.utils.ToastUtils
import java.util.Locale

class AddExamActivity : AppCompatActivity() {

    // 视图组件
    private lateinit var editExamName: EditText
    private lateinit var btnBack: ImageButton
    private lateinit var btnDate: Button
    private lateinit var btnTime: Button
    private lateinit var btnIsPinned: Button
    private lateinit var swIsPinned: Switch
    private lateinit var btnSaveToolbar: ImageButton
    private lateinit var btnSaveBottom: Button

    // 数据相关
    private lateinit var examDao: ExamDao
    private var calendar = Calendar.getInstance()
    private var selectedDateTime: Long = 0 // 合并后的日期时间戳

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_exam)

        initViews()

        setupClickListeners()
    }

    private fun initViews() {
        // 初始化所有视图组件
        editExamName = findViewById(R.id.editExamName)
        btnBack = findViewById(R.id.btn_back)
        btnDate = findViewById(R.id.btnDate)
        btnTime = findViewById(R.id.btnTime)
        btnIsPinned = findViewById(R.id.btnIsPinned)
        swIsPinned = findViewById(R.id.swIsPinned)
        btnSaveToolbar = findViewById(R.id.imBtn_save)
        btnSaveBottom = findViewById(R.id.btn_save)
        examDao = ExamDao(this)
    }

    private fun setupClickListeners() {
        // 返回按钮
        btnBack.setOnClickListener {
            finishWithTransition()
        }

        // 两个保存按钮共用同一逻辑
        btnSaveToolbar.setOnClickListener {
            saveExamInfo()
        }
        btnSaveBottom.setOnClickListener {
            saveExamInfo()
        }

        // 日期选择
        btnDate.setOnClickListener {
            showDatePicker()
        }

        // 时间选择
        btnTime.setOnClickListener {
            showTimePicker()
        }

        // 置顶开关
        btnIsPinned.setOnClickListener {
            togglePinnedSwitch()
        }
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, day ->
                updateSelectedDate(year, month, day)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showTimePicker() {
        TimePickerDialog(
            this,
            { _, hour, minute ->
                updateSelectedTime(hour, minute)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun updateSelectedDate(year: Int, month: Int, day: Int) {
        calendar.set(year, month, day)
        btnDate.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }

    private fun updateSelectedTime(hour: Int, minute: Int) {
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        btnTime.text = String.format("%02d:%02d", hour, minute)
    }

    private fun togglePinnedSwitch() {
        swIsPinned.isChecked = !swIsPinned.isChecked
    }

    private fun saveExamInfo() {
        if (!validateInput()) return

        selectedDateTime = calendar.timeInMillis
        val exam = createExamFromInput()

        if (examDao.insertExam(exam) != -1L) {
            showSuccessAndFinish()
        } else {
            showSaveFailed()
        }
    }

    private fun validateInput(): Boolean {
        return when {
            editExamName.text.toString().trim().isEmpty() -> {
                showToast("请输入考试名称")
                false
            }
            btnDate.text.toString() == getString(R.string.select_date) -> {
                showToast("请选择考试日期")
                false
            }
            btnTime.text.toString() == getString(R.string.select_time) -> {
                showToast("请选择考试时间")
                false
            }
            else -> true
        }
    }

    private fun createExamFromInput(): Exam {
        return Exam(
            name = editExamName.text.toString().trim(),
            targetDate = calendar.timeInMillis,
            isPinned = swIsPinned.isChecked
        )
    }

    private fun showToast(message: String) {
        ToastUtils.show(this, message, Toast.LENGTH_SHORT)
    }

    private fun showSuccessAndFinish() {
        showToast("考试信息已保存")
        finishWithTransition()
    }

    private fun showSaveFailed() {
        showToast("保存失败，请重试")
    }

    private fun finishWithTransition() {
        finish()
        overridePendingTransition(R.anim.slidein_left, R.anim.slideout_right)
    }
}