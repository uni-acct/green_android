package com.blockstream.compose.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import blockstream_green.common.generated.resources.Res
import blockstream_green.common.generated.resources.box_arrow_down
import blockstream_green.common.generated.resources.flask_fill
import blockstream_green.common.generated.resources.id_1d_minutes
import blockstream_green.common.generated.resources.id_2fa_threshold
import blockstream_green.common.generated.resources.id_a_screen_lock_must_be_enabled
import blockstream_green.common.generated.resources.id_add_a_pgp_public_key_to_receive
import blockstream_green.common.generated.resources.id_archived_account
import blockstream_green.common.generated.resources.id_archived_accounts
import blockstream_green.common.generated.resources.id_auto_logout_timeout
import blockstream_green.common.generated.resources.id_backup_recovery_phrase
import blockstream_green.common.generated.resources.id_biometric_login_is_disabled
import blockstream_green.common.generated.resources.id_biometric_login_is_enabled
import blockstream_green.common.generated.resources.id_change_pin
import blockstream_green.common.generated.resources.id_continue
import blockstream_green.common.generated.resources.id_copy_amp_id
import blockstream_green.common.generated.resources.id_copy_support_id
import blockstream_green.common.generated.resources.id_denomination__exchange_rate
import blockstream_green.common.generated.resources.id_display_values_in_s_and
import blockstream_green.common.generated.resources.id_enabled
import blockstream_green.common.generated.resources.id_experimental_feature
import blockstream_green.common.generated.resources.id_experimental_features_might
import blockstream_green.common.generated.resources.id_generate_amp_id
import blockstream_green.common.generated.resources.id_genuine_check
import blockstream_green.common.generated.resources.id_get_support
import blockstream_green.common.generated.resources.id_i_lost_my_2fa
import blockstream_green.common.generated.resources.id_legacy_script_coins
import blockstream_green.common.generated.resources.id_lightning
import blockstream_green.common.generated.resources.id_login_with_biometrics
import blockstream_green.common.generated.resources.id_logout
import blockstream_green.common.generated.resources.id_minute
import blockstream_green.common.generated.resources.id_pgp_key
import blockstream_green.common.generated.resources.id_recovery_transaction_emails
import blockstream_green.common.generated.resources.id_recovery_transactions
import blockstream_green.common.generated.resources.id_rename_wallet
import blockstream_green.common.generated.resources.id_request_recovery_transactions
import blockstream_green.common.generated.resources.id_set_an_email_for_recovery
import blockstream_green.common.generated.resources.id_set_twofactor_threshold
import blockstream_green.common.generated.resources.id_support
import blockstream_green.common.generated.resources.id_there_is_already_an_archived
import blockstream_green.common.generated.resources.id_touch_to_display
import blockstream_green.common.generated.resources.id_twofactor_authentication
import blockstream_green.common.generated.resources.id_verify_the_authenticity_of
import blockstream_green.common.generated.resources.id_version
import blockstream_green.common.generated.resources.id_wallet_details
import blockstream_green.common.generated.resources.sign_out
import com.adamglin.PhosphorIcons
import com.adamglin.phosphoricons.Regular
import com.adamglin.phosphoricons.regular.CaretRight
import com.adamglin.phosphoricons.regular.Copy
import com.blockstream.common.SupportType
import com.blockstream.common.data.LogoutReason
import com.blockstream.common.data.SupportData
import com.blockstream.common.data.TwoFactorMethod
import com.blockstream.common.data.TwoFactorSetupAction
import com.blockstream.common.data.WalletSetting
import com.blockstream.common.events.Events.Logout
import com.blockstream.common.gdk.data.AccountAssetBalance
import com.blockstream.common.gdk.data.AccountAssetBalanceList
import com.blockstream.common.gdk.data.AccountType
import com.blockstream.common.models.settings.DenominationExchangeRateViewModel
import com.blockstream.common.models.settings.WalletSettingsSection
import com.blockstream.common.models.settings.WalletSettingsViewModel
import com.blockstream.common.models.settings.WalletSettingsViewModel.LocalEvents
import com.blockstream.common.models.settings.WalletSettingsViewModel.LocalEvents.SetCsvTime
import com.blockstream.common.models.settings.WalletSettingsViewModel.LocalEvents.Toggle2FA
import com.blockstream.common.models.settings.WalletSettingsViewModel.LocalSideEffects
import com.blockstream.common.models.settings.WalletSettingsViewModelAbstract
import com.blockstream.common.navigation.NavigateDestinations
import com.blockstream.common.navigation.NavigateDestinations.ArchivedAccounts
import com.blockstream.common.navigation.NavigateDestinations.JadeGenuineCheck
import com.blockstream.common.navigation.NavigateDestinations.RenameWallet
import com.blockstream.common.navigation.NavigateDestinations.Support
import com.blockstream.common.navigation.NavigateDestinations.WalletSettings
import com.blockstream.common.sideeffects.SideEffects
import com.blockstream.common.utils.StringHolder
import com.blockstream.common.utils.getBitcoinOrLiquidUnit
import com.blockstream.compose.LocalBiometricState
import com.blockstream.compose.LocalDialog
import com.blockstream.compose.components.GreenButton
import com.blockstream.compose.components.GreenButtonType
import com.blockstream.compose.components.LearnMoreButton
import com.blockstream.compose.components.OnProgressStyle
import com.blockstream.compose.dialogs.DenominationExchangeDialog
import com.blockstream.compose.dialogs.SingleChoiceDialog
import com.blockstream.compose.dialogs.TextDialog
import com.blockstream.compose.extensions.colorText
import com.blockstream.compose.screens.jade.JadeQRResult
import com.blockstream.compose.sideeffects.OpenDialogData
import com.blockstream.compose.theme.bodyLarge
import com.blockstream.compose.theme.bodyMedium
import com.blockstream.compose.theme.red
import com.blockstream.compose.theme.titleSmall
import com.blockstream.compose.theme.whiteHigh
import com.blockstream.compose.theme.whiteLow
import com.blockstream.compose.theme.whiteMedium
import com.blockstream.compose.utils.SetupScreen
import com.blockstream.ui.components.GreenColumn
import com.blockstream.ui.components.GreenRow
import com.blockstream.ui.navigation.LocalInnerPadding
import com.blockstream.ui.navigation.getResult
import com.blockstream.ui.utils.bottom
import com.blockstream.ui.utils.ifTrue
import com.blockstream.ui.utils.plus
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun WalletSettingsScreen(
    viewModel: WalletSettingsViewModelAbstract,
    isInnerTab: Boolean = false
) {
    var denominationExchangeRateViewModel by remember {
        mutableStateOf<DenominationExchangeRateViewModel?>(null)
    }
    var showPgpDialog by remember { mutableStateOf<String?>(null) }
    var showAutologoutTimeoutDialog by remember { mutableStateOf<Int?>(null) }
    var showThresholdDialog by remember { mutableStateOf<String?>(null) }
    var showTwoFactorChangeDialog by remember { mutableStateOf<WalletSettingsViewModel.LocalSideEffects.Disable2FA?>(null) }
    val onProgress by viewModel.onProgress.collectAsStateWithLifecycle()

    val biometricsState = LocalBiometricState.current

    denominationExchangeRateViewModel?.also {
        DenominationExchangeDialog(viewModel = it) {
            denominationExchangeRateViewModel = null
        }
    }

    if (showPgpDialog != null) {
        TextDialog(
            title = stringResource(Res.string.id_pgp_key),
            message = stringResource(Res.string.id_add_a_pgp_public_key_to_receive),
            label = stringResource(Res.string.id_pgp_key)
        ) { key ->
            showPgpDialog = null

            if (key != null) {
                viewModel.postEvent(WalletSettingsViewModel.LocalEvents.SetPgp(key))
            }
        }
    }

    if (showThresholdDialog != null) {
        TextDialog(
            title = stringResource(Res.string.id_set_twofactor_threshold),
            label = stringResource(Res.string.id_2fa_threshold),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Decimal,
                imeAction = ImeAction.Done
            ),
            initialText = showThresholdDialog,
            suffixText = getBitcoinOrLiquidUnit(session = viewModel.session)
        ) { value ->
            showThresholdDialog = null

            if (value != null) {
                viewModel.postEvent(WalletSettingsViewModel.LocalEvents.SetTwoFactorThreshold(value))
            }
        }
    }

    if (showTwoFactorChangeDialog != null) {
        showTwoFactorChangeDialog?.also { disable2fa ->

            SingleChoiceDialog(
                title = disable2fa.title,
                message = disable2fa.message,
                items = disable2fa.availableMethods.map { stringResource(it.localized) },
                onNeutralText = stringResource(Res.string.id_i_lost_my_2fa),
                onNeutralClick = {
                    showTwoFactorChangeDialog = null
                    viewModel.postEvent(
                        NavigateDestinations.TwoFactorSetup(
                            greenWallet = viewModel.greenWallet,
                            network = disable2fa.network,
                            method = TwoFactorMethod.EMAIL,
                            action = TwoFactorSetupAction.RESET,
                            isSmsBackup = false
                        )
                    )
                }
            ) { position ->
                showTwoFactorChangeDialog = null

                if (position != null) {
                    disable2fa.availableMethods.getOrNull(position)?.also {
                        viewModel.postEvent(
                            WalletSettingsViewModel.LocalEvents.Disable2FA(
                                method = disable2fa.method,
                                authenticateMethod = it
                            )
                        )
                    }
                }
            }
        }
    }

    if (showAutologoutTimeoutDialog != null) {
        val values = listOf(1, 2, 5, 10, 60)
        val entries = values.map {
            if (it == 1) "1 ${stringResource(Res.string.id_minute)}" else stringResource(
                Res.string.id_1d_minutes,
                it
            )
        }

        SingleChoiceDialog(
            title = stringResource(Res.string.id_auto_logout_timeout),
            items = entries,
            checkedItem = values.indexOf(showAutologoutTimeoutDialog).takeIf { it >= 0 }
        ) { position ->
            showAutologoutTimeoutDialog = null

            if (position != null) {
                viewModel.postEvent(WalletSettingsViewModel.LocalEvents.SetAutologoutTimeout(values[position]))
            }
        }
    }

    NavigateDestinations.JadeQR.getResult<JadeQRResult> {
        viewModel.postEvent(LocalEvents.CreateLightningAccount(it.result))
    }

    NavigateDestinations.Accounts.getResult<AccountAssetBalance> {
        viewModel.postEvent(LocalEvents.CopyAmpId(it.account))
    }

    val items by viewModel.items.collectAsStateWithLifecycle()
    val innerPadding = LocalInnerPadding.current
    val dialog = LocalDialog.current

    SetupScreen(
        viewModel = viewModel,
        withPadding = false,
        withInsets = !isInnerTab,
        withBottomInsets = false,
        onProgressStyle = OnProgressStyle.Full(bluBackground = true),
        sideEffectsHandler = {
            when (it) {
                is LocalSideEffects.CopyAmpId -> {
                    viewModel.postEvent(
                        NavigateDestinations.Accounts(
                            greenWallet = viewModel.greenWallet,
                            title = getString(Res.string.id_copy_amp_id),
                            accounts = AccountAssetBalanceList(it.accounts.map { it.accountAssetBalance }),
                            withAsset = false,
                            withAssetIcon = false,
                            withArrow = false,
                        )
                    )
                }

                is LocalSideEffects.ArchivedAccountDialog -> {
                    launch {
                        dialog.openDialog(
                            OpenDialogData(
                                title = StringHolder.create(Res.string.id_archived_account),
                                message = StringHolder.create(Res.string.id_there_is_already_an_archived),
                                icon = Res.drawable.box_arrow_down,
                                primaryText = getString(Res.string.id_continue),
                                onPrimary = {
                                    viewModel.postEvent(it.event)
                                },
                                secondaryText = getString(Res.string.id_archived_accounts),
                                onSecondary = {
                                    viewModel.postEvent(
                                        NavigateDestinations.ArchivedAccounts(
                                            greenWallet = viewModel.greenWallet,
                                            navigateToRoot = true
                                        )
                                    )
                                }
                            )
                        )
                    }
                }

                is LocalSideEffects.ExperimentalFeaturesDialog -> {
                    launch {
                        dialog.openDialog(
                            OpenDialogData(
                                title = StringHolder.create(Res.string.id_experimental_feature),
                                message = StringHolder.create(Res.string.id_experimental_features_might),
                                icon = Res.drawable.flask_fill,
                                onPrimary = {
                                    viewModel.postEvent(it.event)
                                }
                            )
                        )
                    }
                }

                is LocalSideEffects.OpenAutoLogoutTimeout -> {
                    showAutologoutTimeoutDialog = it.minutes
                }

                is LocalSideEffects.OpenPgpKey -> {
                    showPgpDialog = it.pgp
                }

                is LocalSideEffects.OpenTwoFactorThershold -> {
                    showThresholdDialog = it.threshold
                }

                is LocalSideEffects.LaunchBiometrics -> {
                    biometricsState?.getBiometricsCipher(viewModel)
                }

                is LocalSideEffects.Disable2FA -> {
                    showTwoFactorChangeDialog = it
                }

                is SideEffects.OpenDenominationExchangeRate -> {
                    denominationExchangeRateViewModel =
                        DenominationExchangeRateViewModel(viewModel.greenWallet)
                }
            }
        }) {

        LazyColumn(
            contentPadding = innerPadding.bottom()
                .plus(PaddingValues(horizontal = 16.dp))
                .plus(PaddingValues(bottom = (if (viewModel.section == WalletSettingsSection.General) 80.dp else 0.dp) + 16.dp)),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            items(items) { item ->
                when (item) {
                    is WalletSetting.Text -> {
                        item.title?.also {
                            Text(
                                text = it,
                                style = bodyLarge,
                                color = whiteMedium,
                                modifier = Modifier.padding(top = 16.dp).fillMaxWidth()
                            )
                        }
                        item.message?.also {
                            Text(
                                text = it,
                                style = bodyMedium,
                                color = whiteMedium
                            )
                        }
                    }

                    is WalletSetting.LearnMore -> {
                        LearnMoreButton {
                            viewModel.postEvent(item.event)
                        }
                    }

                    WalletSetting.Logout -> {
                        Setting(
                            title = stringResource(Res.string.id_logout),
                            subtitleColor = red,
                            painter = painterResource(Res.drawable.sign_out),
                            modifier = Modifier.clickable {
                                viewModel.postEvent(Logout(LogoutReason.USER_ACTION))
                            }
                        )
                    }

                    is WalletSetting.DenominationExchangeRate -> {
                        val list = listOf(
                            item.unit,
                            item.currency,
                            item.exchange
                        )
                        Setting(
                            title = stringResource(Res.string.id_denomination__exchange_rate),
                            subtitleAnnotated = colorText(
                                text = stringResource(
                                    Res.string.id_display_values_in_s_and, *list.toTypedArray()
                                ),
                                coloredTexts = list,
                                baseColor = whiteMedium,
                                color = whiteHigh
                            ),
                            modifier = Modifier.clickable {
                                viewModel.postEvent(LocalEvents.DenominationExchangeRate)
                            }
                        )
                    }

                    is WalletSetting.ArchivedAccounts -> {
                        Setting(
                            title = stringResource(Res.string.id_archived_accounts),
                            imageVector = PhosphorIcons.Regular.CaretRight,
                            modifier = Modifier.clickable {
                                viewModel.postEvent(
                                    ArchivedAccounts(
                                        greenWallet = viewModel.greenWallet
                                    )
                                )
                            })
                    }

                    WalletSetting.WatchOnly -> {
                        Setting(
                            title = stringResource(Res.string.id_wallet_details),
                            imageVector = PhosphorIcons.Regular.CaretRight,
                            modifier = Modifier.clickable {
                                viewModel.postEvent(LocalEvents.WatchOnly)
                            }
                        )
                    }

                    is WalletSetting.AutoLogoutTimeout -> {
                        Setting(
                            title = stringResource(Res.string.id_auto_logout_timeout),
                            subtitle = if (item.timeout == 1) "${item.timeout} ${stringResource(Res.string.id_minute)}" else stringResource(
                                Res.string.id_1d_minutes,
                                item.timeout
                            ),
                            modifier = Modifier.clickable {
                                viewModel.postEvent(LocalEvents.AutologoutTimeout)
                            })
                    }

                    is WalletSetting.JadeGenuineCheck -> {
                        Setting(
                            title = stringResource(Res.string.id_genuine_check),
                            subtitle = stringResource(Res.string.id_verify_the_authenticity_of),
                            modifier = Modifier.clickable {
                                viewModel.postEvent(JadeGenuineCheck(greenWalletOrNull = viewModel.greenWalletOrNull))
                            }
                        )
                    }

                    is WalletSetting.RecoveryTransactionEmails -> {
                        Setting(
                            title = stringResource(Res.string.id_recovery_transaction_emails),
                            checked = item.enabled,
                            onCheckedChange = {
                                viewModel.postEvent(LocalEvents.RecoveryTransactionEmails)
                            },
                            modifier = Modifier.clickable {
                                viewModel.postEvent(LocalEvents.RecoveryTransactionEmails)
                            }
                        )
                    }

                    WalletSetting.RequestRecoveryTransactions -> {
                        Setting(
                            title = stringResource(Res.string.id_request_recovery_transactions),
                            modifier = Modifier.clickable {
                                viewModel.postEvent(LocalEvents.RequestRecoveryTransactions)
                            })
                    }

                    WalletSetting.SetupEmailRecovery -> {
                        Setting(
                            title = stringResource(Res.string.id_set_an_email_for_recovery),
                            imageVector = PhosphorIcons.Regular.CaretRight,
                            modifier = Modifier.clickable {
                                viewModel.postEvent(LocalEvents.SetupEmailRecovery)
                            })
                    }

                    WalletSetting.ChangePin -> {
                        Setting(
                            title = stringResource(Res.string.id_change_pin),
                            imageVector = PhosphorIcons.Regular.CaretRight,
                            modifier = Modifier.clickable {
                                viewModel.postEvent(LocalEvents.ChangePin)
                            })
                    }

                    is WalletSetting.LoginWithBiometrics -> {
                        Setting(
                            title = stringResource(Res.string.id_login_with_biometrics),
                            subtitle = stringResource(
                                if (item.canEnable) {
                                    if (item.enabled) {
                                        Res.string.id_biometric_login_is_enabled
                                    } else {
                                        Res.string.id_biometric_login_is_disabled
                                    }
                                } else {
                                    Res.string.id_a_screen_lock_must_be_enabled
                                }
                            ),
                            checked = item.enabled,
                            enabled = !onProgress,
                            onCheckedChange = {
                                viewModel.postEvent(LocalEvents.LoginWithBiometrics)
                            },
                            modifier = Modifier.clickable {
                                if (!onProgress) {
                                    viewModel.postEvent(LocalEvents.LoginWithBiometrics)
                                }
                            }
                        )
                    }

                    is WalletSetting.PgpKey -> {
                        Setting(
                            title = stringResource(Res.string.id_pgp_key),
                            subtitle = if (item.enabled) null else stringResource(Res.string.id_add_a_pgp_public_key_to_receive),
                            modifier = Modifier.clickable {
                                viewModel.postEvent(LocalEvents.PgpKey)
                            }
                        )
                    }

                    WalletSetting.TwoFactorAuthentication -> {
                        Setting(
                            title = stringResource(Res.string.id_twofactor_authentication),
                            imageVector = PhosphorIcons.Regular.CaretRight,
                            modifier = Modifier.clickable {
                                viewModel.postEvent(LocalEvents.TwoFactorAuthentication)
                            }
                        )
                    }

                    WalletSetting.RecoveryPhrase -> {
                        Setting(
                            title = stringResource(Res.string.id_backup_recovery_phrase),
                            subtitle = stringResource(Res.string.id_touch_to_display),
                            imageVector = PhosphorIcons.Regular.CaretRight,
                            modifier = Modifier.clickable {
                                viewModel.postEvent(LocalEvents.RecoveryPhrase)
                            }
                        )
                    }

                    is WalletSetting.Version -> {
                        Setting(
                            title = stringResource(Res.string.id_version),
                            subtitle = item.version,
                            modifier = Modifier.clickable {
                                viewModel.postEvent(NavigateDestinations.About)
                            }
                        )
                    }

                    WalletSetting.SupportId -> {
                        Setting(
                            title = stringResource(Res.string.id_support),
                            subtitle = stringResource(Res.string.id_copy_support_id),
                            imageVector = PhosphorIcons.Regular.Copy,
                            modifier = Modifier.clickable {
                                viewModel.postEvent(LocalEvents.SupportId)
                            })
                    }

                    is WalletSetting.TwoFactorMethod -> {

                        Setting(
                            title = stringResource(item.method.localized),
                            subtitle = item.data.takeIf { item.enabled }
                                ?: if (item.enabled) stringResource(Res.string.id_enabled) else null,
                            checked = item.enabled,
                            onCheckedChange = {
                                viewModel.postEvent(
                                    Toggle2FA(
                                        item.method
                                    )
                                )
                            },
                            modifier = Modifier.clickable {
                                viewModel.postEvent(
                                    Toggle2FA(
                                        item.method
                                    )
                                )
                            }
                        )
                    }

                    is WalletSetting.TwoFactorThreshold -> {
                        Setting(
                            title = stringResource(Res.string.id_2fa_threshold),
                            subtitle = item.subtitle,
                            modifier = Modifier.clickable {
                                viewModel.postEvent(LocalEvents.TwoFactorThreshold)
                            }
                        )
                    }

                    is WalletSetting.RequestRecovery -> {
                        Setting(
                            title = stringResource(Res.string.id_recovery_transactions),
                            subtitle = stringResource(Res.string.id_legacy_script_coins),
                            imageVector = PhosphorIcons.Regular.CaretRight,
                            modifier = Modifier.clickable {
                                viewModel.postEvent(
                                    WalletSettings(
                                        greenWallet = viewModel.greenWallet,
                                        section = WalletSettingsSection.RecoveryTransactions,
                                        network = item.network
                                    )
                                )
                            }
                        )
                    }

                    is WalletSetting.ButtonEvent -> {
                        GreenButton(
                            text = item.title,
                            modifier = Modifier.fillMaxWidth(),
                            type = GreenButtonType.OUTLINE
                        ) {
                            viewModel.postEvent(item.event)
                        }
                    }

                    is WalletSetting.TwoFactorBucket -> {
                        Setting(
                            title = item.title,
                            subtitle = item.subtitle,
                            isRadio = true,
                            checked = item.enabled,
                            onCheckedChange = {
                                viewModel.postEvent(
                                    SetCsvTime(
                                        item.bucket
                                    )
                                )
                            },
                            modifier = Modifier.clickable {
                                viewModel.postEvent(
                                    SetCsvTime(
                                        item.bucket
                                    )
                                )
                            }
                        )
                    }

                    is WalletSetting.Lightning -> {
                        Setting(
                            title = stringResource(Res.string.id_lightning),
                            checked = item.enabled,
                            onCheckedChange = {
                                if (it) {
                                    viewModel.postEvent(LocalEvents.ChooseAccountType(AccountType.LIGHTNING))
                                } else {
                                    viewModel.postEvent(LocalEvents.DisableLightning)
                                }
                            },
                        )
                    }

                    WalletSetting.GetSupport -> {
                        Setting(
                            title = stringResource(Res.string.id_get_support),
                            imageVector = PhosphorIcons.Regular.CaretRight,
                            modifier = Modifier.clickable {
                                viewModel.postEvent(
                                    Support(
                                        type = SupportType.INCIDENT,
                                        supportData = SupportData.create(session = viewModel.sessionOrNull),
                                        greenWalletOrNull = viewModel.greenWalletOrNull
                                    )
                                )
                            }
                        )
                    }

                    WalletSetting.RenameWallet -> {
                        Setting(
                            title = stringResource(Res.string.id_rename_wallet),
                            modifier = Modifier.clickable {
                                viewModel.postEvent(RenameWallet(viewModel.greenWallet))
                            }
                        )
                    }

                    is WalletSetting.CopyAmpId -> {
                        Setting(
                            title = stringResource(Res.string.id_copy_amp_id),
                            imageVector = PhosphorIcons.Regular.CaretRight,
                            modifier = Modifier.clickable {
                                viewModel.postEvent(LocalEvents.CopyAmpId())
                            }
                        )
                    }

                    WalletSetting.CreateAmpAccount -> {
                        Setting(
                            title = stringResource(Res.string.id_generate_amp_id),
                            modifier = Modifier.clickable {
                                viewModel.postEvent(LocalEvents.ChooseAccountType(AccountType.AMP_ACCOUNT))
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Setting(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String? = null,
    subtitleAnnotated: AnnotatedString? = null,
    subtitleColor: Color = whiteMedium,
    painter: Painter? = null,
    imageVector: ImageVector? = null,
    checked: Boolean? = null,
    isRadio: Boolean = false,
    enabled: Boolean = true,
    onCheckedChange: ((Boolean) -> Unit) = {},
) {
    OutlinedCard(modifier = Modifier.then(modifier)) {
        GreenRow(space = 8, padding = 0, verticalAlignment = Alignment.Top) {
            GreenColumn(
                padding = 0, space = 4,
                modifier = Modifier
                    .padding(vertical = 24.dp)
                    .padding(start = 16.dp)
                    .ifTrue(imageVector == null && painter == null && checked == null) {
                        it.padding(end = 16.dp)
                    }
                    .weight(1f)
            ) {
                Text(
                    text = title,
                    style = titleSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                if (subtitle != null) {
                    Text(
                        text = subtitle,
                        style = bodyLarge,
                        color = subtitleColor
                    )
                }
                if (subtitleAnnotated != null) {
                    Text(
                        text = subtitleAnnotated,
                        style = bodyLarge,
                        color = subtitleColor
                    )
                }
            }

            if (checked != null) {
                if (isRadio) {
                    RadioButton(
                        selected = checked,
                        enabled = enabled,
                        onClick = {
                            onCheckedChange.invoke(true)
                        },
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 16.dp)
                    )
                } else {
                    Switch(
                        checked = checked,
                        onCheckedChange = onCheckedChange,
                        enabled = enabled,
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 16.dp)
                    )
                }

            }

            if (painter != null) {
                Icon(
                    painter = painter,
                    contentDescription = null,
                    tint = whiteLow,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 16.dp)
                )
            }

            imageVector?.also {
                Icon(
                    imageVector = it,
                    contentDescription = null,
                    tint = whiteLow,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(end = 16.dp)
                )
            }
        }
    }
}
