package com.example.homework6.data.test_data

import com.example.homework6.data.entities.PostEntity
import com.example.homework6.data.entities.TestPostData

fun generateTestPosts(): List<TestPostData> {
    val defaultAuthor = "Александра Пушкина"

    return listOf(
        // Пост 1: Природа (1) + Спорт (4)
        TestPostData(
            post = PostEntity(
                id = 1,
                userId = 1,
                authorName = defaultAuthor,
                title = "Утренняя йога в парке: наполняемся энергией",
                content = "Нет ничего лучше, чем приветствие солнцу на свежем воздухе. Шум деревьев помогает отключить мысли, а растяжка на траве ощущается совсем иначе, чем в зале. Мой любимый спот в парке Горького...",
                imageUrl = "https://media.istockphoto.com/id/1322925863/photo/young-woman-practices-yoga-in-grassy-meadow-at-sunrise.jpg?s=612x612&w=0&k=20&c=p8srKUlRAOpQKqn7WNXEvX7mZouWNoRc5j3BYcMeXck="
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
                imageUrl = "https://i.pinimg.com/736x/2f/ef/0e/2fef0e490c6925e014df951aa5ac141b.jpg"
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
                imageUrl = "https://i.pinimg.com/1200x/94/06/a1/9406a1acff16f9ad32387917b3d86caa.jpg"
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
                imageUrl = "https://i.pinimg.com/736x/ee/4b/99/ee4b99cd5165d88f996105108154542f.jpg"
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
                imageUrl = "https://i.pinimg.com/736x/67/83/f4/6783f477e7a404f6fe9923704c529849.jpg"
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
                imageUrl = "https://i.pinimg.com/1200x/a6/8a/4d/a68a4d7bd187de27d19f0e1cf4144e73.jpg"
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
                imageUrl = "https://i.pinimg.com/1200x/a9/ac/22/a9ac22d07b1adbe011de6813266fe86f.jpg"
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
                imageUrl = "https://i.pinimg.com/736x/5c/e7/5b/5ce75b1702ea52b73ce0cf08d8e3d5c4.jpg"
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
                imageUrl = "https://i.pinimg.com/1200x/8c/e4/d3/8ce4d309c89a7ccb43661697f8584388.jpg"
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
                imageUrl = "https://i.pinimg.com/736x/b8/3d/50/b83d50cd811dc6654f74c60699d5adba.jpg"
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
                imageUrl = "https://i.pinimg.com/1200x/94/3e/6c/943e6cb6d21c66c87e0258f887b37c70.jpg"
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
                imageUrl = "https://i.pinimg.com/1200x/ea/56/75/ea56758ec428131942e7c44c828ae10f.jpg"
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
                imageUrl = "https://i.pinimg.com/736x/6b/db/c8/6bdbc8e3b45c0237cf1c9c37a015f35b.jpg"
            ),
            topicIds = listOf(1)
        )
    )
}