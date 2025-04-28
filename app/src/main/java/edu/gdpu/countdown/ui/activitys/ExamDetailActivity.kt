package edu.gdpu.countdown.ui.activitys

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.gdpu.countdown.R
import edu.gdpu.countdown.data.Exam
import edu.gdpu.countdown.data.ExamDao
import edu.gdpu.countdown.utils.DateUtils
import edu.gdpu.countdown.utils.ToastUtils

class ExamDetailActivity : AppCompatActivity() {
    private lateinit var examDao: ExamDao
    private lateinit var exam: Exam
    private var examId: Int = -1

    // 声明视图组件
    private lateinit var tvExamName: TextView
    private lateinit var tvExamLeft: TextView
    private lateinit var tvExamTarget: TextView
    private lateinit var tvTimeLeft: TextView
    private lateinit var btnEdit: Button
    private lateinit var btnEditIcon: ImageButton
    private lateinit var btnBack: ImageButton
    private lateinit var btnDel: Button
    private lateinit var countDownTimer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exam_detail)

        initData()

        initViews()

        setupClickListeners()

        startCountDownTimer()
    }

    override fun onResume() {
        super.onResume()
        refreshExamData()
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel() // 避免内存泄漏
    }

    private fun initData() {
        examDao = ExamDao(this)
        examId = intent.getIntExtra("exam", -1)
        exam = examDao.getExamById(examId) ?: run {
            finish()
            return
        }
    }

    private fun initViews() {
        tvExamName = findViewById(R.id.tv_exam_name)
        tvExamLeft = findViewById(R.id.tv_days_left)
        tvExamTarget = findViewById(R.id.tv_exam_target)
        tvTimeLeft = findViewById(R.id.tv_left_time)
        btnEdit = findViewById(R.id.btn_edit)
        btnEditIcon = findViewById(R.id.imBtn_edit)
        btnBack = findViewById(R.id.btn_back)
        btnDel = findViewById(R.id.btn_del)

        updateExamInfoUI()
    }

    private fun setupClickListeners() {
        btnEdit.setOnClickListener { navigateToEditExam() }
        btnEditIcon.setOnClickListener { navigateToEditExam() }
        btnBack.setOnClickListener { finish() }
        btnDel.setOnClickListener { showDeleteConfirmationDialog() }
    }

    private fun refreshExamData() {
        exam = examDao.getExamById(examId) ?: run {
            finish()
            return
        }
        updateExamInfoUI()
    }

    private fun updateExamInfoUI() {
        var daysLeft = DateUtils.daysUntil(exam.targetDate)
        tvExamName.text = if (daysLeft >= 0) "${exam.name} 还有" else "${exam.name} 已过去"
        if (daysLeft < 0) daysLeft = -daysLeft
        tvExamLeft.text = daysLeft.toString()
        tvExamTarget.text = "考试日期: ${DateUtils.timestampToString(exam.targetDate)}"
    }

    private fun startCountDownTimer() {
        countDownTimer = object : CountDownTimer(Long.MAX_VALUE, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateRemainingTimeUI()
            }

            override fun onFinish() {
                // 理论上不会执行
            }
        }.start()
    }

    private fun updateRemainingTimeUI() {
        val currentTime = System.currentTimeMillis()
        val remainingTime = exam.targetDate - currentTime

        when {
            remainingTime > 0 -> {
                val days = remainingTime / (1000 * 60 * 60 * 24)
                val hours = (remainingTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
                val minutes = (remainingTime % (1000 * 60 * 60)) / (1000 * 60)
                val seconds = (remainingTime % (1000 * 60)) / 1000

                tvTimeLeft.text = String.format("%d天%d小时%d分%d秒", days, hours, minutes, seconds)
            }
            remainingTime < 0 -> {
                val passedTime = -remainingTime
                val days = passedTime / (1000 * 60 * 60 * 24)
                val hours = (passedTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
                val minutes = (passedTime % (1000 * 60 * 60)) / (1000 * 60)
                val seconds = (passedTime % (1000 * 60)) / 1000

                tvTimeLeft.text = String.format("已过去%d天%d小时%d分%d秒", days, hours, minutes, seconds)
            }
            else -> {
                tvTimeLeft.text = "考试开始啦！"
            }
        }
    }

    private fun navigateToEditExam() {
        val intent = Intent(this, EditExamActivity::class.java).apply {
            putExtra("examId", exam.id)
        }
        startActivity(intent)
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this, R.style.AlertDialogCustom)
            .setTitle("删除考试")
            .setMessage("确定要删除 ${exam.name} 吗？")
            .setPositiveButton("确定") { _, _ ->
                examDao.deleteExam(exam.id)
                ToastUtils.show(this, "删除成功", Toast.LENGTH_SHORT)
                finish()
            }
            .setNegativeButton("取消", null)
            .create()
            .show()
    }
}