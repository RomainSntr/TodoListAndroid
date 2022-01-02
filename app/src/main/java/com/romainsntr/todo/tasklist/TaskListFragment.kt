package com.romainsntr.todo.tasklist
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.romainsntr.todo.R
import com.romainsntr.todo.databinding.FragmentTaskListBinding
import com.romainsntr.todo.form.FormActivity
import com.romainsntr.todo.network.Api


class TaskListFragment : Fragment() {

    val myAdapter = TaskListAdapter()

    val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // ici on récupérera le résultat pour le traiter
        val task = result.data?.getSerializableExtra("task") as? Task
        if (task != null)
        {
            taskList = taskList + task
            myAdapter.submitList(taskList)
            val oldTask = taskList.firstOrNull { it.id == task.id }
            if (oldTask != null) taskList = taskList - oldTask
        }
    }

    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )

    private lateinit var _binding: FragmentTaskListBinding
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
        //return inflater.inflate(com.romainsntr.todo.R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = myAdapter
        myAdapter.submitList(taskList)
        myAdapter.onClickDelete = { task ->
            taskList = taskList - task
            myAdapter.submitList(taskList)
        }

        myAdapter.onClickEdit = { task ->
            val intent = Intent(context, FormActivity::class.java)
            intent.putExtra("editTask", task)
            formLauncher.launch(intent)
        }

        val buttonAddTask = view.findViewById<FloatingActionButton>(R.id.floatingActionButton)
        buttonAddTask.setOnClickListener {
            val intent = Intent(context, FormActivity::class.java)
            formLauncher.launch(intent)
        }
    }
}