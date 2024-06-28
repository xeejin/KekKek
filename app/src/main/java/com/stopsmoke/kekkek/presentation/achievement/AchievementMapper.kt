package com.stopsmoke.kekkek.presentation.achievement

import com.stopsmoke.kekkek.domain.model.Achievement


internal fun Achievement.getItem() = AchievementItem(
    id = id,
    name = name,
    description = description,
    image = image,
    category = category,
    maxProgress = maxProgress,
    requestCode = requestCode
)