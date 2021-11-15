package com.karry.managermyphone

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.karry.managermyphone.api.ApiClient
import com.karry.managermyphone.api.ApiService
import com.karry.managermyphone.databinding.FragmentDeviceManagerBinding
import com.karry.managermyphone.model.Device
import com.karry.managermyphone.viewmodel.DatabaseViewModel
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class DeviceManagerFragment : Fragment(), View.OnClickListener {
    private var _binding: FragmentDeviceManagerBinding? = null
    private val binding get() = _binding!!
    private lateinit var databaseViewModel: DatabaseViewModel
    private lateinit var device: Device
    private val messageSend = arrayListOf("Get Location", "Lock", "Wipe Data")

    private val header = hashMapOf(
        "Authorization" to "key=AAAArcHd2fg:APA91bFsoF9s2cF8Ole04Yd8G7Qbvw9lp5BsR4te3i8x-Q6pnt2SQ-7hoPIQ7kUgG1tQBk34VlCR0fc54-hV3hFQSpoFMj-nOVlMjNbzoYixkX8ViUJjYjSROtctQHLJksJRUK4ITiVf",
        "Content-Type" to "application/json"
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceManagerBinding.inflate(inflater, container, false)
        device = arguments?.getParcelable("device")!!
        databaseViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(DatabaseViewModel::class.java)
        with(binding) {
            buttonGetLocation.setOnClickListener(this@DeviceManagerFragment)
            buttonLock.setOnClickListener(this@DeviceManagerFragment)
            buttonWipeData.setOnClickListener(this@DeviceManagerFragment)
        }
        return binding.root
    }

    private fun sendNotification(message: String) {
        val data = JSONObject()
        data.put("message", message)
        val body = JSONObject()
        body.put("data", data)
        body.put("to", device.fcmToken)
        Log.d("FCM_Json","JSON: $body")
        ApiClient.client.create(ApiService::class.java).sendMessage(header, body.toString())
            .enqueue(object : retrofit2.Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        try {
                            if (response.body() != null) {
                                val responseJson = JSONObject(response.body()!!)
                                val results = responseJson.getJSONArray("results")
                                if (responseJson.getInt("failure") == 1) {
                                    val error = results[0] as JSONObject
                                    Toast.makeText(context, error.getString("error"), Toast.LENGTH_SHORT).show()
                                    return
                                }
                            }
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                    } else {
                        Toast.makeText(context, "Error: ${response.code()}", Toast.LENGTH_SHORT).show()

                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    Log.e("API Post", "onFailure: ", t)
                }
            })
        databaseViewModel.fetchLocation(device.deviceId)
        databaseViewModel.location.observe(viewLifecycleOwner) {
            val latitude = it.getDouble("Latitude")
            val longitude = it.getDouble("Longitude")
            val time = it.getLong("Time")

            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm aa", Locale("vi", "VN"))
            val text = "Latitude: $latitude\nLongitude: $longitude\nTime: ${simpleDateFormat.format(time)}"
            binding.tvLocation.text = text
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.buttonGetLocation -> sendNotification(messageSend[0])
            binding.buttonLock -> sendNotification(messageSend[1])
            binding.buttonWipeData -> sendNotification(messageSend[2])
            else -> return
        }
    }

}