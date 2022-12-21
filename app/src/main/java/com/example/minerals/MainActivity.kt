package com.example.minerals

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.minerals.Repositories.IRepository
import com.example.minerals.Repositories.MineralSQLiteRepository
import com.example.minerals.databinding.ActivityMainBinding
import com.example.minerals.Services.FragmentManagerService
import com.example.minerals.fragments.MainFragment
import com.example.minerals.fragments.ListFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var listFragment: ListFragment
    private lateinit var addMineralFragment: MainFragment
    private lateinit var MineralRepository: IRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MineralRepository = MineralSQLiteRepository(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        listFragment = ListFragment()
        FragmentManagerService.openMineralFragment(
            supportFragmentManager,
            R.id.fragment_main,
            listFragment,
            true
        )

        //registerMineralListFragment(LayoutManagerType.Linear)

        listFragment.addMineral = {
            addMineral()
        }

    }

    private fun addMineral() {
        addMineralFragment = MainFragment()
        addMineralFragment.onMineralAdded = {
            FragmentManagerService.openMineralFragmentWithRemove(
                supportFragmentManager,
                R.id.fragment_main,
                listFragment,
                true
            )
        }

        FragmentManagerService.openMineralFragment(
            supportFragmentManager,
            R.id.fragment_main,
            addMineralFragment,
            true
        )
    }
}
