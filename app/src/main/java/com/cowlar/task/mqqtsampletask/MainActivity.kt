package com.cowlar.task.mqqtsampletask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.cowlar.task.mqqtsampletask.databinding.ActivityMainBinding
import com.example.mqttkotlinsample.MQTT_CLIENT_ID
import com.example.mqttkotlinsample.MQTT_PWD
import com.example.mqttkotlinsample.MQTT_SERVER_URI
import com.example.mqttkotlinsample.MQTT_USERNAME
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class MainActivity : AppCompatActivity() {

    lateinit var mqttClient : MQTTClient
       lateinit var binding :  ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mqttClient = MQTTClient(this, MQTT_SERVER_URI, MQTT_CLIENT_ID)
         onBackPressDisConnect()
        initViewClickSetup()
    }

     fun onBackPressDisConnect() {
        onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (mqttClient.isConnected()) {
                    // Disconnect from MQTT Broker
                    mqttClient.disconnect(object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken?) {
                            Log.e(this.javaClass.name, "Disconnected")

                            Toast.makeText(this@MainActivity, "MQTT Disconnection success", Toast.LENGTH_SHORT).show()
                                 finish()
                            // Disconnection success, come back to Connect Fragment
                        }

                        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                            Log.e(this.javaClass.name, "Failed to disconnect")
                            finish()
                        }
                    })
                } else {
                    finish()
                    Log.e(this.javaClass.name, "Impossible to disconnect, no server connected")
                }
            }
        })
    }

    fun initConnectMqttBroker(){
        // Open MQTT Broker communication

        // Connect and login to MQTT Broker
        mqttClient.connect( MQTT_USERNAME,
            MQTT_PWD,
            object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    Log.e(this.javaClass.name, "Connection success")

                    Toast.makeText(this@MainActivity, "MQTT Connection success", Toast.LENGTH_SHORT).show()
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    Log.e(this.javaClass.name, "Connection failure: ${exception.toString()}")

                    Toast.makeText(this@MainActivity, "MQTT Connection fails: ${exception.toString()}", Toast.LENGTH_SHORT).show()

                    // Come back to Connect Fragment
                }
            },
            object : MqttCallback {
                override fun messageArrived(topic: String?, message: MqttMessage?) {
                    val msg = "Receive message: ${message.toString()} from topic: $topic"
                    Log.e(this.javaClass.name, msg)
                      binding.textviewReciveResult.text= msg.toString()
                    Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                }

                override fun connectionLost(cause: Throwable?) {
                    Log.e(this.javaClass.name, "Connection lost ${cause.toString()}")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {
                    Log.e(this.javaClass.name, "Delivery complete ${token.toString()}")
                }

            })
    }
    fun initViewClickSetup(){
        binding.buttonConnectMqqt.setOnClickListener {
                initConnectMqttBroker()
        }

        binding.buttonPublish.setOnClickListener {
            val topic   =binding.edittextPubtopic.text.toString()
            val message =binding.edittextPubmsg.text.toString()

            if (mqttClient.isConnected()) {
                mqttClient.publish(topic,
                    message,
                    1,
                    false,
                    object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken?) {
                            val msg ="Publish message: $message to topic: $topic"
                            Log.e(this.javaClass.name, msg)

                            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                            Log.e(this.javaClass.name, "Failed to publish message to topic")
                        }
                    })
            } else {
//                binding.textviewReciveResult.text=resources.getString(R.string.no_server_connected_publish)
                Log.e(this.javaClass.name, "Impossible to publish, no server connected")
            }
        }

       binding.buttonSubscribe.setOnClickListener {
            val topic  =binding.edittextSubtopic.text.toString()
            if (mqttClient.isConnected()) {
                mqttClient.subscribe(topic,
                    1,
                    object : IMqttActionListener {
                        override fun onSuccess(asyncActionToken: IMqttToken?) {
                            val msg = "Subscribed to: $topic"
                            Log.e(this.javaClass.name, msg)

                            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                            Log.e(this.javaClass.name, "Failed to subscribe: $topic")
                        }
                    })
            } else {
                binding.textviewReciveResult.text=resources.getString(R.string.no_server_connected_subscriber)
                Log.e(this.javaClass.name, "Impossible to subscribe, no server connected")
            }
        }

//       findViewById<Button>(R.id.button_unsubscribe).setOnClickListener {
//            val topic   =findViewById<EditText>(R.id.edittext_subtopic).text.toString()
//
//            if (mqttClient.isConnected()) {
//                mqttClient.unsubscribe( topic,
//                    object : IMqttActionListener {
//                        override fun onSuccess(asyncActionToken: IMqttToken?) {
//                            val msg = "Unsubscribed to: $topic"
//                            Log.e(this.javaClass.name, msg)
//
//                            Toast.makeText(this@MainActivity, msg, Toast.LENGTH_SHORT).show()
//                        }
//
//                        override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
//                            Log.e(this.javaClass.name, "Failed to unsubscribe: $topic")
//                        }
//                    })
//            } else {
//                Log.e(this.javaClass.name, "Impossible to unsubscribe, no server connected")
//            }
//        }

    }
}