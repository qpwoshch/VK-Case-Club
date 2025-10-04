package com.fiveBoys.rustore

class Connect {
    fun getData(name : String) : String {

        return """
            {
              "id": 101,
              "name": "Вконтакте",
              "developer": "VK",
              "category": "Мессенджер",
              "ratingAge": "14+",
              "fullDesc": "«ВКонтакте» (международное название — VK) — российская социальная сеть. Предназначена для общения, обмена информацией и развлекательного досуга. ",
              "iconUrl": "https://upload.wikimedia.org/wikipedia/commons/2/21/VK.com-logo.svg",
              "screenshots": [
                "https://upload.wikimedia.org/wikipedia/commons/3/3d/VK_Desktop.png",
                "https://upload.wikimedia.org/wikipedia/commons/f/f0/VK_mobile_app.png"
              ],
              "apkUrl": "https://rt.pornhub.com/",
              "apkSize": 14567
            }
        """.trimIndent()
    }
}