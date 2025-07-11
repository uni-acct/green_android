package com.blockstream.compose.sheets

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import blockstream_green.common.generated.resources.Res
import blockstream_green.common.generated.resources.id_2fa_dispute_in_progress
import blockstream_green.common.generated.resources.id_2fa_reset_in_progress
import blockstream_green.common.generated.resources.id_cancel_2fa_reset
import blockstream_green.common.generated.resources.id_dispute_twofactor_reset
import blockstream_green.common.generated.resources.id_how_to_stop_this_reset
import blockstream_green.common.generated.resources.id_if_you_are_the_rightful_owner
import blockstream_green.common.generated.resources.id_if_you_did_not_request_the
import blockstream_green.common.generated.resources.id_if_you_have_access_to_a
import blockstream_green.common.generated.resources.id_if_you_initiated_the_2fa_reset
import blockstream_green.common.generated.resources.id_permanently_block_this_wallet
import blockstream_green.common.generated.resources.id_the_1_year_2fa_reset_process
import blockstream_green.common.generated.resources.id_the_waiting_period_is_necessary
import blockstream_green.common.generated.resources.id_undo_2fa_dispute
import blockstream_green.common.generated.resources.id_your_wallet_is_locked_for_a
import blockstream_green.common.generated.resources.id_your_wallet_is_locked_under_2fa
import com.blockstream.common.data.TwoFactorMethod
import com.blockstream.common.data.TwoFactorSetupAction
import com.blockstream.common.gdk.data.Network
import com.blockstream.common.gdk.data.TwoFactorReset
import com.blockstream.common.models.GreenViewModel
import com.blockstream.common.navigation.NavigateDestinations
import com.blockstream.compose.components.GreenBottomSheet
import com.blockstream.compose.components.GreenButton
import com.blockstream.compose.theme.bodyMedium
import com.blockstream.compose.theme.labelLarge
import com.blockstream.ui.components.GreenColumn
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TwoFactorResetBottomSheet(
    viewModel: GreenViewModel,
    network: Network,
    twoFactorReset: TwoFactorReset?,
    onDismissRequest: () -> Unit,
) {

    GreenBottomSheet(
        title = stringResource(if (twoFactorReset?.isDisputed == true) Res.string.id_2fa_dispute_in_progress else Res.string.id_2fa_reset_in_progress),
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        viewModel = viewModel,
        onDismissRequest = onDismissRequest
    ) {

        GreenColumn(
            padding = 0,
            space = 24,
            modifier = Modifier
                .padding(top = 16.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {

            if (twoFactorReset?.isDisputed == true) {
                GreenColumn(padding = 0, space = 8) {
                    Text(
                        stringResource(
                            Res.string.id_your_wallet_is_locked_under_2fa
                        ), style = labelLarge
                    )
                    Text(
                        stringResource(Res.string.id_the_1_year_2fa_reset_process),
                        style = bodyMedium
                    )
                }

                GreenColumn(padding = 0, space = 8) {
                    Text(
                        stringResource(Res.string.id_if_you_are_the_rightful_owner),
                        style = bodyMedium
                    )

                    GreenButton(
                        text = stringResource(Res.string.id_cancel_2fa_reset),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.postEvent(
                            NavigateDestinations.TwoFactorSetup(
                                greenWallet = viewModel.greenWallet,
                                method = TwoFactorMethod.EMAIL,
                                action = TwoFactorSetupAction.CANCEL,
                                network = network
                            )
                        )
                        onDismissRequest()
                    }
                }

                GreenColumn(padding = 0, space = 8) {

                    Text(
                        stringResource(Res.string.id_if_you_initiated_the_2fa_reset),
                        style = bodyMedium
                    )

                    GreenButton(
                        text = stringResource(Res.string.id_undo_2fa_dispute),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.postEvent(
                            NavigateDestinations.TwoFactorSetup(
                                greenWallet = viewModel.greenWallet,
                                method = TwoFactorMethod.EMAIL,
                                action = TwoFactorSetupAction.UNDO_DISPUTE,
                                network = network
                            )
                        )
                        onDismissRequest()
                    }
                }

            } else if (twoFactorReset != null) {
                GreenColumn(padding = 0, space = 8) {
                    Text(
                        stringResource(
                            Res.string.id_your_wallet_is_locked_for_a,
                            twoFactorReset.daysRemaining
                        ), style = labelLarge
                    )
                    Text(
                        stringResource(Res.string.id_the_waiting_period_is_necessary),
                        style = bodyMedium
                    )
                }

                GreenColumn(padding = 0, space = 8) {
                    Text(
                        stringResource(
                            Res.string.id_how_to_stop_this_reset,
                        ), style = labelLarge
                    )
                    Text(
                        stringResource(Res.string.id_if_you_have_access_to_a, twoFactorReset.daysRemaining),
                        style = bodyMedium
                    )

                    GreenButton(
                        text = stringResource(Res.string.id_cancel_2fa_reset),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.postEvent(
                            NavigateDestinations.TwoFactorSetup(
                                greenWallet = viewModel.greenWallet,
                                method = TwoFactorMethod.EMAIL,
                                action = TwoFactorSetupAction.CANCEL,
                                network = network
                            )
                        )
                        onDismissRequest()
                    }
                }

                GreenColumn(padding = 0, space = 8) {
                    Text(
                        stringResource(
                            Res.string.id_permanently_block_this_wallet,
                        ), style = labelLarge
                    )
                    Text(
                        stringResource(Res.string.id_if_you_did_not_request_the),
                        style = bodyMedium
                    )

                    GreenButton(
                        text = stringResource(Res.string.id_dispute_twofactor_reset),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        viewModel.postEvent(
                            NavigateDestinations.TwoFactorSetup(
                                greenWallet = viewModel.greenWallet,
                                method = TwoFactorMethod.EMAIL,
                                action = TwoFactorSetupAction.DISPUTE,
                                network = network
                            )
                        )
                        onDismissRequest()
                    }
                }
            }
        }
    }
}
