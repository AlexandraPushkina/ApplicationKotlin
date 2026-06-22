package com.example.homework6.data.test_data

import com.example.homework6.data.entities.PostEntity
import com.example.homework6.data.entities.TestPostData
import com.example.homework6.utils.RecommendationAlgorithm.ONE_DAY_IN_MILLIS
import com.example.homework6.utils.RecommendationAlgorithm.ONE_HOUR_ON_MILLIS

fun generateTestPosts(): List<TestPostData> {
    val defaultAuthor = "Александра Пушкина"
    val currentTime = System.currentTimeMillis()

    return listOf(
        // Пост 1: Природа (1) + Спорт (4)
        TestPostData(
            post = PostEntity(
                id = 1,
                userId = 1,
                authorName = defaultAuthor,
                title = "Утренняя йога в парке: наполняемся энергией",
                content = "Нет ничего лучше, чем приветствие солнцу на свежем воздухе. Шум деревьев помогает отключить мысли, а растяжка на траве ощущается совсем иначе, чем в зале. Мой любимый спот в парке Горького...",
                imageUrl = "https://media.istockphoto.com/id/1322925863/photo/young-woman-practices-yoga-in-grassy-meadow-at-sunrise.jpg?s=612x612&w=0&k=20&c=p8srKUlRAOpQKqn7WNXEvX7mZouWNoRc5j3BYcMeXck=",
                createdAt = currentTime
            ),
            topicIds = listOf(1, 4)
        ),

        TestPostData(
            post = PostEntity(
                id = 2,
                userId = 1,
                authorName = defaultAuthor,
                title = "Макияж как искусство: тренд на цветные стрелки",
                content = "Лицо — это холст. В этом сезоне визажисты предлагают забыть о скучной классике и добавить ярких красок. Синий электрик или неоновый розовый? Показываю, как я экспериментирую с геометрией...",
                imageUrl = "https://i.pinimg.com/736x/2f/ef/0e/2fef0e490c6925e014df951aa5ac141b.jpg",
                createdAt = currentTime - (2 * ONE_DAY_IN_MILLIS)
            ),
            topicIds = listOf(2, 3)
        ),

        // Пост 3: Еда (5)
        TestPostData(
            post = PostEntity(
                id = 3,
                userId = 1,
                authorName = defaultAuthor,
                title = "Тот самый рецепт авокадо-тоста 🥑",
                content = "Кажется, я нашла секретный ингредиент, который подают в лучших кофейнях. Всё дело не в хлебе, а в специях и капле лимонного сока. Ловите простой рецепт для идеального завтрака...",
                imageUrl = "https://i.pinimg.com/1200x/94/06/a1/9406a1acff16f9ad32387917b3d86caa.jpg",
                createdAt = currentTime - ONE_DAY_IN_MILLIS
            ),
            topicIds = listOf(5)
        ),

        TestPostData(
            post = PostEntity(
                id = 4,
                userId = 1,
                authorName = defaultAuthor,
                title = "Как сохранить кожу чистой, если часто тренируешься?",
                content = "Спорт — это здоровье, но пот и макияж — враги для пор. Рассказываю о своем уходе до и после зала. Спойлер: мицеллярная вода всегда должна быть в спортивной сумке!",
                imageUrl = "https://i.pinimg.com/736x/ee/4b/99/ee4b99cd5165d88f996105108154542f.jpg",
                createdAt = currentTime - ONE_HOUR_ON_MILLIS
            ),
            topicIds = listOf(3, 4)
        ),

        TestPostData(
            post = PostEntity(
                id = 5,
                userId = 1,
                authorName = defaultAuthor,
                title = "Эстетичный пикник: чек-лист",
                content = "Красивая плетеная корзина, сезонные ягоды, круассаны и, конечно, правильная локация у воды. Как организовать пикник, чтобы он выглядел как картинка из Pinterest, и ничего не забыть...",
                imageUrl = "https://i.pinimg.com/736x/67/83/f4/6783f477e7a404f6fe9923704c529849.jpg",
                createdAt = currentTime - (2 * ONE_DAY_IN_MILLIS)
            ),
            topicIds = listOf(1, 5)
        ),

        TestPostData(
            post = PostEntity(
                id = 6,
                userId = 1,
                authorName = defaultAuthor,
                title = "Сходила на гончарный круг — это медитация!",
                content = "Давно хотела попробовать сделать чашку своими руками. Ощущение глины в руках невероятно успокаивает. Получилось не идеально ровно, зато с душой. Всем советую для разгрузки головы.",
                imageUrl = "https://i.pinimg.com/1200x/a6/8a/4d/a68a4d7bd187de27d19f0e1cf4144e73.jpg",
                createdAt = currentTime - (5 * ONE_DAY_IN_MILLIS)
            ),
            topicIds = listOf(2)
        ),

        TestPostData(
            post = PostEntity(
                id = 7,
                userId = 1,
                authorName = defaultAuthor,
                title = "Сладкое на диете: полезные десерты",
                content = "Быть в форме не значит отказаться от вкусненького. Делюсь рецептом конфет из фиников и орехов, которые дают энергию перед тренировкой и не вредят фигуре.",
                imageUrl = "https://i.pinimg.com/1200x/a9/ac/22/a9ac22d07b1adbe011de6813266fe86f.jpg",
                createdAt = currentTime - (6 * ONE_DAY_IN_MILLIS)
            ),
            topicIds = listOf(4, 5)
        ),

        TestPostData(
            post = PostEntity(
                id = 8,
                userId = 1,
                authorName = defaultAuthor,
                title = "Бюджетные находки: тушь, которая не осыпается",
                content = "Не обязательно тратить миллионы на люкс. Нашла в обычном магазине тушь, которая делает ресницы кукольными и держится весь день. Честный обзор без рекламы.",
                imageUrl = "https://i.pinimg.com/736x/5c/e7/5b/5ce75b1702ea52b73ce0cf08d8e3d5c4.jpg",
                createdAt = currentTime - (7 * ONE_DAY_IN_MILLIS)
            ),
            topicIds = listOf(3)
        ),

        TestPostData(
            post = PostEntity(
                id = 9,
                userId = 1,
                authorName = defaultAuthor,
                title = "Осенняя фотосессия: идеи для кадров",
                content = "Золотая листва — лучший фон. Подобрала для вас 5 идей для позирования в парке, чтобы фотографии получились живыми и атмосферными. Не забудьте взять с собой книгу или стаканчик кофе.",
                imageUrl = "https://i.pinimg.com/1200x/8c/e4/d3/8ce4d309c89a7ccb43661697f8584388.jpg",
                createdAt = currentTime - (7 * ONE_DAY_IN_MILLIS)
            ),
            topicIds = listOf(1, 2)
        ),

        TestPostData(
            post = PostEntity(
                id = 10,
                userId = 1,
                authorName = defaultAuthor,
                title = "Мой новый рекорд в планке",
                content = "Сегодня продержалась целых 3 минуты! Сначала казалось невозможным, но регулярность творит чудеса. Главное — не сдаваться.",
                imageUrl = "https://i.pinimg.com/736x/b8/3d/50/b83d50cd811dc6654f74c60699d5adba.jpg",
                createdAt = currentTime - (8 * ONE_DAY_IN_MILLIS)
            ),
            topicIds = listOf(4)
        ),

        TestPostData(
            post = PostEntity(
                id = 11,
                userId = 1,
                authorName = defaultAuthor,
                title = "Честный отзыв о сыворотке с витамином С",
                content = "Пользуюсь уже две недели. Кожа стала заметно ярче, но есть нюансы с нанесением. Рассказываю подробно...",
                imageUrl = "https://i.pinimg.com/1200x/94/3e/6c/943e6cb6d21c66c87e0258f887b37c70.jpg",
                createdAt = currentTime - (9 * ONE_DAY_IN_MILLIS)
            ),
            topicIds = listOf(3)
        ),

        TestPostData(
            post = PostEntity(
                id = 12,
                userId = 1,
                authorName = defaultAuthor,
                title = "Впечатления от выставки авангардистов",
                content = "Сложно, непонятно, но безумно интересно. Каждый видит что-то свое. Особенно запомнилась картина 'Красный квадрат'.",
                imageUrl = "https://i.pinimg.com/1200x/ea/56/75/ea56758ec428131942e7c44c828ae10f.jpg",
                createdAt = currentTime - (11 * ONE_DAY_IN_MILLIS)
            ),
            topicIds = listOf(2)
        ),

        TestPostData(
            post = PostEntity(
                id = 13,
                userId = 1,
                authorName = defaultAuthor,
                title = "Закат на набережной",
                content = "Невероятные краски неба сегодня. Розовый переходит в фиолетовый. Такие моменты заставляют остановиться и просто смотреть.",
                imageUrl = "https://i.pinimg.com/736x/6b/db/c8/6bdbc8e3b45c0237cf1c9c37a015f35b.jpg",
                createdAt = currentTime - (12 * ONE_DAY_IN_MILLIS)
            ),
            topicIds = listOf(1)
        ),
        TestPostData(
            post = PostEntity(
                id = 14,
                userId = 1,
                authorName = defaultAuthor,
                title = "Мой первый опыт с Kotlin",
                content = "Сегодня наконец-то переписал старый кусок кода с Java на Kotlin. Extension-функции — это просто магия! Код стал короче раза в два. Кто какие фишки языка любит больше всего?",
                imageUrl = "https://habrastorage.org/webt/jz/x_/bn/jzx_bnlrngqkioy5kc3k5quzid4.jpeg",
                createdAt = currentTime - (15 * ONE_HOUR_ON_MILLIS)
            ),
            topicIds = listOf(6)
        ),
        TestPostData(
            post = PostEntity(
                id = 15,
                userId = 1,
                authorName = defaultAuthor,
                title = "Как перестать бояться Git",
                content = "Помню, как в начале пути боялась сделать git push origin main. Главное правило: не делай force push в общую ветку, и никто не пострадает. Собрала для вас шпаргалку базовых команд.",
                imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSBKUbhCTM__XjWqhFpngv2adiRoDnvsbsvN90RVgrtTRatbVMrHKZa004&s=10",
                createdAt = currentTime - (2 * ONE_DAY_IN_MILLIS) // 2 дня назад
            ),
            topicIds = listOf(6)
        ),

        TestPostData(
            post = PostEntity(
                id = 16,
                userId = 1,
                authorName = defaultAuthor,
                title = "Нейросети пишут код за нас?",
                content = "Тестировал новый ИИ-ассистент для написания кода. Он отлично справляется с рутиной, но архитектуру приложения за вас пока не продумает. Кожаные мешки могут спать спокойно.",
                imageUrl = "https://images.ctfassets.net/ri4db8kokucw/6SnVpbbdektzbX0yg5ttQK/292b017a339eb5ac73c1bb18aeb77f9d/MSFT-Code-on-a-computer.png",
                createdAt = currentTime - (5 * ONE_DAY_IN_MILLIS) // 5 дней назад
            ),
            topicIds = listOf(6, 8) // IT + Наука
        ),

        // ================= ПУТЕШЕСТВИЯ (Тема: 7) =================
        TestPostData(
            post = PostEntity(
                id = 17,
                userId = 1,
                authorName = defaultAuthor,
                title = "Секретные места Стамбула",
                content = "Случайно свернул с туристической тропы возле Галатской башни и нашел потрясающую старую кофейню. Местные пьют там чай и играют в нарды. Сохраняйте координаты!",
                imageUrl = "https://cdn2.tu-tu.ru/image/pagetree_node_data/2/29cac70d1c729a91876c37b13a3c1149/",
                createdAt = currentTime - (3 * ONE_HOUR_ON_MILLIS)
            ),
            topicIds = listOf(7, 5)
        ),
        TestPostData(
            post = PostEntity(
                id = 18,
                userId = 1,
                authorName = defaultAuthor,
                title = "Что взять с собой в горы",
                content = "Мой личный топ ошибок новичка в горах: хлопковые футболки и тяжелые консервы. Берите термобелье, легкие сублиматы и обязательно треккинговые палки — ваши колени скажут спасибо.",
                imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSzdccsOXvovBcf4tJKXiz-cleDqLb6qoXWtYUVw5T7jxEG_Fw_2Aab2xk&s=10",
                createdAt = currentTime - (1 * ONE_DAY_IN_MILLIS)
            ),
            topicIds = listOf(7, 1, 4)
        ),
        TestPostData(
            post = PostEntity(
                id = 19,
                userId = 1,
                authorName = defaultAuthor,
                title = "Перелет длиною в сутки",
                content = "Три пересадки, потерянный багаж и сон на железных креслах в аэропорту. Звучит ужасно, но рассвет, который я встретила по прилете, стоил каждой минуты этих мучений.",
                imageUrl = "https://i.pinimg.com/736x/3f/d0/6a/3fd06a132bba68c00fa905af7d5bb56c.jpg",
                createdAt = currentTime - (7 * ONE_DAY_IN_MILLIS)
            ),
            topicIds = listOf(7)
        ),

        // ================= НАУКА (Тема: 8) =================
        TestPostData(
            post = PostEntity(
                id = 20,
                userId = 1,
                authorName = defaultAuthor,
                title = "Новое открытие телескопа Джеймс Уэбб",
                content = "Ученые обнаружили новую экзопланету, в атмосфере которой есть признаки водяного пара. Она находится в обитаемой зоне своей звезды! Просто вдумайтесь в масштабы Вселенной.",
                imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZxLLGE9CT7U6cAuOuIJWGI2DaS5jP0VsIIkOkPXI7arX4tuvhZ1dIYWA&s=10",
                createdAt = currentTime - (1 * ONE_HOUR_ON_MILLIS)
            ),
            topicIds = listOf(8, 1) // Наука + Природа
        ),
        TestPostData(
            post = PostEntity(
                id = 21,
                userId = 1,
                authorName = defaultAuthor,
                title = "Квантовые компьютеры простым языком",
                content = "В отличие от обычных битов (0 или 1), кубиты могут находиться в суперпозиции. Это значит, что они решают миллионы вариантов задачи одновременно. Будущее шифрования под угрозой?",
                imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ03cc_lN77-U4QDOxYff8nRM649wh85opONQCYyZo3l4wHKyY4vxeXJIxk&s=10",
                createdAt = currentTime - (4 * ONE_DAY_IN_MILLIS)
            ),
            topicIds = listOf(8, 6) // Наука + IT
        ),
        TestPostData(
            post = PostEntity(
                id = 22,
                userId = 1,
                authorName = defaultAuthor,
                title = "Биохакинг и сон",
                content = "Изучала последние статьи про влияние мелатонина на фазы глубокого сна. Оказывается, блокировка синего света от экранов за час до сна улучшает восстановление организма на 30%.",
                imageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRyyC4AIO-EMlbMPYJAD3VTfl1KGr2f5RLttYPe6SgWvw&s",
                createdAt = currentTime - (6 * ONE_DAY_IN_MILLIS)
            ),
            topicIds = listOf(8)
        )
    )
}