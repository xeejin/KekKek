package com.stopsmoke.kekkek.presentation.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.oss.licenses.OssLicensesMenuActivity
import com.stopsmoke.kekkek.R
import com.stopsmoke.kekkek.databinding.FragmentSettingsBinding
import com.stopsmoke.kekkek.presentation.settings.model.ProfileInfo
import com.stopsmoke.kekkek.presentation.settings.model.SettingsItem
import com.stopsmoke.kekkek.presentation.settings.model.SettingsMultiViewEnum
import com.stopsmoke.kekkek.presentation.settings.model.SettingsOnClickListener


class SettingsFragment : Fragment(), SettingsOnClickListener {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var settingAdapter: SettingsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
        initView()
    }

    private fun initData() {
        settingAdapter = SettingsAdapter(this@SettingsFragment)
        settingAdapter.itemList = listOf(
            SettingsItem(
                profileInfo = ProfileInfo(
                    profileImg = com.stopsmoke.kekkek.R.drawable.ic_launcher_background,
                    userNickname = "희진",
                    userDateOfBirth = "2024년 3월 1일",
                    userIntroduction = "글이 길면 이렇게 요약됩니다. 글이 길면 이렇게 요약됩니다. 글이 길면 이렇게 요약됩니다."
                ),
                settingTitle = null,
                version = null,
                cardViewType = SettingsMultiViewEnum.MY_PAGE
            ),
            SettingsItem(
                settingTitle = "알림",
                cardViewType = SettingsMultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingsItem(
                settingTitle = "언어",
                cardViewType = SettingsMultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingsItem(
                settingTitle = "테마",
                cardViewType = SettingsMultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingsItem(
                settingTitle = "친구",
                cardViewType = SettingsMultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingsItem(
                settingTitle = "오픈 소스 고지",
                cardViewType = SettingsMultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingsItem(
                settingTitle = "개인 정보 보호 및 보안 안내",
                cardViewType = SettingsMultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingsItem(
                settingTitle = "지원",
                cardViewType = SettingsMultiViewEnum.LIST,
                profileInfo = null,
                version = null
            ),
            SettingsItem(
                version = "현재 버전 2.0.51",
                cardViewType = SettingsMultiViewEnum.VERSION,
                profileInfo = null,
                settingTitle = null
            )
        )
    }

    private fun initView() = with(binding) {
        rvSetting.adapter = settingAdapter
        rvSetting.layoutManager = LinearLayoutManager(requireContext())
        setting.tvUserProfileTitle .text ="설정"
        setting.ivUserProfileBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClickProfile(settingItem: SettingsItem) {
        // settingItem 에 정보를 담은 채로 보내야함
        findNavController().navigate("setting_profile")
    }

    override fun onClickSettingList(settingItem: SettingsItem) {
        when(settingItem.settingTitle) {
            "알림" -> {
                findNavController().navigate("setting_notification")
            }
            "언어" -> {
                findNavController().navigate("setting_language")
            }
            "테마" -> {

            }
            "친구" -> {

            }
            "오픈 소스 고지" -> {
                startActivity(Intent(requireContext(), OssLicensesMenuActivity::class.java))
                OssLicensesMenuActivity.setActivityTitle(getString(com.stopsmoke.kekkek.R.string.custom_license_title))
            }
            "개인 정보 보호 및 보안 안내" -> {
                findNavController().navigate(R.id.action_setting_to_setting_privatepolicy)
            }
            "지원" -> {
                findNavController().navigate(R.id.action_setting_to_setting_support)
            }
        }
    }
}