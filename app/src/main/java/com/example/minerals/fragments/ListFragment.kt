package com.example.minerals.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minerals.Mineral
import com.example.minerals.R
import com.example.minerals.Repositories.IRepository
import com.example.minerals.Repositories.MineralSQLiteRepository
import com.example.minerals.adapters.ListImageItemAdapter
import com.example.minerals.databinding.FragmentListBinding

class ListFragment : Fragment() {
    companion object {
        fun createGridLayout(context: Context): RecyclerView.LayoutManager = GridLayoutManager(context, 2, RecyclerView.VERTICAL, false)
        fun createLinearLayout(context: Context) : RecyclerView.LayoutManager = LinearLayoutManager(context)
    }
    private lateinit var binding: FragmentListBinding
    var addMineral : (()->(Unit))? = null

    private lateinit var list: RecyclerView
    private lateinit var adapter: ListImageItemAdapter
    private lateinit var Minerals: ArrayList<Mineral>
    private var filteredMinerals: ArrayList<Mineral> = ArrayList()
    private lateinit var MineralRepository: IRepository

    var onMineralClick: ((Mineral) -> Unit)? = null
    var onMineralEdit: ((Mineral) -> Unit)? = null
    var onMineralDelete: ((Mineral) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        binding.fab.setOnClickListener { view->
            addMineral?.invoke()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MineralRepository = MineralSQLiteRepository(requireContext())
        Minerals = MineralRepository.getMineral()
        filteredMinerals.addAll(Minerals)
        list = binding.MineralListListView
        adapter = ListImageItemAdapter(filteredMinerals)

        adapter.onItemClick = {
            onMineralClick?.invoke(it)
        }

        adapter.onItemLongClick = {
            val popup = PopupMenu(view.context, view)
            popup.inflate(R.menu.popup_menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_update -> {
                        onMineralEdit?.invoke(it)
                        true
                    }
                    R.id.action_delete -> {
                        onMineralDelete?.invoke(it)
                        true
                    }
                    else -> true
                }

            }
            popup.show()
        }
        binding.MineralListListView.adapter = adapter

        binding.filter.doOnTextChanged { text, _, _, _ ->
            if (!text.toString().isBlank()) {
                filteredMinerals.clear()
                filteredMinerals.addAll(ArrayList(Minerals.filter { Mineral ->
                    Mineral.type.toString().lowercase().contains(text!!.toString().lowercase())
                }))
                adapter.notifyDataSetChanged()
            } else {
                filteredMinerals.clear()
                filteredMinerals.addAll(Minerals)
                Log.i("Filtered Characters ", filteredMinerals.count().toString())
                Log.i("Characters ", Minerals.count().toString())
                adapter.notifyDataSetChanged()
            }
        }
    }
}


