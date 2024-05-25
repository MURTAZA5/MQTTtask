package com.cowlar.task.mqqtsampletask

import androidx.activity.OnBackPressedDispatcher
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.robolectric.Robolectric

@RunWith(MockitoJUnitRunner::class)
class MainActivityTest {

    @Mock
    private lateinit var mqttClient: MQTTClient

    @Mock
    private lateinit var mqttToken: IMqttToken

    @Mock
    private lateinit var mqttException: Throwable

    @Mock
    private lateinit var onBackPressedDispatcher: OnBackPressedDispatcher

    @InjectMocks
    private lateinit var mainActivity: MainActivity

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
//        mainActivity.onBackPressedDispatcher = onBackPressedDispatcher
        val activityController = Robolectric.buildActivity(MainActivity::class.java).create().start().resume()
        mainActivity = activityController.get()

        // Mock the MQTT client in the MainActivity
        mainActivity.mqttClient = mqttClient
    }

    @Test
    fun testOnBackPressDisConnect_whenConnected() {
        Mockito.`when`(mqttClient.isConnected()).thenReturn(true)

        mainActivity.onBackPressDisConnect()

        // Trigger the back press event
        mainActivity.onBackPressedDispatcher.onBackPressed()

        Mockito.verify(mqttClient).disconnect(any())
    }

    @Test
    fun testOnBackPressDisConnect_whenNotConnected() {
        Mockito.`when`(mqttClient.isConnected()).thenReturn(false)

        mainActivity.onBackPressDisConnect()

        // Trigger the back press event
        mainActivity.onBackPressedDispatcher.onBackPressed()

        // Ensure no disconnect attempt
        Mockito.verify(mqttClient, Mockito.never()).disconnect(any())
    }
}
