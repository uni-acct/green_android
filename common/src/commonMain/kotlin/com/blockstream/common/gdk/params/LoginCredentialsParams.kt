package com.blockstream.common.gdk.params

import com.blockstream.common.data.HwWatchOnlyCredentials
import com.blockstream.common.gdk.GreenJson
import com.blockstream.common.gdk.data.Credentials
import com.blockstream.common.gdk.data.PinData
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LoginCredentialsParams @OptIn(ExperimentalSerializationApi::class) constructor(
    @SerialName("mnemonic")
    val mnemonic: String? = null,
    @SerialName("bip39_passphrase")
    val bip39Passphrase: String? = null,
    @SerialName("pin")
    val pin: String? = null,
    @SerialName("pin_data")
    val pinData: PinData? = null,
    @SerialName("username")
    val username: String? = null,
    @SerialName("password")
    val password: String? = null,
    @SerialName("watch_only_data")
    val watchOnlyData: String? = null,
    @SerialName("master_xpub")
    val masterXpub: String? = null,
    @SerialName("core_descriptors")
    val coreDescriptors: List<String>? = null,
    @SerialName("hwWatchOnlyCredentials")
    val hwWatchOnlyCredentials: HwWatchOnlyCredentials? = null, // this value is not used by GDK, only from the app
    @SerialName("slip132_extended_pubkeys")
    val slip132ExtendedPubkeys: List<String>? = null,
) : GreenJson<LoginCredentialsParams>() {
    override fun encodeDefaultsValues() = false

    override fun kSerializer() = serializer()

    val isWatchOnly
        get() = !username.isNullOrBlank() || !slip132ExtendedPubkeys.isNullOrEmpty() || !coreDescriptors.isNullOrEmpty() || hwWatchOnlyCredentials != null

    fun hwWatchOnlyCredentialsToLoginCredentialsParams(network: String): LoginCredentialsParams? {
        return hwWatchOnlyCredentials?.credentials?.get(network)?.toLoginCredentials()
    }

    companion object {
        val empty = LoginCredentialsParams()

        fun fromCredentials(credentials: Credentials): LoginCredentialsParams {
            return LoginCredentialsParams(
                mnemonic = credentials.mnemonic,
                bip39Passphrase = credentials.bip39Passphrase,
            )
        }
    }
}