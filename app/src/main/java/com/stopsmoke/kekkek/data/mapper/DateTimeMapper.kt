package com.stopsmoke.kekkek.data.mapper

import com.google.firebase.Timestamp
import com.stopsmoke.kekkek.domain.model.DateTime
import com.stopsmoke.kekkek.firestore.model.DateTimeEntity
import java.time.LocalDateTime
import java.time.ZoneOffset

internal fun DateTimeEntity?.asExternalModel(): DateTime =
    DateTime(
        created = this?.created?.toLocalDateTime() ?: LocalDateTime.MIN,
        modified = this?.modified?.toLocalDateTime() ?: LocalDateTime.MIN
    )

internal fun DateTime.toEntity(): DateTimeEntity =
    DateTimeEntity(
        created = Timestamp(created.toEpochSecond(ZoneOffset.UTC), created.nano),
        modified = Timestamp(modified.toEpochSecond(ZoneOffset.UTC), modified.nano),
    )