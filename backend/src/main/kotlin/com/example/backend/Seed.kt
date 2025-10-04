package com.example.backend

internal data class SeedApp(
    val id: String,
    val name: String,
    val developer: String,
    val category: String,
    val ratingAge: String,
    val shortDesc: String,
    val fullDesc: String,
    val iconPath: String,
    val screenshotPaths: List<String>,
    val apkPath: String
)

internal val appsSeed = listOf(
    SeedApp(
        id = "1",
        name = "Palette",
        developer = "Saud Shaikh ",
        category = "Инструменты",
        ratingAge = "6+",
        shortDesc = "Palette — это универсальный центр для поиска красиво оформленных и высоко персонализированных настроек домашнего экрана.",
        fullDesc = "Palette — это универсальный центр для поиска красиво оформленных и высоко персонализированных настроек домашнего экрана.\n" +
                "\n" +
                "Если вы ищете вдохновение для создания удивительной настройки домашнего экрана, просто пролистайте приложение, найдите понравившуюся вам настройку, и вся необходимая информация (например, пакеты иконок, виджеты, обои и т.д.) будет сразу же доступна.\n" +
                "\n" +
                "После того как вы создадите несколько собственных уникальных настроек домашнего экрана, вы можете отправить их для показа в приложении (функция доступна только для премиум-класса).\n" +
                "\n" +
                "Красиво оформленный интерфейс.\n" +
                "Новые настройки добавляются каждую неделю!\n" +
                "Прямые ссылки на все ресурсы, которые могут понадобиться для повторения настроек на вашем собственном телефоне.\n" +
                "Шанс попасть на YouTube-канал Сэма Бекмана!",
        iconPath = "icons/1/icon.png",
        screenshotPaths = listOf(
            "screenshots/1/screen1.png",
            "screenshots/1/screen2.png",
            "screenshots/1/screen3.png"
        ),
        apkPath = "apks/demo.apk"
    ),
    SeedApp(
        id = "2",
        name = "Wildberries",
        developer = "Wildberries LLC",
        category = "Инструменты",
        ratingAge = "6+",
        shortDesc = "Wildberries — крупнейшая площадка с 500+ млн товаров. Доставка, пункты выдачи, примерка и быстрый возврат.",
        fullDesc =
            "Wildberries — цифровая платформа, где можно найти почти всё: от одежды и техники до товаров для дома. " +
                    "80% заказов привозят уже на следующий день. Можно заказывать в пункты выдачи в РФ и странах СНГ, " +
                    "пользоваться примеркой, отслеживать посылки и оформлять возвраты прямо в приложении.",
        iconPath = "icons/2/icon.png",
        screenshotPaths = listOf(
            "screenshots/2/screen1.png",
            "screenshots/2/screen2.png",
            "screenshots/2/screen3.png"
        ),
        apkPath = "apks/demo.apk"
    ),
    SeedApp(
        id = "3",
        name = "ВКонтакте",
        developer = "VK",
        category = "Инструменты",
        ratingAge = "12+",
        shortDesc = "ВКонтакте — соцсеть: чаты и звонки, музыка и видео, клипы и мини-приложения — всё в одном приложении.",
        fullDesc =
            "ВКонтакте — общение и мессенджер с бесплатными звонками, музыка и видео, клипы, игры и мини-приложения. " +
                    "Слушайте треки и плейлисты, смотрите видео и сохраняйте офлайн, общайтесь в чатах и видеозвонках, " +
                    "подписывайтесь на сообщества и пользуйтесь сервисами VK — от чекбэка до шагомера.",
        iconPath = "icons/3/icon.png",
        screenshotPaths = listOf(
            "screenshots/3/screen1.png",
            "screenshots/3/screen2.png",
            "screenshots/3/screen3.png"
        ),
        apkPath = "apks/demo.apk"
    ),
    SeedApp(
        id = "4",
        name = "2GIS",
        developer = "2GIS",
        category = "Транспорт",
        ratingAge = "0+",
        shortDesc = "2GIS — офлайн-карта и навигатор: автобусы, маршруты, справочник организаций.",
        fullDesc =
            "2GIS — подробные офлайн-карты городов с навигацией для авто и пешком, маршруты общественного транспорта с временем в пути и пересадками. " +
                    "Справочник компаний с телефонами, графиком работы, отзывами и входами в здания. Работает без интернета после загрузки карт города.",
        iconPath = "icons/4/icon.png",
        screenshotPaths = listOf(
            "screenshots/4/screen1.png",
            "screenshots/4/screen2.png",
            "screenshots/4/screen3.png"
        ),
        apkPath = "apks/demo.apk"
    ),
    SeedApp(
        id = "5",
        name = "Subway Surfers",
        developer = "SYBO Games",
        category = "Игры",
        ratingAge = "6+",
        shortDesc = "Бесконечный раннер по рельсам: уворачивайся от поездов, собирай монеты и бустеры.",
        fullDesc =
            "Subway Surfers — аркадный бесконечный раннер. Беги по рельсам, избегай препятствий и поездов, используй джетпак и магнит, " +
                    "открывай персонажей и доски. Регулярные события и «World Tour» с новыми локациями и заданиями.",
        iconPath = "icons/5/icon.png",
        screenshotPaths = listOf(
            "screenshots/5/screen1.png",
            "screenshots/5/screen2.png",
            "screenshots/5/screen3.png"
        ),
        apkPath = "apks/demo.apk"
    ),
    SeedApp(
        id = "6",
        name = "Spotify",
        developer = "Spotify AB",
        category = "Музыка",
        ratingAge = "12+",
        shortDesc = "Музыка и подкасты без ограничений: плейлисты, рекомендации и офлайн-прослушивание.",
        fullDesc =
            "Spotify — миллионы треков и подкастов с персональными рекомендациями. Создавайте плейлисты, " +
                    "слушайте подборки дня, открывайте новые релизы и сохраняйте контент для офлайн-прослушивания. " +
                    "Доступны избранные чарты, радиостанции по настроению и совместные плейлисты.",
        iconPath = "icons/6/icon.png",
        screenshotPaths = listOf(
            "screenshots/6/screen1.png",
            "screenshots/6/screen2.png",
            "screenshots/6/screen3.png"
        ),
        apkPath = "apks/demo.apk"
    ),
    SeedApp(
        id = "7",
        name = "Tetris",
        developer = "Tetris Company / PlayStudios",
        category = "Игры",
        ratingAge = "6+",
        shortDesc = "Классический тетрис: собирай линии, повышай уровень и ставь рекорды.",
        fullDesc =
            "Tetris — легендарная головоломка с понятными правилами: поворачивайте фигуры, заполняйте ряды и " +
                    "очищайте поле. Соревнуйтесь за рекорды, увеличивайте скорость с ростом уровня и тренируйте реакцию " +
                    "и стратегическое мышление. Простая механика — бесконечный челлендж.",
        iconPath = "icons/7/icon.png",
        screenshotPaths = listOf(
            "screenshots/7/screen1.png",
            "screenshots/7/screen2.png",
            "screenshots/7/screen3.png"
        ),
        apkPath = "apks/demo.apk"
    )
)

val AllowedCategories = setOf("Финансы", "Инструменты", "Игры", "Государственные", "Транспорт", "Музыка")
val AllowedAgeRatings = setOf("0+", "6+", "8+", "12+", "16+", "18+")

fun validateSeeds() {
    val badCats = appsSeed.filter { it.category !in AllowedCategories }
    val badAges = appsSeed.filter { it.ratingAge !in AllowedAgeRatings }
    require(badCats.isEmpty()) { "Seed category is out of allowed set: $badCats" }
    require(badAges.isEmpty()) { "Seed age rating is out of allowed set: $badAges" }
}
