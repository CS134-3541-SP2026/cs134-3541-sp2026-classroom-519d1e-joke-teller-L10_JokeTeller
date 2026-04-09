package edu.cs134.joketeller

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.contracts.contract

class MainActivity : ComponentActivity(), TextToSpeech.OnInitListener {
    // todo 2a: declare TextToSpeech
    // hint: use a lateinit so we can instantiate it later

    override fun onInit(status: Int) {
        speakText("Press the button and ask for a joke")
    }

    private fun speakText(text: String) {
        // todo 2c: call speak on your text to speech variable
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // todo 2b: instantiate your TextToSpeech object

        setContent {
            var jokeText by remember { mutableStateOf("Press the button and ask for a joke!") }
            var loading by remember { mutableStateOf(false) }
            val coroutineScope = rememberCoroutineScope()

            fun fetchAndSpeakJoke(currentText: String, updateText: (String) -> Unit) {
                // todo 1: launch a coroutine scope
                // it should trigger the loading circle (loading = true)
                // and clear the circle when done
                // between the loading settings, fetch a joke,
                // then update text and call speech
                coroutineScope.launch {
                    // stuff in our braces runs on a separate thread!
                    loading = true

                    val joke = JokeRepository.fetchJokeText()
                    updateText(joke)
                    speakText(joke)

                    loading = false
                }
            }

            // todo 3: We need microphone permission to proceed with audio

            val speechLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) {
                // todo 5: correctly identify the result type of this activity
                // hint: check step 4's intent for a hint
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(jokeText, modifier = Modifier.padding(bottom = 24.dp))

                Spacer(modifier = Modifier.height(16.dp))

                // Test joke button
                Button(onClick = {
                    fetchAndSpeakJoke("Testing joke website") { joke -> jokeText = joke }
                }) {
                    Text("Test Joke")
                }

                // Voice button
                Button(onClick = {
                    if (!SpeechRecognizer.isRecognitionAvailable(this@MainActivity)) return@Button
                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                        // todo 4: establishing intent
                    }
                    speechLauncher.launch(intent)
                }) {
                    Text("Ask for Joke")
                }

                if (loading) CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            }
        }
    }

    override fun onDestroy() {
        // todo 2d: call stop() and shutdown() on your TextToSpeech object
        super.onDestroy()
    }
}