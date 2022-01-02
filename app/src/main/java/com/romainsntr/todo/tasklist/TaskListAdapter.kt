package com.romainsntr.todo.tasklist
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.romainsntr.todo.R
import com.romainsntr.todo.form.FormActivity

object TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldItem: Task, newItem: Task) : Boolean {
        return oldItem.id == newItem.id;
    }
    override fun areContentsTheSame(oldItem: Task, newItem: Task) : Boolean {
        return (oldItem.title == newItem.title) && (oldItem.description == newItem.description);
    }
}

class TaskListAdapter() : androidx.recyclerview.widget.ListAdapter<Task, TaskListAdapter.TaskViewHolder>(TaskDiffCallback) {

    var onClickDelete: (Task) -> Unit = {}

    var onClickEdit: (Task) -> Unit = {}

    inner class TaskViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {
        fun bind(task : Task) {
            val textView = itemView.findViewById<TextView>(R.id.text_view)
            textView.text = task.title

            val textViewDescription = itemView.findViewById<TextView>(R.id.description)
            textViewDescription.text = task.description

            val buttonDelete = itemView.findViewById<ImageButton>(R.id.imageButton5)
            buttonDelete.setOnClickListener {
                onClickDelete(task)
            }

            val buttonEdit = itemView.findViewById<ImageButton>(R.id.imageButton)
            buttonEdit.setOnClickListener {
                onClickEdit(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}
