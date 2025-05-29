package ues.pdm115.proyectopdm115grupo3
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

object DataStoreManager {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

    val USERNAME_KEY = stringPreferencesKey("username")
    val USER_TYPE_KEY = stringPreferencesKey("user_type") // Nuevo campo: "comprador" o "repartidor"

    suspend fun saveUsername(context: Context, username: String, userType: String) {
        context.dataStore.edit { preferences ->
            preferences[USERNAME_KEY] = username
            preferences[USER_TYPE_KEY] = userType
        }
    }

    suspend fun getUserType(context: Context): String? {
        val preferences = context.dataStore.data.first()
        return preferences[USER_TYPE_KEY]
    }

    suspend fun getUsername(context: Context): String? {
        val preferences = context.dataStore.data.first()
        return preferences[USERNAME_KEY]
    }

    suspend fun clearUsername(context: Context) {
        context.dataStore.edit { preferences ->
            preferences.remove(USERNAME_KEY)
        }
    }
}