package com.example.studentdetails

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.studentdetails.ui.theme.StudentDetailsTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentDetailsTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    StudentDetailsScreen()

                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun StudentDetailsScreen() {
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val studentId = remember { mutableStateOf(TextFieldValue()) }
    val username = remember { mutableStateOf(TextFieldValue()) }
    val courseName = remember { mutableStateOf(TextFieldValue()) }


    val store = DataStoreManager(context)
    val studentIdText = store.getStudentId.collectAsState(initial = "")
    val usernameText = store.getUserName.collectAsState(initial = "")
    val courseNameText = store.getCourseName.collectAsState(initial = "")


    // Populate fields with DataStore data
       LaunchedEffect(studentIdText.value, usernameText.value, courseNameText.value) {
           studentId.value = TextFieldValue(studentIdText.value)
           username.value = TextFieldValue(usernameText.value)
           courseName.value = TextFieldValue(courseNameText.value)
       }
        Column(
            modifier = Modifier
              .fillMaxSize()
                .clickable { keyboardController?.hide() }
                .padding(15.dp),

            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(text = "Enter Student Details  ", fontWeight = FontWeight.Bold)
            // Input fields
            Spacer(modifier = Modifier.height(20.dp))

            TextField(
                value = studentId.value,
                onValueChange = { studentId.value = it },
                label = { Text("Student ID") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = username.value,
                onValueChange = { username.value = it },
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
            )
            TextField(
                value = courseName.value,
                onValueChange = { courseName.value = it },
                label = { Text("Course Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = {
                    // Load button: Load data from DataStore
                    studentId.value = TextFieldValue(studentIdText.value)
                    username.value = TextFieldValue(usernameText.value)
                    courseName.value = TextFieldValue(courseNameText.value)
                }, modifier = Modifier.weight(1f)) {
                    Text("Load")
                }
                Button(onClick = {
                    // Store button: Save data to DataStore
                    CoroutineScope(Dispatchers.IO).launch {
                        store.saveStudentDetails(
                            studentId.value.text,
                            username.value.text,
                            courseName.value.text
                        )
                    }
                }, modifier = Modifier.weight(1f)) {
                    Text("Store")
                }
            }
            Button(onClick = {
                // Reset button: Clear data in DataStore
                CoroutineScope(Dispatchers.IO).launch {
                    store.resetStudentDetails()
                    studentId.value = TextFieldValue("")
                    username.value = TextFieldValue("")
                    courseName.value = TextFieldValue("")
                }
            }, modifier = Modifier.fillMaxWidth()) {
                Text("Reset")
            }
               //Text(text = usernameText.value)
            // About Section
            Spacer(modifier = Modifier.height(32.dp))
            Text("Student Details:", style = MaterialTheme.typography.titleLarge)
            Text("Student Name: ${usernameText.value}")
            Text("Course Name: ${courseNameText.value}")

        }
    }


