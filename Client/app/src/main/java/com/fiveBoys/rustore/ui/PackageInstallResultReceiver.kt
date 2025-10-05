package com.fiveBoys.rustore

import android.content.*
import android.content.pm.PackageInstaller
import android.os.Build
import android.widget.Toast

class PackageInstallResultReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val st  = intent.getIntExtra(PackageInstaller.EXTRA_STATUS, PackageInstaller.STATUS_FAILURE)
        val msg = intent.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE) ?: ""
        when (st) {
            PackageInstaller.STATUS_PENDING_USER_ACTION -> {
                val confirm = if (Build.VERSION.SDK_INT >= 33)
                    intent.getParcelableExtra(Intent.EXTRA_INTENT, Intent::class.java)
                else @Suppress("DEPRECATION")
                intent.getParcelableExtra(Intent.EXTRA_INTENT) as? Intent
                confirm?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                if (confirm != null) context.startActivity(confirm)
                else Toast.makeText(context, "Нужно подтверждение установки", Toast.LENGTH_LONG).show()
            }
            PackageInstaller.STATUS_SUCCESS ->
                Toast.makeText(context, "Установка завершена ✅", Toast.LENGTH_LONG).show()
            PackageInstaller.STATUS_FAILURE_ABORTED ->
                Toast.makeText(context, "Установка отменена", Toast.LENGTH_LONG).show()
            else ->
                Toast.makeText(context, "Ошибка установки ($st): $msg", Toast.LENGTH_LONG).show()
        }
    }
}
