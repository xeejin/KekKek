package com.stopsmoke.kekkek.presentation.achievement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.stopsmoke.kekkek.databinding.FragmentAchievementBinding
import com.stopsmoke.kekkek.presentation.achievement.adapter.AchievementListAdapter
import com.stopsmoke.kekkek.presentation.collectLatestWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.domain.model.User

@AndroidEntryPoint
class AchievementFragment : Fragment() {

    private var _binding: FragmentAchievementBinding? = null
    private val binding: FragmentAchievementBinding get() = _binding!!

    private val achievementListAdapter: AchievementListAdapter by lazy {
        AchievementListAdapter(viewModel)
    }

    private val viewModel: AchievementViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAchievementBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }


    private fun initView() = with(binding) {
        setupAppBar()
        setupRecyclerView()
    }

    private fun setupRecyclerView() = with(binding.rvAchievementItem) {
        adapter = achievementListAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupAppBar() = with(binding.includeAchievementAppBar) {
        ivAchievementBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initViewModel() = with(viewModel) {
        viewLifecycleOwner.lifecycleScope.launch {
            achievements.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collectLatest { achievements ->
                    achievementListAdapter.submitData(achievements)
                }
        }

        activities.collectLatestWithLifecycle(lifecycle) {
            when (it) {
                is Result.Success -> {
                    val activities = it.data
                    viewModel.getCurrentProgress(activities)
                }

                else -> {}
            }
        }

        achievements.collectLatestWithLifecycle(lifecycle) {
            achievementListAdapter.submitData(it)
            bindTopProgress()
        }
    }

    private suspend fun bindTopProgress() = with(binding) {
        val progress = viewModel.getCurrentItem()
        val user = viewModel.user.value as User.Registered
        val maxProgressCount = viewModel.getAchievementCount()
        icludeAchievementTop.tvAchievementQuitSmokingDayCount.text = "${progress.user} 일"
        icludeAchievementTop.tvAchievementQuitSmokingCount.text =
            "${user.clearAchievementsList.size} / ${maxProgressCount}"

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

