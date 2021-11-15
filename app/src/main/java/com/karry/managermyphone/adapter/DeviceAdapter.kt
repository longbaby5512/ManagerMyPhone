package com.karry.managermyphone.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.karry.managermyphone.databinding.ItemDeviceBinding
import com.karry.managermyphone.model.Device

class DeviceAdapter(private val devices: ArrayList<Device>) : RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder>() {
    private var onItemClicked: OnItemClicked ?= null

    inner class DeviceViewHolder(private val binding: ItemDeviceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(device: Device) {
            with(binding) {
                tvDeviceId.text = device.deviceId
                tvDeviceName.text = device.deviceName
            }
        }
        init {
            binding.root.apply {
                setOnClickListener { onItemClicked?.onItemClicked(it, adapterPosition) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding = ItemDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeviceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(devices[position])
    }

    override fun getItemCount() = devices.size

    fun setOnItemClickedListener(listener: OnItemClicked) {
        onItemClicked = listener
    }

    interface OnItemClicked {
        fun onItemClicked(v: View, position: Int)
    }

}