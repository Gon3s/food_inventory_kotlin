package com.gones.foodinventorykotlin.common

import com.gones.foodinventorykotlin.R

sealed class ExpirySections(
    val title: UiText,
) {
    data object Expired : ExpirySections(UiText.StringResource(R.string.expired))
    data object ExpiredIn3Days : ExpirySections(UiText.StringResource(R.string.less_x_days, 3))
    data object ExpireIn15Days : ExpirySections(UiText.StringResource(R.string.less_x_days, 15))
    data object ExpireIn1Month : ExpirySections(UiText.StringResource(R.string.less_x_month, 1))
    data object ExpireIn3Month : ExpirySections(UiText.StringResource(R.string.less_x_month, 3))
    data object ExpireIn3MonthPlus : ExpirySections(UiText.StringResource(R.string.more_x_month, 3))
    data object NoExpiry : ExpirySections(UiText.StringResource(R.string.no_expiry_date))
}