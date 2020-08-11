package app.ishizuka.ryo.firebasetest

import android.content.Context
import android.speech.RecognizerIntent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(private val context: Context):
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    val items: MutableList<TestData> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_view_data_cell, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.nameTextView.text = item.name
        holder.foodTextView.text = item.food
    }

    fun addAll(items: MutableList<TestData>) {
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.nameView)
        val foodTextView: TextView = view.findViewById(R.id.foodView)
    }
}