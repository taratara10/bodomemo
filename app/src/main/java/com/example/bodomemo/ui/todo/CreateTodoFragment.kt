package com.example.bodomemo.ui.todo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bodomemo.R
import com.example.bodomemo.data.db.GameEntity
import com.example.bodomemo.ui.notifications.NotificationsViewModel
import kotlinx.android.synthetic.main.fragment_create_todo.*

class CreateTodoFragment : Fragment() {
    private lateinit var todoViewModel: TodoViewModel
    var gameEntity: GameEntity? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)
        return inflater.inflate(R.layout.fragment_create_todo, container, false)
    }

    override fun onStart() {
        super.onStart()

        btn_save_title.setOnClickListener {
            saveTodo()
        }

    }

    /**
     * Sends the updated information back to calling Activity
     * */
    private fun saveTodo() {
        if (validateFields()) {
            val id = if (gameEntity != null) gameEntity?.id else 0
            val todo = id?.let { GameEntity(id = id, title = et_game_title.text.toString(),todoCheck = 0) }
            if (todo != null) { todoViewModel.saveGame(todo) }

            findNavController().navigate(R.id.action_navigation_create_todo_to_navigation_todo)
        }
    }

    /**
     * Validation of EditText 検証
     * */
    private fun validateFields(): Boolean {
        if (et_game_title.text?.isEmpty() == true) {
            til_game_title.error ="pleaseEnterTitle"
            et_game_title.requestFocus()
            return false
        }
        return true
    }
}