package com.example.minerals.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.minerals.data.Mineral
import com.example.minerals.R
import com.example.minerals.adapters.ListImageItemAdapter
import com.example.minerals.data.MineralsViewModel
import com.example.minerals.databinding.FragmentListBinding

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    var addMineral : (()->(Unit))? = null

    private lateinit var list: RecyclerView
    private lateinit var adapter: ListImageItemAdapter
    private lateinit var mMineralsViewModel: MineralsViewModel
    private var filteredMinerals = ArrayList<Mineral>()

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

        list = binding.MineralListListView
        list.setLayoutManager(LinearLayoutManager(requireContext()));
        adapter = ListImageItemAdapter()
        mMineralsViewModel = ViewModelProvider(this).get(MineralsViewModel::class.java)
//        mMineralsViewModel.nukeTable()
        list.adapter = adapter
        mMineralsViewModel.getAllMinerals.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        adapter.onItemClick = {
            onMineralClick?.invoke(it)
        }

        adapter.onItemLongClick = {
            val popup = PopupMenu(view?.context, view)
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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.filter.doOnTextChanged { text, _, _, _ ->
            if (!text.toString().isBlank()) {
                filteredMinerals.clear()
                filteredMinerals.addAll(ArrayList(mMineralsViewModel.getAllMinerals.value?.filter { Mineral ->
                    Mineral.type.toString().lowercase().contains(text!!.toString().lowercase())
                }))
                adapter.notifyDataSetChanged()
            } else {
                filteredMinerals.clear()
                filteredMinerals.addAll(mMineralsViewModel.getAllMinerals.value!!)
                adapter.notifyDataSetChanged()
            }
        }
    }
}


