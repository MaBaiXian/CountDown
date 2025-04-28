package edu.gdpu.countdown.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import edu.gdpu.countdown.R
import edu.gdpu.countdown.data.Exam
import edu.gdpu.countdown.utils.DateUtils

class ExamAdapter(
    private var exams: List<Exam>,
    private val onItemClick :(Exam) -> Unit
) : RecyclerView.Adapter<ExamAdapter.ExamViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExamViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exam, parent, false)
        return ExamViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExamViewHolder, position: Int) {
        val exam = exams[position]
        holder.bind(exam)
    }

    override fun getItemCount(): Int = exams.size

    fun updateExams(newExams: List<Exam>) {
        exams = newExams
        notifyDataSetChanged()
    }


    inner class ExamViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvExamName: TextView = itemView.findViewById(R.id.tvExamName)
        private val tvExamDate: TextView = itemView.findViewById(R.id.tvExamDate)
        private val tvDaysLeft: TextView = itemView.findViewById(R.id.tvDaysLeft)

        fun bind(exam: Exam) {
            tvExamName.text = exam.name
            tvExamDate.text = "考试日期: ${DateUtils.timestampToString(exam.targetDate)}"
            val daysLeft = DateUtils.daysUntil(exam.targetDate)
            tvDaysLeft.text = if (daysLeft > 0) "剩余 $daysLeft 天" else if (daysLeft == 0) "就在今天" else "已过期"

            itemView.setOnClickListener {
                onItemClick(exam)
            }
        }
    }
}