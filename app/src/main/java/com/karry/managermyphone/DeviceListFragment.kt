package com.karry.managermyphone

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.karry.managermyphone.adapter.DeviceAdapter
import com.karry.managermyphone.databinding.FragmentDeviceListBinding
import com.karry.managermyphone.model.Device
import com.karry.managermyphone.viewmodel.DatabaseViewModel

@SuppressLint("HardwareIds")
class DeviceListFragment : Fragment() {
    private var _binding: FragmentDeviceListBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: DeviceAdapter
    private val devices = arrayListOf<Device>()
    private lateinit var databaseViewModel: DatabaseViewModel
    private val deviceId: String by lazy { Settings.Secure.getString(this@DeviceListFragment.context?.contentResolver, Settings.Secure.ANDROID_ID) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDeviceListBinding.inflate(inflater, container, false)
        init()
        listen()
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun init() {
        databaseViewModel = ViewModelProvider(requireActivity(), ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(DatabaseViewModel::class.java)
        adapter = DeviceAdapter(devices)
        binding.rvDevices.adapter = adapter
        databaseViewModel.fetchDevices(deviceId)
        databaseViewModel.devices.observe(viewLifecycleOwner) {
            devices.addAll(it)
            ViewCompat.setNestedScrollingEnabled(binding.rvDevices, false)
            adapter.notifyDataSetChanged()
        }
    }

    private fun listen() {
        val animation = AlphaAnimation(1f, 0.7f)
        adapter.setOnItemClickedListener(object : DeviceAdapter.OnItemClicked {
            override fun onItemClicked(v: View, position: Int) {
                v.startAnimation(animation)
                Log.d(TAG, "onItemClicked: ${devices[position]}")
                val bundle = bundleOf("device" to devices[position])
                v.findNavController().navigate(R.id.action_deviceListFragment_to_deviceManagerFragment, bundle)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "DeviceListFragment"
    }
}