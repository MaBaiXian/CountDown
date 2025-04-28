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

class EditExamActivity : AppCompatActivity() {

    private lateinit var editExamName: EditText
    private lateinit var btnBack: ImageButton
    private lateinit var btnDate: Button
    private lateinit var btnTime: Button
    private lateinit var btnIsPinned: Button
    private lateinit var swIsPinned: Switch
    private lateinit var btnSaveToolbar: ImageButton
    private lateinit var btnSaveBottom: Button
    private lateinit var examDao: ExamDao
    private lateinit var calendar: Calendar
    private var examId: Int = -1
    private lateinit var originalExam: Exam

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_exam)

        initData()

        initViews()

        setupClickListeners()

        loadExamData()
    }

    private fun initViews() {
        editExamName = findViewById(R.id.editExamName)
        btnBack = findViewById(R.id.btn_back)
        btnDate = findViewById(R.id.btnDate)
        btnTime = findViewById(R.id.btnTime)
        btnIsPinned = findViewById(R.id.btnIsPinned)
        swIsPinned = findViewById(R.id.swIsPinned)
        btnSaveToolbar = findViewById(R.id.imBtn_save)
        btnSaveBottom = findViewById(R.id.btn_save)
        calendar = Calendar.getInstance()
    }

    private fun setupClickListeners() {
        // 设置监听器（与AddExamActivity相同）
        btnBack.setOnClickListener { finishWithAnimation() }
        btnSaveToolbar.setOnClickListener { saveExamInfo() }
        btnSaveBottom.setOnClickListener { saveExamInfo() }
        btnDate.setOnClickListener { showDatePicker() }
        btnTime.setOnClickListener { showTimePicker() }
        btnIsPinned.setOnClickListener { togglePinnedSwitch() }
    }

    private fun initData() {
        examDao = ExamDao(this)
        examId = intent.getIntExtra("examId", -1)
        if (examId == -1) {
            ToastUtils.show(this, "无效的考试ID", Toast.LENGTH_SHORT)
            finish()
            return
        }
    }

    private fun loadExamData() {
        val exam = examDao.getExamById(examId) ?: run {
            ToastUtils.show(this, "考试不存在", Toast.LENGTH_SHORT)
            finish()
            return
        }
        originalExam = exam

        // 填充现有数据
        editExamName.setText(exam.name)
        swIsPinned.isChecked = exam.isPinned

        // 设置日期和时间
        calendar.timeInMillis = exam.targetDate
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

        btnDate.text = dateFormat.format(calendar.time)
        btnTime.text = timeFormat.format(calendar.time)
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, day ->
                calendar.set(year, month, day)
                btnDate.text = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    .format(calendar.time)
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
                calendar.set(Calendar.HOUR_OF_DAY, hour)
                calendar.set(Calendar.MINUTE, minute)
                btnTime.text = SimpleDateFormat("HH:mm", Locale.getDefault())
                    .format(calendar.time)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun saveExamInfo() {
        val examName = editExamName.text.toString().trim()
        if (examName.isEmpty()) {
            ToastUtils.show(this, "请输入考试名称", Toast.LENGTH_SHORT)
            return
        }

        val updatedExam = originalExam.copy(
            name = examName,
            targetDate = calendar.timeInMillis,
            isPinned = swIsPinned.isChecked
        )

        if (examDao.updateExam(updatedExam) > 0) {
            ToastUtils.show(this, "更新成功", Toast.LENGTH_SHORT)
            setResult(RESULT_OK)
            finishWithAnimation()
        } else {
            ToastUtils.show(this, "更新失败", Toast.LENGTH_SHORT)
        }
    }

    private fun finishWithAnimation() {
        finish()
    }

    private fun togglePinnedSwitch() {
        swIsPinned.isChecked = !swIsPinned.isChecked
    }
}