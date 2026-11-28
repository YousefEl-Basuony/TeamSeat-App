package com.stackmates.teamseat.data.local

import android.content.Context
import com.stackmates.teamseat.core.model.User
import org.json.JSONArray
import org.json.JSONObject

data class SavedAccount(
    val uid: String,
    val name: String,
    val email: String,
    val roleTag: String,
    val avatarEmoji: String,
    val savedPassword: String = ""
)

class SavedAccountManager(context: Context) {
    private val prefs = context.getSharedPreferences("teamseat_saved_accounts", Context.MODE_PRIVATE)

    fun getSavedAccounts(): List<SavedAccount> {
        val raw = prefs.getString("accounts_json", null) ?: return emptyList()
        return runCatching {
            val array = JSONArray(raw)
            buildList {
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    add(
                        SavedAccount(
                            uid = obj.optString("uid"),
                            name = obj.optString("name"),
                            email = obj.optString("email"),
                            roleTag = obj.optString("roleTag"),
                            avatarEmoji = obj.optString("avatarEmoji", "👩‍💻"),
                            savedPassword = obj.optString("savedPassword", "")
                        )
                    )
                }
            }
        }.getOrDefault(emptyList())
    }

    fun saveAccount(user: User, password: String? = null) {
        if (user.uid.isBlank()) return
        val existingList = getSavedAccounts()
        val previousPassword = existingList.firstOrNull { it.uid == user.uid }?.savedPassword.orEmpty()
        val effectivePassword = if (!password.isNullOrBlank()) password else previousPassword

        val current = existingList.filterNot { it.uid == user.uid }.toMutableList()
        current.add(
            0,
            SavedAccount(
                uid = user.uid,
                name = user.name.ifBlank { user.email.substringBefore("@") },
                email = user.email,
                roleTag = user.roleTag,
                avatarEmoji = user.avatarEmoji.ifBlank { "👩‍💻" },
                savedPassword = effectivePassword
            )
        )
        persistAccounts(current)
    }

    fun removeAccount(uid: String) {
        if (uid.isBlank()) return
        val remaining = getSavedAccounts().filterNot { it.uid == uid }
        persistAccounts(remaining)
    }

    private fun persistAccounts(accounts: List<SavedAccount>) {
        val array = JSONArray()
        accounts.forEach { acc ->
            array.put(
                JSONObject().apply {
                    put("uid", acc.uid)
                    put("name", acc.name)
                    put("email", acc.email)
                    put("roleTag", acc.roleTag)
                    put("avatarEmoji", acc.avatarEmoji)
                    put("savedPassword", acc.savedPassword)
                }
            )
        }
        prefs.edit().putString("accounts_json", array.toString()).apply()
    }
}
