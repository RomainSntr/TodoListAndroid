package com.romainsntr.todo.tasklist
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.romainsntr.todo.R
import com.romainsntr.todo.databinding.FragmentTaskListBinding
import com.romainsntr.todo.form.FormActivity
import com.romainsntr.todo.network.Api
import com.romainsntr.todo.network.TasksRepository
import kotlinx.coroutines.launch


class TaskListFragment : Fragment() {

    //private val tasksRepository = TasksRepository()

    val myAdapter = TaskListAdapter()

    private val viewModel: TaskListViewModel by viewModels()

    val formLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        // ici on récupérera le résultat pour le traiter
        val task = result.data?.getSerializableExtra("task") as? Task
        if (task != null)
        {
            /*taskList = taskList + task
            myAdapter.submitList(taskList)
            val oldTask = taskList.firstOrNull { it.id == task.id }
            if (oldTask != null) taskList = taskList - oldTask*/
            lifecycleScope.launch {
                viewModel.addOrEdit(task)
                //tasksRepository.createOrUpdate(task)
                //tasksRepository.refresh()
            }
        }
    }

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
        //myAdapter.submitList(taskList)

        myAdapter.onClickDelete = { task ->
            /*taskList = taskList - task
            myAdapter.submitList(taskList)*/
            lifecycleScope.launch {
                viewModel.delete(task)
                //tasksRepository.delete(task)
                //tasksRepository.refresh()
            }
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

        lifecycleScope.launch { // on lance une coroutine car `collect` est `suspend`
            viewModel.taskList.collect { newList: List<Task> ->
                // cette lambda est executée à chaque fois que la liste est mise à jour dans le repository
                // -> ici, on met à jour la liste dans l'adapteur
                myAdapter.submitList(newList)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.refresh()
/*
        lifecycleScope.launch {
            tasksRepository.refresh() // on demande de rafraîchir les données sans attendre le retour directement
        }*/
    }
}