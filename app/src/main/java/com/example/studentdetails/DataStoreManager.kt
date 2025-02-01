package com.example.studentdetails

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreManager(private val context: Context) {

        companion object {
        // Define the DataStore
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("Student_Details")

        // Define the keys for the preferences
        private val studentIdKey = stringPreferencesKey("student_number")
        private val usernameKey = stringPreferencesKey("user_name")
        private val courseNameKey = stringPreferencesKey("course_name")
    }



    suspend fun saveStudentDetails(studentId: String, username: String, courseName: String) {
        context.dataStore.edit { preferences ->
            preferences[studentIdKey] = studentId
            preferences[usernameKey] = username
            preferences[courseNameKey] = courseName
        }
    }

    // Get student ID from DataStore
    val getStudentId: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[studentIdKey] ?: "157" }

    // Get username from DataStore
    val getUserName: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[usernameKey] ?: "" }

    // Get course name from DataStore
    val getCourseName: Flow<String> = context.dataStore.data
        .map { preferences -> preferences[courseNameKey] ?: "" }

    // Reset student details (clear the preferences)
    suspend fun resetStudentDetails() {
        context.dataStore.edit { preferences ->
            preferences.remove(studentIdKey)
            preferences.remove(usernameKey)
            preferences.remove(courseNameKey)
        }
    }
}
