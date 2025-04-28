package edu.gdpu.countdown.ui.activitys

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.navigation.NavigationView
import edu.gdpu.countdown.R
import edu.gdpu.countdown.data.ExamDao
import edu.gdpu.countdown.ui.adapters.ExamAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnMenu: ImageButton
    private lateinit var btnAdd: ImageButton
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var examDao: ExamDao
    private lateinit var navDrawer: NavigationView
    private lateinit var examAdapter: ExamAdapter
    private lateinit var btnChangeLayout: MaterialButton
    private var isGridLayout = false //记录是否为网格布局

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        setupRecyclerView()

        setupClickListeners()
    }

    override fun onResume() {
        super.onResume()
        refreshExamData()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        btnMenu = findViewById(R.id.btn_menu)
        btnAdd = findViewById(R.id.btn_add)
        btnChangeLayout = findViewById(R.id.btnChangeLayout)
        drawerLayout = findViewById(R.id.drawer_layout)
        navDrawer = findViewById(R.id.nav_drawer)
        examDao = ExamDao(this)
    }

    private fun setupRecyclerView() {
        recyclerView.layoutManager = LinearLayoutManager(this)
        examAdapter = ExamAdapter(emptyList()) { exam ->
            navigateToExamDetail(exam.id)
        }
        recyclerView.adapter = examAdapter
        refreshExamData()
    }

    private fun setupClickListeners() {
        btnMenu.setOnClickListener {
            drawerLayout.openDrawer(navDrawer)
        }

        btnAdd.setOnClickListener {
            navigateToAddExam()
        }

        btnChangeLayout.setOnClickListener {
            changeLayout()
        }
    }

    private fun refreshExamData() {
        val exams = examDao.getAllExams()
        examAdapter.updateExams(exams)
    }

    private fun navigateToExamDetail(examId: Int) {
        val intent = Intent(this, ExamDetailActivity::class.java).apply {
            putExtra("exam", examId)
        }
        startActivity(intent)
    }

    private fun navigateToAddExam() {
        val intent = Intent(this, AddExamActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slidein_right, R.anim.slideout_left)
    }

    private fun changeLayout() {
        val layoutManager = if (isGridLayout) {
            LinearLayoutManager(this)
        } else {
            GridLayoutManager(this, 2)
        }
        recyclerView.layoutManager = layoutManager
        isGridLayout = !isGridLayout
    }
}