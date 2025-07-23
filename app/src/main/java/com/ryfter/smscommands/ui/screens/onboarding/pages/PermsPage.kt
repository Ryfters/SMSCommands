package com.ryfter.smscommands.ui.screens.onboarding.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ryfter.smscommands.R
import com.ryfter.smscommands.data.UiStateViewModel
import com.ryfter.smscommands.permissions.Permission
import com.ryfter.smscommands.ui.navigation.MyNavBackStack
import com.ryfter.smscommands.ui.navigation.Route
import com.ryfter.smscommands.ui.navigation.navigate
import com.ryfter.smscommands.ui.screens.onboarding.OnboardingPage
import com.ryfter.smscommands.ui.screens.perms.PermissionItem

class PermsPage : OnboardingPage {
    override val title = R.string.screen_onboarding_perms_title

    private var isSmsGranted by mutableStateOf(false)

    @Composable
    override fun Content(
        viewModel: UiStateViewModel,
        backStack: MyNavBackStack,
        modifier: Modifier
    ) {
        val permissionsState by viewModel.permissionsState.collectAsState()
        isSmsGranted = permissionsState[Permission.SMS.id] == true

        Column(modifier) {
            Text(
                stringResource(R.string.screen_onboarding_perms_info),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            HorizontalDivider(Modifier.padding(start = 24.dp, end = 24.dp, top = 8.dp))

            Permission.ALL.forEach { permission ->
                PermissionItem(
                    permission = permission,
                    isGranted = permissionsState[permission.id] == true,
                    onGrant = { isGranted ->
                        viewModel.updateSinglePermissionState(permission.id, isGranted)
                        if (!isGranted) backStack.navigate(Route.Perms.DeclineWarningDialog)
                    }
                )
            }
        }
    }

    override val nextButtonText: Int
        get() = if (isSmsGranted) R.string.common_continue else R.string.common_skip
}