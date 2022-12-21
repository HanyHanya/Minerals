package com.example.minerals.Services

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.minerals.databinding.FragmentListBinding

class FragmentManagerService {
    companion object {
        fun openMineralFragment(fragmentManager: FragmentManager, frameLayoutId: Int, fragment: Fragment, withAddToBackStack: Boolean = false) {
            fragmentManager.executePendingTransactions();
            fragmentManager.beginTransaction().apply {
                replace(frameLayoutId, fragment)
                if(withAddToBackStack) {
                    addToBackStack(null)
                }
                commit()
            }
        }

        fun openMineralFragmentWithRemove(fragmentManager: FragmentManager, frameLayoutId: Int, fragment: Fragment, withAddToBackStack: Boolean = false) {
            fragmentManager.beginTransaction().remove(fragment).commit()
            fragmentManager.executePendingTransactions();
            fragmentManager.beginTransaction().apply {
                replace(frameLayoutId, fragment)
                if(withAddToBackStack) {
                    addToBackStack(null)
                }
                commit()
            }
        }
    }
}